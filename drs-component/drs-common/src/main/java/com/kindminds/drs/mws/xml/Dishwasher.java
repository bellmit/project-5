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
 *         &lt;element name="AnnualEnergyConsumption" type="{}EnergyConsumptionDimension" minOccurs="0"/&gt;
 *         &lt;element name="AnnualWaterConsumption" type="{}WaterConsumptionDimension" minOccurs="0"/&gt;
 *         &lt;element name="CompatibleDevice" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="ControlsType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="CoolingVents" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="CounterDepth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DryerPowerSource" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="DryingPerformanceRating" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="IceCapacity" type="{}IceCapacityDimension" minOccurs="0"/&gt;
 *         &lt;element name="IsPortable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="LightingMethod" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="OptionCycles" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="RecommendedProductUses" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="ShelfType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="StandardCycleCapacity" type="{}CapacityDimension" minOccurs="0"/&gt;
 *         &lt;element name="StandardCycles" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="TrayType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="WasherArms" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="WashingPerformanceRating" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="WaterConsumption" type="{}WaterConsumptionDimension" minOccurs="0"/&gt;
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
    "annualEnergyConsumption",
    "annualWaterConsumption",
    "compatibleDevice",
    "controlsType",
    "coolingVents",
    "counterDepth",
    "dryerPowerSource",
    "dryingPerformanceRating",
    "iceCapacity",
    "isPortable",
    "lightingMethod",
    "optionCycles",
    "recommendedProductUses",
    "shelfType",
    "standardCycleCapacity",
    "standardCycles",
    "trayType",
    "washerArms",
    "washingPerformanceRating",
    "waterConsumption"
})
@XmlRootElement(name = "Dishwasher")
public class Dishwasher {

