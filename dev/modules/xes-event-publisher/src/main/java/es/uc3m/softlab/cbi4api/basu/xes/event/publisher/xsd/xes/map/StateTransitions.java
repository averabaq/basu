//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.14 at 04:10:29 PM BST 
//


package es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StateTransitions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StateTransitions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="State" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="bpaf">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="source" type="{http://www.uc3m.es/softlab/basu/xesmap}BPAFState" />
 *                           &lt;attribute name="target" use="required" type="{http://www.uc3m.es/softlab/basu/xesmap}BPAFState" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="xes" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StateTransitions", propOrder = {
    "state"
})
public class StateTransitions
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "State", required = true)
    protected List<StateTransitions.State> state;
    @XmlAttribute(required = true)
    protected String key;

    /**
     * Gets the value of the state property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the state property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getState().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StateTransitions.State }
     * 
     * 
     */
    public List<StateTransitions.State> getState() {
        if (state == null) {
            state = new ArrayList<StateTransitions.State>();
        }
        return this.state;
    }

    public boolean isSetState() {
        return ((this.state!= null)&&(!this.state.isEmpty()));
    }

    public void unsetState() {
        this.state = null;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="bpaf">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="source" type="{http://www.uc3m.es/softlab/basu/xesmap}BPAFState" />
     *                 &lt;attribute name="target" use="required" type="{http://www.uc3m.es/softlab/basu/xesmap}BPAFState" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="xes" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bpaf"
    })
    public static class State
        implements Serializable
    {

        private final static long serialVersionUID = 1L;
        @XmlElement(required = true)
        protected StateTransitions.State.Bpaf bpaf;
        @XmlAttribute(required = true)
        protected String xes;

        /**
         * Gets the value of the bpaf property.
         * 
         * @return
         *     possible object is
         *     {@link StateTransitions.State.Bpaf }
         *     
         */
        public StateTransitions.State.Bpaf getBpaf() {
            return bpaf;
        }

        /**
         * Sets the value of the bpaf property.
         * 
         * @param value
         *     allowed object is
         *     {@link StateTransitions.State.Bpaf }
         *     
         */
        public void setBpaf(StateTransitions.State.Bpaf value) {
            this.bpaf = value;
        }

        public boolean isSetBpaf() {
            return (this.bpaf!= null);
        }

        /**
         * Gets the value of the xes property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getXes() {
            return xes;
        }

        /**
         * Sets the value of the xes property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setXes(String value) {
            this.xes = value;
        }

        public boolean isSetXes() {
            return (this.xes!= null);
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
         *       &lt;attribute name="source" type="{http://www.uc3m.es/softlab/basu/xesmap}BPAFState" />
         *       &lt;attribute name="target" use="required" type="{http://www.uc3m.es/softlab/basu/xesmap}BPAFState" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Bpaf
            implements Serializable
        {

            private final static long serialVersionUID = 1L;
            @XmlAttribute
            protected BPAFState source;
            @XmlAttribute(required = true)
            protected BPAFState target;

            /**
             * Gets the value of the source property.
             * 
             * @return
             *     possible object is
             *     {@link BPAFState }
             *     
             */
            public BPAFState getSource() {
                return source;
            }

            /**
             * Sets the value of the source property.
             * 
             * @param value
             *     allowed object is
             *     {@link BPAFState }
             *     
             */
            public void setSource(BPAFState value) {
                this.source = value;
            }

            public boolean isSetSource() {
                return (this.source!= null);
            }

            /**
             * Gets the value of the target property.
             * 
             * @return
             *     possible object is
             *     {@link BPAFState }
             *     
             */
            public BPAFState getTarget() {
                return target;
            }

            /**
             * Sets the value of the target property.
             * 
             * @param value
             *     allowed object is
             *     {@link BPAFState }
             *     
             */
            public void setTarget(BPAFState value) {
                this.target = value;
            }

            public boolean isSetTarget() {
                return (this.target!= null);
            }

        }

    }

}
