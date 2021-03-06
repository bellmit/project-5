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
 *         &lt;element name="MaximumOperatingDistance" type="{}LengthIntegerDimension" minOccurs="0"/&gt;
 *         &lt;element name="AutomotiveRegionId" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="BodyStyle" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="InteriorColorMap" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="InteriorColorName" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MaximumCompatibleNumberOfSeats" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ModelYearRange" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MultimediaFunctionality" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="TransmissionType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Configuration" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="DriveSystem" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="EngineType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="EnvironmentalDescription" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="FuelType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="IncludedFeatures" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="Indications" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="MaximumOutputPower" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="Model" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="ModelName" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="ModelYear" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfDoors" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="OfferAdditionalDetails" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="PreviousOwnerCount" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="SpecialFeatures" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="StyleName" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="TireType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="MaximumHorsepower" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="WheelMaterial" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="VariationData" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Parentage"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;enumeration value="parent"/&gt;
 *                         &lt;enumeration value="child"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="VariationTheme" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;enumeration value="stylename-colorname-configuration"/&gt;
 *                         &lt;enumeration value="stylename-configuration-colorname-interiorcolorname"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EuEnergyLabelEfficiencyClass" type="{}EnergyLabelEfficiencyClass" minOccurs="0"/&gt;
 *         &lt;element name="Acceleration" type="{}AccelerationUnit" minOccurs="0"/&gt;
 *         &lt;element name="Mileage" type="{}MileageUnit" minOccurs="0"/&gt;
 *         &lt;element name="Co2Emission" type="{}LMVEmissionsDimension" minOccurs="0"/&gt;
 *         &lt;element name="FuelEconomyCity" type="{}FuelDimension" minOccurs="0"/&gt;
 *         &lt;element name="FuelEconomyHighway" type="{}FuelDimension" minOccurs="0"/&gt;
 *         &lt;element name="FuelEconomyCombined" type="{}FuelDimension" minOccurs="0"/&gt;
 *         &lt;element name="EngineDisplacement" type="{}EngineDisplacementUnit" minOccurs="0"/&gt;
 *         &lt;element name="FuelCapacity" type="{}FuelCapacityUnit" minOccurs="0"/&gt;
 *         &lt;element name="MaximumSpeed" type="{}MaximumSpeedUnit" minOccurs="0"/&gt;
 *         &lt;element name="WheelBase" type="{}CycleLengthDimension" minOccurs="0"/&gt;
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
    "maximumOperatingDistance",
    "automotiveRegionId",
    "bodyStyle",
    "interiorColorMap",
    "interiorColorName",
    "maximumCompatibleNumberOfSeats",
    "modelYearRange",
    "multimediaFunctionality",
    "transmissionType",
    "configuration",
    "driveSystem",
    "engineType",
    "environmentalDescription",
    "fuelType",
    "includedFeatures",
    "indications",
    "maximumOutputPower",
    "model",
    "modelName",
    "modelYear",
    "numberOfDoors",
    "offerAdditionalDetails",
    "previousOwnerCount",
    "specialFeatures",
    "styleName",
    "tireType",
    "maximumHorsepower",
    "wheelMaterial",
    "variationData",
    "euEnergyLabelEfficiencyClass",
    "acceleration",
    "mileage",
    "co2Emission",
    "fuelEconomyCity",
    "fuelEconomyHighway",
    "fuelEconomyCombined",
    "engineDisplacement",
    "fuelCapacity",
    "maximumSpeed",
    "wheelBase"
})
@XmlRootElement(name = "LightMotorVehicle")
public class LightMotorVehicle {

