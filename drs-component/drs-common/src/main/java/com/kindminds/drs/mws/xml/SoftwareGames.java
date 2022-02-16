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
 *         &lt;element name="SoftwareVideoGamesGenre" type="{}StringNotNull" maxOccurs="5"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="ESRBRating" type="{}FortyStringNotNull"/&gt;
 *           &lt;element name="BBFCRating" type="{}BBFCRatingType"/&gt;
 *           &lt;element name="PEGIRating" type="{}PEGIRatingType"/&gt;
 *           &lt;element name="USKRating" type="{}USKRatingType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="MediaFormat" type="{}MediumStringNotNull" maxOccurs="5"/&gt;
 *         &lt;element name="OperatingSystem" type="{}MediumStringNotNull" maxOccurs="7"/&gt;
 *         &lt;element name="Bundles" type="{}ThirtyStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ESRBDescriptors" type="{}StringNotNull" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element name="HardwarePlatform" type="{}MediumStringNotNull" maxOccurs="5" minOccurs="0"/&gt;
 *         &lt;element name="PEGIDetails" type="{}PEGIDetailsType" minOccurs="0"/&gt;
 *         &lt;element name="MFGSuggestedAgeMin" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="MFGSuggestedAgeMax" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="MaxNumberOfPlayers" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element ref="{}SoftwarePlatform" maxOccurs="4" minOccurs="0"/&gt;
 *         &lt;element name="ComputerPlatform" type="{}ComputerPlatformValues" minOccurs="0"/&gt;
 *         &lt;element name="OnlinePlay" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="AgeRangeDescription" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ContentRatingValue" type="{}StringNotNull" minOccurs="0"/&gt;
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
    "softwareVideoGamesGenre",
    "esrbRating",
    "bbfcRating",
    "pegiRating",
    "uskRating",
    "mediaFormat",
    "operatingSystem",
    "bundles",
    "esrbDescriptors",
    "hardwarePlatform",
    "pegiDetails",
    "mfgSuggestedAgeMin",
    "mfgSuggestedAgeMax",
    "maxNumberOfPlayers",
    "softwarePlatform",
    "computerPlatform",
    "onlinePlay",
    "ageRangeDescription",
    "contentRatingValue"
})
@XmlRootElement(name = "SoftwareGames")
public class SoftwareGames {

