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
 *         &lt;element name="HardDriveSize" type="{}MemorySizeDimension" maxOccurs="8"/&gt;
 *         &lt;element name="HardDriveInterface" type="{}HardDriveInterfaceTypeValues" maxOccurs="4"/&gt;
 *         &lt;element name="ComputerMemoryType" type="{}GraphicsRAMTypeValues" maxOccurs="10"/&gt;
 *         &lt;element name="RAMSize" type="{}MemorySizeDimension"/&gt;
 *         &lt;element name="PowerSource" type="{}FortyStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ProcessorBrand" type="{}FortyStringNotNull"/&gt;
 *         &lt;element name="ProcessorSpeed" type="{}FrequencyDimension"/&gt;
 *         &lt;element name="ProcessorType" type="{}ProcessorTypeValues"/&gt;
 *         &lt;element name="ProcessorCount" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/&gt;
 *         &lt;element name="OperatingSystem" type="{}MediumStringNotNull" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element name="HardwarePlatform" type="{}MediumStringNotNull"/&gt;
 *         &lt;element name="WirelessType" type="{}WirelessTypeValues" maxOccurs="3" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalDrives" type="{}FortyStringNotNull" maxOccurs="10" minOccurs="0"/&gt;
 *         &lt;element name="SoftwareIncluded" type="{}LongStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="MemorySlotsAvailable" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ScreenResolution" type="{}FortyStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ScreenSize" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="U-RackSize" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger"&gt;
 *               &lt;minInclusive value="1"/&gt;
 *               &lt;maxInclusive value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref="{}GraphicsCard" maxOccurs="2" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalFeatures" type="{}LongStringNotNull" minOccurs="0"/&gt;
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
    "hardDriveSize",
    "hardDriveInterface",
    "computerMemoryType",
    "ramSize",
    "powerSource",
    "processorBrand",
    "processorSpeed",
    "processorType",
    "processorCount",
    "operatingSystem",
    "hardwarePlatform",
    "wirelessType",
    "additionalDrives",
    "softwareIncluded",
    "memorySlotsAvailable",
    "screenResolution",
    "screenSize",
    "uRackSize",
    "graphicsCard",
    "additionalFeatures"
})
@XmlRootElement(name = "PC")
public class PC {

