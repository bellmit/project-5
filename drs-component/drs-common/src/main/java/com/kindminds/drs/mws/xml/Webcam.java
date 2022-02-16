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
 *         &lt;element ref="{}BuiltInMicrophone" minOccurs="0"/&gt;
 *         &lt;element ref="{}CameraType" minOccurs="0"/&gt;
 *         &lt;element ref="{}DigitalStillResolution" minOccurs="0"/&gt;
 *         &lt;element ref="{}ImageSensor" minOccurs="0"/&gt;
 *         &lt;element ref="{}InputType" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaxWebcamImageResolution" minOccurs="0"/&gt;
 *         &lt;element ref="{}MinimumSystemRequirements" minOccurs="0"/&gt;
 *         &lt;element ref="{}ModelNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}NetworkingProtocol" minOccurs="0"/&gt;
 *         &lt;element ref="{}VideoCallingResolution" minOccurs="0"/&gt;
 *         &lt;element ref="{}Voltage" minOccurs="0"/&gt;
 *         &lt;element ref="{}Wattage" minOccurs="0"/&gt;
 *         &lt;element ref="{}WebcamVideoCaptureResolution" minOccurs="0"/&gt;
 *         &lt;element ref="{}WirelessStandard" minOccurs="0"/&gt;
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
    "builtInMicrophone",
    "cameraType",
    "digitalStillResolution",
    "imageSensor",
    "inputType",
    "maxWebcamImageResolution",
    "minimumSystemRequirements",
    "modelNumber",
    "networkingProtocol",
    "videoCallingResolution",
    "voltage",
    "wattage",
    "webcamVideoCaptureResolution",
    "wirelessStandard",
    "powerPlugType"
})
@XmlRootElement(name = "Webcam")
public class Webcam {

    @XmlElement(name = "VariationData")
    protected VariationData variationData;
    @XmlElement(name = "BuiltInMicrophone")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String builtInMicrophone;
    @XmlElement(name = "CameraType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String cameraType;
    @XmlElement(name = "DigitalStillResolution")
    protected ResolutionFiveDigitDimension digitalStillResolution;
    @XmlElement(name = "ImageSensor")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String imageSensor;
    @XmlElement(name = "InputType")
    @XmlSchemaType(name = "string")
    protected InputTypeValues inputType;
    @XmlElement(name = "MaxWebcamImageResolution")
    protected ResolutionFiveDigitDimension maxWebcamImageResolution;
    @XmlElement(name = "MinimumSystemRequirements")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String minimumSystemRequirements;
    @XmlElement(name = "ModelNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String modelNumber;
    @XmlElement(name = "NetworkingProtocol")
    protected String networkingProtocol;
    @XmlElement(name = "VideoCallingResolution")
    protected ResolutionFiveDigitDimension videoCallingResolution;
    @XmlElement(name = "Voltage")
    protected BigDecimal voltage;
    @XmlElement(name = "Wattage")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger wattage;
    @XmlElement(name = "WebcamVideoCaptureResolution")
    protected ResolutionFiveDigitDimension webcamVideoCaptureResolution;
    @XmlElement(name = "WirelessStandard")
    @XmlSchemaType(name = "string")
    protected WirelessStandardTypeValues wirelessStandard;
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
     * Gets the value of the builtInMicrophone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuiltInMicrophone() {
        return builtInMicrophone;
    }

    /**
     * Sets the value of the builtInMicrophone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuiltInMicrophone(String value) {
        this.builtInMicrophone = value;
    }

    /**
     * Gets the value of the cameraType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCameraType() {
        return cameraType;
    }

    /**
     * Sets the value of the cameraType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCameraType(String value) {
        this.cameraType = value;
    }

    /**
     * Gets the value of the digitalStillResolution property.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public ResolutionFiveDigitDimension getDigitalStillResolution() {
        return digitalStillResolution;
    }

    /**
     * Sets the value of the digitalStillResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public void setDigitalStillResolution(ResolutionFiveDigitDimension value) {
        this.digitalStillResolution = value;
    }

    /**
     * Gets the value of the imageSensor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageSensor() {
        return imageSensor;
    }

    /**
     * Sets the value of the imageSensor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageSensor(String value) {
        this.imageSensor = value;
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
     * Gets the value of the maxWebcamImageResolution property.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public ResolutionFiveDigitDimension getMaxWebcamImageResolution() {
        return maxWebcamImageResolution;
    }

    /**
     * Sets the value of the maxWebcamImageResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public void setMaxWebcamImageResolution(ResolutionFiveDigitDimension value) {
        this.maxWebcamImageResolution = value;
    }

    /**
     * Gets the value of the minimumSystemRequirements property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinimumSystemRequirements() {
        return minimumSystemRequirements;
    }

    /**
     * Sets the value of the minimumSystemRequirements property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinimumSystemRequirements(String value) {
        this.minimumSystemRequirements = value;
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
     * Gets the value of the networkingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkingProtocol() {
        return networkingProtocol;
    }

    /**
     * Sets the value of the networkingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkingProtocol(String value) {
        this.networkingProtocol = value;
    }

    /**
     * Gets the value of the videoCallingResolution property.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public ResolutionFiveDigitDimension getVideoCallingResolution() {
        return videoCallingResolution;
    }

    /**
     * Sets the value of the videoCallingResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public void setVideoCallingResolution(ResolutionFiveDigitDimension value) {
        this.videoCallingResolution = value;
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
     * Gets the value of the webcamVideoCaptureResolution property.
     * 
     * @return
     *     possible object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public ResolutionFiveDigitDimension getWebcamVideoCaptureResolution() {
        return webcamVideoCaptureResolution;
    }

    /**
     * Sets the value of the webcamVideoCaptureResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolutionFiveDigitDimension }
     *     
     */
    public void setWebcamVideoCaptureResolution(ResolutionFiveDigitDimension value) {
        this.webcamVideoCaptureResolution = value;
    }

    /**
     * Gets the value of the wirelessStandard property.
     * 
     * @return
     *     possible object is
     *     {@link WirelessStandardTypeValues }
     *     
     */
    public WirelessStandardTypeValues getWirelessStandard() {
        return wirelessStandard;
    }

    /**
     * Sets the value of the wirelessStandard property.
     * 
     * @param value
     *     allowed object is
     *     {@link WirelessStandardTypeValues }
     *     
     */
    public void setWirelessStandard(WirelessStandardTypeValues value) {
        this.wirelessStandard = value;
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