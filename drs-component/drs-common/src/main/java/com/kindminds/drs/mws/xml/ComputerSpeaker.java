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
 *         &lt;element ref="{}AudioOutputMode" minOccurs="0"/&gt;
 *         &lt;element ref="{}CommunicationInterface" minOccurs="0"/&gt;
 *         &lt;element ref="{}DigitalAudioCapacity" minOccurs="0"/&gt;
 *         &lt;element ref="{}FrequencyRange" minOccurs="0"/&gt;
 *         &lt;element ref="{}InputType" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaximumOperatingDistance" minOccurs="0"/&gt;
 *         &lt;element ref="{}MemorySlotsAvailable" minOccurs="0"/&gt;
 *         &lt;element ref="{}ModelNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}MovementDetectionTechnology" minOccurs="0"/&gt;
 *         &lt;element ref="{}NumberOfSatelliteSpeakers" minOccurs="0"/&gt;
 *         &lt;element ref="{}OutputWattage" minOccurs="0"/&gt;
 *         &lt;element ref="{}RecordingCapacity" minOccurs="0"/&gt;
 *         &lt;element ref="{}RemoteIncluded" minOccurs="0"/&gt;
 *         &lt;element ref="{}SpeakerDiameter" minOccurs="0"/&gt;
 *         &lt;element ref="{}SpeakerOutputChannelQuantity" minOccurs="0"/&gt;
 *         &lt;element ref="{}SpeakersMaximumOutputPower" minOccurs="0"/&gt;
 *         &lt;element ref="{}SpeakersNominalOutputPower" minOccurs="0"/&gt;
 *         &lt;element ref="{}SpeakerSurroundSoundChannelConfiguration" minOccurs="0"/&gt;
 *         &lt;element ref="{}Voltage" minOccurs="0"/&gt;
 *         &lt;element ref="{}Wattage" minOccurs="0"/&gt;
 *         &lt;element name="PowerPlugType" type="{}PowerPlugType" minOccurs="0"/&gt;
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
    "audioOutputMode",
    "communicationInterface",
    "digitalAudioCapacity",
    "frequencyRange",
    "inputType",
    "maximumOperatingDistance",
    "memorySlotsAvailable",
    "modelNumber",
    "movementDetectionTechnology",
    "numberOfSatelliteSpeakers",
    "outputWattage",
    "recordingCapacity",
    "remoteIncluded",
    "speakerDiameter",
    "speakerOutputChannelQuantity",
    "speakersMaximumOutputPower",
    "speakersNominalOutputPower",
    "speakerSurroundSoundChannelConfiguration",
    "voltage",
    "wattage",
    "powerPlugType"
})
@XmlRootElement(name = "ComputerSpeaker")
public class ComputerSpeaker {

