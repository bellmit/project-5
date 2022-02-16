//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

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
 *         &lt;element name="TelescopeType" maxOccurs="2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="general"/&gt;
 *               &lt;enumeration value="mirror-lens"/&gt;
 *               &lt;enumeration value="schmidt-cassegrain"/&gt;
 *               &lt;enumeration value="maksutov-cassegrain"/&gt;
 *               &lt;enumeration value="reflecting"/&gt;
 *               &lt;enumeration value="newtonian-reflector"/&gt;
 *               &lt;enumeration value="rich-field-reflector"/&gt;
 *               &lt;enumeration value="dobsonian-reflector"/&gt;
 *               &lt;enumeration value="refracting"/&gt;
 *               &lt;enumeration value="achromatic-refractor"/&gt;
 *               &lt;enumeration value="apochromatic-refractor"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TelescopeEyepiece" maxOccurs="2" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="erfle"/&gt;
 *               &lt;enumeration value="kellner-and-rke"/&gt;
 *               &lt;enumeration value="nagler"/&gt;
 *               &lt;enumeration value="orthoscopic"/&gt;
 *               &lt;enumeration value="plossl"/&gt;
 *               &lt;enumeration value="sma"/&gt;
 *               &lt;enumeration value="ultra-wide"/&gt;
 *               &lt;enumeration value="zoom"/&gt;
 *               &lt;enumeration value="telescope-eyepieces-general"/&gt;
 *               &lt;enumeration value="other-eyepieces"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MinAperture" type="{}ApertureDimension" minOccurs="0"/&gt;
 *         &lt;element name="MaxAperture" type="{}ApertureDimension" minOccurs="0"/&gt;
 *         &lt;element name="PrimaryAperture" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="under-80mm"/&gt;
 *               &lt;enumeration value="80mm-90mm"/&gt;
 *               &lt;enumeration value="100mm-150mm"/&gt;
 *               &lt;enumeration value="150mm-200mm"/&gt;
 *               &lt;enumeration value="over-200mm"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FocalLength" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="ResolvingPower" type="{}ResolvingPowerDimension" minOccurs="0"/&gt;
 *         &lt;element name="Mount" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="HighestUsefulMagnification" type="{}ZoomDimension" minOccurs="0"/&gt;
 *         &lt;element name="LCDTiltAngle" type="{}DegreeDimension" minOccurs="0"/&gt;
 *         &lt;element name="LowestUsefulMagnification" type="{}ZoomDimension" minOccurs="0"/&gt;
 *         &lt;element name="OpticalTubeLength" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="OpticalTubeDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="OpticalCoatings" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MotorizedControls" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Viewfinder" type="{}StringNotNull" maxOccurs="3" minOccurs="0"/&gt;
 *         &lt;element name="EyepieceType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="OutdoorUse" type="{}LongString" minOccurs="0"/&gt;
 *         &lt;element name="PhotographicResolution" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="DawesLimit" type="{}DawesLimitDimension" minOccurs="0"/&gt;
 *         &lt;element ref="{}ComputerPlatform" maxOccurs="3" minOccurs="0"/&gt;
 *         &lt;element name="BatteryType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="BatteryIncluded" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="WeightLimit" type="{}WeightDimension" minOccurs="0"/&gt;
 *         &lt;element name="WirelessTechnology" type="{}LongString" minOccurs="0"/&gt;
 *         &lt;element name="ZoomRatio" type="{}LongString" minOccurs="0"/&gt;
 *         &lt;element name="SupportedStandards" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ObjectiveLensDiameter" type="{}PositiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="PowerType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="ac"/&gt;
 *               &lt;enumeration value="dc"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ItemTypeName" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="IncludedFeatures" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ItemDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
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
    "telescopeType",
    "telescopeEyepiece",
    "minAperture",
    "maxAperture",
    "primaryAperture",
    "focalLength",
    "resolvingPower",
    "mount",
    "highestUsefulMagnification",
    "lcdTiltAngle",
    "lowestUsefulMagnification",
    "opticalTubeLength",
    "opticalTubeDiameter",
    "opticalCoatings",
    "motorizedControls",
    "viewfinder",
    "eyepieceType",
    "outdoorUse",
    "photographicResolution",
    "dawesLimit",
    "computerPlatform",
    "batteryType",
    "batteryIncluded",
    "weightLimit",
    "wirelessTechnology",
    "zoomRatio",
    "supportedStandards",
    "objectiveLensDiameter",
    "powerType",
    "itemTypeName",
    "includedFeatures",
    "itemDiameter"
})
@XmlRootElement(name = "Telescope")
public class Telescope {

