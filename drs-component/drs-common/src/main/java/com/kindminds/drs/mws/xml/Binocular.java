//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

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
 *         &lt;element name="BinocularType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="binoculars"/&gt;
 *               &lt;enumeration value="monoculars"/&gt;
 *               &lt;enumeration value="laser-rangefinders"/&gt;
 *               &lt;enumeration value="spotting-scopes"/&gt;
 *               &lt;enumeration value="night-vision"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FocusType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="PrismType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ObjectiveLensDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="ExitPupilDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="FieldOfView" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="ApparentAngleOfView" type="{}DegreeDimension" minOccurs="0"/&gt;
 *         &lt;element name="RealAngleOfView" type="{}DegreeDimension" minOccurs="0"/&gt;
 *         &lt;element name="EyeRelief" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DiopterAdjustmentRange" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Coating" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="EyepieceLensConstruction" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ObjectiveLensConstruction" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="TripodReady" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="Features" maxOccurs="9" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="compact"/&gt;
 *               &lt;enumeration value="full-size"/&gt;
 *               &lt;enumeration value="image-stabilizing"/&gt;
 *               &lt;enumeration value="waterproof"/&gt;
 *               &lt;enumeration value="fogproof"/&gt;
 *               &lt;enumeration value="zoom"/&gt;
 *               &lt;enumeration value="uv-protection"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Magnification" type="{}ZoomDimension" minOccurs="0"/&gt;
 *         &lt;element name="SpecificUses" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="tabletop"/&gt;
 *               &lt;enumeration value="travel"/&gt;
 *               &lt;enumeration value="hiking-and-outdoors"/&gt;
 *               &lt;enumeration value="hunting-and-shooting"/&gt;
 *               &lt;enumeration value="sports"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="WeightLimit" type="{}WeightDimension" minOccurs="0"/&gt;
 *         &lt;element name="WirelessTechnology" type="{}LongString" minOccurs="0"/&gt;
 *         &lt;element name="ZoomRatio" type="{}LongString" minOccurs="0"/&gt;
 *         &lt;element name="ItemTypeName" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="InternationalProtectionRating" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="IncludedFeatures" type="{}StringNotNull" minOccurs="0"/&gt;
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
    "binocularType",
    "focusType",
    "prismType",
    "objectiveLensDiameter",
    "exitPupilDiameter",
    "fieldOfView",
    "apparentAngleOfView",
    "realAngleOfView",
    "eyeRelief",
    "diopterAdjustmentRange",
    "coating",
    "eyepieceLensConstruction",
    "objectiveLensConstruction",
    "tripodReady",
    "features",
    "magnification",
    "specificUses",
    "weightLimit",
    "wirelessTechnology",
    "zoomRatio",
    "itemTypeName",
    "internationalProtectionRating",
    "includedFeatures"
})
@XmlRootElement(name = "Binocular")
public class Binocular {

