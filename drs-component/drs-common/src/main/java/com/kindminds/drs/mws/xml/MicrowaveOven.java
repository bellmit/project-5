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
 *         &lt;element name="ChamberVolume" type="{}WaterConsumptionDimension" minOccurs="0"/&gt;
 *         &lt;element name="ControlsType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="EnergyConsumptionConvection" type="{}EnergyConsumptionDimension" minOccurs="0"/&gt;
 *         &lt;element name="EnergyConsumptionStandard" type="{}EnergyConsumptionDimension" minOccurs="0"/&gt;
 *         &lt;element name="FuelType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="LightingMethod" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="Racks" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="RecommendedProductUses" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="ShelfType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="VolumeCapacityName" type="{}StringNotNull" minOccurs="0"/&gt;
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
    "chamberVolume",
    "controlsType",
    "energyConsumptionConvection",
    "energyConsumptionStandard",
    "fuelType",
    "lightingMethod",
    "racks",
    "recommendedProductUses",
    "shelfType",
    "volumeCapacityName"
})
@XmlRootElement(name = "MicrowaveOven")
public class MicrowaveOven {

    @XmlElement(name = "ChamberVolume")
    protected WaterConsumptionDimension chamberVolume;
    @XmlElement(name = "ControlsType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String controlsType;
    @XmlElement(name = "EnergyConsumptionConvection")
    protected EnergyConsumptionDimension energyConsumptionConvection;
    @XmlElement(name = "EnergyConsumptionStandard")
    protected EnergyConsumptionDimension energyConsumptionStandard;
    @XmlElement(name = "FuelType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fuelType;
    @XmlElement(name = "LightingMethod")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String lightingMethod;
    @XmlElement(name = "Racks")
    protected BigInteger racks;
    @XmlElement(name = "RecommendedProductUses")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String recommendedProductUses;
    @XmlElement(name = "ShelfType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String shelfType;
    @XmlElement(name = "VolumeCapacityName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String volumeCapacityName;

    /**
     * Gets the value of the chamberVolume property.
     * 
     * @return
     *     possible object is
     *     {@link WaterConsumptionDimension }
     *     
     */
    public WaterConsumptionDimension getChamberVolume() {
        return chamberVolume;
    }

    /**
     * Sets the value of the chamberVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaterConsumptionDimension }
     *     
     */
    public void setChamberVolume(WaterConsumptionDimension value) {
        this.chamberVolume = value;
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
     * Gets the value of the energyConsumptionConvection property.
     * 
     * @return
     *     possible object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public EnergyConsumptionDimension getEnergyConsumptionConvection() {
        return energyConsumptionConvection;
    }

    /**
     * Sets the value of the energyConsumptionConvection property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public void setEnergyConsumptionConvection(EnergyConsumptionDimension value) {
        this.energyConsumptionConvection = value;
    }

    /**
     * Gets the value of the energyConsumptionStandard property.
     * 
     * @return
     *     possible object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public EnergyConsumptionDimension getEnergyConsumptionStandard() {
        return energyConsumptionStandard;
    }

    /**
     * Sets the value of the energyConsumptionStandard property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public void setEnergyConsumptionStandard(EnergyConsumptionDimension value) {
        this.energyConsumptionStandard = value;
    }

    /**
     * Gets the value of the fuelType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Sets the value of the fuelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuelType(String value) {
        this.fuelType = value;
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
     * Gets the value of the racks property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRacks() {
        return racks;
    }

    /**
     * Sets the value of the racks property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRacks(BigInteger value) {
        this.racks = value;
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
     * Gets the value of the volumeCapacityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeCapacityName() {
        return volumeCapacityName;
    }

    /**
     * Sets the value of the volumeCapacityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeCapacityName(String value) {
        this.volumeCapacityName = value;
    }

}
