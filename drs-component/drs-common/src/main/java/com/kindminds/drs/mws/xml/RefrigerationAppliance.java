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
 *         &lt;element name="AdditionalProductInformation" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="AnnualEnergyConsumption" type="{}EnergyConsumptionDimension" minOccurs="0"/&gt;
 *         &lt;element name="BottleCount" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="CompatibleDevice" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="ConnectorType" type="{}String" maxOccurs="2" minOccurs="0"/&gt;
 *         &lt;element name="ControlsType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="CoolingVents" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="CounterDepth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DefrostSystemType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="DoorMaterialType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="DoorOrientation" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="Drawers" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="DryerPowerSource" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="EuEnergyLabelEfficiencyClass1992" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="FilterPoreSize" type="{}FilterPoreSizeDimension" minOccurs="0"/&gt;
 *         &lt;element name="FreezerCapacity" type="{}VolumeDimension" minOccurs="0"/&gt;
 *         &lt;element name="FreshFoodCapacity" type="{}VolumeDimension" minOccurs="0"/&gt;
 *         &lt;element name="FreezerLocation" type="{}VolumeDimension" minOccurs="0"/&gt;
 *         &lt;element name="IceCapacity" type="{}IceCapacityDimension" minOccurs="0"/&gt;
 *         &lt;element name="LightingMethod" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="MaximumHorsepower" type="{}MaximumHorsepowerDimension" minOccurs="0"/&gt;
 *         &lt;element name="RecommendedProductUses" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="RefrigerationClimateClassification" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ShelfType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="Shelves" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="StorageVolume" type="{}VolumeDimension" minOccurs="0"/&gt;
 *         &lt;element name="TrayType" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="VegetableCompartmentCapacity" type="{}String" minOccurs="0"/&gt;
 *         &lt;element name="WasherArms" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
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
    "additionalProductInformation",
    "annualEnergyConsumption",
    "bottleCount",
    "compatibleDevice",
    "connectorType",
    "controlsType",
    "coolingVents",
    "counterDepth",
    "defrostSystemType",
    "doorMaterialType",
    "doorOrientation",
    "drawers",
    "dryerPowerSource",
    "euEnergyLabelEfficiencyClass1992",
    "filterPoreSize",
    "freezerCapacity",
    "freshFoodCapacity",
    "freezerLocation",
    "iceCapacity",
    "lightingMethod",
    "maximumHorsepower",
    "recommendedProductUses",
    "refrigerationClimateClassification",
    "shelfType",
    "shelves",
    "storageVolume",
    "trayType",
    "vegetableCompartmentCapacity",
    "washerArms"
})
@XmlRootElement(name = "RefrigerationAppliance")
public class RefrigerationAppliance {

