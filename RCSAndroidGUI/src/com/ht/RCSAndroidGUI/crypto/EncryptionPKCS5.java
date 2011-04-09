/* *******************************************
 * Copyright (c) 2011
 * HT srl,   All rights reserved.
 * Project      : RCS, RCSAndroid
 * File         : EncryptionPKCS5.java
 * Created      : Apr 9, 2011
 * Author		: zeno
 * *******************************************/
package com.ht.RCSAndroidGUI.crypto;

import java.util.Arrays;

import com.ht.RCSAndroidGUI.Debug;
import com.ht.RCSAndroidGUI.utils.Check;
import com.ht.RCSAndroidGUI.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class EncryptionPKCS5.
 */
public class EncryptionPKCS5 extends Encryption {
	
	/**
	 * Instantiates a new encryption pkc s5.
	 *
	 * @param key the key
	 */
	public EncryptionPKCS5(final byte[] key) {
		super(key);
	}

	/**
	 * Instantiates a new encryption pkc s5.
	 */
	public EncryptionPKCS5() {
		super(Keys.self().getAesKey());
	}

	/** The Constant DIGEST_LENGTH. */
	private static final int DIGEST_LENGTH = 20;
	// #ifdef DEBUG
	/** The debug. */
	private static Debug debug = new Debug("EncryptionPKCS5");

	// #endif
	/**
	 * Gets the next multiple.
	 * 
	 * @param len
	 *            the len
	 * @return the next multiple
	 */
	@Override
	public int getNextMultiple(final int len) {
		// #ifdef DBC
		Check.requires(len >= 0, "len < 0");
		// #endif

		final int newlen = len + (16 - len % 16);

		// #ifdef DBC
		Check.ensures(newlen > len, "newlen <= len");
		// #endif
		// #ifdef DBC
		Check.ensures(newlen % 16 == 0, "Wrong newlen");
		// #endif
		return newlen;
	}

	/* (non-Javadoc)
	 * @see com.ht.RCSAndroidGUI.crypto.Encryption#pad(byte[], int, int)
	 */
	@Override
	protected byte[] pad(final byte[] plain, final int offset, final int len) {
		return pad(plain, offset, len, true);
	}

	/* (non-Javadoc)
	 * @see com.ht.RCSAndroidGUI.crypto.Encryption#decryptData(byte[], int, int)
	 */
	@Override
	public byte[] decryptData(final byte[] cyphered, final int enclen,
			final int offset) throws CryptoException {
		// #ifdef DEBUG
		debug.trace("decryptData PKCS5");
		// #endif

		// int padlen = cyphered[cyphered.length -1];
		// int plainlen = enclen - padlen;

		// #ifdef DBC
		Check.requires(enclen % 16 == 0, "Wrong padding");
		// Check.requires(enclen >= plainlen, "Wrong plainlen");
		// #endif

		final byte[] paddedplain = new byte[enclen];
		byte[] plain = null;
		int plainlen = 0;
		byte[] iv = new byte[16];

		final byte[] pt = new byte[16];

		final int numblock = enclen / 16;
		for (int i = 0; i < numblock; i++) {
			final byte[] ct = Utils.copy(cyphered, i * 16 + offset, 16);

			crypto.decrypt(ct, pt);
			xor(pt, iv);
			iv = Utils.copy(ct);
			Utils.copy(paddedplain, i * 16, pt, 0, 16);
		}

		final int padlen = paddedplain[paddedplain.length - 1];

		if (padlen <= 0 || padlen > 16) {
			// #ifdef DEBUG
			debug.error("decryptData, wrong padlen: " + padlen);
			// #endif
			throw new CryptoException();
		}

		plainlen = enclen - padlen;
		plain = new byte[plainlen];

		Utils.copy(plain, 0, paddedplain, 0, plainlen);

		// #ifdef DBC
		Check.ensures(plain != null, "null plain");
		Check.ensures(plain.length == plainlen, "wrong plainlen");
		// #endif
		return plain;
	}

	/**
	 * Encrypt data integrity.
	 *
	 * @param plain the plain
	 * @return the byte[]
	 */
	public byte[] encryptDataIntegrity(final byte[] plain) {

		final byte[] sha = SHA1(plain);
		final byte[] plainSha = Utils.concat(plain, sha);

		// #ifdef DBC
		Check.asserts(sha.length == DIGEST_LENGTH, "sha.length");
		Check.asserts(plainSha.length == plain.length + DIGEST_LENGTH,
				"plainSha.length");
		// #endif

		// #ifdef DEBUG
		debug.trace("encryptDataIntegrity plain: " + plain.length);
		debug.trace("encryptDataIntegrity plainSha: " + plainSha.length);
		// #endif

		return encryptData(plainSha, 0);
	}

	/**
	 * Decrypt data integrity.
	 *
	 * @param cyphered the cyphered
	 * @return the byte[]
	 * @throws CryptoException the crypto exception
	 */
	public byte[] decryptDataIntegrity(final byte[] cyphered)
			throws CryptoException {
		final byte[] plainSha = decryptData(cyphered, 0);
		final byte[] plain = Utils.copy(plainSha, 0, plainSha.length
				- DIGEST_LENGTH);
		final byte[] sha = Utils.copy(plainSha,
				plainSha.length - DIGEST_LENGTH, DIGEST_LENGTH);
		final byte[] calculatedSha = SHA1(plainSha, 0, plainSha.length
				- DIGEST_LENGTH);

		// #ifdef DBC
		// Check.asserts(SHA1Digest.DIGEST_LENGTH == 20, "DIGEST_LENGTH");
		Check.asserts(plain.length + DIGEST_LENGTH == plainSha.length,
				"plain.length");
		Check.asserts(sha.length == DIGEST_LENGTH, "sha.length");
		Check.asserts(calculatedSha.length == DIGEST_LENGTH,
				"calculatedSha.length");
		// #endif

		if (Arrays.equals(calculatedSha, sha)) {
			// #ifdef DEBUG
			debug.trace("decryptDataIntegrity: sha corrected");
			// #endif
			return plain;
		} else {
			// #ifdef DEBUG
			debug.error("decryptDataIntegrity: sha error!");
			// #endif
			throw new CryptoException();
		}
	}

}
