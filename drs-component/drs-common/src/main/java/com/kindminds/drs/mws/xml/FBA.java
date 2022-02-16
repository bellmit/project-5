//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="IsExpirationDatedProduct" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="IsHighValue" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="CanShipInOriginalContainer" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="SerialNumScan" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="HazmatItem" type="{}HazmatItemType" minOccurs="0"/&gt;
 *         &lt;element name="AmazonMaturityRating" type="{}AmazonMaturityRatingType" minOccurs="0"/&gt;
 *         &lt;element name="IdentityPackage" type="{}IdentityPackageType" minOccurs="0"/&gt;
 *         &lt;element name="SerialNumberFormat" type="{}SerialNumberFormatType" minOccurs="0"/&gt;
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
    "isExpirationDatedProduct",
    "isHighValue",
    "canShipInOriginalContainer",
    "serialNumScan",
    "hazmatItem",
    "amazonMaturityRating",
    "identityPackage",
    "serialNumberFormat"
})
@XmlRootElement(name = "FBA")
public class FBA {

    @XmlElement(name = "IsExpirationDatedProduct")
    protected Boolean isExpirationDatedProduct;
    @XmlElement(name = "IsHighValue")
    protected Boolean isHighValue;
    @XmlElement(name = "CanShipInOriginalContainer")
    protected Boolean canShipInOriginalContainer;
    @XmlElement(name = "SerialNumScan")
    protected Boolean serialNumScan;
    @XmlElement(name = "HazmatItem")
    @XmlSchemaType(name = "string")
    protected HazmatItemType hazmatItem;
    @XmlElement(name = "AmazonMaturityRating")
    @XmlSchemaType(name = "string")
    protected AmazonMaturityRatingType amazonMaturityRating;
    @XmlElement(name = "IdentityPackage")
    @XmlSchemaType(name = "string")
    protected IdentityPackageType identityPackage;
    @XmlElement(name = "SerialNumberFormat")
    @XmlSchemaType(name = "string")
    protected SerialNumberFormatType serialNumberFormat;

    /**
     * Gets the value of the isExpirationDatedProduct property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsExpirationDatedProduct() {
        return isExpirationDatedProduct;
    }

    /**
     * Sets the value of the isExpirationDatedProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsExpirationDatedProduct(Boolean value) {
        this.isExpirationDatedProduct = value;
    }

    /**
     * Gets the value of the isHighValue property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsHighValue() {
        return isHighValue;
    }

    /**
     * Sets the value of the isHighValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsHighValue(Boolean value) {
        this.isHighValue = value;
    }

    /**
     * Gets the value of the canShipInOriginalContainer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCanShipInOriginalContainer() {
        return canShipInOriginalContainer;
    }

    /**
     * Sets the value of the canShipInOriginalContainer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCanShipInOriginalContainer(Boolean value) {
        this.canShipInOriginalContainer = value;
    }

    /**
     * Gets the value of the serialNumScan property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSerialNumScan() {
        return serialNumScan;
    }

    /**
     * Sets the value of the serialNumScan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSerialNumScan(Boolean value) {
        this.serialNumScan = value;
    }

    /**
     * Gets the value of the hazmatItem property.
     * 
     * @return
     *     possible object is
     *     {@link HazmatItemType }
     *     
     */
    public HazmatItemType getHazmatItem() {
        return hazmatItem;
    }

    /**
     * Sets the value of the hazmatItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link HazmatItemType }
     *     
     */
    public void setHazmatItem(HazmatItemType value) {
        this.hazmatItem = value;
    }

    /**
     * Gets the value of the amazonMaturityRating property.
     * 
     * @return
     *     possible object is
     *     {@link AmazonMaturityRatingType }
     *     
     */
    public AmazonMaturityRatingType getAmazonMaturityRating() {
        return amazonMaturityRating;
    }

    /**
     * Sets the value of the amazonMaturityRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmazonMaturityRatingType }
     *     
     */
    public void setAmazonMaturityRating(AmazonMaturityRatingType value) {
        this.amazonMaturityRating = value;
    }

    /**
     * Gets the value of the identityPackage property.
     * 
     * @return
     *     possible object is
     *     {@link IdentityPackageType }
     *     
     */
    public IdentityPackageType getIdentityPackage() {
        return identityPackage;
    }

    /**
     * Sets the value of the identityPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentityPackageType }
     *     
     */
    public void setIdentityPackage(IdentityPackageType value) {
        this.identityPackage = value;
    }

    /**
     * Gets the value of the serialNumberFormat property.
     * 
     * @return
     *     possible object is
     *     {@link SerialNumberFormatType }
     *     
     */
    public SerialNumberFormatType getSerialNumberFormat() {
        return serialNumberFormat;
    }

    /**
     * Sets the value of the serialNumberFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerialNumberFormatType }
     *     
     */
    public void setSerialNumberFormat(SerialNumberFormatType value) {
        this.serialNumberFormat = value;
    }

}