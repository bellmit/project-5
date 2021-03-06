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
 *                         &lt;enumeration value="Size"/&gt;
 *                         &lt;enumeration value="Color"/&gt;
 *                         &lt;enumeration value="ItemWeight"/&gt;
 *                         &lt;enumeration value="Size-Color"/&gt;
 *                         &lt;enumeration value="Style"/&gt;
 *                         &lt;enumeration value="PATTERN"/&gt;
 *                         &lt;enumeration value="CustomerPackageType"/&gt;
 *                         &lt;enumeration value="ColorName-CustomerPackageType"/&gt;
 *                         &lt;enumeration value="SizeName-CustomerPackageType"/&gt;
 *                         &lt;enumeration value="SizeName-ColorName-CustomerPackageType"/&gt;
 *                         &lt;enumeration value="StyleName-CustomerPackageType"/&gt;
 *                         &lt;enumeration value="SizeName-StyleName-CustomerPackageType"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Size" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Color" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ColorMap" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Material" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MinimumEfficiencyReportingValue" type="{}MERVType" minOccurs="0"/&gt;
 *         &lt;element name="PowerSource" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Voltage" type="{}Dimension" minOccurs="0"/&gt;
 *         &lt;element name="Wattage" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="BatteryCapacity" type="{}BatteryPowerDimension" minOccurs="0"/&gt;
 *         &lt;element name="ManufacturerWarrantyDescription" type="{}LongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="SellerWarrantyDescription" type="{}SuperLongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="CustomerPackageType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="LegalComplianceCertificationMetadata" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="LegalComplianceCertificationDateOfIssue" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="LegalComplianceCertificationExpirationDate" type="{}StringNotNull" minOccurs="0"/&gt;
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
    "size",
    "color",
    "colorMap",
    "material",
    "minimumEfficiencyReportingValue",
    "powerSource",
    "voltage",
    "wattage",
    "batteryCapacity",
    "manufacturerWarrantyDescription",
    "sellerWarrantyDescription",
    "customerPackageType",
    "legalComplianceCertificationMetadata",
    "legalComplianceCertificationDateOfIssue",
    "legalComplianceCertificationExpirationDate",
    "powerPlugType"
})
@XmlRootElement(name = "MajorHomeAppliances")
public class MajorHomeAppliances {

