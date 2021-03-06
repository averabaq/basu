//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.21 at 05:38:43 PM IST 
//


package es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.event;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for State.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="State">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Open"/>
 *     &lt;enumeration value="Open.NotRunning"/>
 *     &lt;enumeration value="Open.NotRunning.Ready"/>
 *     &lt;enumeration value="Open.NotRunning.Assigned"/>
 *     &lt;enumeration value="Open.NotRunning.Reserved"/>
 *     &lt;enumeration value="Open.NotRunning.Suspended"/>
 *     &lt;enumeration value="Open.NotRunning.Suspended.Assigned"/>
 *     &lt;enumeration value="Open.NotRunning.Suspended.Reserved"/>
 *     &lt;enumeration value="Open.Running"/>
 *     &lt;enumeration value="Open.Running.InProgress"/>
 *     &lt;enumeration value="Open.Running.Suspended"/>
 *     &lt;enumeration value="Closed"/>
 *     &lt;enumeration value="Closed.Completed"/>
 *     &lt;enumeration value="Closed.Completed.Success"/>
 *     &lt;enumeration value="Closed.Completed.Failed"/>
 *     &lt;enumeration value="Closed.Cancelled"/>
 *     &lt;enumeration value="Closed.Cancelled.Exited"/>
 *     &lt;enumeration value="Closed.Cancelled.Error"/>
 *     &lt;enumeration value="Closed.Cancelled.Obsolete"/>
 *     &lt;enumeration value="Closed.Cancelled.Aborted"/>
 *     &lt;enumeration value="Closed.Cancelled.Terminated"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "State")
@XmlEnum
public enum State {

    @XmlEnumValue("Open")
    OPEN("Open"),
    @XmlEnumValue("Open.NotRunning")
    OPEN_NOT_RUNNING("Open.NotRunning"),
    @XmlEnumValue("Open.NotRunning.Ready")
    OPEN_NOT_RUNNING_READY("Open.NotRunning.Ready"),
    @XmlEnumValue("Open.NotRunning.Assigned")
    OPEN_NOT_RUNNING_ASSIGNED("Open.NotRunning.Assigned"),
    @XmlEnumValue("Open.NotRunning.Reserved")
    OPEN_NOT_RUNNING_RESERVED("Open.NotRunning.Reserved"),
    @XmlEnumValue("Open.NotRunning.Suspended")
    OPEN_NOT_RUNNING_SUSPENDED("Open.NotRunning.Suspended"),
    @XmlEnumValue("Open.NotRunning.Suspended.Assigned")
    OPEN_NOT_RUNNING_SUSPENDED_ASSIGNED("Open.NotRunning.Suspended.Assigned"),
    @XmlEnumValue("Open.NotRunning.Suspended.Reserved")
    OPEN_NOT_RUNNING_SUSPENDED_RESERVED("Open.NotRunning.Suspended.Reserved"),
    @XmlEnumValue("Open.Running")
    OPEN_RUNNING("Open.Running"),
    @XmlEnumValue("Open.Running.InProgress")
    OPEN_RUNNING_IN_PROGRESS("Open.Running.InProgress"),
    @XmlEnumValue("Open.Running.Suspended")
    OPEN_RUNNING_SUSPENDED("Open.Running.Suspended"),
    @XmlEnumValue("Closed")
    CLOSED("Closed"),
    @XmlEnumValue("Closed.Completed")
    CLOSED_COMPLETED("Closed.Completed"),
    @XmlEnumValue("Closed.Completed.Success")
    CLOSED_COMPLETED_SUCCESS("Closed.Completed.Success"),
    @XmlEnumValue("Closed.Completed.Failed")
    CLOSED_COMPLETED_FAILED("Closed.Completed.Failed"),
    @XmlEnumValue("Closed.Cancelled")
    CLOSED_CANCELLED("Closed.Cancelled"),
    @XmlEnumValue("Closed.Cancelled.Exited")
    CLOSED_CANCELLED_EXITED("Closed.Cancelled.Exited"),
    @XmlEnumValue("Closed.Cancelled.Error")
    CLOSED_CANCELLED_ERROR("Closed.Cancelled.Error"),
    @XmlEnumValue("Closed.Cancelled.Obsolete")
    CLOSED_CANCELLED_OBSOLETE("Closed.Cancelled.Obsolete"),
    @XmlEnumValue("Closed.Cancelled.Aborted")
    CLOSED_CANCELLED_ABORTED("Closed.Cancelled.Aborted"),
    @XmlEnumValue("Closed.Cancelled.Terminated")
    CLOSED_CANCELLED_TERMINATED("Closed.Cancelled.Terminated");
    private final String value;

    State(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static State fromValue(String v) {
        for (State c: State.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
