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
 *         &lt;element name="DisplaySize" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="Touchscreen" type="{}TouchscreenTypeValues" minOccurs="0"/&gt;
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
    "displaySize",
    "touchscreen"
})
@XmlRootElement(name = "DigitalFrame")
public class DigitalFrame {

    @XmlElement(name = "DisplaySize")
    protected LengthDimension displaySize;
    @XmlElement(name = "Touchscreen")
    @XmlSchemaType(name = "string")
    protected TouchscreenTypeValues touchscreen;

    /**
     * Gets the value of the displaySize property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getDisplaySize() {
        return displaySize;
    }

    /**
     * Sets the value of the displaySize property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setDisplaySize(LengthDimension value) {
        this.displaySize = value;
    }

    /**
     * Gets the value of the touchscreen property.
     * 
     * @return
     *     possible object is
     *     {@link TouchscreenTypeValues }
     *     
     */
    public TouchscreenTypeValues getTouchscreen() {
        return touchscreen;
    }

    /**
     * Sets the value of the touchscreen property.
     * 
     * @param value
     *     allowed object is
     *     {@link TouchscreenTypeValues }
     *     
     */
    public void setTouchscreen(TouchscreenTypeValues value) {
        this.touchscreen = value;
    }

}