    @XmlElement(name = "SoftwareVideoGamesGenre", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> softwareVideoGamesGenre;
    @XmlElement(name = "ESRBRating")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String esrbRating;
    @XmlElement(name = "BBFCRating")
    @XmlSchemaType(name = "string")
    protected BBFCRatingType bbfcRating;
    @XmlElement(name = "PEGIRating")
    @XmlSchemaType(name = "string")
    protected PEGIRatingType pegiRating;
    @XmlElement(name = "USKRating")
    @XmlSchemaType(name = "string")
    protected USKRatingType uskRating;
    @XmlElement(name = "MediaFormat", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> mediaFormat;
    @XmlElement(name = "OperatingSystem", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> operatingSystem;
    @XmlElement(name = "Bundles")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String bundles;
    @XmlElement(name = "ESRBDescriptors")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> esrbDescriptors;
    @XmlElement(name = "HardwarePlatform")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> hardwarePlatform;
    @XmlElement(name = "PEGIDetails")
    protected PEGIDetailsType pegiDetails;
    @XmlElement(name = "MFGSuggestedAgeMin")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger mfgSuggestedAgeMin;
    @XmlElement(name = "MFGSuggestedAgeMax")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger mfgSuggestedAgeMax;
    @XmlElement(name = "MaxNumberOfPlayers")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger maxNumberOfPlayers;
    @XmlElement(name = "SoftwarePlatform")
    protected List<SoftwarePlatform> softwarePlatform;
    @XmlElement(name = "ComputerPlatform")
    @XmlSchemaType(name = "string")
    protected ComputerPlatformValues computerPlatform;
    @XmlElement(name = "OnlinePlay")
    protected Boolean onlinePlay;
    @XmlElement(name = "AgeRangeDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String ageRangeDescription;
    @XmlElement(name = "ContentRatingValue")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String contentRatingValue;

    /**
     * Gets the value of the softwareVideoGamesGenre property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the softwareVideoGamesGenre property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSoftwareVideoGamesGenre().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSoftwareVideoGamesGenre() {
        if (softwareVideoGamesGenre == null) {
            softwareVideoGamesGenre = new ArrayList<String>();
        }
        return this.softwareVideoGamesGenre;
    }

    /**
     * Gets the value of the esrbRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESRBRating() {
        return esrbRating;
    }

    /**
     * Sets the value of the esrbRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESRBRating(String value) {
        this.esrbRating = value;
    }

    /**
     * Gets the value of the bbfcRating property.
     * 
     * @return
     *     possible object is
     *     {@link BBFCRatingType }
     *     
     */
    public BBFCRatingType getBBFCRating() {
        return bbfcRating;
    }

    /**
     * Sets the value of the bbfcRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link BBFCRatingType }
     *     
     */
    public void setBBFCRating(BBFCRatingType value) {
        this.bbfcRating = value;
    }

    /**
     * Gets the value of the pegiRating property.
     * 
     * @return
     *     possible object is
     *     {@link PEGIRatingType }
     *     
     */
    public PEGIRatingType getPEGIRating() {
        return pegiRating;
    }

    /**
     * Sets the value of the pegiRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link PEGIRatingType }
     *     
     */
    public void setPEGIRating(PEGIRatingType value) {
        this.pegiRating = value;
    }

    /**
     * Gets the value of the uskRating property.
     * 
     * @return
     *     possible object is
     *     {@link USKRatingType }
     *     
     */
    public USKRatingType getUSKRating() {
        return uskRating;
    }

    /**
     * Sets the value of the uskRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link USKRatingType }
     *     
     */
    public void setUSKRating(USKRatingType value) {
        this.uskRating = value;
    }

    /**
     * Gets the value of the mediaFormat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mediaFormat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMediaFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMediaFormat() {
        if (mediaFormat == null) {
            mediaFormat = new ArrayList<String>();
        }
        return this.mediaFormat;
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
     * Gets the value of the bundles property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBundles() {
        return bundles;
    }

    /**
     * Sets the value of the bundles property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBundles(String value) {
        this.bundles = value;
    }

    /**
     * Gets the value of the esrbDescriptors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the esrbDescriptors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getESRBDescriptors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getESRBDescriptors() {
        if (esrbDescriptors == null) {
            esrbDescriptors = new ArrayList<String>();
        }
        return this.esrbDescriptors;
    }

    /**
     * Gets the value of the hardwarePlatform property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hardwarePlatform property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHardwarePlatform().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getHardwarePlatform() {
        if (hardwarePlatform == null) {
            hardwarePlatform = new ArrayList<String>();
        }
        return this.hardwarePlatform;
    }

    /**
     * Gets the value of the pegiDetails property.
     * 
     * @return
     *     possible object is
     *     {@link PEGIDetailsType }
     *     
     */
    public PEGIDetailsType getPEGIDetails() {
        return pegiDetails;
    }

    /**
     * Sets the value of the pegiDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link PEGIDetailsType }
     *     
     */
    public void setPEGIDetails(PEGIDetailsType value) {
        this.pegiDetails = value;
    }

    /**
     * Gets the value of the mfgSuggestedAgeMin property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMFGSuggestedAgeMin() {
        return mfgSuggestedAgeMin;
    }

    /**
     * Sets the value of the mfgSuggestedAgeMin property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMFGSuggestedAgeMin(BigInteger value) {
        this.mfgSuggestedAgeMin = value;
    }

    /**
     * Gets the value of the mfgSuggestedAgeMax property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMFGSuggestedAgeMax() {
        return mfgSuggestedAgeMax;
    }

    /**
     * Sets the value of the mfgSuggestedAgeMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMFGSuggestedAgeMax(BigInteger value) {
        this.mfgSuggestedAgeMax = value;
    }

    /**
     * Gets the value of the maxNumberOfPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    /**
     * Sets the value of the maxNumberOfPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxNumberOfPlayers(BigInteger value) {
        this.maxNumberOfPlayers = value;
    }

    /**
     * Gets the value of the softwarePlatform property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the softwarePlatform property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSoftwarePlatform().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SoftwarePlatform }
     * 
     * 
     */
    public List<SoftwarePlatform> getSoftwarePlatform() {
        if (softwarePlatform == null) {
            softwarePlatform = new ArrayList<SoftwarePlatform>();
        }
        return this.softwarePlatform;
    }

    /**
     * Gets the value of the computerPlatform property.
     * 
     * @return
     *     possible object is
     *     {@link ComputerPlatformValues }
     *     
     */
    public ComputerPlatformValues getComputerPlatform() {
        return computerPlatform;
    }

    /**
     * Sets the value of the computerPlatform property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComputerPlatformValues }
     *     
     */
    public void setComputerPlatform(ComputerPlatformValues value) {
        this.computerPlatform = value;
    }

    /**
     * Gets the value of the onlinePlay property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOnlinePlay() {
        return onlinePlay;
    }

    /**
     * Sets the value of the onlinePlay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOnlinePlay(Boolean value) {
        this.onlinePlay = value;
    }

    /**
     * Gets the value of the ageRangeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgeRangeDescription() {
        return ageRangeDescription;
    }

    /**
     * Sets the value of the ageRangeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgeRangeDescription(String value) {
        this.ageRangeDescription = value;
    }

    /**
     * Gets the value of the contentRatingValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentRatingValue() {
        return contentRatingValue;
    }

    /**
     * Sets the value of the contentRatingValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentRatingValue(String value) {
        this.contentRatingValue = value;
    }

}
