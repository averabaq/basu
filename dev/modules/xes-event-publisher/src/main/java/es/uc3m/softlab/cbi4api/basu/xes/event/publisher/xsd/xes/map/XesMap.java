//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.01 at 04:10:21 PM BST 
//


package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Trace" type="{http://www.uc3m.es/softlab/basu/xesmap}Trace"/>
 *         &lt;element name="Event" type="{http://www.uc3m.es/softlab/basu/xesmap}Event"/>
 *         &lt;element name="StateTransitions" type="{http://www.uc3m.es/softlab/basu/xesmap}StateTransitions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "trace",
    "event",
    "stateTransitions"
})
@XmlRootElement(name = "XesMap")
public class XesMap
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Trace", required = true)
    protected Trace trace;
    @XmlElement(name = "Event", required = true)
    protected Event event;
    @XmlElement(name = "StateTransitions")
    protected StateTransitions stateTransitions;

    /**
     * Gets the value of the trace property.
     * 
     * @return
     *     possible object is
     *     {@link Trace }
     *     
     */
    public Trace getTrace() {
        return trace;
    }

    /**
     * Sets the value of the trace property.
     * 
     * @param value
     *     allowed object is
     *     {@link Trace }
     *     
     */
    public void setTrace(Trace value) {
        this.trace = value;
    }

    public boolean isSetTrace() {
        return (this.trace!= null);
    }

    /**
     * Gets the value of the event property.
     * 
     * @return
     *     possible object is
     *     {@link Event }
     *     
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the value of the event property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event }
     *     
     */
    public void setEvent(Event value) {
        this.event = value;
    }

    public boolean isSetEvent() {
        return (this.event!= null);
    }

    /**
     * Gets the value of the stateTransitions property.
     * 
     * @return
     *     possible object is
     *     {@link StateTransitions }
     *     
     */
    public StateTransitions getStateTransitions() {
        return stateTransitions;
    }

    /**
     * Sets the value of the stateTransitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link StateTransitions }
     *     
     */
    public void setStateTransitions(StateTransitions value) {
        this.stateTransitions = value;
    }

    public boolean isSetStateTransitions() {
        return (this.stateTransitions!= null);
    }

}
