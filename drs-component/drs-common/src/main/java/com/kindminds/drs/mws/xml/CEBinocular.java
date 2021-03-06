//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

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
 *         &lt;element ref="{}BatteryCellType" minOccurs="0"/&gt;
 *         &lt;element name="BatteryChargeCycles" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element ref="{}BatteryPower" minOccurs="0"/&gt;
 *         &lt;element ref="{}BoxContents" minOccurs="0"/&gt;
 *         &lt;element ref="{}CoatingDescription" minOccurs="0"/&gt;
 *         &lt;element ref="{}DigitalZoom" minOccurs="0"/&gt;
 *         &lt;element ref="{}EffectiveStillResolution" minOccurs="0"/&gt;
 *         &lt;element ref="{}FinishType" minOccurs="0"/&gt;
 *         &lt;element ref="{}FocusType" minOccurs="0"/&gt;
 *         &lt;element ref="{}ImageStabilization" minOccurs="0"/&gt;
 *         &lt;element name="Lens" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MemorySlotsAvailable" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaxFocalLength" minOccurs="0"/&gt;
 *         &lt;element name="PowerSource" type="{}FortyStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="RemovableMemory" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="SoftwareIncluded" type="{}LongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}ViewFinderType" minOccurs="0"/&gt;
 *         &lt;element ref="{}Efficiency" minOccurs="0"/&gt;
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
    "batteryCellType",
    "batteryChargeCycles",
    "batteryPower",
    "boxContents",
    "coatingDescription",
    "digitalZoom",
    "effectiveStillResolution",
    "finishType",
    "focusType",
    "imageStabilization",
    "lens",
    "memorySlotsAvailable",
    "maxFocalLength",
    "powerSource",
    "removableMemory",
    "softwareIncluded",
    "viewFinderType",
    "efficiency"
})
@XmlRootElement(name = "CEBinocular")
public class CEBinocular {

