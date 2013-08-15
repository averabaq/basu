//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.14 at 04:16:29 PM BST 
//


package es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.gbas.request;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.uc3m.softlab.cbi4api.basu.event.subscriber.xsd.basu.process.Basu;


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
 *         &lt;element name="basu" type="{http://www.uc3m.es/softlab/basu/process}basu"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="type" use="required" type="{http://www.uc3m.es/softlab/gbas/request}OrderType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "basu"
})
@XmlRootElement(name = "OrderRequest")
public class OrderRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Basu basu;
    @XmlAttribute(required = true)
    protected long id;
    @XmlAttribute(required = true)
    protected OrderType type;

    /**
     * Gets the value of the basu property.
     * 
     * @return
     *     possible object is
     *     {@link Basu }
     *     
     */
    public Basu getBasu() {
        return basu;
    }

    /**
     * Sets the value of the basu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Basu }
     *     
     */
    public void setBasu(Basu value) {
        this.basu = value;
    }

    public boolean isSetBasu() {
        return (this.basu!= null);
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    public boolean isSetId() {
        return true;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link OrderType }
     *     
     */
    public OrderType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderType }
     *     
     */
    public void setType(OrderType value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

}