    @XmlElement(name = "VariationData")
    protected MajorHomeAppliances.VariationData variationData;
    @XmlElement(name = "Size")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String size;
    @XmlElement(name = "Color")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String color;
    @XmlElement(name = "ColorMap")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String colorMap;
    @XmlElement(name = "Material")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String material;
    @XmlElement(name = "MinimumEfficiencyReportingValue")
    @XmlSchemaType(name = "positiveInteger")
    protected Integer minimumEfficiencyReportingValue;
    @XmlElement(name = "PowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String powerSource;
    @XmlElement(name = "Voltage")
    protected BigDecimal voltage;
    @XmlElement(name = "Wattage")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger wattage;
    @XmlElement(name = "BatteryCapacity")
    protected BatteryPowerDimension batteryCapacity;
    @XmlElement(name = "ManufacturerWarrantyDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String manufacturerWarrantyDescription;
    @XmlElement(name = "SellerWarrantyDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String sellerWarrantyDescription;
    @XmlElement(name = "CustomerPackageType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String customerPackageType;
    @XmlElement(name = "LegalComplianceCertificationMetadata")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String legalComplianceCertificationMetadata;
    @XmlElement(name = "LegalComplianceCertificationDateOfIssue")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String legalComplianceCertificationDateOfIssue;
    @XmlElement(name = "LegalComplianceCertificationExpirationDate")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String legalComplianceCertificationExpirationDate;
    @XmlElement(name = "PowerPlugType")
    @XmlSchemaType(name = "normalizedString")
    protected PowerPlugType powerPlugType;

    /**
     * Gets the value of the variationData property.
     * 
     * @return
     *     possible object is
     *     {@link MajorHomeAppliances.VariationData }
     *     
     */
    public MajorHomeAppliances.VariationData getVariationData() {
        return variationData;
    }

    /**
     * Sets the value of the variationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MajorHomeAppliances.VariationData }
     *     
     */
    public void setVariationData(MajorHomeAppliances.VariationData value) {
        this.variationData = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the colorMap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorMap() {
        return colorMap;
    }

    /**
     * Sets the value of the colorMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorMap(String value) {
        this.colorMap = value;
    }

    /**
     * Gets the value of the material property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Sets the value of the material property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterial(String value) {
        this.material = value;
    }

    /**
     * Gets the value of the minimumEfficiencyReportingValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinimumEfficiencyReportingValue() {
        return minimumEfficiencyReportingValue;
    }

    /**
     * Sets the value of the minimumEfficiencyReportingValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinimumEfficiencyReportingValue(Integer value) {
        this.minimumEfficiencyReportingValue = value;
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
     * Gets the value of the batteryCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link BatteryPowerDimension }
     *     
     */
    public BatteryPowerDimension getBatteryCapacity() {
        return batteryCapacity;
    }

    /**
     * Sets the value of the batteryCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatteryPowerDimension }
     *     
     */
    public void setBatteryCapacity(BatteryPowerDimension value) {
        this.batteryCapacity = value;
    }

    /**
     * Gets the value of the manufacturerWarrantyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerWarrantyDescription() {
        return manufacturerWarrantyDescription;
    }

    /**
     * Sets the value of the manufacturerWarrantyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerWarrantyDescription(String value) {
        this.manufacturerWarrantyDescription = value;
    }

    /**
     * Gets the value of the sellerWarrantyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerWarrantyDescription() {
        return sellerWarrantyDescription;
    }

    /**
     * Sets the value of the sellerWarrantyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerWarrantyDescription(String value) {
        this.sellerWarrantyDescription = value;
    }

    /**
     * Gets the value of the customerPackageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerPackageType() {
        return customerPackageType;
    }

    /**
     * Sets the value of the customerPackageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerPackageType(String value) {
        this.customerPackageType = value;
    }

    /**
     * Gets the value of the legalComplianceCertificationMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalComplianceCertificationMetadata() {
        return legalComplianceCertificationMetadata;
    }

    /**
     * Sets the value of the legalComplianceCertificationMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalComplianceCertificationMetadata(String value) {
        this.legalComplianceCertificationMetadata = value;
    }

    /**
     * Gets the value of the legalComplianceCertificationDateOfIssue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalComplianceCertificationDateOfIssue() {
        return legalComplianceCertificationDateOfIssue;
    }

    /**
     * Sets the value of the legalComplianceCertificationDateOfIssue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalComplianceCertificationDateOfIssue(String value) {
        this.legalComplianceCertificationDateOfIssue = value;
    }

    /**
     * Gets the value of the legalComplianceCertificationExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalComplianceCertificationExpirationDate() {
        return legalComplianceCertificationExpirationDate;
    }

    /**
     * Sets the value of the legalComplianceCertificationExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalComplianceCertificationExpirationDate(String value) {
        this.legalComplianceCertificationExpirationDate = value;
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
     *               &lt;enumeration value="Size"/&gt;
     *               &lt;enumeration value="Color"/&gt;
     *               &lt;enumeration value="ItemWeight"/&gt;
     *               &lt;enumeration value="Size-Color"/&gt;
     *               &lt;enumeration value="Style"/&gt;
     *               &lt;enumeration value="PATTERN"/&gt;
     *               &lt;enumeration value="CustomerPackageType"/&gt;
     *               &lt;enumeration value="ColorName-CustomerPackageType"/&gt;
     *               &lt;enumeration value="SizeName-CustomerPackageType"/&gt;
     *               &lt;enumeration value="SizeName-ColorName-CustomerPackageType"/&gt;
     *               &lt;enumeration value="StyleName-CustomerPackageType"/&gt;
     *               &lt;enumeration value="SizeName-StyleName-CustomerPackageType"/&gt;
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
