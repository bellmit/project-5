//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProductType"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element ref="{}LabSupply"/&gt;
 *                   &lt;element ref="{}SafetySupply"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "productType"
})
@XmlRootElement(name = "LabSupplies")
public class LabSupplies {

    @XmlElement(name = "ProductType", required = true)
    protected LabSupplies.ProductType productType;

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link LabSupplies.ProductType }
     *     
     */
    public LabSupplies.ProductType getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link LabSupplies.ProductType }
     *     
     */
    public void setProductType(LabSupplies.ProductType value) {
        this.productType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element ref="{}LabSupply"/&gt;
     *         &lt;element ref="{}SafetySupply"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "labSupply",
        "safetySupply"
    })
    public static class ProductType {

        @XmlElement(name = "LabSupply")
        protected LabSupply labSupply;
        @XmlElement(name = "SafetySupply")
        protected SafetySupply safetySupply;

        /**
         * Gets the value of the labSupply property.
         * 
         * @return
         *     possible object is
         *     {@link LabSupply }
         *     
         */
        public LabSupply getLabSupply() {
            return labSupply;
        }

        /**
         * Sets the value of the labSupply property.
         * 
         * @param value
         *     allowed object is
         *     {@link LabSupply }
         *     
         */
        public void setLabSupply(LabSupply value) {
            this.labSupply = value;
        }

        /**
         * Gets the value of the safetySupply property.
         * 
         * @return
         *     possible object is
         *     {@link SafetySupply }
         *     
         */
        public SafetySupply getSafetySupply() {
            return safetySupply;
        }

        /**
         * Sets the value of the safetySupply property.
         * 
         * @param value
         *     allowed object is
         *     {@link SafetySupply }
         *     
         */
        public void setSafetySupply(SafetySupply value) {
            this.safetySupply = value;
        }

    }

}
