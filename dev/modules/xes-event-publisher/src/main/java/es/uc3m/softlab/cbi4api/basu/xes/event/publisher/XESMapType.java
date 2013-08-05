/* 
 * $Id: XESMapType.java,v 1.0 2013-10-26 17:32:14 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

/**
 * Enumeration for xes mapping element types.
 *  
 * @author averab
 * @version 1.0.0 
 */
public enum XESMapType {
	/** Representational element for XES trace data */
	TRACE("Trace"),
	/** Representational element for XES event data */
	EVENT("Event");
	
	/** enumerator value */
	private final String value;

	/**
	 * Constructor for this enumerator class.
	 * 
	 * @param value
	 */
	private XESMapType(String value) {
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