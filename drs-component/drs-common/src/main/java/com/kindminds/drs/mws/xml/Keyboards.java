//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element ref="{}VariationData" minOccurs="0"/&gt;
 *         &lt;element ref="{}HandOrientation" minOccurs="0"/&gt;
 *         &lt;element ref="{}InputDeviceDesignStyle" minOccurs="0"/&gt;
 *         &lt;element ref="{}KeyboardDescription" minOccurs="0"/&gt;
 *         &lt;element ref="{}ModelNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}Voltage" minOccurs="0"/&gt;
 *         &lt;element ref="{}Wattage" minOccurs="0"/&gt;
 *         &lt;element ref="{}WirelessInputDeviceProtocol" minOccurs="0"/&gt;
 *         &lt;element ref="{}WirelessInputDeviceTechnology" minOccurs="0"/&gt;
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
    "variationData",
    "handOrientation",
    "inputDeviceDesignStyle",
    "keyboardDescription",
    "modelNumber",
    "voltage",
    "wattage",
    "wirelessInputDeviceProtocol",
    "wirelessInputDeviceTechnology"
})
@XmlRootElement(name = "Keyboards")
public class Keyboards {

    @XmlElement(name = "VariationData")
    protected VariationData variationData;
    @XmlElement(name = "HandOrientation")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String handOrientation;
    @XmlElement(name = "InputDeviceDesignStyle")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String inputDeviceDesignStyle;
    @XmlElement(name = "KeyboardDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String keyboardDescription;
    @XmlElement(name = "ModelNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelNumber;
    @XmlElement(name = "Voltage")
    protected BigDecimal voltage;
    @XmlElement(name = "Wattage")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger wattage;
    @XmlElement(name = "WirelessInputDeviceProtocol")
    @XmlSchemaType(name = "string")
    protected WirelessInputDeviceProtocolTypeValues wirelessInputDeviceProtocol;
    @XmlElement(name = "WirelessInputDeviceTechnology")
    @XmlSchemaType(name = "string")
    protected WirelessInputDeviceTechnologyTypeValues wirelessInputDeviceTechnology;

    /**
     * Gets the value of the variationData property.
     * 
     * @return
     *     possible object is
     *     {@link VariationData }
     *     
     */
    public VariationData getVariationData() {
        return variationData;
    }

    /**
     * Sets the value of the variationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link VariationData }
     *     
     */
    public void setVariationData(VariationData value) {
        this.variationData = value;
    }

    /**
     * Gets the value of the handOrientation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHandOrientation() {
        return handOrientation;
    }

    /**
     * Sets the value of the handOrientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHandOrientation(String value) {
        this.handOrientation = value;
    }

    /**
     * Gets the value of the inputDeviceDesignStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputDeviceDesignStyle() {
        return inputDeviceDesignStyle;
    }

    /**
     * Sets the value of the inputDeviceDesignStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputDeviceDesignStyle(String value) {
        this.inputDeviceDesignStyle = value;
    }

    /**
     * Gets the value of the keyboardDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyboardDescription() {
        return keyboardDescription;
    }

    /**
     * Sets the value of the keyboardDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyboardDescription(String value) {
        this.keyboardDescription = value;
    }

    /**
     * Gets the value of the modelNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * Sets the value of the modelNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelNumber(String value) {
        this.modelNumber = value;
    }

    /**
     * Gets the value of the voltage property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVoltage() {
        return voltage;
    }

    /**
     * Sets the value of the voltage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVoltage(BigDecimal value) {
        this.voltage = value;
    }

    /**
     * Gets the value of the wattage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWattage() {
        return wattage;
    }

    /**
     * Sets the value of the wattage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWattage(BigInteger value) {
        this.wattage = value;
    }

    /**
     * Gets the value of the wirelessInputDeviceProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link WirelessInputDeviceProtocolTypeValues }
     *     
     */
    public WirelessInputDeviceProtocolTypeValues getWirelessInputDeviceProtocol() {
        return wirelessInputDeviceProtocol;
    }

    /**
     * Sets the value of the wirelessInputDeviceProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link WirelessInputDeviceProtocolTypeValues }
     *     
     */
    public void setWirelessInputDeviceProtocol(WirelessInputDeviceProtocolTypeValues value) {
        this.wirelessInputDeviceProtocol = value;
    }

    /**
     * Gets the value of the wirelessInputDeviceTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link WirelessInputDeviceTechnologyTypeValues }
     *     
     */
    public WirelessInputDeviceTechnologyTypeValues getWirelessInputDeviceTechnology() {
        return wirelessInputDeviceTechnology;
    }

    /**
     * Sets the value of the wirelessInputDeviceTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link WirelessInputDeviceTechnologyTypeValues }
     *     
     */
    public void setWirelessInputDeviceTechnology(WirelessInputDeviceTechnologyTypeValues value) {
        this.wirelessInputDeviceTechnology = value;
    }

}
