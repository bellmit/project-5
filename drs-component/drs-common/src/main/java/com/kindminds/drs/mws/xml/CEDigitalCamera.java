//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}AnalogRGBInput" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element ref="{}BatteryCellType" minOccurs="0"/&gt;
 *         &lt;element name="BatteryChargeCycles" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element ref="{}BatteryPower" minOccurs="0"/&gt;
 *         &lt;element ref="{}BoxContents" minOccurs="0"/&gt;
 *         &lt;element name="CableLength" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element ref="{}CameraFlash" minOccurs="0"/&gt;
 *         &lt;element name="CameraLens" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}CompatibleMountings" minOccurs="0"/&gt;
 *         &lt;element ref="{}ContinuousShootingSpeed" minOccurs="0"/&gt;
 *         &lt;element ref="{}DigitalZoom" minOccurs="0"/&gt;
 *         &lt;element ref="{}EffectiveStillResolution" minOccurs="0"/&gt;
 *         &lt;element ref="{}FinishType" minOccurs="0"/&gt;
 *         &lt;element ref="{}FixedFocalLength" minOccurs="0"/&gt;
 *         &lt;element ref="{}FlashModesDescription" minOccurs="0"/&gt;
 *         &lt;element ref="{}FocusType" minOccurs="0"/&gt;
 *         &lt;element ref="{}GuideNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{}ImageStabilization" minOccurs="0"/&gt;
 *         &lt;element ref="{}IsHotShoeIncluded" minOccurs="0"/&gt;
 *         &lt;element name="Lens" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaxAperture" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaxFocalLength" minOccurs="0"/&gt;
 *         &lt;element ref="{}MaxShutterSpeed" minOccurs="0"/&gt;
 *         &lt;element name="MemorySlotsAvailable" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}MinAperture" minOccurs="0"/&gt;
 *         &lt;element ref="{}MinFocalLength" minOccurs="0"/&gt;
 *         &lt;element ref="{}MinShutterSpeed" minOccurs="0"/&gt;
 *         &lt;element ref="{}OpticalZoom" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterLensSize" minOccurs="0"/&gt;
 *         &lt;element name="PhotoSensorSize" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="PhotoSensorTechnology" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="PowerSource" type="{}FortyStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="RemoteControlDescription" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="RemovableMemory" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ScreenSize" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="SoftwareIncluded" type="{}LongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}ThreeDTechnology" minOccurs="0"/&gt;
 *         &lt;element ref="{}TotalFirewirePorts" minOccurs="0"/&gt;
 *         &lt;element ref="{}TotalHdmiPorts" minOccurs="0"/&gt;
 *         &lt;element name="TotalSVideoOutPorts" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element ref="{}TotalUSBPorts" minOccurs="0"/&gt;
 *         &lt;element ref="{}VideoEncoding" minOccurs="0"/&gt;
 *         &lt;element name="VideoResolution" type="{}PixelDimension" minOccurs="0"/&gt;
 *         &lt;element ref="{}ViewFinderType" minOccurs="0"/&gt;
 *         &lt;element ref="{}WaterResistantLevel" minOccurs="0"/&gt;
 *         &lt;element name="WirelessTechnology" type="{}StringNotNull" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element ref="{}PowerPlugType" minOccurs="0"/&gt;
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
    "analogRGBInput",
    "batteryCellType",
    "batteryChargeCycles",
    "batteryPower",
    "boxContents",
    "cableLength",
    "cameraFlash",
    "cameraLens",
    "compatibleMountings",
    "continuousShootingSpeed",
    "digitalZoom",
    "effectiveStillResolution",
    "finishType",
    "fixedFocalLength",
    "flashModesDescription",
    "focusType",
    "guideNumber",
    "imageStabilization",
    "isHotShoeIncluded",
    "lens",
    "maxAperture",
    "maxFocalLength",
    "maxShutterSpeed",
    "memorySlotsAvailable",
    "minAperture",
    "minFocalLength",
    "minShutterSpeed",
    "opticalZoom",
    "photoFilterLensSize",
    "photoSensorSize",
    "photoSensorTechnology",
    "powerSource",
    "remoteControlDescription",
    "removableMemory",
    "screenSize",
    "softwareIncluded",
    "threeDTechnology",
    "totalFirewirePorts",
    "totalHdmiPorts",
    "totalSVideoOutPorts",
    "totalUSBPorts",
    "videoEncoding",
    "videoResolution",
    "viewFinderType",
    "waterResistantLevel",
    "wirelessTechnology",
    "powerPlugType",
    "efficiency"
})
@XmlRootElement(name = "CEDigitalCamera")
public class CEDigitalCamera {

