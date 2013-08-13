//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.09 at 03:34:43 PM BST 
//


package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.basu.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.TimeStampAdapter;


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
 *         &lt;element name="EventDetails">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="CurrentState" use="required" type="{http://www.uc3m.es/softlab/basu/event}State" />
 *                 &lt;attribute name="PreviousState" type="{http://www.uc3m.es/softlab/basu/event}State" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;choice>
 *           &lt;element name="ProcessInstanceID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN"/>
 *           &lt;element name="Correlation" type="{http://www.uc3m.es/softlab/basu/event}Correlation"/>
 *         &lt;/choice>
 *         &lt;element name="DataElement" type="{http://www.uc3m.es/softlab/basu/event}DataElement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Payload" type="{http://www.uc3m.es/softlab/basu/event}Payload" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EventID" use="required" type="{http://www.uc3m.es/softlab/basu/event}ID" />
 *       &lt;attribute name="ServerID" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessDefinitionID" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ActivityDefinitionID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityInstanceID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityParentID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Timestamp" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eventDetails",
    "processInstanceID",
    "correlation",
    "dataElement",
    "payload"
})
@XmlRootElement(name = "Event")
public class Event
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "EventDetails", required = true)
    protected Event.EventDetails eventDetails;
    @XmlElement(name = "ProcessInstanceID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String processInstanceID;
    @XmlElement(name = "Correlation")
    protected Correlation correlation;
    @XmlElement(name = "DataElement")
    protected List<DataElement> dataElement;
    @XmlElement(name = "Payload")
    protected List<Payload> payload;
    @XmlAttribute(name = "EventID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String eventID;
    @XmlAttribute(name = "ServerID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String serverID;
    @XmlAttribute(name = "ProcessDefinitionID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String processDefinitionID;
    @XmlAttribute(name = "ProcessName")
    protected String processName;
    @XmlAttribute(name = "ActivityDefinitionID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String activityDefinitionID;
    @XmlAttribute(name = "ActivityInstanceID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String activityInstanceID;
    @XmlAttribute(name = "ActivityParentID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String activityParentID;
    @XmlAttribute(name = "ActivityName")
    protected String activityName;
    @XmlAttribute(name = "Timestamp", required = true)
    @XmlJavaTypeAdapter(TimeStampAdapter.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar timestamp;

    /**
     * Gets the value of the eventDetails property.
     * 
     * @return
     *     possible object is
     *     {@link Event.EventDetails }
     *     
     */
    public Event.EventDetails getEventDetails() {
        return eventDetails;
    }

    /**
     * Sets the value of the eventDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.EventDetails }
     *     
     */
    public void setEventDetails(Event.EventDetails value) {
        this.eventDetails = value;
    }

    public boolean isSetEventDetails() {
        return (this.eventDetails!= null);
    }

    /**
     * Gets the value of the processInstanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessInstanceID() {
        return processInstanceID;
    }

    /**
     * Sets the value of the processInstanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessInstanceID(String value) {
        this.processInstanceID = value;
    }

    public boolean isSetProcessInstanceID() {
        return (this.processInstanceID!= null);
    }

    /**
     * Gets the value of the correlation property.
     * 
     * @return
     *     possible object is
     *     {@link Correlation }
     *     
     */
    public Correlation getCorrelation() {
        return correlation;
    }

    /**
     * Sets the value of the correlation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Correlation }
     *     
     */
    public void setCorrelation(Correlation value) {
        this.correlation = value;
    }

    public boolean isSetCorrelation() {
        return (this.correlation!= null);
    }

    /**
     * Gets the value of the dataElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataElement }
     * 
     * 
     */
    public List<DataElement> getDataElement() {
        if (dataElement == null) {
            dataElement = new ArrayList<DataElement>();
        }
        return this.dataElement;
    }

    public boolean isSetDataElement() {
        return ((this.dataElement!= null)&&(!this.dataElement.isEmpty()));
    }

    public void unsetDataElement() {
        this.dataElement = null;
    }

    /**
     * Gets the value of the payload property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the payload property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPayload().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Payload }
     * 
     * 
     */
    public List<Payload> getPayload() {
        if (payload == null) {
            payload = new ArrayList<Payload>();
        }
        return this.payload;
    }

    public boolean isSetPayload() {
        return ((this.payload!= null)&&(!this.payload.isEmpty()));
    }

    public void unsetPayload() {
        this.payload = null;
    }

    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventID(String value) {
        this.eventID = value;
    }

    public boolean isSetEventID() {
        return (this.eventID!= null);
    }

    /**
     * Gets the value of the serverID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerID() {
        return serverID;
    }

    /**
     * Sets the value of the serverID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerID(String value) {
        this.serverID = value;
    }

    public boolean isSetServerID() {
        return (this.serverID!= null);
    }

    /**
     * Gets the value of the processDefinitionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessDefinitionID() {
        return processDefinitionID;
    }

    /**
     * Sets the value of the processDefinitionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessDefinitionID(String value) {
        this.processDefinitionID = value;
    }

    public boolean isSetProcessDefinitionID() {
        return (this.processDefinitionID!= null);
    }

    /**
     * Gets the value of the processName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Sets the value of the processName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessName(String value) {
        this.processName = value;
    }

    public boolean isSetProcessName() {
        return (this.processName!= null);
    }

    /**
     * Gets the value of the activityDefinitionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityDefinitionID() {
        return activityDefinitionID;
    }

    /**
     * Sets the value of the activityDefinitionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityDefinitionID(String value) {
        this.activityDefinitionID = value;
    }

    public boolean isSetActivityDefinitionID() {
        return (this.activityDefinitionID!= null);
    }

    /**
     * Gets the value of the activityInstanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityInstanceID() {
        return activityInstanceID;
    }

    /**
     * Sets the value of the activityInstanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityInstanceID(String value) {
        this.activityInstanceID = value;
    }

    public boolean isSetActivityInstanceID() {
        return (this.activityInstanceID!= null);
    }

    /**
     * Gets the value of the activityParentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityParentID() {
        return activityParentID;
    }

    /**
     * Sets the value of the activityParentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityParentID(String value) {
        this.activityParentID = value;
    }

    public boolean isSetActivityParentID() {
        return (this.activityParentID!= null);
    }

    /**
     * Gets the value of the activityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Sets the value of the activityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityName(String value) {
        this.activityName = value;
    }

    public boolean isSetActivityName() {
        return (this.activityName!= null);
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(Calendar value) {
        this.timestamp = value;
    }

    public boolean isSetTimestamp() {
        return (this.timestamp!= null);
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="CurrentState" use="required" type="{http://www.uc3m.es/softlab/basu/event}State" />
     *       &lt;attribute name="PreviousState" type="{http://www.uc3m.es/softlab/basu/event}State" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class EventDetails
        implements Serializable
    {

        private final static long serialVersionUID = 1L;
        @XmlAttribute(name = "CurrentState", required = true)
        protected State currentState;
        @XmlAttribute(name = "PreviousState")
        protected State previousState;

        /**
         * Gets the value of the currentState property.
         * 
         * @return
         *     possible object is
         *     {@link State }
         *     
         */
        public State getCurrentState() {
            return currentState;
        }

        /**
         * Sets the value of the currentState property.
         * 
         * @param value
         *     allowed object is
         *     {@link State }
         *     
         */
        public void setCurrentState(State value) {
            this.currentState = value;
        }

        public boolean isSetCurrentState() {
            return (this.currentState!= null);
        }

        /**
         * Gets the value of the previousState property.
         * 
         * @return
         *     possible object is
         *     {@link State }
         *     
         */
        public State getPreviousState() {
            return previousState;
        }

        /**
         * Sets the value of the previousState property.
         * 
         * @param value
         *     allowed object is
         *     {@link State }
         *     
         */
        public void setPreviousState(State value) {
            this.previousState = value;
        }

        public boolean isSetPreviousState() {
            return (this.previousState!= null);
        }

    }

}