    @XmlElement(name = "VariationData")
    protected VariationData variationData;
    @XmlElement(name = "AdditionalFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String additionalFeatures;
    @XmlElement(name = "AudioOutputMode")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String audioOutputMode;
    @XmlElement(name = "CommunicationInterface")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String communicationInterface;
    @XmlElement(name = "DigitalAudioCapacity")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String digitalAudioCapacity;
    @XmlElement(name = "FrequencyRange")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String frequencyRange;
    @XmlElement(name = "InputType")
    @XmlSchemaType(name = "string")
    protected InputTypeValues inputType;
    @XmlElement(name = "MaximumOperatingDistance")
    protected LengthIntegerDimension maximumOperatingDistance;
    @XmlElement(name = "MemorySlotsAvailable")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String memorySlotsAvailable;
    @XmlElement(name = "ModelNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelNumber;
    @XmlElement(name = "MovementDetectionTechnology")
    protected Boolean movementDetectionTechnology;
    @XmlElement(name = "NumberOfSatelliteSpeakers")
    protected BigDecimal numberOfSatelliteSpeakers;
    @XmlElement(name = "OutputWattage")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger outputWattage;
    @XmlElement(name = "RecordingCapacity")
    protected TimeIntegerDimension recordingCapacity;
    @XmlElement(name = "RemoteIncluded")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String remoteIncluded;
    @XmlElement(name = "SpeakerDiameter")
    protected LengthDimension speakerDiameter;
    @XmlElement(name = "SpeakerOutputChannelQuantity")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger speakerOutputChannelQuantity;
    @XmlElement(name = "SpeakersMaximumOutputPower")
    protected WattageDimension speakersMaximumOutputPower;
    @XmlElement(name = "SpeakersNominalOutputPower")
    protected WattageDimension speakersNominalOutputPower;
    @XmlElement(name = "SpeakerSurroundSoundChannelConfiguration")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String speakerSurroundSoundChannelConfiguration;
    @XmlElement(name = "Voltage")
    protected BigDecimal voltage;
    @XmlElement(name = "Wattage")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger wattage;
    @XmlElement(name = "PowerPlugType")
    @XmlSchemaType(name = "normalizedString")
    protected PowerPlugType powerPlugType;

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
     * Gets the value of the audioOutputMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAudioOutputMode() {
        return audioOutputMode;
    }

    /**
     * Sets the value of the audioOutputMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAudioOutputMode(String value) {
        this.audioOutputMode = value;
    }

    /**
     * Gets the value of the communicationInterface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommunicationInterface() {
        return communicationInterface;
    }

    /**
     * Sets the value of the communicationInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommunicationInterface(String value) {
        this.communicationInterface = value;
    }

    /**
     * Gets the value of the digitalAudioCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDigitalAudioCapacity() {
        return digitalAudioCapacity;
    }

    /**
     * Sets the value of the digitalAudioCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDigitalAudioCapacity(String value) {
        this.digitalAudioCapacity = value;
    }

    /**
     * Gets the value of the frequencyRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequencyRange() {
        return frequencyRange;
    }

    /**
     * Sets the value of the frequencyRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequencyRange(String value) {
        this.frequencyRange = value;
    }

    /**
     * Gets the value of the inputType property.
     * 
     * @return
     *     possible object is
     *     {@link InputTypeValues }
     *     
     */
    public InputTypeValues getInputType() {
        return inputType;
    }

    /**
     * Sets the value of the inputType property.
     * 
     * @param value
     *     allowed object is
     *     {@link InputTypeValues }
     *     
     */
    public void setInputType(InputTypeValues value) {
        this.inputType = value;
    }

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
     * Gets the value of the movementDetectionTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMovementDetectionTechnology() {
        return movementDetectionTechnology;
    }

    /**
     * Sets the value of the movementDetectionTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMovementDetectionTechnology(Boolean value) {
        this.movementDetectionTechnology = value;
    }

    /**
     * Gets the value of the numberOfSatelliteSpeakers property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumberOfSatelliteSpeakers() {
        return numberOfSatelliteSpeakers;
    }

    /**
     * Sets the value of the numberOfSatelliteSpeakers property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumberOfSatelliteSpeakers(BigDecimal value) {
        this.numberOfSatelliteSpeakers = value;
    }

    /**
     * Gets the value of the outputWattage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOutputWattage() {
        return outputWattage;
    }

    /**
     * Sets the value of the outputWattage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOutputWattage(BigInteger value) {
        this.outputWattage = value;
    }

    /**
     * Gets the value of the recordingCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link TimeIntegerDimension }
     *     
     */
    public TimeIntegerDimension getRecordingCapacity() {
        return recordingCapacity;
    }

    /**
     * Sets the value of the recordingCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeIntegerDimension }
     *     
     */
    public void setRecordingCapacity(TimeIntegerDimension value) {
        this.recordingCapacity = value;
    }

    /**
     * Gets the value of the remoteIncluded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteIncluded() {
        return remoteIncluded;
    }

    /**
     * Sets the value of the remoteIncluded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteIncluded(String value) {
        this.remoteIncluded = value;
    }

    /**
     * Gets the value of the speakerDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getSpeakerDiameter() {
        return speakerDiameter;
    }

    /**
     * Sets the value of the speakerDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setSpeakerDiameter(LengthDimension value) {
        this.speakerDiameter = value;
    }

    /**
     * Gets the value of the speakerOutputChannelQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSpeakerOutputChannelQuantity() {
        return speakerOutputChannelQuantity;
    }

    /**
     * Sets the value of the speakerOutputChannelQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSpeakerOutputChannelQuantity(BigInteger value) {
        this.speakerOutputChannelQuantity = value;
    }

    /**
     * Gets the value of the speakersMaximumOutputPower property.
     * 
     * @return
     *     possible object is
     *     {@link WattageDimension }
     *     
     */
    public WattageDimension getSpeakersMaximumOutputPower() {
        return speakersMaximumOutputPower;
    }

    /**
     * Sets the value of the speakersMaximumOutputPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link WattageDimension }
     *     
     */
    public void setSpeakersMaximumOutputPower(WattageDimension value) {
        this.speakersMaximumOutputPower = value;
    }

    /**
     * Gets the value of the speakersNominalOutputPower property.
     * 
     * @return
     *     possible object is
     *     {@link WattageDimension }
     *     
     */
    public WattageDimension getSpeakersNominalOutputPower() {
        return speakersNominalOutputPower;
    }

    /**
     * Sets the value of the speakersNominalOutputPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link WattageDimension }
     *     
     */
    public void setSpeakersNominalOutputPower(WattageDimension value) {
        this.speakersNominalOutputPower = value;
    }

    /**
     * Gets the value of the speakerSurroundSoundChannelConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeakerSurroundSoundChannelConfiguration() {
        return speakerSurroundSoundChannelConfiguration;
    }

    /**
     * Sets the value of the speakerSurroundSoundChannelConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeakerSurroundSoundChannelConfiguration(String value) {
        this.speakerSurroundSoundChannelConfiguration = value;
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
     * Gets the value of the powerPlugType property.
     * 
     * @return
     *     possible object is
     *     {@link PowerPlugType }
     *     
     */
    public PowerPlugType getPowerPlugType() {
        return powerPlugType;
    }

    /**
     * Sets the value of the powerPlugType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PowerPlugType }
     *     
     */
    public void setPowerPlugType(PowerPlugType value) {
        this.powerPlugType = value;
    }

}
