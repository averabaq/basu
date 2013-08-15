//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.14 at 04:10:29 PM BST 
//


package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes;

import java.io.Serializable;
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
 * <p>Java class for AttributeStringType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttributeStringType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="string" type="{http://www.xes-standard.org/}AttributeStringType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="date" type="{http://www.xes-standard.org/}AttributeDateType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="int" type="{http://www.xes-standard.org/}AttributeIntType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="float" type="{http://www.xes-standard.org/}AttributeFloatType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="boolean" type="{http://www.xes-standard.org/}AttributeBooleanType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}Name" />
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeStringType", propOrder = {
    "stringOrDateOrInt"
})
public class AttributeStringType implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElements({
        @XmlElement(name = "int", type = AttributeIntType.class),
        @XmlElement(name = "date", type = AttributeDateType.class),
        @XmlElement(name = "string", type = AttributeStringType.class),
        @XmlElement(name = "float", type = AttributeFloatType.class),
        @XmlElement(name = "boolean", type = AttributeBooleanType.class)
    })
    protected List<Serializable> stringOrDateOrInt;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "Name")
    protected String key;
    @XmlAttribute(required = true)
    protected String value;

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
     * {@link AttributeIntType }
     * {@link AttributeDateType }
     * {@link AttributeStringType }
     * {@link AttributeFloatType }
     * {@link AttributeBooleanType }
     * 
     * 
     */
    public List<Serializable> getStringOrDateOrInt() {
        if (stringOrDateOrInt == null) {
            stringOrDateOrInt = new ArrayList<Serializable>();
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
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    public boolean isSetKey() {
        return (this.key!= null);
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return (this.value!= null);
    }

}
