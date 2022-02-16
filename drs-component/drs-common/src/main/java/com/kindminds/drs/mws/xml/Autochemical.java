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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element name="PartInterchangeData" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="OEManufacturer" type="{}StringNotNull"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Viscosity" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="WarrantyDescription" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Efficiency" type="{}StringNotNull" minOccurs="0"/&gt;
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
    "partInterchangeData",
    "viscosity",
    "warrantyDescription",
    "efficiency"
})
@XmlRootElement(name = "Autochemical")
public class Autochemical {

    @XmlElement(name = "PartInterchangeData")
    protected Autochemical.PartInterchangeData partInterchangeData;
    @XmlElement(name = "Viscosity")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String viscosity;
    @XmlElement(name = "WarrantyDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String warrantyDescription;
    @XmlElement(name = "Efficiency")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String efficiency;

    /**
     * Gets the value of the partInterchangeData property.
     * 
     * @return
     *     possible object is
     *     {@link Autochemical.PartInterchangeData }
     *     
     */
    public Autochemical.PartInterchangeData getPartInterchangeData() {
        return partInterchangeData;
    }

    /**
     * Sets the value of the partInterchangeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Autochemical.PartInterchangeData }
     *     
     */
    public void setPartInterchangeData(Autochemical.PartInterchangeData value) {
        this.partInterchangeData = value;
    }

    /**
     * Gets the value of the viscosity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViscosity() {
        return viscosity;
    }

    /**
     * Sets the value of the viscosity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViscosity(String value) {
        this.viscosity = value;
    }

    /**
     * Gets the value of the warrantyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarrantyDescription() {
        return warrantyDescription;
    }

    /**
     * Sets the value of the warrantyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarrantyDescription(String value) {
        this.warrantyDescription = value;
    }

    /**
     * Gets the value of the efficiency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEfficiency() {
        return efficiency;
    }

    /**
     * Sets the value of the efficiency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEfficiency(String value) {
        this.efficiency = value;
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
     *       &lt;sequence&gt;
     *         &lt;element name="OEManufacturer" type="{}StringNotNull"/&gt;
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
        "oeManufacturer"
    })
    public static class PartInterchangeData {

        @XmlElement(name = "OEManufacturer", required = true)
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        @XmlSchemaType(name = "normalizedString")
        protected String oeManufacturer;

        /**
         * Gets the value of the oeManufacturer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOEManufacturer() {
            return oeManufacturer;
        }

        /**
         * Sets the value of the oeManufacturer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOEManufacturer(String value) {
            this.oeManufacturer = value;
        }

    }

}
