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
 *         &lt;element ref="{}AdditionalFeatures" minOccurs="0"/&gt;
 *         &lt;element ref="{}CasLatency" minOccurs="0"/&gt;
 *         &lt;element ref="{}ComputerMemoryFormFactor" minOccurs="0"/&gt;
 *         &lt;element ref="{}ComputerMemoryTechnology" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaxMemorySpeed" minOccurs="0"/&gt;
 *         &lt;element ref="{}MemoryCapacityPerSTICK" minOccurs="0"/&gt;
 *         &lt;element ref="{}ModelNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}MultiChannelKit" minOccurs="0"/&gt;
 *         &lt;element ref="{}NumberOfMemorySticks" minOccurs="0"/&gt;
 *         &lt;element ref="{}RAMClockSpeed" minOccurs="0"/&gt;
 *         &lt;element ref="{}Voltage" minOccurs="0"/&gt;
 *         &lt;element ref="{}VoltageRating" minOccurs="0"/&gt;
 *         &lt;element ref="{}Wattage" minOccurs="0"/&gt;
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
    "additionalFeatures",
    "casLatency",
    "computerMemoryFormFactor",
    "computerMemoryTechnology",
    "maxMemorySpeed",
    "memoryCapacityPerSTICK",
    "modelNumber",
    "multiChannelKit",
    "numberOfMemorySticks",
    "ramClockSpeed",
    "voltage",
    "voltageRating",
    "wattage"
})
@XmlRootElement(name = "RamMemory")
public class RamMemory {

    @XmlElement(name = "VariationData")
    protected VariationData variationData;
    @XmlElement(name = "AdditionalFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String additionalFeatures;
    @XmlElement(name = "CasLatency")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger casLatency;
    @XmlElement(name = "ComputerMemoryFormFactor")
    protected String computerMemoryFormFactor;
    @XmlElement(name = "ComputerMemoryTechnology")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String computerMemoryTechnology;
    @XmlElement(name = "MaxMemorySpeed")
    protected BigDecimal maxMemorySpeed;
    @XmlElement(name = "MemoryCapacityPerSTICK")
    protected MemorySizeFiveDigitIntegerDimension memoryCapacityPerSTICK;
    @XmlElement(name = "ModelNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelNumber;
    @XmlElement(name = "MultiChannelKit")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger multiChannelKit;
    @XmlElement(name = "NumberOfMemorySticks")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfMemorySticks;
    @XmlElement(name = "RAMClockSpeed")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger ramClockSpeed;
    @XmlElement(name = "Voltage")
    protected BigDecimal voltage;
    @XmlElement(name = "VoltageRating")
    protected VoltageSevenDigitDecimalDimension voltageRating;
    @XmlElement(name = "Wattage")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger wattage;

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
     * Gets the value of the additionalFeatures property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalFeatures() {
        return additionalFeatures;
    }

    /**
     * Sets the value of the additionalFeatures property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalFeatures(String value) {
        this.additionalFeatures = value;
    }

    /**
     * Gets the value of the casLatency property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCasLatency() {
        return casLatency;
    }

    /**
     * Sets the value of the casLatency property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCasLatency(BigInteger value) {
        this.casLatency = value;
    }

    /**
     * Gets the value of the computerMemoryFormFactor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComputerMemoryFormFactor() {
        return computerMemoryFormFactor;
    }

    /**
     * Sets the value of the computerMemoryFormFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComputerMemoryFormFactor(String value) {
        this.computerMemoryFormFactor = value;
    }

    /**
     * Gets the value of the computerMemoryTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComputerMemoryTechnology() {
        return computerMemoryTechnology;
    }

    /**
     * Sets the value of the computerMemoryTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComputerMemoryTechnology(String value) {
        this.computerMemoryTechnology = value;
    }

    /**
     * Gets the value of the maxMemorySpeed property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxMemorySpeed() {
        return maxMemorySpeed;
    }

    /**
     * Sets the value of the maxMemorySpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxMemorySpeed(BigDecimal value) {
        this.maxMemorySpeed = value;
    }

    /**
     * Gets the value of the memoryCapacityPerSTICK property.
     * 
     * @return
     *     possible object is
     *     {@link MemorySizeFiveDigitIntegerDimension }
     *     
     */
    public MemorySizeFiveDigitIntegerDimension getMemoryCapacityPerSTICK() {
        return memoryCapacityPerSTICK;
    }

    /**
     * Sets the value of the memoryCapacityPerSTICK property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemorySizeFiveDigitIntegerDimension }
     *     
     */
    public void setMemoryCapacityPerSTICK(MemorySizeFiveDigitIntegerDimension value) {
        this.memoryCapacityPerSTICK = value;
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
     * Gets the value of the multiChannelKit property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMultiChannelKit() {
        return multiChannelKit;
    }

    /**
     * Sets the value of the multiChannelKit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMultiChannelKit(BigInteger value) {
        this.multiChannelKit = value;
    }

    /**
     * Gets the value of the numberOfMemorySticks property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfMemorySticks() {
        return numberOfMemorySticks;
    }

    /**
     * Sets the value of the numberOfMemorySticks property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfMemorySticks(BigInteger value) {
        this.numberOfMemorySticks = value;
    }

    /**
     * Gets the value of the ramClockSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRAMClockSpeed() {
        return ramClockSpeed;
    }

    /**
     * Sets the value of the ramClockSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRAMClockSpeed(BigInteger value) {
        this.ramClockSpeed = value;
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
     * Gets the value of the voltageRating property.
     * 
     * @return
     *     possible object is
     *     {@link VoltageSevenDigitDecimalDimension }
     *     
     */
    public VoltageSevenDigitDecimalDimension getVoltageRating() {
        return voltageRating;
    }

    /**
     * Sets the value of the voltageRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link VoltageSevenDigitDecimalDimension }
     *     
     */
    public void setVoltageRating(VoltageSevenDigitDecimalDimension value) {
        this.voltageRating = value;
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

}
