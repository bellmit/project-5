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
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element ref="{}BuildingMaterials"/&gt;
 *                   &lt;element ref="{}Hardware"/&gt;
 *                   &lt;element ref="{}Electrical"/&gt;
 *                   &lt;element ref="{}PlumbingFixtures"/&gt;
 *                   &lt;element name="Tools" type="{}HomeImprovementTools"/&gt;
 *                   &lt;element ref="{}OrganizersAndStorage"/&gt;
 *                   &lt;element ref="{}MajorHomeAppliances"/&gt;
 *                   &lt;element ref="{}SecurityElectronics"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref="{}HICommon" minOccurs="0"/&gt;
 *         &lt;element ref="{}Battery" minOccurs="0"/&gt;
 *         &lt;element name="BatteryAverageLife" type="{}PositiveDimension" minOccurs="0"/&gt;
 *         &lt;element name="BatteryAverageLifeStandby" type="{}PositiveDimension" minOccurs="0"/&gt;
 *         &lt;element name="BatteryChargeTime" type="{}PositiveDimension" minOccurs="0"/&gt;
 *         &lt;element name="BatteryTypeLithiumIon" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="BatteryTypeLithiumMetal" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="CountryOfOrigin" type="{}CountryOfOriginType" minOccurs="0"/&gt;
 *         &lt;element name="ItemDisplayArea" type="{}AreaDimension" minOccurs="0"/&gt;
 *         &lt;element name="LithiumBatteryEnergyContent" type="{}PositiveDimension" minOccurs="0"/&gt;
 *         &lt;element name="LithiumBatteryPackaging" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="batteries_contained_in_equipment"/&gt;
 *               &lt;enumeration value="batteries_only"/&gt;
 *               &lt;enumeration value="batteries_packed_with_equipment"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LithiumBatteryVoltage" type="{}PositiveDimension" minOccurs="0"/&gt;
 *         &lt;element name="LithiumBatteryWeight" type="{}PositiveDimension" minOccurs="0"/&gt;
 *         &lt;element name="MfrWarrantyDescriptionLabor" type="{}SuperLongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MfrWarrantyDescriptionParts" type="{}SuperLongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MfrWarrantyDescriptionType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfLithiumIonCells" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfLithiumMetalCells" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="Warnings" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="FabricType" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ImportDesignation" type="{}String" minOccurs="0"/&gt;
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
    "hiCommon",
    "battery",
    "batteryAverageLife",
    "batteryAverageLifeStandby",
    "batteryChargeTime",
    "batteryTypeLithiumIon",
    "batteryTypeLithiumMetal",
    "countryOfOrigin",
    "itemDisplayArea",
    "lithiumBatteryEnergyContent",
    "lithiumBatteryPackaging",
    "lithiumBatteryVoltage",
    "lithiumBatteryWeight",
    "mfrWarrantyDescriptionLabor",
    "mfrWarrantyDescriptionParts",
    "mfrWarrantyDescriptionType",
    "numberOfLithiumIonCells",
    "numberOfLithiumMetalCells",
    "warnings",
    "fabricType",
    "importDesignation"
})
@XmlRootElement(name = "HomeImprovement")
public class HomeImprovement {

    @XmlElement(name = "ProductType", required = true)
    protected HomeImprovement.ProductType productType;
    @XmlElement(name = "HICommon")
    protected HICommon hiCommon;
    @XmlElement(name = "Battery")
    protected Battery battery;
    @XmlElement(name = "BatteryAverageLife")
    protected BigDecimal batteryAverageLife;
    @XmlElement(name = "BatteryAverageLifeStandby")
    protected BigDecimal batteryAverageLifeStandby;
    @XmlElement(name = "BatteryChargeTime")
    protected BigDecimal batteryChargeTime;
    @XmlElement(name = "BatteryTypeLithiumIon")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger batteryTypeLithiumIon;
    @XmlElement(name = "BatteryTypeLithiumMetal")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger batteryTypeLithiumMetal;
    @XmlElement(name = "CountryOfOrigin")
    protected String countryOfOrigin;
    @XmlElement(name = "ItemDisplayArea")
    protected AreaDimension itemDisplayArea;
    @XmlElement(name = "LithiumBatteryEnergyContent")
    protected BigDecimal lithiumBatteryEnergyContent;
    @XmlElement(name = "LithiumBatteryPackaging")
    protected String lithiumBatteryPackaging;
    @XmlElement(name = "LithiumBatteryVoltage")
    protected BigDecimal lithiumBatteryVoltage;
    @XmlElement(name = "LithiumBatteryWeight")
    protected BigDecimal lithiumBatteryWeight;
    @XmlElement(name = "MfrWarrantyDescriptionLabor")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String mfrWarrantyDescriptionLabor;
    @XmlElement(name = "MfrWarrantyDescriptionParts")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String mfrWarrantyDescriptionParts;
    @XmlElement(name = "MfrWarrantyDescriptionType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String mfrWarrantyDescriptionType;
    @XmlElement(name = "NumberOfLithiumIonCells")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfLithiumIonCells;
    @XmlElement(name = "NumberOfLithiumMetalCells")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfLithiumMetalCells;
    @XmlElement(name = "Warnings")
    protected Object warnings;
    @XmlElement(name = "FabricType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fabricType;
    @XmlElement(name = "ImportDesignation")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String importDesignation;

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link HomeImprovement.ProductType }
     *     
     */
    public HomeImprovement.ProductType getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link HomeImprovement.ProductType }
     *     
     */
    public void setProductType(HomeImprovement.ProductType value) {
        this.productType = value;
    }