    @XmlElement(name = "AnnualEnergyConsumption")
    protected EnergyConsumptionDimension annualEnergyConsumption;
    @XmlElement(name = "AnnualWaterConsumption")
    protected WaterConsumptionDimension annualWaterConsumption;
    @XmlElement(name = "CompatibleDevice")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String compatibleDevice;
    @XmlElement(name = "ControlsType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String controlsType;
    @XmlElement(name = "CoolingVents")
    protected BigInteger coolingVents;
    @XmlElement(name = "CounterDepth")
    protected LengthDimension counterDepth;
    @XmlElement(name = "DryerPowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String dryerPowerSource;
    @XmlElement(name = "DryingPerformanceRating")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String dryingPerformanceRating;
    @XmlElement(name = "IceCapacity")
    protected IceCapacityDimension iceCapacity;
    @XmlElement(name = "IsPortable")
    protected Boolean isPortable;
    @XmlElement(name = "LightingMethod")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String lightingMethod;
    @XmlElement(name = "OptionCycles")
    protected BigInteger optionCycles;
    @XmlElement(name = "RecommendedProductUses")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String recommendedProductUses;
    @XmlElement(name = "ShelfType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String shelfType;
    @XmlElement(name = "StandardCycleCapacity")
    protected CapacityDimension standardCycleCapacity;
    @XmlElement(name = "StandardCycles")
    protected BigInteger standardCycles;
    @XmlElement(name = "TrayType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String trayType;
    @XmlElement(name = "WasherArms")
    protected BigInteger washerArms;
    @XmlElement(name = "WashingPerformanceRating")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String washingPerformanceRating;
    @XmlElement(name = "WaterConsumption")
    protected WaterConsumptionDimension waterConsumption;

    /**
     * Gets the value of the annualEnergyConsumption property.
     * 
     * @return
     *     possible object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public EnergyConsumptionDimension getAnnualEnergyConsumption() {
        return annualEnergyConsumption;
    }

    /**
     * Sets the value of the annualEnergyConsumption property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public void setAnnualEnergyConsumption(EnergyConsumptionDimension value) {
        this.annualEnergyConsumption = value;
    }

    /**
     * Gets the value of the annualWaterConsumption property.
     * 
     * @return
     *     possible object is
     *     {@link WaterConsumptionDimension }
     *     
     */
    public WaterConsumptionDimension getAnnualWaterConsumption() {
        return annualWaterConsumption;
    }

    /**
     * Sets the value of the annualWaterConsumption property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaterConsumptionDimension }
     *     
     */
    public void setAnnualWaterConsumption(WaterConsumptionDimension value) {
        this.annualWaterConsumption = value;
    }

    /**
     * Gets the value of the compatibleDevice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompatibleDevice() {
        return compatibleDevice;
    }

    /**
     * Sets the value of the compatibleDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompatibleDevice(String value) {
        this.compatibleDevice = value;
    }

    /**
     * Gets the value of the controlsType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlsType() {
        return controlsType;
    }

    /**
     * Sets the value of the controlsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlsType(String value) {
        this.controlsType = value;
    }

    /**
     * Gets the value of the coolingVents property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCoolingVents() {
        return coolingVents;
    }

    /**
     * Sets the value of the coolingVents property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCoolingVents(BigInteger value) {
        this.coolingVents = value;
    }

    /**
     * Gets the value of the counterDepth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getCounterDepth() {
        return counterDepth;
    }

    /**
     * Sets the value of the counterDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setCounterDepth(LengthDimension value) {
        this.counterDepth = value;
    }

    /**
     * Gets the value of the dryerPowerSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDryerPowerSource() {
        return dryerPowerSource;
    }

    /**
     * Sets the value of the dryerPowerSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDryerPowerSource(String value) {
        this.dryerPowerSource = value;
    }

    /**
     * Gets the value of the dryingPerformanceRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDryingPerformanceRating() {
        return dryingPerformanceRating;
    }

    /**
     * Sets the value of the dryingPerformanceRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDryingPerformanceRating(String value) {
        this.dryingPerformanceRating = value;
    }

    /**
     * Gets the value of the iceCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link IceCapacityDimension }
     *     
     */
    public IceCapacityDimension getIceCapacity() {
        return iceCapacity;
    }

    /**
     * Sets the value of the iceCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link IceCapacityDimension }
     *     
     */
    public void setIceCapacity(IceCapacityDimension value) {
        this.iceCapacity = value;
    }

    /**
     * Gets the value of the isPortable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPortable() {
        return isPortable;
    }

    /**
     * Sets the value of the isPortable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPortable(Boolean value) {
        this.isPortable = value;
    }

    /**
     * Gets the value of the lightingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLightingMethod() {
        return lightingMethod;
    }

    /**
     * Sets the value of the lightingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLightingMethod(String value) {
        this.lightingMethod = value;
    }

    /**
     * Gets the value of the optionCycles property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOptionCycles() {
        return optionCycles;
    }

    /**
     * Sets the value of the optionCycles property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOptionCycles(BigInteger value) {
        this.optionCycles = value;
    }

    /**
     * Gets the value of the recommendedProductUses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecommendedProductUses() {
        return recommendedProductUses;
    }

    /**
     * Sets the value of the recommendedProductUses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecommendedProductUses(String value) {
        this.recommendedProductUses = value;
    }

    /**
     * Gets the value of the shelfType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShelfType() {
        return shelfType;
    }

    /**
     * Sets the value of the shelfType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShelfType(String value) {
        this.shelfType = value;
    }

    /**
     * Gets the value of the standardCycleCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link CapacityDimension }
     *     
     */
    public CapacityDimension getStandardCycleCapacity() {
        return standardCycleCapacity;
    }

    /**
     * Sets the value of the standardCycleCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link CapacityDimension }
     *     
     */
    public void setStandardCycleCapacity(CapacityDimension value) {
        this.standardCycleCapacity = value;
    }

    /**
     * Gets the value of the standardCycles property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStandardCycles() {
        return standardCycles;
    }

    /**
     * Sets the value of the standardCycles property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStandardCycles(BigInteger value) {
        this.standardCycles = value;
    }

    /**
     * Gets the value of the trayType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrayType() {
        return trayType;
    }

    /**
     * Sets the value of the trayType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrayType(String value) {
        this.trayType = value;
    }

    /**
     * Gets the value of the washerArms property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWasherArms() {
        return washerArms;
    }

    /**
     * Sets the value of the washerArms property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWasherArms(BigInteger value) {
        this.washerArms = value;
    }

    /**
     * Gets the value of the washingPerformanceRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWashingPerformanceRating() {
        return washingPerformanceRating;
    }

    /**
     * Sets the value of the washingPerformanceRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWashingPerformanceRating(String value) {
        this.washingPerformanceRating = value;
    }

    /**
     * Gets the value of the waterConsumption property.
     * 
     * @return
     *     possible object is
     *     {@link WaterConsumptionDimension }
     *     
     */
    public WaterConsumptionDimension getWaterConsumption() {
        return waterConsumption;
    }

    /**
     * Sets the value of the waterConsumption property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaterConsumptionDimension }
     *     
     */
    public void setWaterConsumption(WaterConsumptionDimension value) {
        this.waterConsumption = value;
    }

}
