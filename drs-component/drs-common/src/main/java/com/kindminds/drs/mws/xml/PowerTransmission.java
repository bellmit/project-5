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
 *         &lt;element name="ProductType"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{}HundredString"&gt;
 *               &lt;enumeration value="BearingsAndBushings"/&gt;
 *               &lt;enumeration value="Belts"/&gt;
 *               &lt;enumeration value="CompressionSprings"/&gt;
 *               &lt;enumeration value="ExtensionSprings"/&gt;
 *               &lt;enumeration value="FlexibleCouplings"/&gt;
 *               &lt;enumeration value="Gears"/&gt;
 *               &lt;enumeration value="RigidCouplings"/&gt;
 *               &lt;enumeration value="ShaftCollar"/&gt;
 *               &lt;enumeration value="TorsionSprings"/&gt;
 *               &lt;enumeration value="LinearGuidesAndRails"/&gt;
 *               &lt;enumeration value="Pulleys"/&gt;
 *               &lt;enumeration value="RollerChain"/&gt;
 *               &lt;enumeration value="CouplingsCollarsAndUniversalJoints"/&gt;
 *               &lt;enumeration value="Springs"/&gt;
 *               &lt;enumeration value="Sprockets"/&gt;
 *               &lt;enumeration value="UniversalJoints"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ActiveCoils" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="AxialMisalignment" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="BeltCrossSection" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="BeltWidth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="BodyOutsideDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="BoreDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="CompressedLength" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DeflectionAngle" type="{}DegreeDimension" minOccurs="0"/&gt;
 *         &lt;element name="FaceWidth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="FlangeOutsideDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="FlangeThickness" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="GuideSupportType" type="{}HundredString" minOccurs="0"/&gt;
 *         &lt;element name="HubDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="HubProjection" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="ItemPackageQuantity" type="{}Dimension" minOccurs="0"/&gt;
 *         &lt;element name="ItemPitch" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="ItemThickness" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="KeyWayDepth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="KeyWayWidth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="LegLength" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="LoadCapacity" type="{}WeightDimension" minOccurs="0"/&gt;
 *         &lt;element name="MaterialType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MaximumAngularMisalignment" type="{}DegreeDimension" minOccurs="0"/&gt;
 *         &lt;element name="MaximumParallelMisalignment" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="MaximumRotationalSpeed" type="{}SpeedDimension" minOccurs="0"/&gt;
 *         &lt;element name="MaximumSpringCompressionLoad" type="{}TorqueType" minOccurs="0"/&gt;
 *         &lt;element name="MaximumTensionLoad" type="{}TorqueType" minOccurs="0"/&gt;
 *         &lt;element name="MaximumTorque" type="{}TorqueType" minOccurs="0"/&gt;
 *         &lt;element name="MinimumSpringCompressionLoad" type="{}TorqueType" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfBands" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfGrooves" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfItems" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfTeeth" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="OuterRingWidth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="SetScrewThreadType" type="{}HundredString" minOccurs="0"/&gt;
 *         &lt;element name="SlideTravelDistance" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="SpringRate" type="{}TorqueType" minOccurs="0"/&gt;
 *         &lt;element name="SpringWindDirection" type="{}HundredString" minOccurs="0"/&gt;
 *         &lt;element name="StrandType" type="{}HundredString" minOccurs="0"/&gt;
 *         &lt;element name="TradeSizeName" type="{}HundredString" minOccurs="0"/&gt;
 *         &lt;element name="WireDiameter" type="{}LengthDimension" minOccurs="0"/&gt;
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
    "productType",
    "activeCoils",
    "axialMisalignment",
    "beltCrossSection",
    "beltWidth",
    "bodyOutsideDiameter",
    "boreDiameter",
    "compressedLength",
    "deflectionAngle",
    "faceWidth",
    "flangeOutsideDiameter",
    "flangeThickness",
    "guideSupportType",
    "hubDiameter",
    "hubProjection",
    "itemPackageQuantity",
    "itemPitch",
    "itemThickness",
    "keyWayDepth",
    "keyWayWidth",
    "legLength",
    "loadCapacity",
    "materialType",
    "maximumAngularMisalignment",
    "maximumParallelMisalignment",
    "maximumRotationalSpeed",
    "maximumSpringCompressionLoad",
    "maximumTensionLoad",
    "maximumTorque",
    "minimumSpringCompressionLoad",
    "numberOfBands",
    "numberOfGrooves",
    "numberOfItems",
    "numberOfTeeth",
    "outerRingWidth",
    "setScrewThreadType",
    "slideTravelDistance",
    "springRate",
    "springWindDirection",
    "strandType",
    "tradeSizeName",
    "wireDiameter"
})
@XmlRootElement(name = "PowerTransmission")
public class PowerTransmission {