    /**
     * Gets the value of the hiCommon property.
     * 
     * @return
     *     possible object is
     *     {@link HICommon }
     *     
     */
    public HICommon getHICommon() {
        return hiCommon;
    }

    /**
     * Sets the value of the hiCommon property.
     * 
     * @param value
     *     allowed object is
     *     {@link HICommon }
     *     
     */
    public void setHICommon(HICommon value) {
        this.hiCommon = value;
    }

    /**
     * Gets the value of the battery property.
     * 
     * @return
     *     possible object is
     *     {@link Battery }
     *     
     */
    public Battery getBattery() {
        return battery;
    }

    /**
     * Sets the value of the battery property.
     * 
     * @param value
     *     allowed object is
     *     {@link Battery }
     *     
     */
    public void setBattery(Battery value) {
        this.battery = value;
    }

    /**
     * Gets the value of the batteryAverageLife property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBatteryAverageLife() {
        return batteryAverageLife;
    }

    /**
     * Sets the value of the batteryAverageLife property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBatteryAverageLife(BigDecimal value) {
        this.batteryAverageLife = value;
    }

    /**
     * Gets the value of the batteryAverageLifeStandby property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBatteryAverageLifeStandby() {
        return batteryAverageLifeStandby;
    }

    /**
     * Sets the value of the batteryAverageLifeStandby property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBatteryAverageLifeStandby(BigDecimal value) {
        this.batteryAverageLifeStandby = value;
    }

    /**
     * Gets the value of the batteryChargeTime property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBatteryChargeTime() {
        return batteryChargeTime;
    }

    /**
     * Sets the value of the batteryChargeTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBatteryChargeTime(BigDecimal value) {
        this.batteryChargeTime = value;
    }

    /**
     * Gets the value of the batteryTypeLithiumIon property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBatteryTypeLithiumIon() {
        return batteryTypeLithiumIon;
    }

    /**
     * Sets the value of the batteryTypeLithiumIon property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBatteryTypeLithiumIon(BigInteger value) {
        this.batteryTypeLithiumIon = value;
    }

    /**
     * Gets the value of the batteryTypeLithiumMetal property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBatteryTypeLithiumMetal() {
        return batteryTypeLithiumMetal;
    }

    /**
     * Sets the value of the batteryTypeLithiumMetal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBatteryTypeLithiumMetal(BigInteger value) {
        this.batteryTypeLithiumMetal = value;
    }

    /**
     * Gets the value of the countryOfOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Sets the value of the countryOfOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfOrigin(String value) {
        this.countryOfOrigin = value;
    }

    /**
     * Gets the value of the itemDisplayArea property.
     * 
     * @return
     *     possible object is
     *     {@link AreaDimension }
     *     
     */
    public AreaDimension getItemDisplayArea() {
        return itemDisplayArea;
    }

    /**
     * Sets the value of the itemDisplayArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaDimension }
     *     
     */
    public void setItemDisplayArea(AreaDimension value) {
        this.itemDisplayArea = value;
    }

