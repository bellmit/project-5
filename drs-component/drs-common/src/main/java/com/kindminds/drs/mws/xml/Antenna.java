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
 *         &lt;element ref="{}AntennaDescription" minOccurs="0"/&gt;
 *         &lt;element ref="{}AntennaLocation" minOccurs="0"/&gt;
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
    "antennaDescription",
    "antennaLocation"
})
@XmlRootElement(name = "Antenna")
public class Antenna {

    @XmlElement(name = "AntennaDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String antennaDescription;
    @XmlElement(name = "AntennaLocation")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String antennaLocation;

    /**
     * Gets the value of the antennaDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntennaDescription() {
        return antennaDescription;
    }

    /**
     * Sets the value of the antennaDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntennaDescription(String value) {
        this.antennaDescription = value;
    }

    /**
     * Gets the value of the antennaLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntennaLocation() {
        return antennaLocation;
    }

    /**
     * Sets the value of the antennaLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntennaLocation(String value) {
        this.antennaLocation = value;
    }

}