    @XmlElement(name = "TelescopeType")
    protected List<String> telescopeType;
    @XmlElement(name = "TelescopeEyepiece")
    protected List<String> telescopeEyepiece;
    @XmlElement(name = "MinAperture")
    protected ApertureDimension minAperture;
    @XmlElement(name = "MaxAperture")
    protected ApertureDimension maxAperture;
    @XmlElement(name = "PrimaryAperture")
    protected String primaryAperture;
    @XmlElement(name = "FocalLength")
    protected LengthDimension focalLength;
    @XmlElement(name = "ResolvingPower")
    protected ResolvingPowerDimension resolvingPower;
    @XmlElement(name = "Mount")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String mount;
    @XmlElement(name = "HighestUsefulMagnification")
    protected ZoomDimension highestUsefulMagnification;
    @XmlElement(name = "LCDTiltAngle")
    protected DegreeDimension lcdTiltAngle;
    @XmlElement(name = "LowestUsefulMagnification")
    protected ZoomDimension lowestUsefulMagnification;
    @XmlElement(name = "OpticalTubeLength")
    protected LengthDimension opticalTubeLength;
    @XmlElement(name = "OpticalTubeDiameter")
    protected LengthDimension opticalTubeDiameter;
    @XmlElement(name = "OpticalCoatings")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String opticalCoatings;
    @XmlElement(name = "MotorizedControls")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String motorizedControls;
    @XmlElement(name = "Viewfinder")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> viewfinder;
    @XmlElement(name = "EyepieceType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String eyepieceType;
    @XmlElement(name = "OutdoorUse")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String outdoorUse;
    @XmlElement(name = "PhotographicResolution")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String photographicResolution;
    @XmlElement(name = "DawesLimit")
    protected DawesLimitDimension dawesLimit;
    @XmlElement(name = "ComputerPlatform")
    protected List<ComputerPlatform> computerPlatform;
    @XmlElement(name = "BatteryType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String batteryType;
    @XmlElement(name = "BatteryIncluded")
    protected Boolean batteryIncluded;
    @XmlElement(name = "WeightLimit")
    protected WeightDimension weightLimit;
    @XmlElement(name = "WirelessTechnology")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String wirelessTechnology;
    @XmlElement(name = "ZoomRatio")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String zoomRatio;
    @XmlElement(name = "SupportedStandards")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String supportedStandards;
    @XmlElement(name = "ObjectiveLensDiameter")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger objectiveLensDiameter;
    @XmlElement(name = "PowerType")
    protected String powerType;
    @XmlElement(name = "ItemTypeName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String itemTypeName;
    @XmlElement(name = "IncludedFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String includedFeatures;
    @XmlElement(name = "ItemDiameter")
    protected LengthDimension itemDiameter;

    /**
     * Gets the value of the telescopeType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telescopeType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelescopeType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTelescopeType() {
        if (telescopeType == null) {
            telescopeType = new ArrayList<String>();
        }
        return this.telescopeType;
    }

    /**
     * Gets the value of the telescopeEyepiece property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telescopeEyepiece property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelescopeEyepiece().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTelescopeEyepiece() {
        if (telescopeEyepiece == null) {
            telescopeEyepiece = new ArrayList<String>();
        }
        return this.telescopeEyepiece;
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
     * Gets the value of the primaryAperture property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryAperture() {
        return primaryAperture;
    }

    /**
     * Sets the value of the primaryAperture property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryAperture(String value) {
        this.primaryAperture = value;
    }

    /**
     * Gets the value of the focalLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getFocalLength() {
        return focalLength;
    }

    /**
     * Sets the value of the focalLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setFocalLength(LengthDimension value) {
        this.focalLength = value;
    }

    /**
     * Gets the value of the resolvingPower property.
     * 
     * @return
     *     possible object is
     *     {@link ResolvingPowerDimension }
     *     
     */
    public ResolvingPowerDimension getResolvingPower() {
        return resolvingPower;
    }

    /**
     * Sets the value of the resolvingPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolvingPowerDimension }
     *     
     */
    public void setResolvingPower(ResolvingPowerDimension value) {
        this.resolvingPower = value;
    }

    /**
     * Gets the value of the mount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMount() {
        return mount;
    }

    /**
     * Sets the value of the mount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMount(String value) {
        this.mount = value;
    }

    /**
     * Gets the value of the highestUsefulMagnification property.
     * 
     * @return
     *     possible object is
     *     {@link ZoomDimension }
     *     
     */
    public ZoomDimension getHighestUsefulMagnification() {
        return highestUsefulMagnification;
    }

    /**
     * Sets the value of the highestUsefulMagnification property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoomDimension }
     *     
     */
    public void setHighestUsefulMagnification(ZoomDimension value) {
        this.highestUsefulMagnification = value;
    }

    /**
     * Gets the value of the lcdTiltAngle property.
     * 
     * @return
     *     possible object is
     *     {@link DegreeDimension }
     *     
     */
    public DegreeDimension getLCDTiltAngle() {
        return lcdTiltAngle;
    }

    /**
     * Sets the value of the lcdTiltAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link DegreeDimension }
     *     
     */
    public void setLCDTiltAngle(DegreeDimension value) {
        this.lcdTiltAngle = value;
    }

    /**
     * Gets the value of the lowestUsefulMagnification property.
     * 
     * @return
     *     possible object is
     *     {@link ZoomDimension }
     *     
     */
    public ZoomDimension getLowestUsefulMagnification() {
        return lowestUsefulMagnification;
    }

    /**
     * Sets the value of the lowestUsefulMagnification property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoomDimension }
     *     
     */
    public void setLowestUsefulMagnification(ZoomDimension value) {
        this.lowestUsefulMagnification = value;
    }

    /**
     * Gets the value of the opticalTubeLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getOpticalTubeLength() {
        return opticalTubeLength;
    }

    /**
     * Sets the value of the opticalTubeLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setOpticalTubeLength(LengthDimension value) {
        this.opticalTubeLength = value;
    }

    /**
     * Gets the value of the opticalTubeDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getOpticalTubeDiameter() {
        return opticalTubeDiameter;
    }

    /**
     * Sets the value of the opticalTubeDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setOpticalTubeDiameter(LengthDimension value) {
        this.opticalTubeDiameter = value;
    }

    /**
     * Gets the value of the opticalCoatings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpticalCoatings() {
        return opticalCoatings;
    }

    /**
     * Sets the value of the opticalCoatings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpticalCoatings(String value) {
        this.opticalCoatings = value;
    }

    /**
     * Gets the value of the motorizedControls property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotorizedControls() {
        return motorizedControls;
    }

    /**
     * Sets the value of the motorizedControls property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotorizedControls(String value) {
        this.motorizedControls = value;
    }

    /**
     * Gets the value of the viewfinder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the viewfinder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViewfinder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getViewfinder() {
        if (viewfinder == null) {
            viewfinder = new ArrayList<String>();
        }
        return this.viewfinder;
    }

    /**
     * Gets the value of the eyepieceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEyepieceType() {
        return eyepieceType;
    }

    /**
     * Sets the value of the eyepieceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEyepieceType(String value) {
        this.eyepieceType = value;
    }

    /**
     * Gets the value of the outdoorUse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutdoorUse() {
        return outdoorUse;
    }

    /**
     * Sets the value of the outdoorUse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutdoorUse(String value) {
        this.outdoorUse = value;
    }

    /**
     * Gets the value of the photographicResolution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhotographicResolution() {
        return photographicResolution;
    }

    /**
     * Sets the value of the photographicResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhotographicResolution(String value) {
        this.photographicResolution = value;
    }

    /**
     * Gets the value of the dawesLimit property.
     * 
     * @return
     *     possible object is
     *     {@link DawesLimitDimension }
     *     
     */
    public DawesLimitDimension getDawesLimit() {
        return dawesLimit;
    }

    /**
     * Sets the value of the dawesLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link DawesLimitDimension }
     *     
     */
    public void setDawesLimit(DawesLimitDimension value) {
        this.dawesLimit = value;
    }

    /**
     * Gets the value of the computerPlatform property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the computerPlatform property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComputerPlatform().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComputerPlatform }
     * 
     * 
     */
    public List<ComputerPlatform> getComputerPlatform() {
        if (computerPlatform == null) {
            computerPlatform = new ArrayList<ComputerPlatform>();
        }
        return this.computerPlatform;
    }

    /**
     * Gets the value of the batteryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatteryType() {
        return batteryType;
    }

    /**
     * Sets the value of the batteryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatteryType(String value) {
        this.batteryType = value;
    }

    /**
     * Gets the value of the batteryIncluded property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBatteryIncluded() {
        return batteryIncluded;
    }

    /**
     * Sets the value of the batteryIncluded property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBatteryIncluded(Boolean value) {
        this.batteryIncluded = value;
    }

    /**
     * Gets the value of the weightLimit property.
     * 
     * @return
     *     possible object is
     *     {@link WeightDimension }
     *     
     */
    public WeightDimension getWeightLimit() {
        return weightLimit;
    }

    /**
     * Sets the value of the weightLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link WeightDimension }
     *     
     */
    public void setWeightLimit(WeightDimension value) {
        this.weightLimit = value;
    }

    /**
     * Gets the value of the wirelessTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWirelessTechnology() {
        return wirelessTechnology;
    }

    /**
     * Sets the value of the wirelessTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWirelessTechnology(String value) {
        this.wirelessTechnology = value;
    }

    /**
     * Gets the value of the zoomRatio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoomRatio() {
        return zoomRatio;
    }

    /**
     * Sets the value of the zoomRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoomRatio(String value) {
        this.zoomRatio = value;
    }

    /**
     * Gets the value of the supportedStandards property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupportedStandards() {
        return supportedStandards;
    }

    /**
     * Sets the value of the supportedStandards property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupportedStandards(String value) {
        this.supportedStandards = value;
    }

    /**
     * Gets the value of the objectiveLensDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getObjectiveLensDiameter() {
        return objectiveLensDiameter;
    }

    /**
     * Sets the value of the objectiveLensDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setObjectiveLensDiameter(BigInteger value) {
        this.objectiveLensDiameter = value;
    }

    /**
     * Gets the value of the powerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerType() {
        return powerType;
    }

    /**
     * Sets the value of the powerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerType(String value) {
        this.powerType = value;
    }

    /**
     * Gets the value of the itemTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * Sets the value of the itemTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemTypeName(String value) {
        this.itemTypeName = value;
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
     * Gets the value of the itemDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getItemDiameter() {
        return itemDiameter;
    }

    /**
     * Sets the value of the itemDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setItemDiameter(LengthDimension value) {
        this.itemDiameter = value;
    }

}