    /**
     * Gets the value of the lithiumBatteryEnergyContent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLithiumBatteryEnergyContent() {
        return lithiumBatteryEnergyContent;
    }

    /**
     * Sets the value of the lithiumBatteryEnergyContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLithiumBatteryEnergyContent(BigDecimal value) {
        this.lithiumBatteryEnergyContent = value;
    }

    /**
     * Gets the value of the lithiumBatteryPackaging property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLithiumBatteryPackaging() {
        return lithiumBatteryPackaging;
    }

    /**
     * Sets the value of the lithiumBatteryPackaging property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLithiumBatteryPackaging(String value) {
        this.lithiumBatteryPackaging = value;
    }

    /**
     * Gets the value of the lithiumBatteryVoltage property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLithiumBatteryVoltage() {
        return lithiumBatteryVoltage;
    }

    /**
     * Sets the value of the lithiumBatteryVoltage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLithiumBatteryVoltage(BigDecimal value) {
        this.lithiumBatteryVoltage = value;
    }

    /**
     * Gets the value of the lithiumBatteryWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLithiumBatteryWeight() {
        return lithiumBatteryWeight;
    }

    /**
     * Sets the value of the lithiumBatteryWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLithiumBatteryWeight(BigDecimal value) {
        this.lithiumBatteryWeight = value;
    }

    /**
     * Gets the value of the mfrWarrantyDescriptionLabor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMfrWarrantyDescriptionLabor() {
        return mfrWarrantyDescriptionLabor;
    }

    /**
     * Sets the value of the mfrWarrantyDescriptionLabor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMfrWarrantyDescriptionLabor(String value) {
        this.mfrWarrantyDescriptionLabor = value;
    }

    /**
     * Gets the value of the mfrWarrantyDescriptionParts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMfrWarrantyDescriptionParts() {
        return mfrWarrantyDescriptionParts;
    }

    /**
     * Sets the value of the mfrWarrantyDescriptionParts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMfrWarrantyDescriptionParts(String value) {
        this.mfrWarrantyDescriptionParts = value;
    }

    /**
     * Gets the value of the mfrWarrantyDescriptionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMfrWarrantyDescriptionType() {
        return mfrWarrantyDescriptionType;
    }

    /**
     * Sets the value of the mfrWarrantyDescriptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMfrWarrantyDescriptionType(String value) {
        this.mfrWarrantyDescriptionType = value;
    }

    /**
     * Gets the value of the numberOfLithiumIonCells property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfLithiumIonCells() {
        return numberOfLithiumIonCells;
    }

    /**
     * Sets the value of the numberOfLithiumIonCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfLithiumIonCells(BigInteger value) {
        this.numberOfLithiumIonCells = value;
    }

    /**
     * Gets the value of the numberOfLithiumMetalCells property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfLithiumMetalCells() {
        return numberOfLithiumMetalCells;
    }

    /**
     * Sets the value of the numberOfLithiumMetalCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfLithiumMetalCells(BigInteger value) {
        this.numberOfLithiumMetalCells = value;
    }

    /**
     * Gets the value of the warnings property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getWarnings() {
        return warnings;
    }

    /**
     * Sets the value of the warnings property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setWarnings(Object value) {
        this.warnings = value;
    }

    /**
     * Gets the value of the fabricType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFabricType() {
        return fabricType;
    }

    /**
     * Sets the value of the fabricType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFabricType(String value) {
        this.fabricType = value;
    }

    /**
     * Gets the value of the importDesignation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImportDesignation() {
        return importDesignation;
    }

    /**
     * Sets the value of the importDesignation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportDesignation(String value) {
        this.importDesignation = value;
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
     *       &lt;choice&gt;
     *         &lt;element ref="{}BuildingMaterials"/&gt;
     *         &lt;element ref="{}Hardware"/&gt;
     *         &lt;element ref="{}Electrical"/&gt;
     *         &lt;element ref="{}PlumbingFixtures"/&gt;
     *         &lt;element name="Tools" type="{}HomeImprovementTools"/&gt;
     *         &lt;element ref="{}OrganizersAndStorage"/&gt;
     *         &lt;element ref="{}MajorHomeAppliances"/&gt;
     *         &lt;element ref="{}SecurityElectronics"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "buildingMaterials",
        "hardware",
        "electrical",
        "plumbingFixtures",
        "tools",
        "organizersAndStorage",
        "majorHomeAppliances",
        "securityElectronics"
    })
    public static class ProductType {

        @XmlElement(name = "BuildingMaterials")
        protected BuildingMaterials buildingMaterials;
        @XmlElement(name = "Hardware")
        protected Hardware hardware;
        @XmlElement(name = "Electrical")
        protected Electrical electrical;
        @XmlElement(name = "PlumbingFixtures")
        protected PlumbingFixtures plumbingFixtures;
        @XmlElement(name = "Tools")
        protected HomeImprovementTools tools;
        @XmlElement(name = "OrganizersAndStorage")
        protected OrganizersAndStorage organizersAndStorage;
        @XmlElement(name = "MajorHomeAppliances")
        protected MajorHomeAppliances majorHomeAppliances;
        @XmlElement(name = "SecurityElectronics")
        protected SecurityElectronics securityElectronics;

        /**
         * Gets the value of the buildingMaterials property.
         * 
         * @return
         *     possible object is
         *     {@link BuildingMaterials }
         *     
         */
        public BuildingMaterials getBuildingMaterials() {
            return buildingMaterials;
        }

