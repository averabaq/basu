/* 
 * $Id: BPAFMapType.java,v 1.0 2013-10-26 17:32:14 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

/**
 * Enumeration for bpaf mapping element types.
 *  
 * @author averab
 * @version 1.0.0 
 */
public enum BPAFMapType {
	/** Representational element for BPAF EventID data */
	EVENT_ID("EventID"),
	/** Representational element for BPAF ServerID data */
	SERVER_ID("ServerID"),
	/** Representational element for BPAF ProcessDefinitionID data */
	PROCESS_DEFINITION_ID("ProcessDefinitionID"),
	/** Representational element for BPAF ProcessInstanceID data */
	PROCESS_INSTANCE_ID("ProcessInstanceID"),
	/** Representational element for BPAF ProcessName data */
	PROCESS_NAME("ProcessName"),
	/** Representational element for BPAF ActivityDefinitionID data */
	ACTIVITY_DEFINITION_ID("ActivityDefinitionID"),
	/** Representational element for BPAF ActivityInstanceID data */
	ACTIVITY_INSTANCE_ID("ActivityInstanceID"),
	/** Representational element for BPAF ActivityName data */
	ACTIVITY_NAME("ActivityName"),
	/** Representational element for BPAF Timestamp */
	TIMESTAMP("Timestamp");
	/** Representational element for BPAF Correlation data */
	//CORRELATION("Correlation"),
	/** Representational element for BPAF Payload data */
	//PAYLOAD("Payload");
	
	/** enumerator value */
	private final String value;

	/**
	 * Constructor for this enumerator class.
	 * 
	 * @param value
	 */
	private BPAFMapType(String value) {
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
	/**
	 * Gets the enum type from its string value representation.
	 * 
	 * @param value string value representation of this enum type.
	 * @return enum type.
	 */
    public static BPAFMapType fromValue(String value) {
        for (BPAFMapType type: BPAFMapType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("BPAF State " + value + " is not a valid enum type.");
    }
}