    @XmlElement(name = "BinocularType")
    protected String binocularType;
    @XmlElement(name = "FocusType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String focusType;
    @XmlElement(name = "PrismType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String prismType;
    @XmlElement(name = "ObjectiveLensDiameter")
    protected LengthDimension objectiveLensDiameter;
    @XmlElement(name = "ExitPupilDiameter")
    protected LengthDimension exitPupilDiameter;
    @XmlElement(name = "FieldOfView")
    protected LengthDimension fieldOfView;
    @XmlElement(name = "ApparentAngleOfView")
    protected DegreeDimension apparentAngleOfView;
    @XmlElement(name = "RealAngleOfView")
    protected DegreeDimension realAngleOfView;
    @XmlElement(name = "EyeRelief")
    protected LengthDimension eyeRelief;
    @XmlElement(name = "DiopterAdjustmentRange")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String diopterAdjustmentRange;
    @XmlElement(name = "Coating")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String coating;
    @XmlElement(name = "EyepieceLensConstruction")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String eyepieceLensConstruction;
    @XmlElement(name = "ObjectiveLensConstruction")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String objectiveLensConstruction;
    @XmlElement(name = "TripodReady")
    protected Boolean tripodReady;
    @XmlElement(name = "Features")
    protected List<String> features;
    @XmlElement(name = "Magnification")
    protected ZoomDimension magnification;
    @XmlElement(name = "SpecificUses")
    protected String specificUses;
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
    @XmlElement(name = "ItemTypeName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String itemTypeName;
    @XmlElement(name = "InternationalProtectionRating")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String internationalProtectionRating;
    @XmlElement(name = "IncludedFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String includedFeatures;

    /**
     * Gets the value of the binocularType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinocularType() {
        return binocularType;
    }

    /**
     * Sets the value of the binocularType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinocularType(String value) {
        this.binocularType = value;
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
     * Gets the value of the prismType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrismType() {
        return prismType;
    }

    /**
     * Sets the value of the prismType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrismType(String value) {
        this.prismType = value;
    }

    /**
     * Gets the value of the objectiveLensDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getObjectiveLensDiameter() {
        return objectiveLensDiameter;
    }

    /**
     * Sets the value of the objectiveLensDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setObjectiveLensDiameter(LengthDimension value) {
        this.objectiveLensDiameter = value;
    }

    /**
     * Gets the value of the exitPupilDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getExitPupilDiameter() {
        return exitPupilDiameter;
    }

    /**
     * Sets the value of the exitPupilDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setExitPupilDiameter(LengthDimension value) {
        this.exitPupilDiameter = value;
    }

    /**
     * Gets the value of the fieldOfView property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getFieldOfView() {
        return fieldOfView;
    }

    /**
     * Sets the value of the fieldOfView property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setFieldOfView(LengthDimension value) {
        this.fieldOfView = value;
    }

    /**
     * Gets the value of the apparentAngleOfView property.
     * 
     * @return
     *     possible object is
     *     {@link DegreeDimension }
     *     
     */
    public DegreeDimension getApparentAngleOfView() {
        return apparentAngleOfView;
    }

    /**
     * Sets the value of the apparentAngleOfView property.
     * 
     * @param value
     *     allowed object is
     *     {@link DegreeDimension }
     *     
     */
    public void setApparentAngleOfView(DegreeDimension value) {
        this.apparentAngleOfView = value;
    }

    /**
     * Gets the value of the realAngleOfView property.
     * 
     * @return
     *     possible object is
     *     {@link DegreeDimension }
     *     
     */
    public DegreeDimension getRealAngleOfView() {
        return realAngleOfView;
    }

    /**
     * Sets the value of the realAngleOfView property.
     * 
     * @param value
     *     allowed object is
     *     {@link DegreeDimension }
     *     
     */
    public void setRealAngleOfView(DegreeDimension value) {
        this.realAngleOfView = value;
    }

    /**
     * Gets the value of the eyeRelief property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getEyeRelief() {
        return eyeRelief;
    }

    /**
     * Sets the value of the eyeRelief property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setEyeRelief(LengthDimension value) {
        this.eyeRelief = value;
    }

    /**
     * Gets the value of the diopterAdjustmentRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiopterAdjustmentRange() {
        return diopterAdjustmentRange;
    }

    /**
     * Sets the value of the diopterAdjustmentRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiopterAdjustmentRange(String value) {
        this.diopterAdjustmentRange = value;
    }

    /**
     * Gets the value of the coating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoating() {
        return coating;
    }

    /**
     * Sets the value of the coating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoating(String value) {
        this.coating = value;
    }

    /**
     * Gets the value of the eyepieceLensConstruction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEyepieceLensConstruction() {
        return eyepieceLensConstruction;
    }

    /**
     * Sets the value of the eyepieceLensConstruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEyepieceLensConstruction(String value) {
        this.eyepieceLensConstruction = value;
    }

    /**
     * Gets the value of the objectiveLensConstruction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectiveLensConstruction() {
        return objectiveLensConstruction;
    }

    /**
     * Sets the value of the objectiveLensConstruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectiveLensConstruction(String value) {
        this.objectiveLensConstruction = value;
    }

    /**
     * Gets the value of the tripodReady property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTripodReady() {
        return tripodReady;
    }

    /**
     * Sets the value of the tripodReady property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTripodReady(Boolean value) {
        this.tripodReady = value;
    }

    /**
     * Gets the value of the features property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the features property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFeatures().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFeatures() {
        if (features == null) {
            features = new ArrayList<String>();
        }
        return this.features;
    }

    /**
     * Gets the value of the magnification property.
     * 
     * @return
     *     possible object is
     *     {@link ZoomDimension }
     *     
     */
    public ZoomDimension getMagnification() {
        return magnification;
    }

    /**
     * Sets the value of the magnification property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoomDimension }
     *     
     */
    public void setMagnification(ZoomDimension value) {
        this.magnification = value;
    }

    /**
     * Gets the value of the specificUses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecificUses() {
        return specificUses;
    }

    /**
     * Sets the value of the specificUses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecificUses(String value) {
        this.specificUses = value;
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
     * Gets the value of the internationalProtectionRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternationalProtectionRating() {
        return internationalProtectionRating;
    }

    /**
     * Sets the value of the internationalProtectionRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternationalProtectionRating(String value) {
        this.internationalProtectionRating = value;
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

}