        /**
         * Sets the value of the buildingMaterials property.
         * 
         * @param value
         *     allowed object is
         *     {@link BuildingMaterials }
         *     
         */
        public void setBuildingMaterials(BuildingMaterials value) {
            this.buildingMaterials = value;
        }

        /**
         * Gets the value of the hardware property.
         * 
         * @return
         *     possible object is
         *     {@link Hardware }
         *     
         */
        public Hardware getHardware() {
            return hardware;
        }

        /**
         * Sets the value of the hardware property.
         * 
         * @param value
         *     allowed object is
         *     {@link Hardware }
         *     
         */
        public void setHardware(Hardware value) {
            this.hardware = value;
        }

        /**
         * Gets the value of the electrical property.
         * 
         * @return
         *     possible object is
         *     {@link Electrical }
         *     
         */
        public Electrical getElectrical() {
            return electrical;
        }

        /**
         * Sets the value of the electrical property.
         * 
         * @param value
         *     allowed object is
         *     {@link Electrical }
         *     
         */
        public void setElectrical(Electrical value) {
            this.electrical = value;
        }

        /**
         * Gets the value of the plumbingFixtures property.
         * 
         * @return
         *     possible object is
         *     {@link PlumbingFixtures }
         *     
         */
        public PlumbingFixtures getPlumbingFixtures() {
            return plumbingFixtures;
        }

        /**
         * Sets the value of the plumbingFixtures property.
         * 
         * @param value
         *     allowed object is
         *     {@link PlumbingFixtures }
         *     
         */
        public void setPlumbingFixtures(PlumbingFixtures value) {
            this.plumbingFixtures = value;
        }

        /**
         * Gets the value of the tools property.
         * 
         * @return
         *     possible object is
         *     {@link HomeImprovementTools }
         *     
         */
        public HomeImprovementTools getTools() {
            return tools;
        }

        /**
         * Sets the value of the tools property.
         * 
         * @param value
         *     allowed object is
         *     {@link HomeImprovementTools }
         *     
         */
        public void setTools(HomeImprovementTools value) {
            this.tools = value;
        }

        /**
         * Gets the value of the organizersAndStorage property.
         * 
         * @return
         *     possible object is
         *     {@link OrganizersAndStorage }
         *     
         */
        public OrganizersAndStorage getOrganizersAndStorage() {
            return organizersAndStorage;
        }

        /**
         * Sets the value of the organizersAndStorage property.
         * 
         * @param value
         *     allowed object is
         *     {@link OrganizersAndStorage }
         *     
         */
        public void setOrganizersAndStorage(OrganizersAndStorage value) {
            this.organizersAndStorage = value;
        }

        /**
         * Gets the value of the majorHomeAppliances property.
         * 
         * @return
         *     possible object is
         *     {@link MajorHomeAppliances }
         *     
         */
        public MajorHomeAppliances getMajorHomeAppliances() {
            return majorHomeAppliances;
        }

        /**
         * Sets the value of the majorHomeAppliances property.
         * 
         * @param value
         *     allowed object is
         *     {@link MajorHomeAppliances }
         *     
         */
        public void setMajorHomeAppliances(MajorHomeAppliances value) {
            this.majorHomeAppliances = value;
        }

        /**
         * Gets the value of the securityElectronics property.
         * 
         * @return
         *     possible object is
         *     {@link SecurityElectronics }
         *     
         */
        public SecurityElectronics getSecurityElectronics() {
            return securityElectronics;
        }

        /**
         * Sets the value of the securityElectronics property.
         * 
         * @param value
         *     allowed object is
         *     {@link SecurityElectronics }
         *     
         */
        public void setSecurityElectronics(SecurityElectronics value) {
            this.securityElectronics = value;
        }

    }

}