    @XmlElement(name = "ProductType", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String productType;
    @XmlElement(name = "ActiveCoils")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger activeCoils;
    @XmlElement(name = "AxialMisalignment")
    protected LengthDimension axialMisalignment;
    @XmlElement(name = "BeltCrossSection")
    protected LengthDimension beltCrossSection;
    @XmlElement(name = "BeltWidth")
    protected LengthDimension beltWidth;
    @XmlElement(name = "BodyOutsideDiameter")
    protected LengthDimension bodyOutsideDiameter;
    @XmlElement(name = "BoreDiameter")
    protected LengthDimension boreDiameter;
    @XmlElement(name = "CompressedLength")
    protected LengthDimension compressedLength;
    @XmlElement(name = "DeflectionAngle")
    protected DegreeDimension deflectionAngle;
    @XmlElement(name = "FaceWidth")
    protected LengthDimension faceWidth;
    @XmlElement(name = "FlangeOutsideDiameter")
    protected LengthDimension flangeOutsideDiameter;
    @XmlElement(name = "FlangeThickness")
    protected LengthDimension flangeThickness;
    @XmlElement(name = "GuideSupportType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String guideSupportType;
    @XmlElement(name = "HubDiameter")
    protected LengthDimension hubDiameter;
    @XmlElement(name = "HubProjection")
    protected LengthDimension hubProjection;
    @XmlElement(name = "ItemPackageQuantity")
    protected BigDecimal itemPackageQuantity;
    @XmlElement(name = "ItemPitch")
    protected LengthDimension itemPitch;
    @XmlElement(name = "ItemThickness")
    protected LengthDimension itemThickness;
    @XmlElement(name = "KeyWayDepth")
    protected LengthDimension keyWayDepth;
    @XmlElement(name = "KeyWayWidth")
    protected LengthDimension keyWayWidth;
    @XmlElement(name = "LegLength")
    protected LengthDimension legLength;
    @XmlElement(name = "LoadCapacity")
    protected WeightDimension loadCapacity;
    @XmlElement(name = "MaterialType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String materialType;
    @XmlElement(name = "MaximumAngularMisalignment")
    protected DegreeDimension maximumAngularMisalignment;
    @XmlElement(name = "MaximumParallelMisalignment")
    protected LengthDimension maximumParallelMisalignment;
    @XmlElement(name = "MaximumRotationalSpeed")
    protected SpeedDimension maximumRotationalSpeed;
    @XmlElement(name = "MaximumSpringCompressionLoad")
    protected TorqueType maximumSpringCompressionLoad;
    @XmlElement(name = "MaximumTensionLoad")
    protected TorqueType maximumTensionLoad;
    @XmlElement(name = "MaximumTorque")
    protected TorqueType maximumTorque;
    @XmlElement(name = "MinimumSpringCompressionLoad")
    protected TorqueType minimumSpringCompressionLoad;
    @XmlElement(name = "NumberOfBands")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfBands;
    @XmlElement(name = "NumberOfGrooves")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfGrooves;
    @XmlElement(name = "NumberOfItems")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfItems;
    @XmlElement(name = "NumberOfTeeth")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfTeeth;
    @XmlElement(name = "OuterRingWidth")
    protected LengthDimension outerRingWidth;
    @XmlElement(name = "SetScrewThreadType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String setScrewThreadType;
    @XmlElement(name = "SlideTravelDistance")
    protected LengthDimension slideTravelDistance;
    @XmlElement(name = "SpringRate")
    protected TorqueType springRate;
    @XmlElement(name = "SpringWindDirection")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String springWindDirection;
    @XmlElement(name = "StrandType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String strandType;
    @XmlElement(name = "TradeSizeName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String tradeSizeName;
    @XmlElement(name = "WireDiameter")
    protected LengthDimension wireDiameter;

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * Gets the value of the activeCoils property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getActiveCoils() {
        return activeCoils;
    }

    /**
     * Sets the value of the activeCoils property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setActiveCoils(BigInteger value) {
        this.activeCoils = value;
    }

    /**
     * Gets the value of the axialMisalignment property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getAxialMisalignment() {
        return axialMisalignment;
    }

    /**
     * Sets the value of the axialMisalignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setAxialMisalignment(LengthDimension value) {
        this.axialMisalignment = value;
    }

    /**
     * Gets the value of the beltCrossSection property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getBeltCrossSection() {
        return beltCrossSection;
    }

    /**
     * Sets the value of the beltCrossSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setBeltCrossSection(LengthDimension value) {
        this.beltCrossSection = value;
    }

    /**
     * Gets the value of the beltWidth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getBeltWidth() {
        return beltWidth;
    }

    /**
     * Sets the value of the beltWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setBeltWidth(LengthDimension value) {
        this.beltWidth = value;
    }

    /**
     * Gets the value of the bodyOutsideDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getBodyOutsideDiameter() {
        return bodyOutsideDiameter;
    }

    /**
     * Sets the value of the bodyOutsideDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setBodyOutsideDiameter(LengthDimension value) {
        this.bodyOutsideDiameter = value;
    }

    /**
     * Gets the value of the boreDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getBoreDiameter() {
        return boreDiameter;
    }

    /**
     * Sets the value of the boreDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setBoreDiameter(LengthDimension value) {
        this.boreDiameter = value;
    }

    /**
     * Gets the value of the compressedLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getCompressedLength() {
        return compressedLength;
    }

    /**
     * Sets the value of the compressedLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setCompressedLength(LengthDimension value) {
        this.compressedLength = value;
    }

    /**
     * Gets the value of the deflectionAngle property.
     * 
     * @return
     *     possible object is
     *     {@link DegreeDimension }
     *     
     */
    public DegreeDimension getDeflectionAngle() {
        return deflectionAngle;
    }

    /**
     * Sets the value of the deflectionAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link DegreeDimension }
     *     
     */
    public void setDeflectionAngle(DegreeDimension value) {
        this.deflectionAngle = value;
    }

    /**
     * Gets the value of the faceWidth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getFaceWidth() {
        return faceWidth;
    }

    /**
     * Sets the value of the faceWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setFaceWidth(LengthDimension value) {
        this.faceWidth = value;
    }

    /**
     * Gets the value of the flangeOutsideDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getFlangeOutsideDiameter() {
        return flangeOutsideDiameter;
    }

    /**
     * Sets the value of the flangeOutsideDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setFlangeOutsideDiameter(LengthDimension value) {
        this.flangeOutsideDiameter = value;
    }

    /**
     * Gets the value of the flangeThickness property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getFlangeThickness() {
        return flangeThickness;
    }

    /**
     * Sets the value of the flangeThickness property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setFlangeThickness(LengthDimension value) {
        this.flangeThickness = value;
    }

    /**
     * Gets the value of the guideSupportType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuideSupportType() {
        return guideSupportType;
    }

    /**
     * Sets the value of the guideSupportType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuideSupportType(String value) {
        this.guideSupportType = value;
    }

    /**
     * Gets the value of the hubDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getHubDiameter() {
        return hubDiameter;
    }

    /**
     * Sets the value of the hubDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setHubDiameter(LengthDimension value) {
        this.hubDiameter = value;
    }

    /**
     * Gets the value of the hubProjection property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getHubProjection() {
        return hubProjection;
    }

    /**
     * Sets the value of the hubProjection property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setHubProjection(LengthDimension value) {
        this.hubProjection = value;
    }

    /**
     * Gets the value of the itemPackageQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getItemPackageQuantity() {
        return itemPackageQuantity;
    }

    /**
     * Sets the value of the itemPackageQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setItemPackageQuantity(BigDecimal value) {
        this.itemPackageQuantity = value;
    }

    /**
     * Gets the value of the itemPitch property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getItemPitch() {
        return itemPitch;
    }

    /**
     * Sets the value of the itemPitch property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setItemPitch(LengthDimension value) {
        this.itemPitch = value;
    }

    /**
     * Gets the value of the itemThickness property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getItemThickness() {
        return itemThickness;
    }

    /**
     * Sets the value of the itemThickness property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setItemThickness(LengthDimension value) {
        this.itemThickness = value;
    }

    /**
     * Gets the value of the keyWayDepth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getKeyWayDepth() {
        return keyWayDepth;
    }

    /**
     * Sets the value of the keyWayDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setKeyWayDepth(LengthDimension value) {
        this.keyWayDepth = value;
    }

    /**
     * Gets the value of the keyWayWidth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getKeyWayWidth() {
        return keyWayWidth;
    }

    /**
     * Sets the value of the keyWayWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setKeyWayWidth(LengthDimension value) {
        this.keyWayWidth = value;
    }

    /**
     * Gets the value of the legLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getLegLength() {
        return legLength;
    }

    /**
     * Sets the value of the legLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setLegLength(LengthDimension value) {
        this.legLength = value;
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
     * Gets the value of the materialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialType() {
        return materialType;
    }

    /**
     * Sets the value of the materialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialType(String value) {
        this.materialType = value;
    }

    /**
     * Gets the value of the maximumAngularMisalignment property.
     * 
     * @return
     *     possible object is
     *     {@link DegreeDimension }
     *     
     */
    public DegreeDimension getMaximumAngularMisalignment() {
        return maximumAngularMisalignment;
    }

    /**
     * Sets the value of the maximumAngularMisalignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link DegreeDimension }
     *     
     */
    public void setMaximumAngularMisalignment(DegreeDimension value) {
        this.maximumAngularMisalignment = value;
    }

    /**
     * Gets the value of the maximumParallelMisalignment property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getMaximumParallelMisalignment() {
        return maximumParallelMisalignment;
    }

    /**
     * Sets the value of the maximumParallelMisalignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setMaximumParallelMisalignment(LengthDimension value) {
        this.maximumParallelMisalignment = value;
    }

    /**
     * Gets the value of the maximumRotationalSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link SpeedDimension }
     *     
     */
    public SpeedDimension getMaximumRotationalSpeed() {
        return maximumRotationalSpeed;
    }

    /**
     * Sets the value of the maximumRotationalSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpeedDimension }
     *     
     */
    public void setMaximumRotationalSpeed(SpeedDimension value) {
        this.maximumRotationalSpeed = value;
    }

    /**
     * Gets the value of the maximumSpringCompressionLoad property.
     * 
     * @return
     *     possible object is
     *     {@link TorqueType }
     *     
     */
    public TorqueType getMaximumSpringCompressionLoad() {
        return maximumSpringCompressionLoad;
    }

    /**
     * Sets the value of the maximumSpringCompressionLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link TorqueType }
     *     
     */
    public void setMaximumSpringCompressionLoad(TorqueType value) {
        this.maximumSpringCompressionLoad = value;
    }

    /**
     * Gets the value of the maximumTensionLoad property.
     * 
     * @return
     *     possible object is
     *     {@link TorqueType }
     *     
     */
    public TorqueType getMaximumTensionLoad() {
        return maximumTensionLoad;
    }

    /**
     * Sets the value of the maximumTensionLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link TorqueType }
     *     
     */
    public void setMaximumTensionLoad(TorqueType value) {
        this.maximumTensionLoad = value;
    }

    /**
     * Gets the value of the maximumTorque property.
     * 
     * @return
     *     possible object is
     *     {@link TorqueType }
     *     
     */
    public TorqueType getMaximumTorque() {
        return maximumTorque;
    }

    /**
     * Sets the value of the maximumTorque property.
     * 
     * @param value
     *     allowed object is
     *     {@link TorqueType }
     *     
     */
    public void setMaximumTorque(TorqueType value) {
        this.maximumTorque = value;
    }

    /**
     * Gets the value of the minimumSpringCompressionLoad property.
     * 
     * @return
     *     possible object is
     *     {@link TorqueType }
     *     
     */
    public TorqueType getMinimumSpringCompressionLoad() {
        return minimumSpringCompressionLoad;
    }

    /**
     * Sets the value of the minimumSpringCompressionLoad property.
     * 
     * @param value
     *     allowed object is
     *     {@link TorqueType }
     *     
     */
    public void setMinimumSpringCompressionLoad(TorqueType value) {
        this.minimumSpringCompressionLoad = value;
    }

    /**
     * Gets the value of the numberOfBands property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfBands() {
        return numberOfBands;
    }

    /**
     * Sets the value of the numberOfBands property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfBands(BigInteger value) {
        this.numberOfBands = value;
    }

    /**
     * Gets the value of the numberOfGrooves property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfGrooves() {
        return numberOfGrooves;
    }

    /**
     * Sets the value of the numberOfGrooves property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfGrooves(BigInteger value) {
        this.numberOfGrooves = value;
    }

    /**
     * Gets the value of the numberOfItems property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * Sets the value of the numberOfItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfItems(BigInteger value) {
        this.numberOfItems = value;
    }

    /**
     * Gets the value of the numberOfTeeth property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfTeeth() {
        return numberOfTeeth;
    }

    /**
     * Sets the value of the numberOfTeeth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfTeeth(BigInteger value) {
        this.numberOfTeeth = value;
    }

    /**
     * Gets the value of the outerRingWidth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getOuterRingWidth() {
        return outerRingWidth;
    }

    /**
     * Sets the value of the outerRingWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setOuterRingWidth(LengthDimension value) {
        this.outerRingWidth = value;
    }

    /**
     * Gets the value of the setScrewThreadType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetScrewThreadType() {
        return setScrewThreadType;
    }

    /**
     * Sets the value of the setScrewThreadType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetScrewThreadType(String value) {
        this.setScrewThreadType = value;
    }

    /**
     * Gets the value of the slideTravelDistance property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getSlideTravelDistance() {
        return slideTravelDistance;
    }

    /**
     * Sets the value of the slideTravelDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setSlideTravelDistance(LengthDimension value) {
        this.slideTravelDistance = value;
    }

    /**
     * Gets the value of the springRate property.
     * 
     * @return
     *     possible object is
     *     {@link TorqueType }
     *     
     */
    public TorqueType getSpringRate() {
        return springRate;
    }

    /**
     * Sets the value of the springRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TorqueType }
     *     
     */
    public void setSpringRate(TorqueType value) {
        this.springRate = value;
    }

    /**
     * Gets the value of the springWindDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpringWindDirection() {
        return springWindDirection;
    }

    /**
     * Sets the value of the springWindDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpringWindDirection(String value) {
        this.springWindDirection = value;
    }

    /**
     * Gets the value of the strandType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrandType() {
        return strandType;
    }

    /**
     * Sets the value of the strandType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrandType(String value) {
        this.strandType = value;
    }

    /**
     * Gets the value of the tradeSizeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTradeSizeName() {
        return tradeSizeName;
    }

    /**
     * Sets the value of the tradeSizeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTradeSizeName(String value) {
        this.tradeSizeName = value;
    }

    /**
     * Gets the value of the wireDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getWireDiameter() {
        return wireDiameter;
    }

    /**
     * Sets the value of the wireDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setWireDiameter(LengthDimension value) {
        this.wireDiameter = value;
    }

}
