//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.09 at 03:34:43 PM BST 
//


package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.ext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="string" type="{}AttributeType"/>
 *         &lt;element name="date" type="{}AttributeType"/>
 *         &lt;element name="int" type="{}AttributeType"/>
 *         &lt;element name="float" type="{}AttributeType"/>
 *         &lt;element name="boolean" type="{}AttributeType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "stringOrDateOrInt"
})
@XmlRootElement(name = "trace")
public class Trace
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElementRefs({
        @XmlElementRef(name = "string", type = JAXBElement.class),
        @XmlElementRef(name = "date", type = JAXBElement.class),
        @XmlElementRef(name = "float", type = JAXBElement.class),
        @XmlElementRef(name = "int", type = JAXBElement.class),
        @XmlElementRef(name = "boolean", type = JAXBElement.class)
    })
    protected List<JAXBElement<AttributeType>> stringOrDateOrInt;

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
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<AttributeType>> getStringOrDateOrInt() {
        if (stringOrDateOrInt == null) {
            stringOrDateOrInt = new ArrayList<JAXBElement<AttributeType>>();
        }
        return this.stringOrDateOrInt;
    }

    public boolean isSetStringOrDateOrInt() {
        return ((this.stringOrDateOrInt!= null)&&(!this.stringOrDateOrInt.isEmpty()));
    }

    public void unsetStringOrDateOrInt() {
        this.stringOrDateOrInt = null;
    }

}
