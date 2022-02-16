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
 *         &lt;element ref="{}CameraFilmSpeed" minOccurs="0"/&gt;
 *         &lt;element ref="{}CameraFlash" minOccurs="0"/&gt;
 *         &lt;element ref="{}CompatibleMountings" minOccurs="0"/&gt;
 *         &lt;element ref="{}DigitalZoom" minOccurs="0"/&gt;
 *         &lt;element ref="{}FinishType" minOccurs="0"/&gt;
 *         &lt;element ref="{}FlashDedication" minOccurs="0"/&gt;
 *         &lt;element ref="{}GuideNumber" minOccurs="0"/&gt;
 *         &lt;element name="Lens" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="LoadCapacity" type="{}WeightDimension" minOccurs="0"/&gt;
 *         &lt;element name="MemorySlotsAvailable" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterBayonetSize" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterDropInSize" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterEffectSize" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterLensSize" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterMountType" minOccurs="0"/&gt;
 *         &lt;element ref="{}PhotoFilterThreadSize" minOccurs="0"/&gt;
 *         &lt;element ref="{}PowerPlugType" minOccurs="0"/&gt;
 *         &lt;element name="PowerSource" type="{}FortyStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="RemoteControlDescription" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="SoftwareIncluded" type="{}LongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="VideoResolution" type="{}PixelDimension" minOccurs="0"/&gt;
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
    "cameraFilmSpeed",
    "cameraFlash",
    "compatibleMountings",
    "digitalZoom",
    "finishType",
    "flashDedication",
    "guideNumber",
    "lens",
    "loadCapacity",
    "memorySlotsAvailable",
    "photoFilterBayonetSize",
    "photoFilterDropInSize",
    "photoFilterEffectSize",
    "photoFilterLensSize",
    "photoFilterMountType",
    "photoFilterThreadSize",
    "powerPlugType",
    "powerSource",
    "remoteControlDescription",
    "softwareIncluded",
    "videoResolution",
    "efficiency"
})
@XmlRootElement(name = "PhotographicStudioItems")
public class PhotographicStudioItems {

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
    @XmlElement(name = "CameraFilmSpeed")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger cameraFilmSpeed;
    @XmlElement(name = "CameraFlash")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String cameraFlash;
    @XmlElement(name = "CompatibleMountings")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String compatibleMountings;
    @XmlElement(name = "DigitalZoom")
    protected ZoomDimension digitalZoom;
    @XmlElement(name = "FinishType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String finishType;
    @XmlElement(name = "FlashDedication")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String flashDedication;
    @XmlElement(name = "GuideNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String guideNumber;
    @XmlElement(name = "Lens")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String lens;
    @XmlElement(name = "LoadCapacity")
    protected WeightDimension loadCapacity;
    @XmlElement(name = "MemorySlotsAvailable")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String memorySlotsAvailable;
    @XmlElement(name = "PhotoFilterBayonetSize")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photoFilterBayonetSize;
    @XmlElement(name = "PhotoFilterDropInSize")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photoFilterDropInSize;
    @XmlElement(name = "PhotoFilterEffectSize")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photoFilterEffectSize;
    @XmlElement(name = "PhotoFilterLensSize")
    protected LengthDimension photoFilterLensSize;
    @XmlElement(name = "PhotoFilterMountType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photoFilterMountType;
    @XmlElement(name = "PhotoFilterThreadSize")
    protected LengthDimension photoFilterThreadSize;
    @XmlElement(name = "PowerPlugType")
    @XmlSchemaType(name = "normalizedString")
    protected PowerPlugType powerPlugType;
    @XmlElement(name = "PowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String powerSource;
    @XmlElement(name = "RemoteControlDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String remoteControlDescription;
    @XmlElement(name = "SoftwareIncluded")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String softwareIncluded;
    @XmlElement(name = "VideoResolution")
    protected PixelDimension videoResolution;
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
     * Gets the value of the cameraFilmSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCameraFilmSpeed() {
        return cameraFilmSpeed;
    }

    /**
     * Sets the value of the cameraFilmSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCameraFilmSpeed(BigInteger value) {
        this.cameraFilmSpeed = value;
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
     * Gets the value of the flashDedication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlashDedication() {
        return flashDedication;
    }

    /**
     * Sets the value of the flashDedication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlashDedication(String value) {
        this.flashDedication = value;
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
     * Gets the value of the loadCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link WeightDimension }
     *     
     */
    public WeightDimension getLoadCapacity() {
        return loadCapacity;
    }

    /**
     * Sets the value of the loadCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link WeightDimension }
     *     
     */
    public void setLoadCapacity(WeightDimension value) {
        this.loadCapacity = value;
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
     * Gets the value of the photoFilterBayonetSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoFilterBayonetSize() {
        return photoFilterBayonetSize;
    }

    /**
     * Sets the value of the photoFilterBayonetSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoFilterBayonetSize(String value) {
        this.photoFilterBayonetSize = value;
    }

    /**
     * Gets the value of the photoFilterDropInSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoFilterDropInSize() {
        return photoFilterDropInSize;
    }

    /**
     * Sets the value of the photoFilterDropInSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoFilterDropInSize(String value) {
        this.photoFilterDropInSize = value;
    }

    /**
     * Gets the value of the photoFilterEffectSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoFilterEffectSize() {
        return photoFilterEffectSize;
    }

    /**
     * Sets the value of the photoFilterEffectSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoFilterEffectSize(String value) {
        this.photoFilterEffectSize = value;
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
     * Gets the value of the photoFilterMountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotoFilterMountType() {
        return photoFilterMountType;
    }

    /**
     * Sets the value of the photoFilterMountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotoFilterMountType(String value) {
        this.photoFilterMountType = value;
    }

    /**
     * Gets the value of the photoFilterThreadSize property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getPhotoFilterThreadSize() {
        return photoFilterThreadSize;
    }

    /**
     * Sets the value of the photoFilterThreadSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setPhotoFilterThreadSize(LengthDimension value) {
        this.photoFilterThreadSize = value;
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
