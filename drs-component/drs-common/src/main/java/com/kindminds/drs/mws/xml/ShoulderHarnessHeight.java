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
 *         &lt;element name="ShoulderHarnessMaximumHeight" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="ShoulderHarnessMinimumHeight" type="{}LengthDimension" minOccurs="0"/&gt;
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
    "shoulderHarnessMaximumHeight",
    "shoulderHarnessMinimumHeight"
})
@XmlRootElement(name = "ShoulderHarnessHeight")
public class ShoulderHarnessHeight {

    @XmlElement(name = "ShoulderHarnessMaximumHeight")
    protected LengthDimension shoulderHarnessMaximumHeight;
    @XmlElement(name = "ShoulderHarnessMinimumHeight")
    protected LengthDimension shoulderHarnessMinimumHeight;

    /**
     * Gets the value of the shoulderHarnessMaximumHeight property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getShoulderHarnessMaximumHeight() {
        return shoulderHarnessMaximumHeight;
    }

    /**
     * Sets the value of the shoulderHarnessMaximumHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setShoulderHarnessMaximumHeight(LengthDimension value) {
        this.shoulderHarnessMaximumHeight = value;
    }

    /**
     * Gets the value of the shoulderHarnessMinimumHeight property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getShoulderHarnessMinimumHeight() {
        return shoulderHarnessMinimumHeight;
    }

    /**
     * Sets the value of the shoulderHarnessMinimumHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setShoulderHarnessMinimumHeight(LengthDimension value) {
        this.shoulderHarnessMinimumHeight = value;
    }

}