    @XmlElement(name = "BatteryCellType")
    @XmlSchemaType(name = "string")
    protected BatteryCellTypeValues batteryCellType;
    @XmlElement(name = "BatteryChargeCycles")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger batteryChargeCycles;
    @XmlElement(name = "BatteryPower")
    protected BatteryPowerIntegerDimension batteryPower;
    @XmlElement(name = "BoxContents")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String boxContents;
    @XmlElement(name = "CoatingDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String coatingDescription;
    @XmlElement(name = "DigitalZoom")
    protected ZoomDimension digitalZoom;
    @XmlElement(name = "EffectiveStillResolution")
    protected ResolutionDimension effectiveStillResolution;
    @XmlElement(name = "FinishType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String finishType;
    @XmlElement(name = "FocusType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String focusType;
    @XmlElement(name = "ImageStabilization")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String imageStabilization;
    @XmlElement(name = "Lens")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String lens;
    @XmlElement(name = "MemorySlotsAvailable")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String memorySlotsAvailable;
    @XmlElement(name = "MaxFocalLength")
    protected LengthDimension maxFocalLength;
    @XmlElement(name = "PowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String powerSource;
    @XmlElement(name = "RemovableMemory")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String removableMemory;
    @XmlElement(name = "SoftwareIncluded")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String softwareIncluded;
    @XmlElement(name = "ViewFinderType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String viewFinderType;
    @XmlElement(name = "Efficiency")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String efficiency;

    /**
     * Gets the value of the batteryCellType property.
     * 
     * @return
     *     possible object is
     *     {@link BatteryCellTypeValues }
     *     
     */
    public BatteryCellTypeValues getBatteryCellType() {
        return batteryCellType;
    }

    /**
     * Sets the value of the batteryCellType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatteryCellTypeValues }
     *     
     */
    public void setBatteryCellType(BatteryCellTypeValues value) {
        this.batteryCellType = value;
    }

    /**
     * Gets the value of the batteryChargeCycles property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBatteryChargeCycles() {
        return batteryChargeCycles;
    }

    /**
     * Sets the value of the batteryChargeCycles property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBatteryChargeCycles(BigInteger value) {
        this.batteryChargeCycles = value;
    }

    /**
     * Gets the value of the batteryPower property.
     * 
     * @return
     *     possible object is
     *     {@link BatteryPowerIntegerDimension }
     *     
     */
    public BatteryPowerIntegerDimension getBatteryPower() {
        return batteryPower;
    }

    /**
     * Sets the value of the batteryPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatteryPowerIntegerDimension }
     *     
     */
    public void setBatteryPower(BatteryPowerIntegerDimension value) {
        this.batteryPower = value;
    }

    /**
     * Gets the value of the boxContents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoxContents() {
        return boxContents;
    }

    /**
     * Sets the value of the boxContents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoxContents(String value) {
        this.boxContents = value;
    }

    /**
     * Gets the value of the coatingDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoatingDescription() {
        return coatingDescription;
    }

    /**
     * Sets the value of the coatingDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoatingDescription(String value) {
        this.coatingDescription = value;
    }

    /**
     * Gets the value of the digitalZoom property.
     * 
     * @return
     *     possible object is
     *     {@link ZoomDimension }
     *     
     */
    public ZoomDimension getDigitalZoom() {
        return digitalZoom;
    }

    /**
     * Sets the value of the digitalZoom property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoomDimension }
     *     
     */
    public void setDigitalZoom(ZoomDimension value) {
        this.digitalZoom = value;
    }

    /**
     * Gets the value of the effectiveStillResolution property.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionDimension }
     *     
     */
    public ResolutionDimension getEffectiveStillResolution() {
        return effectiveStillResolution;
    }

    /**
     * Sets the value of the effectiveStillResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionDimension }
     *     
     */
    public void setEffectiveStillResolution(ResolutionDimension value) {
        this.effectiveStillResolution = value;
    }

    /**
     * Gets the value of the finishType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinishType() {
        return finishType;
    }

    /**
     * Sets the value of the finishType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinishType(String value) {
        this.finishType = value;
    }

    /**
     * Gets the value of the focusType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFocusType() {
        return focusType;
    }

    /**
     * Sets the value of the focusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFocusType(String value) {
        this.focusType = value;
    }

    /**
     * Gets the value of the imageStabilization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageStabilization() {
        return imageStabilization;
    }

    /**
     * Sets the value of the imageStabilization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageStabilization(String value) {
        this.imageStabilization = value;
    }

    /**
     * Gets the value of the lens property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLens() {
        return lens;
    }

    /**
     * Sets the value of the lens property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLens(String value) {
        this.lens = value;
    }

    /**
     * Gets the value of the memorySlotsAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemorySlotsAvailable() {
        return memorySlotsAvailable;
    }

    /**
     * Sets the value of the memorySlotsAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemorySlotsAvailable(String value) {
        this.memorySlotsAvailable = value;
    }

    /**
     * Gets the value of the maxFocalLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getMaxFocalLength() {
        return maxFocalLength;
    }

    /**
     * Sets the value of the maxFocalLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setMaxFocalLength(LengthDimension value) {
        this.maxFocalLength = value;
    }

    /**
     * Gets the value of the powerSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerSource() {
        return powerSource;
    }

    /**
     * Sets the value of the powerSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerSource(String value) {
        this.powerSource = value;
    }

    /**
     * Gets the value of the removableMemory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemovableMemory() {
        return removableMemory;
    }

    /**
     * Sets the value of the removableMemory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemovableMemory(String value) {
        this.removableMemory = value;
    }

    /**
     * Gets the value of the softwareIncluded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoftwareIncluded() {
        return softwareIncluded;
    }

    /**
     * Sets the value of the softwareIncluded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoftwareIncluded(String value) {
        this.softwareIncluded = value;
    }

    /**
     * Gets the value of the viewFinderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViewFinderType() {
        return viewFinderType;
    }

    /**
     * Sets the value of the viewFinderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViewFinderType(String value) {
        this.viewFinderType = value;
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

}