    @XmlElement(name = "AdditionalProductInformation")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String additionalProductInformation;
    @XmlElement(name = "AnnualEnergyConsumption")
    protected EnergyConsumptionDimension annualEnergyConsumption;
    @XmlElement(name = "BottleCount")
    protected BigInteger bottleCount;
    @XmlElement(name = "CompatibleDevice")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String compatibleDevice;
    @XmlElement(name = "ConnectorType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> connectorType;
    @XmlElement(name = "ControlsType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String controlsType;
    @XmlElement(name = "CoolingVents")
    protected BigInteger coolingVents;
    @XmlElement(name = "CounterDepth")
    protected LengthDimension counterDepth;
    @XmlElement(name = "DefrostSystemType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String defrostSystemType;
    @XmlElement(name = "DoorMaterialType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String doorMaterialType;
    @XmlElement(name = "DoorOrientation")
    protected Object doorOrientation;
    @XmlElement(name = "Drawers")
    protected BigInteger drawers;
    @XmlElement(name = "DryerPowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String dryerPowerSource;
    @XmlElement(name = "EuEnergyLabelEfficiencyClass1992")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String euEnergyLabelEfficiencyClass1992;
    @XmlElement(name = "FilterPoreSize")
    protected FilterPoreSizeDimension filterPoreSize;
    @XmlElement(name = "FreezerCapacity")
    protected VolumeDimension freezerCapacity;
    @XmlElement(name = "FreshFoodCapacity")
    protected VolumeDimension freshFoodCapacity;
    @XmlElement(name = "FreezerLocation")
    protected VolumeDimension freezerLocation;
    @XmlElement(name = "IceCapacity")
    protected IceCapacityDimension iceCapacity;
    @XmlElement(name = "LightingMethod")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String lightingMethod;
    @XmlElement(name = "MaximumHorsepower")
    protected MaximumHorsepowerDimension maximumHorsepower;
    @XmlElement(name = "RecommendedProductUses")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String recommendedProductUses;
    @XmlElement(name = "RefrigerationClimateClassification")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String refrigerationClimateClassification;
    @XmlElement(name = "ShelfType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String shelfType;
    @XmlElement(name = "Shelves")
    protected BigInteger shelves;
    @XmlElement(name = "StorageVolume")
    protected VolumeDimension storageVolume;
    @XmlElement(name = "TrayType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String trayType;
    @XmlElement(name = "VegetableCompartmentCapacity")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String vegetableCompartmentCapacity;
    @XmlElement(name = "WasherArms")
    protected BigInteger washerArms;

    /**
     * Gets the value of the additionalProductInformation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalProductInformation() {
        return additionalProductInformation;
    }

    /**
     * Sets the value of the additionalProductInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalProductInformation(String value) {
        this.additionalProductInformation = value;
    }

    /**
     * Gets the value of the annualEnergyConsumption property.
     * 
     * @return
     *     possible object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public EnergyConsumptionDimension getAnnualEnergyConsumption() {
        return annualEnergyConsumption;
    }

    /**
     * Sets the value of the annualEnergyConsumption property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnergyConsumptionDimension }
     *     
     */
    public void setAnnualEnergyConsumption(EnergyConsumptionDimension value) {
        this.annualEnergyConsumption = value;
    }

    /**
     * Gets the value of the bottleCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBottleCount() {
        return bottleCount;
    }

    /**
     * Sets the value of the bottleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBottleCount(BigInteger value) {
        this.bottleCount = value;
    }

    /**
     * Gets the value of the compatibleDevice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompatibleDevice() {
        return compatibleDevice;
    }

    /**
     * Sets the value of the compatibleDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompatibleDevice(String value) {
        this.compatibleDevice = value;
    }

    /**
     * Gets the value of the connectorType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectorType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectorType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getConnectorType() {
        if (connectorType == null) {
            connectorType = new ArrayList<String>();
        }
        return this.connectorType;
    }

    /**
     * Gets the value of the controlsType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlsType() {
        return controlsType;
    }

    /**
     * Sets the value of the controlsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlsType(String value) {
        this.controlsType = value;
    }

    /**
     * Gets the value of the coolingVents property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCoolingVents() {
        return coolingVents;
    }

    /**
     * Sets the value of the coolingVents property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCoolingVents(BigInteger value) {
        this.coolingVents = value;
    }

    /**
     * Gets the value of the counterDepth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getCounterDepth() {
        return counterDepth;
    }

    /**
     * Sets the value of the counterDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setCounterDepth(LengthDimension value) {
        this.counterDepth = value;
    }

    /**
     * Gets the value of the defrostSystemType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefrostSystemType() {
        return defrostSystemType;
    }

    /**
     * Sets the value of the defrostSystemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefrostSystemType(String value) {
        this.defrostSystemType = value;
    }

    /**
     * Gets the value of the doorMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoorMaterialType() {
        return doorMaterialType;
    }

    /**
     * Sets the value of the doorMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoorMaterialType(String value) {
        this.doorMaterialType = value;
    }

    /**
     * Gets the value of the doorOrientation property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDoorOrientation() {
        return doorOrientation;
    }

    /**
     * Sets the value of the doorOrientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDoorOrientation(Object value) {
        this.doorOrientation = value;
    }

    /**
     * Gets the value of the drawers property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDrawers() {
        return drawers;
    }

    /**
     * Sets the value of the drawers property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDrawers(BigInteger value) {
        this.drawers = value;
    }

    /**
     * Gets the value of the dryerPowerSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDryerPowerSource() {
        return dryerPowerSource;
    }

    /**
     * Sets the value of the dryerPowerSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDryerPowerSource(String value) {
        this.dryerPowerSource = value;
    }

    /**
     * Gets the value of the euEnergyLabelEfficiencyClass1992 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEuEnergyLabelEfficiencyClass1992() {
        return euEnergyLabelEfficiencyClass1992;
    }

    /**
     * Sets the value of the euEnergyLabelEfficiencyClass1992 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEuEnergyLabelEfficiencyClass1992(String value) {
        this.euEnergyLabelEfficiencyClass1992 = value;
    }

    /**
     * Gets the value of the filterPoreSize property.
     * 
     * @return
     *     possible object is
     *     {@link FilterPoreSizeDimension }
     *     
     */
    public FilterPoreSizeDimension getFilterPoreSize() {
        return filterPoreSize;
    }

    /**
     * Sets the value of the filterPoreSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterPoreSizeDimension }
     *     
     */
    public void setFilterPoreSize(FilterPoreSizeDimension value) {
        this.filterPoreSize = value;
    }

    /**
     * Gets the value of the freezerCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link VolumeDimension }
     *     
     */
    public VolumeDimension getFreezerCapacity() {
        return freezerCapacity;
    }

    /**
     * Sets the value of the freezerCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link VolumeDimension }
     *     
     */
    public void setFreezerCapacity(VolumeDimension value) {
        this.freezerCapacity = value;
    }

    /**
     * Gets the value of the freshFoodCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link VolumeDimension }
     *     
     */
    public VolumeDimension getFreshFoodCapacity() {
        return freshFoodCapacity;
    }

    /**
     * Sets the value of the freshFoodCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link VolumeDimension }
     *     
     */
    public void setFreshFoodCapacity(VolumeDimension value) {
        this.freshFoodCapacity = value;
    }

    /**
     * Gets the value of the freezerLocation property.
     * 
     * @return
     *     possible object is
     *     {@link VolumeDimension }
     *     
     */
    public VolumeDimension getFreezerLocation() {
        return freezerLocation;
    }

    /**
     * Sets the value of the freezerLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link VolumeDimension }
     *     
     */
    public void setFreezerLocation(VolumeDimension value) {
        this.freezerLocation = value;
    }

    /**
     * Gets the value of the iceCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link IceCapacityDimension }
     *     
     */
    public IceCapacityDimension getIceCapacity() {
        return iceCapacity;
    }

    /**
     * Sets the value of the iceCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link IceCapacityDimension }
     *     
     */
    public void setIceCapacity(IceCapacityDimension value) {
        this.iceCapacity = value;
    }

    /**
     * Gets the value of the lightingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLightingMethod() {
        return lightingMethod;
    }

    /**
     * Sets the value of the lightingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLightingMethod(String value) {
        this.lightingMethod = value;
    }

    /**
     * Gets the value of the maximumHorsepower property.
     * 
     * @return
     *     possible object is
     *     {@link MaximumHorsepowerDimension }
     *     
     */
    public MaximumHorsepowerDimension getMaximumHorsepower() {
        return maximumHorsepower;
    }

    /**
     * Sets the value of the maximumHorsepower property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaximumHorsepowerDimension }
     *     
     */
    public void setMaximumHorsepower(MaximumHorsepowerDimension value) {
        this.maximumHorsepower = value;
    }

    /**
     * Gets the value of the recommendedProductUses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecommendedProductUses() {
        return recommendedProductUses;
    }

    /**
     * Sets the value of the recommendedProductUses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecommendedProductUses(String value) {
        this.recommendedProductUses = value;
    }

    /**
     * Gets the value of the refrigerationClimateClassification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefrigerationClimateClassification() {
        return refrigerationClimateClassification;
    }

    /**
     * Sets the value of the refrigerationClimateClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefrigerationClimateClassification(String value) {
        this.refrigerationClimateClassification = value;
    }

    /**
     * Gets the value of the shelfType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShelfType() {
        return shelfType;
    }

    /**
     * Sets the value of the shelfType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShelfType(String value) {
        this.shelfType = value;
    }

    /**
     * Gets the value of the shelves property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getShelves() {
        return shelves;
    }

    /**
     * Sets the value of the shelves property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setShelves(BigInteger value) {
        this.shelves = value;
    }

    /**
     * Gets the value of the storageVolume property.
     * 
     * @return
     *     possible object is
     *     {@link VolumeDimension }
     *     
     */
    public VolumeDimension getStorageVolume() {
        return storageVolume;
    }

    /**
     * Sets the value of the storageVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link VolumeDimension }
     *     
     */
    public void setStorageVolume(VolumeDimension value) {
        this.storageVolume = value;
    }

    /**
     * Gets the value of the trayType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrayType() {
        return trayType;
    }

    /**
     * Sets the value of the trayType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrayType(String value) {
        this.trayType = value;
    }

    /**
     * Gets the value of the vegetableCompartmentCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVegetableCompartmentCapacity() {
        return vegetableCompartmentCapacity;
    }

    /**
     * Sets the value of the vegetableCompartmentCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVegetableCompartmentCapacity(String value) {
        this.vegetableCompartmentCapacity = value;
    }

    /**
     * Gets the value of the washerArms property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWasherArms() {
        return washerArms;
    }

    /**
     * Sets the value of the washerArms property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWasherArms(BigInteger value) {
        this.washerArms = value;
    }

}
