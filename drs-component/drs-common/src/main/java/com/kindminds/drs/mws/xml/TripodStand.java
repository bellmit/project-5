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
 *               &lt;enumeration value="still-cameras"/&gt;
 *               &lt;enumeration value="camcorders"/&gt;
 *               &lt;enumeration value="still-camera-and-camcorders"/&gt;
 *               &lt;enumeration value="telescopes"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="StandType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="camera-stands"/&gt;
 *               &lt;enumeration value="monopods"/&gt;
 *               &lt;enumeration value="tripods"/&gt;
 *               &lt;enumeration value="car-window-mounts"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="SpecificUses" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="tabletop"/&gt;
 *               &lt;enumeration value="travel"/&gt;
 *               &lt;enumeration value="hiking-and-outdoors"/&gt;
 *               &lt;enumeration value="hunting-and-shooting"/&gt;
 *               &lt;enumeration value="sports"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Material" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="aluminum"/&gt;
 *               &lt;enumeration value="carbon-fiber"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="HeadType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="geared-heads"/&gt;
 *               &lt;enumeration value="ball-heads"/&gt;
 *               &lt;enumeration value="camera-rotator-heads"/&gt;
 *               &lt;enumeration value="pan-and-tilt-heads"/&gt;
 *               &lt;enumeration value="video-heads"/&gt;
 *               &lt;enumeration value="3-way-heads"/&gt;
 *               &lt;enumeration value="panoramic-heads"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PackageType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="head-only"/&gt;
 *               &lt;enumeration value="legs-only"/&gt;
 *               &lt;enumeration value="head-and-leg-units"/&gt;
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
    "standType",
    "specificUses",
    "material",
    "headType",
    "packageType"
})
@XmlRootElement(name = "TripodStand")
public class TripodStand {

    @XmlElement(name = "ForUseWith")
    protected String forUseWith;
    @XmlElement(name = "StandType")
    protected String standType;
    @XmlElement(name = "SpecificUses")
    protected String specificUses;
    @XmlElement(name = "Material")
    protected String material;
    @XmlElement(name = "HeadType")
    protected String headType;
    @XmlElement(name = "PackageType")
    protected String packageType;

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
     * Gets the value of the standType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandType() {
        return standType;
    }

    /**
     * Sets the value of the standType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandType(String value) {
        this.standType = value;
    }

    /**
     * Gets the value of the specificUses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecificUses() {
        return specificUses;
    }

    /**
     * Sets the value of the specificUses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecificUses(String value) {
        this.specificUses = value;
    }

    /**
     * Gets the value of the material property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Sets the value of the material property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterial(String value) {
        this.material = value;
    }

    /**
     * Gets the value of the headType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeadType() {
        return headType;
    }

    /**
     * Sets the value of the headType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeadType(String value) {
        this.headType = value;
    }

    /**
     * Gets the value of the packageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageType() {
        return packageType;
    }

    /**
     * Sets the value of the packageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageType(String value) {
        this.packageType = value;
    }

}
