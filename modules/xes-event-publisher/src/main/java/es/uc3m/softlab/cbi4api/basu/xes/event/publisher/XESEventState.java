/* 
 * $Id: XESEventState.java,v 1.0 2013-10-26 17:32:14 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

/**
 * Representational event states defined by the XES standard transactional model.
 *  
 * @author averab
 * @version 1.0.0 
 */
public enum XESEventState {
	/** XES event for the Schedule state at the XES standard transactional model */
	SCHEDULE("SCHEDULE"),
	/** XES event for the Complete state at the XES standard transactional model */
	COMPLETE("COMPLETE"),
	/** XES event for the Start state at the XES standard transactional model */
	START("START"),
	/** XES event for the Assign state at the XES standard transactional model */
	ASSIGN("ASSIGN"),
	/** XES event for the Reassign state at the XES standard transactional model */
	REASSIGN("REASSIGN"),
	/** XES event for the Autoskip state at the XES standard transactional model */
	AUTOSKIP("AUTOSKIP"),
	/** XES event for the Manualskip state at the XES standard transactional model */
	MANUALSKIP("MANUALSKIP"),
	/** XES event for the Withdraw state at the XES standard transactional model */
	WITHDRAW("WITHDRAW"),
	/** XES event for the Suspend state at the XES standard transactional model */
	SUSPEND("SUSPEND"),
	/** XES event for the Resume state at the XES standard transactional model */
	RESUME("RESUME"),
	/** XES event for the Ate-Abort state at the XES standard transactional model */
	ATE_ABORT("ATE_ABORT"),
	/** XES event for the Pi-Abort state at the XES standard transactional model */
	PI_ABORT("PI_ABORT"),
	/** XES event for Any lifecycle transition not captured by the above categories */
	UNKNOWN("UNKNOWN");
	
	/** enumerator value */
	private final String value;

	/**
	 * Constructor for this enumerator class.
	 * 
	 * @param value
	 */
	private XESEventState(String value) {
		this.value = value;
	}
	/**
	 * Gets the enum current constant value.
	 * 
	 * @return enum current constant value.
	 */
	public String value() {
		return this.value;
	}
}