    @XmlElement(name = "AnalogRGBInput")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> analogRGBInput;
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
    @XmlElement(name = "CableLength")
    protected LengthDimension cableLength;
    @XmlElement(name = "CameraFlash")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String cameraFlash;
    @XmlElement(name = "CameraLens")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String cameraLens;
    @XmlElement(name = "CompatibleMountings")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String compatibleMountings;
    @XmlElement(name = "ContinuousShootingSpeed")
    protected ContinuousShootingDimension continuousShootingSpeed;
    @XmlElement(name = "DigitalZoom")
    protected ZoomDimension digitalZoom;
    @XmlElement(name = "EffectiveStillResolution")
    protected ResolutionDimension effectiveStillResolution;
    @XmlElement(name = "FinishType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String finishType;
    @XmlElement(name = "FixedFocalLength")
    protected LengthDimension fixedFocalLength;
    @XmlElement(name = "FlashModesDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String flashModesDescription;
    @XmlElement(name = "FocusType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String focusType;
    @XmlElement(name = "GuideNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String guideNumber;
    @XmlElement(name = "ImageStabilization")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String imageStabilization;
    @XmlElement(name = "IsHotShoeIncluded")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String isHotShoeIncluded;
    @XmlElement(name = "Lens")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String lens;
    @XmlElement(name = "MaxAperture")
    protected ApertureDimension maxAperture;
    @XmlElement(name = "MaxFocalLength")
    protected LengthDimension maxFocalLength;
    @XmlElement(name = "MaxShutterSpeed")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String maxShutterSpeed;
    @XmlElement(name = "MemorySlotsAvailable")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String memorySlotsAvailable;
    @XmlElement(name = "MinAperture")
    protected ApertureDimension minAperture;
    @XmlElement(name = "MinFocalLength")
    protected LengthDimension minFocalLength;
    @XmlElement(name = "MinShutterSpeed")
    protected BigDecimal minShutterSpeed;
    @XmlElement(name = "OpticalZoom")
    protected ZoomDimension opticalZoom;
    @XmlElement(name = "PhotoFilterLensSize")
    protected LengthDimension photoFilterLensSize;
    @XmlElement(name = "PhotoSensorSize")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photoSensorSize;
    @XmlElement(name = "PhotoSensorTechnology")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photoSensorTechnology;
    @XmlElement(name = "PowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String powerSource;
    @XmlElement(name = "RemoteControlDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String remoteControlDescription;
    @XmlElement(name = "RemovableMemory")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String removableMemory;
    @XmlElement(name = "ScreenSize")
    protected LengthDimension screenSize;
    @XmlElement(name = "SoftwareIncluded")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String softwareIncluded;
    @XmlElement(name = "ThreeDTechnology")
    @XmlSchemaType(name = "string")
    protected ThreeDTechnologyValues threeDTechnology;
    @XmlElement(name = "TotalFirewirePorts")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger totalFirewirePorts;
    @XmlElement(name = "TotalHdmiPorts")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger totalHdmiPorts;
    @XmlElement(name = "TotalSVideoOutPorts")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger totalSVideoOutPorts;
    @XmlElement(name = "TotalUSBPorts")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger totalUSBPorts;
    @XmlElement(name = "VideoEncoding")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String videoEncoding;
    @XmlElement(name = "VideoResolution")
    protected PixelDimension videoResolution;
    @XmlElement(name = "ViewFinderType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String viewFinderType;
    @XmlElement(name = "WaterResistantLevel")
    @XmlSchemaType(name = "string")
    protected WaterResistantType waterResistantLevel;
    @XmlElement(name = "WirelessTechnology")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> wirelessTechnology;
    @XmlElement(name = "PowerPlugType")
    @XmlSchemaType(name = "normalizedString")
    protected PowerPlugType powerPlugType;
    @XmlElement(name = "Efficiency")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String efficiency;

    /**
     * Gets the value of the analogRGBInput property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the analogRGBInput property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnalogRGBInput().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAnalogRGBInput() {
        if (analogRGBInput == null) {
            analogRGBInput = new ArrayList<String>();
        }
        return this.analogRGBInput;
    }

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
     * Gets the value of the cableLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getCableLength() {
        return cableLength;
    }

    /**
     * Sets the value of the cableLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setCableLength(LengthDimension value) {
        this.cableLength = value;
    }

    /**
     * Gets the value of the cameraFlash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCameraFlash() {
        return cameraFlash;
    }

    /**
     * Sets the value of the cameraFlash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCameraFlash(String value) {
        this.cameraFlash = value;
    }

    /**
     * Gets the value of the cameraLens property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCameraLens() {
        return cameraLens;
    }

    /**
     * Sets the value of the cameraLens property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCameraLens(String value) {
        this.cameraLens = value;
    }

    /**
     * Gets the value of the compatibleMountings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompatibleMountings() {
        return compatibleMountings;
    }

    /**
     * Sets the value of the compatibleMountings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompatibleMountings(String value) {
        this.compatibleMountings = value;
    }

    /**
     * Gets the value of the continuousShootingSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link ContinuousShootingDimension }
     *     
     */
    public ContinuousShootingDimension getContinuousShootingSpeed() {
        return continuousShootingSpeed;
    }

    /**
     * Sets the value of the continuousShootingSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContinuousShootingDimension }
     *     
     */
    public void setContinuousShootingSpeed(ContinuousShootingDimension value) {
        this.continuousShootingSpeed = value;
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
     * Gets the value of the fixedFocalLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getFixedFocalLength() {
        return fixedFocalLength;
    }

    /**
     * Sets the value of the fixedFocalLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setFixedFocalLength(LengthDimension value) {
        this.fixedFocalLength = value;
    }

    /**
     * Gets the value of the flashModesDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlashModesDescription() {
        return flashModesDescription;
    }

    /**
     * Sets the value of the flashModesDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlashModesDescription(String value) {
        this.flashModesDescription = value;
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
     * Gets the value of the guideNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuideNumber() {
        return guideNumber;
    }

    /**
     * Sets the value of the guideNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuideNumber(String value) {
        this.guideNumber = value;
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
     * Gets the value of the isHotShoeIncluded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsHotShoeIncluded() {
        return isHotShoeIncluded;
    }

    /**
     * Sets the value of the isHotShoeIncluded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsHotShoeIncluded(String value) {
        this.isHotShoeIncluded = value;
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
     * Gets the value of the maxAperture property.
     * 
     * @return
     *     possible object is
     *     {@link ApertureDimension }
     *     
     */
    public ApertureDimension getMaxAperture() {
        return maxAperture;
    }

    /**
     * Sets the value of the maxAperture property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApertureDimension }
     *     
     */
    public void setMaxAperture(ApertureDimension value) {
        this.maxAperture = value;
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
     * Gets the value of the maxShutterSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxShutterSpeed() {
        return maxShutterSpeed;
    }

    /**
     * Sets the value of the maxShutterSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxShutterSpeed(String value) {
        this.maxShutterSpeed = value;
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
     * Gets the value of the minAperture property.
     * 
     * @return
     *     possible object is
     *     {@link ApertureDimension }
     *     
     */
    public ApertureDimension getMinAperture() {
        return minAperture;
    }

    /**
     * Sets the value of the minAperture property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApertureDimension }
     *     
     */
    public void setMinAperture(ApertureDimension value) {
        this.minAperture = value;
    }

    /**
     * Gets the value of the minFocalLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getMinFocalLength() {
        return minFocalLength;
    }

    /**
     * Sets the value of the minFocalLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setMinFocalLength(LengthDimension value) {
        this.minFocalLength = value;
    }

    /**
     * Gets the value of the minShutterSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinShutterSpeed() {
        return minShutterSpeed;
    }

    /**
     * Sets the value of the minShutterSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinShutterSpeed(BigDecimal value) {
        this.minShutterSpeed = value;
    }

    /**
     * Gets the value of the opticalZoom property.
     * 
     * @return
     *     possible object is
     *     {@link ZoomDimension }
     *     
     */
    public ZoomDimension getOpticalZoom() {
        return opticalZoom;
    }

    /**
     * Sets the value of the opticalZoom property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoomDimension }
     *     
     */
    public void setOpticalZoom(ZoomDimension value) {
        this.opticalZoom = value;
    }

    /**
     * Gets the value of the photoFilterLensSize property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getPhotoFilterLensSize() {
        return photoFilterLensSize;
    }

    /**
     * Sets the value of the photoFilterLensSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setPhotoFilterLensSize(LengthDimension value) {
        this.photoFilterLensSize = value;
    }

    /**
     * Gets the value of the photoSensorSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoSensorSize() {
        return photoSensorSize;
    }

    /**
     * Sets the value of the photoSensorSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoSensorSize(String value) {
        this.photoSensorSize = value;
    }

    /**
     * Gets the value of the photoSensorTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoSensorTechnology() {
        return photoSensorTechnology;
    }

    /**
     * Sets the value of the photoSensorTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoSensorTechnology(String value) {
        this.photoSensorTechnology = value;
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
     * Gets the value of the remoteControlDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteControlDescription() {
        return remoteControlDescription;
    }

    /**
     * Sets the value of the remoteControlDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteControlDescription(String value) {
        this.remoteControlDescription = value;
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
     * Gets the value of the screenSize property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getScreenSize() {
        return screenSize;
    }

    /**
     * Sets the value of the screenSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setScreenSize(LengthDimension value) {
        this.screenSize = value;
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
     * Gets the value of the threeDTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link ThreeDTechnologyValues }
     *     
     */
    public ThreeDTechnologyValues getThreeDTechnology() {
        return threeDTechnology;
    }

    /**
     * Sets the value of the threeDTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreeDTechnologyValues }
     *     
     */
    public void setThreeDTechnology(ThreeDTechnologyValues value) {
        this.threeDTechnology = value;
    }

    /**
     * Gets the value of the totalFirewirePorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalFirewirePorts() {
        return totalFirewirePorts;
    }

    /**
     * Sets the value of the totalFirewirePorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalFirewirePorts(BigInteger value) {
        this.totalFirewirePorts = value;
    }

    /**
     * Gets the value of the totalHdmiPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalHdmiPorts() {
        return totalHdmiPorts;
    }

    /**
     * Sets the value of the totalHdmiPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalHdmiPorts(BigInteger value) {
        this.totalHdmiPorts = value;
    }

    /**
     * Gets the value of the totalSVideoOutPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalSVideoOutPorts() {
        return totalSVideoOutPorts;
    }

    /**
     * Sets the value of the totalSVideoOutPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalSVideoOutPorts(BigInteger value) {
        this.totalSVideoOutPorts = value;
    }

    /**
     * Gets the value of the totalUSBPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalUSBPorts() {
        return totalUSBPorts;
    }

    /**
     * Sets the value of the totalUSBPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalUSBPorts(BigInteger value) {
        this.totalUSBPorts = value;
    }

    /**
     * Gets the value of the videoEncoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVideoEncoding() {
        return videoEncoding;
    }

    /**
     * Sets the value of the videoEncoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVideoEncoding(String value) {
        this.videoEncoding = value;
    }

    /**
     * Gets the value of the videoResolution property.
     * 
     * @return
     *     possible object is
     *     {@link PixelDimension }
     *     
     */
    public PixelDimension getVideoResolution() {
        return videoResolution;
    }

    /**
     * Sets the value of the videoResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link PixelDimension }
     *     
     */
    public void setVideoResolution(PixelDimension value) {
        this.videoResolution = value;
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
     * Gets the value of the waterResistantLevel property.
     * 
     * @return
     *     possible object is
     *     {@link WaterResistantType }
     *     
     */
    public WaterResistantType getWaterResistantLevel() {
        return waterResistantLevel;
    }

    /**
     * Sets the value of the waterResistantLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link WaterResistantType }
     *     
     */
    public void setWaterResistantLevel(WaterResistantType value) {
        this.waterResistantLevel = value;
    }

    /**
     * Gets the value of the wirelessTechnology property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wirelessTechnology property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWirelessTechnology().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getWirelessTechnology() {
        if (wirelessTechnology == null) {
            wirelessTechnology = new ArrayList<String>();
        }
        return this.wirelessTechnology;
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
