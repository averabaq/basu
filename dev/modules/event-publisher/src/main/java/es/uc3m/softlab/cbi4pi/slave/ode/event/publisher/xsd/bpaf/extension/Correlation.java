//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.20 at 09:01:05 PM WET 
//


package es.uc3m.softlab.cbi4api.basu.ode.event.publisher.xsd.bpaf.extension;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Correlation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Correlation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CorrelationElement" type="{http://www.it.nuigalway.ie/ecrg/f4bpa}CorrelationElement" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Correlation", propOrder = {
    "correlationElement"
})
public class Correlation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CorrelationElement", required = true)
    protected List<CorrelationElement> correlationElement;

    /**
     * Gets the value of the correlationElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the correlationElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrelationElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrelationElement }
     * 
     * 
     */
    public List<CorrelationElement> getCorrelationElement() {
        if (correlationElement == null) {
            correlationElement = new ArrayList<CorrelationElement>();
        }
        return this.correlationElement;
    }

    public boolean isSetCorrelationElement() {
        return ((this.correlationElement!= null)&&(!this.correlationElement.isEmpty()));
    }

    public void unsetCorrelationElement() {
        this.correlationElement = null;
    }

}
