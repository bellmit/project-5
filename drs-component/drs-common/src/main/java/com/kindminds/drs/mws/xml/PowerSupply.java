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
 *         &lt;element name="ForUseWith" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="film-cameras"/&gt;
 *               &lt;enumeration value="digital-cameras"/&gt;
 *               &lt;enumeration value="analog-camcorders-general"/&gt;
 *               &lt;enumeration value="8mm-camcorders"/&gt;
 *               &lt;enumeration value="betacam-sp-camcorders"/&gt;
 *               &lt;enumeration value="hi-8-camcorders"/&gt;
 *               &lt;enumeration value="s-vhs-camcorders"/&gt;
 *               &lt;enumeration value="s-vhs-c-camcorders"/&gt;
 *               &lt;enumeration value="vhs-camcorders"/&gt;
 *               &lt;enumeration value="vhs-c-camcorders"/&gt;
 *               &lt;enumeration value="other-analog-formats-camcorders"/&gt;
 *               &lt;enumeration value="digital-camcorders-general"/&gt;
 *               &lt;enumeration value="digital-betacam-camcorders"/&gt;
 *               &lt;enumeration value="dv-camcorders"/&gt;
 *               &lt;enumeration value="dvcam-camcorders"/&gt;
 *               &lt;enumeration value="dvcpro-camcorders"/&gt;
 *               &lt;enumeration value="minidv-camcorders"/&gt;
 *               &lt;enumeration value="micromv-camcorders"/&gt;
 *               &lt;enumeration value="digital8-camcorders"/&gt;
 *               &lt;enumeration value="dvd-camcorders"/&gt;
 *               &lt;enumeration value="minidisc-camcorders"/&gt;
 *               &lt;enumeration value="other-digital-formats-camcorders"/&gt;
 *               &lt;enumeration value="flashes"/&gt;
 *               &lt;enumeration value="lighting"/&gt;
 *               &lt;enumeration value="surveillence-products"/&gt;
 *               &lt;enumeration value="other-products"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CameraPowerSupplyType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="batteries-general"/&gt;
 *               &lt;enumeration value="disposable-batteries"/&gt;
 *               &lt;enumeration value="rechargeable-Batteries"/&gt;
 *               &lt;enumeration value="external-batteries"/&gt;
 *               &lt;enumeration value="battery-packs-general"/&gt;
 *               &lt;enumeration value="shoulder-battery-packs"/&gt;
 *               &lt;enumeration value="belt-battery-packs"/&gt;
 *               &lt;enumeration value="dedicated-battery-packs"/&gt;
 *               &lt;enumeration value="other-batteries-and-packs"/&gt;
 *               &lt;enumeration value="adapters-general"/&gt;
 *               &lt;enumeration value="ac-adapters"/&gt;
 *               &lt;enumeration value="dc-adapters"/&gt;
 *               &lt;enumeration value="battery-chargers"/&gt;
 *               &lt;enumeration value="ac-power-supply"/&gt;
 *               &lt;enumeration value="dc-power-supply"/&gt;
 *               &lt;enumeration value="other-power-supplies"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="BatteryChemicalType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="lead-acid"/&gt;
 *               &lt;enumeration value="lithium"/&gt;
 *               &lt;enumeration value="lithium-ion"/&gt;
 *               &lt;enumeration value="nickel-metal-hydride"/&gt;
 *               &lt;enumeration value="nicd"/&gt;
 *               &lt;enumeration value="silver-oxide"/&gt;
 *               &lt;enumeration value="alkaline"/&gt;
 *               &lt;enumeration value="other-battery-types"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PowerSupplyAccessories" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="battery-holders"/&gt;
 *               &lt;enumeration value="battery-testers"/&gt;
 *               &lt;enumeration value="mounting-plates"/&gt;
 *               &lt;enumeration value="cables-general"/&gt;
 *               &lt;enumeration value="battery-power-cables"/&gt;
 *               &lt;enumeration value="power-supply-cables"/&gt;
 *               &lt;enumeration value="charger-cables"/&gt;
 *               &lt;enumeration value="adapter-cables"/&gt;
 *               &lt;enumeration value="other-cables"/&gt;
 *               &lt;enumeration value="cigarette-connectors"/&gt;
 *               &lt;enumeration value="xlr-connectors"/&gt;
 *               &lt;enumeration value="dc-couplers"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
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
    "forUseWith",
    "cameraPowerSupplyType",
    "batteryChemicalType",
    "powerSupplyAccessories"
})
@XmlRootElement(name = "PowerSupply")
public class PowerSupply {

    @XmlElement(name = "ForUseWith")
    protected String forUseWith;
    @XmlElement(name = "CameraPowerSupplyType")
    protected String cameraPowerSupplyType;
    @XmlElement(name = "BatteryChemicalType")
    protected String batteryChemicalType;
    @XmlElement(name = "PowerSupplyAccessories")
    protected String powerSupplyAccessories;

    /**
     * Gets the value of the forUseWith property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForUseWith() {
        return forUseWith;
    }

    /**
     * Sets the value of the forUseWith property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForUseWith(String value) {
        this.forUseWith = value;
    }

    /**
     * Gets the value of the cameraPowerSupplyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCameraPowerSupplyType() {
        return cameraPowerSupplyType;
    }

    /**
     * Sets the value of the cameraPowerSupplyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCameraPowerSupplyType(String value) {
        this.cameraPowerSupplyType = value;
    }

    /**
     * Gets the value of the batteryChemicalType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatteryChemicalType() {
        return batteryChemicalType;
    }

    /**
     * Sets the value of the batteryChemicalType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatteryChemicalType(String value) {
        this.batteryChemicalType = value;
    }

    /**
     * Gets the value of the powerSupplyAccessories property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerSupplyAccessories() {
        return powerSupplyAccessories;
    }

    /**
     * Sets the value of the powerSupplyAccessories property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerSupplyAccessories(String value) {
        this.powerSupplyAccessories = value;
    }

}