    @XmlElement(name = "MaximumOperatingDistance")
    protected LengthIntegerDimension maximumOperatingDistance;
    @XmlElement(name = "AutomotiveRegionId")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String automotiveRegionId;
    @XmlElement(name = "BodyStyle")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String bodyStyle;
    @XmlElement(name = "InteriorColorMap")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String interiorColorMap;
    @XmlElement(name = "InteriorColorName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String interiorColorName;
    @XmlElement(name = "MaximumCompatibleNumberOfSeats")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String maximumCompatibleNumberOfSeats;
    @XmlElement(name = "ModelYearRange")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelYearRange;
    @XmlElement(name = "MultimediaFunctionality")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String multimediaFunctionality;
    @XmlElement(name = "TransmissionType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String transmissionType;
    @XmlElement(name = "Configuration")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String configuration;
    @XmlElement(name = "DriveSystem")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String driveSystem;
    @XmlElement(name = "EngineType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String engineType;
    @XmlElement(name = "EnvironmentalDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String environmentalDescription;
    @XmlElement(name = "FuelType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fuelType;
    @XmlElement(name = "IncludedFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String includedFeatures;
    @XmlElement(name = "Indications")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String indications;
    @XmlElement(name = "MaximumOutputPower")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String maximumOutputPower;
    @XmlElement(name = "Model")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String model;
    @XmlElement(name = "ModelName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelName;
    @XmlElement(name = "ModelYear")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelYear;
    @XmlElement(name = "NumberOfDoors")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String numberOfDoors;
    @XmlElement(name = "OfferAdditionalDetails")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String offerAdditionalDetails;
    @XmlElement(name = "PreviousOwnerCount")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String previousOwnerCount;
    @XmlElement(name = "SpecialFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String specialFeatures;
    @XmlElement(name = "StyleName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String styleName;
    @XmlElement(name = "TireType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String tireType;
    @XmlElement(name = "MaximumHorsepower")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String maximumHorsepower;
    @XmlElement(name = "WheelMaterial")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String wheelMaterial;
    @XmlElement(name = "VariationData")
    protected LightMotorVehicle.VariationData variationData;
    @XmlElement(name = "EuEnergyLabelEfficiencyClass")
    @XmlSchemaType(name = "string")
    protected EnergyLabelEfficiencyClass euEnergyLabelEfficiencyClass;
    @XmlElement(name = "Acceleration")
    protected AccelerationUnit acceleration;
    @XmlElement(name = "Mileage")
    protected MileageUnit mileage;
    @XmlElement(name = "Co2Emission")
    protected LMVEmissionsDimension co2Emission;
    @XmlElement(name = "FuelEconomyCity")
    protected FuelDimension fuelEconomyCity;
    @XmlElement(name = "FuelEconomyHighway")
    protected FuelDimension fuelEconomyHighway;
    @XmlElement(name = "FuelEconomyCombined")
    protected FuelDimension fuelEconomyCombined;
    @XmlElement(name = "EngineDisplacement")
    protected EngineDisplacementUnit engineDisplacement;
    @XmlElement(name = "FuelCapacity")
    protected FuelCapacityUnit fuelCapacity;
    @XmlElement(name = "MaximumSpeed")
    protected MaximumSpeedUnit maximumSpeed;
    @XmlElement(name = "WheelBase")
    protected CycleLengthDimension wheelBase;

    /**
     * Gets the value of the maximumOperatingDistance property.
     * 
     * @return
     *     possible object is
     *     {@link LengthIntegerDimension }
     *     
     */
    public LengthIntegerDimension getMaximumOperatingDistance() {
        return maximumOperatingDistance;
    }

    /**
     * Sets the value of the maximumOperatingDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthIntegerDimension }
     *     
     */
    public void setMaximumOperatingDistance(LengthIntegerDimension value) {
        this.maximumOperatingDistance = value;
    }

    /**
     * Gets the value of the automotiveRegionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutomotiveRegionId() {
        return automotiveRegionId;
    }

    /**
     * Sets the value of the automotiveRegionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutomotiveRegionId(String value) {
        this.automotiveRegionId = value;
    }

    /**
     * Gets the value of the bodyStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBodyStyle() {
        return bodyStyle;
    }

    /**
     * Sets the value of the bodyStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBodyStyle(String value) {
        this.bodyStyle = value;
    }

    /**
     * Gets the value of the interiorColorMap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInteriorColorMap() {
        return interiorColorMap;
    }

    /**
     * Sets the value of the interiorColorMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInteriorColorMap(String value) {
        this.interiorColorMap = value;
    }

    /**
     * Gets the value of the interiorColorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInteriorColorName() {
        return interiorColorName;
    }

    /**
     * Sets the value of the interiorColorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInteriorColorName(String value) {
        this.interiorColorName = value;
    }

    /**
     * Gets the value of the maximumCompatibleNumberOfSeats property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumCompatibleNumberOfSeats() {
        return maximumCompatibleNumberOfSeats;
    }

    /**
     * Sets the value of the maximumCompatibleNumberOfSeats property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumCompatibleNumberOfSeats(String value) {
        this.maximumCompatibleNumberOfSeats = value;
    }

    /**
     * Gets the value of the modelYearRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelYearRange() {
        return modelYearRange;
    }

    /**
     * Sets the value of the modelYearRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelYearRange(String value) {
        this.modelYearRange = value;
    }

    /**
     * Gets the value of the multimediaFunctionality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultimediaFunctionality() {
        return multimediaFunctionality;
    }

    /**
     * Sets the value of the multimediaFunctionality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultimediaFunctionality(String value) {
        this.multimediaFunctionality = value;
    }

    /**
     * Gets the value of the transmissionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmissionType() {
        return transmissionType;
    }

    /**
     * Sets the value of the transmissionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmissionType(String value) {
        this.transmissionType = value;
    }

    /**
     * Gets the value of the configuration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * Sets the value of the configuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfiguration(String value) {
        this.configuration = value;
    }

    /**
     * Gets the value of the driveSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDriveSystem() {
        return driveSystem;
    }

    /**
     * Sets the value of the driveSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDriveSystem(String value) {
        this.driveSystem = value;
    }

    /**
     * Gets the value of the engineType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngineType() {
        return engineType;
    }

    /**
     * Sets the value of the engineType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngineType(String value) {
        this.engineType = value;
    }

    /**
     * Gets the value of the environmentalDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvironmentalDescription() {
        return environmentalDescription;
    }

    /**
     * Sets the value of the environmentalDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironmentalDescription(String value) {
        this.environmentalDescription = value;
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
     * Gets the value of the includedFeatures property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludedFeatures() {
        return includedFeatures;
    }

    /**
     * Sets the value of the includedFeatures property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludedFeatures(String value) {
        this.includedFeatures = value;
    }

    /**
     * Gets the value of the indications property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndications() {
        return indications;
    }

    /**
     * Sets the value of the indications property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndications(String value) {
        this.indications = value;
    }

    /**
     * Gets the value of the maximumOutputPower property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumOutputPower() {
        return maximumOutputPower;
    }

    /**
     * Sets the value of the maximumOutputPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumOutputPower(String value) {
        this.maximumOutputPower = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the modelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * Gets the value of the modelYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelYear() {
        return modelYear;
    }

    /**
     * Sets the value of the modelYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelYear(String value) {
        this.modelYear = value;
    }

    /**
     * Gets the value of the numberOfDoors property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfDoors() {
        return numberOfDoors;
    }

    /**
     * Sets the value of the numberOfDoors property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfDoors(String value) {
        this.numberOfDoors = value;
    }

    /**
     * Gets the value of the offerAdditionalDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfferAdditionalDetails() {
        return offerAdditionalDetails;
    }

    /**
     * Sets the value of the offerAdditionalDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfferAdditionalDetails(String value) {
        this.offerAdditionalDetails = value;
    }

    /**
     * Gets the value of the previousOwnerCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousOwnerCount() {
        return previousOwnerCount;
    }

    /**
     * Sets the value of the previousOwnerCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousOwnerCount(String value) {
        this.previousOwnerCount = value;
    }

    /**
     * Gets the value of the specialFeatures property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialFeatures() {
        return specialFeatures;
    }

    /**
     * Sets the value of the specialFeatures property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialFeatures(String value) {
        this.specialFeatures = value;
    }

    /**
     * Gets the value of the styleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStyleName() {
        return styleName;
    }

    /**
     * Sets the value of the styleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStyleName(String value) {
        this.styleName = value;
    }

    /**
     * Gets the value of the tireType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTireType() {
        return tireType;
    }

    /**
     * Sets the value of the tireType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTireType(String value) {
        this.tireType = value;
    }

    /**
     * Gets the value of the maximumHorsepower property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumHorsepower() {
        return maximumHorsepower;
    }

    /**
     * Sets the value of the maximumHorsepower property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumHorsepower(String value) {
        this.maximumHorsepower = value;
    }

    /**
     * Gets the value of the wheelMaterial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWheelMaterial() {
        return wheelMaterial;
    }

    /**
     * Sets the value of the wheelMaterial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWheelMaterial(String value) {
        this.wheelMaterial = value;
    }

    /**
     * Gets the value of the variationData property.
     * 
     * @return
     *     possible object is
     *     {@link LightMotorVehicle.VariationData }
     *     
     */
    public LightMotorVehicle.VariationData getVariationData() {
        return variationData;
    }

    /**
     * Sets the value of the variationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link LightMotorVehicle.VariationData }
     *     
     */
    public void setVariationData(LightMotorVehicle.VariationData value) {
        this.variationData = value;
    }

    /**
     * Gets the value of the euEnergyLabelEfficiencyClass property.
     * 
     * @return
     *     possible object is
     *     {@link EnergyLabelEfficiencyClass }
     *     
     */
    public EnergyLabelEfficiencyClass getEuEnergyLabelEfficiencyClass() {
        return euEnergyLabelEfficiencyClass;
    }

    /**
     * Sets the value of the euEnergyLabelEfficiencyClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnergyLabelEfficiencyClass }
     *     
     */
    public void setEuEnergyLabelEfficiencyClass(EnergyLabelEfficiencyClass value) {
        this.euEnergyLabelEfficiencyClass = value;
    }

    /**
     * Gets the value of the acceleration property.
     * 
     * @return
     *     possible object is
     *     {@link AccelerationUnit }
     *     
     */
    public AccelerationUnit getAcceleration() {
        return acceleration;
    }

    /**
     * Sets the value of the acceleration property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccelerationUnit }
     *     
     */
    public void setAcceleration(AccelerationUnit value) {
        this.acceleration = value;
    }

    /**
     * Gets the value of the mileage property.
     * 
     * @return
     *     possible object is
     *     {@link MileageUnit }
     *     
     */
    public MileageUnit getMileage() {
        return mileage;
    }

    /**
     * Sets the value of the mileage property.
     * 
     * @param value
     *     allowed object is
     *     {@link MileageUnit }
     *     
     */
    public void setMileage(MileageUnit value) {
        this.mileage = value;
    }

    /**
     * Gets the value of the co2Emission property.
     * 
     * @return
     *     possible object is
     *     {@link LMVEmissionsDimension }
     *     
     */
    public LMVEmissionsDimension getCo2Emission() {
        return co2Emission;
    }

    /**
     * Sets the value of the co2Emission property.
     * 
     * @param value
     *     allowed object is
     *     {@link LMVEmissionsDimension }
     *     
     */
    public void setCo2Emission(LMVEmissionsDimension value) {
        this.co2Emission = value;
    }

    /**
     * Gets the value of the fuelEconomyCity property.
     * 
     * @return
     *     possible object is
     *     {@link FuelDimension }
     *     
     */
    public FuelDimension getFuelEconomyCity() {
        return fuelEconomyCity;
    }

    /**
     * Sets the value of the fuelEconomyCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuelDimension }
     *     
     */
    public void setFuelEconomyCity(FuelDimension value) {
        this.fuelEconomyCity = value;
    }

    /**
     * Gets the value of the fuelEconomyHighway property.
     * 
     * @return
     *     possible object is
     *     {@link FuelDimension }
     *     
     */
    public FuelDimension getFuelEconomyHighway() {
        return fuelEconomyHighway;
    }

    /**
     * Sets the value of the fuelEconomyHighway property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuelDimension }
     *     
     */
    public void setFuelEconomyHighway(FuelDimension value) {
        this.fuelEconomyHighway = value;
    }

    /**
     * Gets the value of the fuelEconomyCombined property.
     * 
     * @return
     *     possible object is
     *     {@link FuelDimension }
     *     
     */
    public FuelDimension getFuelEconomyCombined() {
        return fuelEconomyCombined;
    }

    /**
     * Sets the value of the fuelEconomyCombined property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuelDimension }
     *     
     */
    public void setFuelEconomyCombined(FuelDimension value) {
        this.fuelEconomyCombined = value;
    }

    /**
     * Gets the value of the engineDisplacement property.
     * 
     * @return
     *     possible object is
     *     {@link EngineDisplacementUnit }
     *     
     */
    public EngineDisplacementUnit getEngineDisplacement() {
        return engineDisplacement;
    }

    /**
     * Sets the value of the engineDisplacement property.
     * 
     * @param value
     *     allowed object is
     *     {@link EngineDisplacementUnit }
     *     
     */
    public void setEngineDisplacement(EngineDisplacementUnit value) {
        this.engineDisplacement = value;
    }

    /**
     * Gets the value of the fuelCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link FuelCapacityUnit }
     *     
     */
    public FuelCapacityUnit getFuelCapacity() {
        return fuelCapacity;
    }

    /**
     * Sets the value of the fuelCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuelCapacityUnit }
     *     
     */
    public void setFuelCapacity(FuelCapacityUnit value) {
        this.fuelCapacity = value;
    }

    /**
     * Gets the value of the maximumSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link MaximumSpeedUnit }
     *     
     */
    public MaximumSpeedUnit getMaximumSpeed() {
        return maximumSpeed;
    }

    /**
     * Sets the value of the maximumSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaximumSpeedUnit }
     *     
     */
    public void setMaximumSpeed(MaximumSpeedUnit value) {
        this.maximumSpeed = value;
    }

    /**
     * Gets the value of the wheelBase property.
     * 
     * @return
     *     possible object is
     *     {@link CycleLengthDimension }
     *     
     */
    public CycleLengthDimension getWheelBase() {
        return wheelBase;
    }

    /**
     * Sets the value of the wheelBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link CycleLengthDimension }
     *     
     */
    public void setWheelBase(CycleLengthDimension value) {
        this.wheelBase = value;
    }


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
     *         &lt;element name="Parentage"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;enumeration value="parent"/&gt;
     *               &lt;enumeration value="child"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="VariationTheme" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;enumeration value="stylename-colorname-configuration"/&gt;
     *               &lt;enumeration value="stylename-configuration-colorname-interiorcolorname"/&gt;
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
        "parentage",
        "variationTheme"
    })
    public static class VariationData {

        @XmlElement(name = "Parentage", required = true)
        protected String parentage;
        @XmlElement(name = "VariationTheme")
        protected String variationTheme;

        /**
         * Gets the value of the parentage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getParentage() {
            return parentage;
        }

        /**
         * Sets the value of the parentage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setParentage(String value) {
            this.parentage = value;
        }

        /**
         * Gets the value of the variationTheme property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVariationTheme() {
            return variationTheme;
        }

        /**
         * Sets the value of the variationTheme property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVariationTheme(String value) {
            this.variationTheme = value;
        }

    }

}
