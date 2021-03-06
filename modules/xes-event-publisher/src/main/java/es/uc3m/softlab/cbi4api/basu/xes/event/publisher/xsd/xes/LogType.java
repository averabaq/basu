//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.21 at 05:38:03 PM IST 
//


package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for LogType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LogType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{http://www.xes-standard.org/}ExtensionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="global" type="{http://www.xes-standard.org/}GlobalsType" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="classifier" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="string" type="{http://www.xes-standard.org/}AttributeStringType" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="date" type="{http://www.xes-standard.org/}AttributeDateType" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="int" type="{http://www.xes-standard.org/}AttributeIntType" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="float" type="{http://www.xes-standard.org/}AttributeFloatType" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="boolean" type="{http://www.xes-standard.org/}AttributeBooleanType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="trace" type="{http://www.xes-standard.org/}TraceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="xes.version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="xes.features" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="openxes.version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogType", propOrder = {
    "extension",
    "global",
    "classifier",
    "stringOrDateOrInt",
    "trace"
})
public class LogType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<ExtensionType> extension;
    protected List<GlobalsType> global;
    protected List<Object> classifier;
    @XmlElements({
        @XmlElement(name = "date", type = AttributeDateType.class),
        @XmlElement(name = "boolean", type = AttributeBooleanType.class),
        @XmlElement(name = "int", type = AttributeIntType.class),
        @XmlElement(name = "float", type = AttributeFloatType.class),
        @XmlElement(name = "string", type = AttributeStringType.class)
    })
    protected List<Object> stringOrDateOrInt;
    @XmlElement(required = true)
    protected List<TraceType> trace;
    @XmlAttribute(name = "xes.version", required = true)
    protected BigDecimal xesVersion;
    @XmlAttribute(name = "xes.features", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String xesFeatures;
    @XmlAttribute(name = "openxes.version", required = true)
    protected String openxesVersion;

    /**
     * Gets the value of the extension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtensionType }
     * 
     * 
     */
    public List<ExtensionType> getExtension() {
        if (extension == null) {
            extension = new ArrayList<ExtensionType>();
        }
        return this.extension;
    }

    public boolean isSetExtension() {
        return ((this.extension!= null)&&(!this.extension.isEmpty()));
    }

    public void unsetExtension() {
        this.extension = null;
    }

    /**
     * Gets the value of the global property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the global property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGlobal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GlobalsType }
     * 
     * 
     */
    public List<GlobalsType> getGlobal() {
        if (global == null) {
            global = new ArrayList<GlobalsType>();
        }
        return this.global;
    }

    public boolean isSetGlobal() {
        return ((this.global!= null)&&(!this.global.isEmpty()));
    }

    public void unsetGlobal() {
        this.global = null;
    }

    /**
     * Gets the value of the classifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getClassifier() {
        if (classifier == null) {
            classifier = new ArrayList<Object>();
        }
        return this.classifier;
    }

    public boolean isSetClassifier() {
        return ((this.classifier!= null)&&(!this.classifier.isEmpty()));
    }

    public void unsetClassifier() {
        this.classifier = null;
    }

    /**
     * Gets the value of the stringOrDateOrInt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stringOrDateOrInt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStringOrDateOrInt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeDateType }
     * {@link AttributeBooleanType }
     * {@link AttributeIntType }
     * {@link AttributeFloatType }
     * {@link AttributeStringType }
     * 
     * 
     */
    public List<Object> getStringOrDateOrInt() {
        if (stringOrDateOrInt == null) {
            stringOrDateOrInt = new ArrayList<Object>();
        }
        return this.stringOrDateOrInt;
    }

    public boolean isSetStringOrDateOrInt() {
        return ((this.stringOrDateOrInt!= null)&&(!this.stringOrDateOrInt.isEmpty()));
    }

    public void unsetStringOrDateOrInt() {
        this.stringOrDateOrInt = null;
    }

    /**
     * Gets the value of the trace property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trace property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TraceType }
     * 
     * 
     */
    public List<TraceType> getTrace() {
        if (trace == null) {
            trace = new ArrayList<TraceType>();
        }
        return this.trace;
    }

    public boolean isSetTrace() {
        return ((this.trace!= null)&&(!this.trace.isEmpty()));
    }

    public void unsetTrace() {
        this.trace = null;
    }

    /**
     * Gets the value of the xesVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getXesVersion() {
        return xesVersion;
    }

    /**
     * Sets the value of the xesVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setXesVersion(BigDecimal value) {
        this.xesVersion = value;
    }

    public boolean isSetXesVersion() {
        return (this.xesVersion!= null);
    }

    /**
     * Gets the value of the xesFeatures property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXesFeatures() {
        return xesFeatures;
    }

    /**
     * Sets the value of the xesFeatures property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXesFeatures(String value) {
        this.xesFeatures = value;
    }

    public boolean isSetXesFeatures() {
        return (this.xesFeatures!= null);
    }

    /**
     * Gets the value of the openxesVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenxesVersion() {
        return openxesVersion;
    }

    /**
     * Sets the value of the openxesVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenxesVersion(String value) {
        this.openxesVersion = value;
    }

    public boolean isSetOpenxesVersion() {
        return (this.openxesVersion!= null);
    }

}