    @XmlElement(name = "HardDriveSize", required = true)
    protected List<MemorySizeDimension> hardDriveSize;
    @XmlElement(name = "HardDriveInterface", required = true)
    @XmlSchemaType(name = "string")
    protected List<HardDriveInterfaceTypeValues> hardDriveInterface;
    @XmlElement(name = "ComputerMemoryType", required = true)
    @XmlSchemaType(name = "string")
    protected List<GraphicsRAMTypeValues> computerMemoryType;
    @XmlElement(name = "RAMSize", required = true)
    protected MemorySizeDimension ramSize;
    @XmlElement(name = "PowerSource")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String powerSource;
    @XmlElement(name = "ProcessorBrand", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String processorBrand;
    @XmlElement(name = "ProcessorSpeed", required = true)
    protected FrequencyDimension processorSpeed;
    @XmlElement(name = "ProcessorType", required = true)
    @XmlSchemaType(name = "string")
    protected ProcessorTypeValues processorType;
    @XmlElement(name = "ProcessorCount", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger processorCount;
    @XmlElement(name = "OperatingSystem")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> operatingSystem;
    @XmlElement(name = "HardwarePlatform", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String hardwarePlatform;
    @XmlElement(name = "WirelessType")
    @XmlSchemaType(name = "string")
    protected List<WirelessTypeValues> wirelessType;
    @XmlElement(name = "AdditionalDrives")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> additionalDrives;
    @XmlElement(name = "SoftwareIncluded")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String softwareIncluded;
    @XmlElement(name = "MemorySlotsAvailable")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String memorySlotsAvailable;
    @XmlElement(name = "ScreenResolution")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String screenResolution;
    @XmlElement(name = "ScreenSize")
    protected LengthDimension screenSize;
    @XmlElement(name = "U-RackSize")
    protected Integer uRackSize;
    @XmlElement(name = "GraphicsCard")
    protected List<GraphicsCard> graphicsCard;
    @XmlElement(name = "AdditionalFeatures")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String additionalFeatures;

    /**
     * Gets the value of the hardDriveSize property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hardDriveSize property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHardDriveSize().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MemorySizeDimension }
     * 
     * 
     */
    public List<MemorySizeDimension> getHardDriveSize() {
        if (hardDriveSize == null) {
            hardDriveSize = new ArrayList<MemorySizeDimension>();
        }
        return this.hardDriveSize;
    }

    /**
     * Gets the value of the hardDriveInterface property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hardDriveInterface property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHardDriveInterface().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HardDriveInterfaceTypeValues }
     * 
     * 
     */
    public List<HardDriveInterfaceTypeValues> getHardDriveInterface() {
        if (hardDriveInterface == null) {
            hardDriveInterface = new ArrayList<HardDriveInterfaceTypeValues>();
        }
        return this.hardDriveInterface;
    }

    /**
     * Gets the value of the computerMemoryType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the computerMemoryType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComputerMemoryType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GraphicsRAMTypeValues }
     * 
     * 
     */
    public List<GraphicsRAMTypeValues> getComputerMemoryType() {
        if (computerMemoryType == null) {
            computerMemoryType = new ArrayList<GraphicsRAMTypeValues>();
        }
        return this.computerMemoryType;
    }

    /**
     * Gets the value of the ramSize property.
     * 
     * @return
     *     possible object is
     *     {@link MemorySizeDimension }
     *     
     */
    public MemorySizeDimension getRAMSize() {
        return ramSize;
    }

    /**
     * Sets the value of the ramSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemorySizeDimension }
     *     
     */
    public void setRAMSize(MemorySizeDimension value) {
        this.ramSize = value;
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
     * Gets the value of the processorBrand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessorBrand() {
        return processorBrand;
    }

    /**
     * Sets the value of the processorBrand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessorBrand(String value) {
        this.processorBrand = value;
    }

    /**
     * Gets the value of the processorSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link FrequencyDimension }
     *     
     */
    public FrequencyDimension getProcessorSpeed() {
        return processorSpeed;
    }

    /**
     * Sets the value of the processorSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrequencyDimension }
     *     
     */
    public void setProcessorSpeed(FrequencyDimension value) {
        this.processorSpeed = value;
    }

    /**
     * Gets the value of the processorType property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessorTypeValues }
     *     
     */
    public ProcessorTypeValues getProcessorType() {
        return processorType;
    }

    /**
     * Sets the value of the processorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessorTypeValues }
     *     
     */
    public void setProcessorType(ProcessorTypeValues value) {
        this.processorType = value;
    }

    /**
     * Gets the value of the processorCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getProcessorCount() {
        return processorCount;
    }

    /**
     * Sets the value of the processorCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setProcessorCount(BigInteger value) {
        this.processorCount = value;
    }

    /**
     * Gets the value of the operatingSystem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operatingSystem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperatingSystem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOperatingSystem() {
        if (operatingSystem == null) {
            operatingSystem = new ArrayList<String>();
        }
        return this.operatingSystem;
    }

    /**
     * Gets the value of the hardwarePlatform property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHardwarePlatform() {
        return hardwarePlatform;
    }

    /**
     * Sets the value of the hardwarePlatform property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHardwarePlatform(String value) {
        this.hardwarePlatform = value;
    }

    /**
     * Gets the value of the wirelessType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wirelessType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWirelessType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WirelessTypeValues }
     * 
     * 
     */
    public List<WirelessTypeValues> getWirelessType() {
        if (wirelessType == null) {
            wirelessType = new ArrayList<WirelessTypeValues>();
        }
        return this.wirelessType;
    }

    /**
     * Gets the value of the additionalDrives property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalDrives property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalDrives().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAdditionalDrives() {
        if (additionalDrives == null) {
            additionalDrives = new ArrayList<String>();
        }
        return this.additionalDrives;
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
     * Gets the value of the screenResolution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScreenResolution() {
        return screenResolution;
    }

    /**
     * Sets the value of the screenResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScreenResolution(String value) {
        this.screenResolution = value;
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
     * Gets the value of the uRackSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getURackSize() {
        return uRackSize;
    }

    /**
     * Sets the value of the uRackSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setURackSize(Integer value) {
        this.uRackSize = value;
    }

    /**
     * Gets the value of the graphicsCard property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the graphicsCard property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGraphicsCard().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GraphicsCard }
     * 
     * 
     */
    public List<GraphicsCard> getGraphicsCard() {
        if (graphicsCard == null) {
            graphicsCard = new ArrayList<GraphicsCard>();
        }
        return this.graphicsCard;
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

}
