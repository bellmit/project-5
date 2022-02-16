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
 *         &lt;element name="MakeAnOfferMinimumPercentage" type="{}BinaryInteger" minOccurs="0"/&gt;
 *         &lt;element name="DisplayHeight" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DisplayLength" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DisplayWidth" type="{}LengthDimension" minOccurs="0"/&gt;
 *         &lt;element name="DisplayWeight" type="{}WeightDimension" minOccurs="0"/&gt;
 *         &lt;element name="Theme" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalProductInformation" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Originality" type="{}Originality" minOccurs="0"/&gt;
 *         &lt;element name="GradeRating" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="Genre" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="AuthenticatedBy" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="GradedBy" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="ModelYear" type="{}FourDigitYear" minOccurs="0"/&gt;
 *         &lt;element name="EstatePeriod" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="SizeName" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="IsVeryHighValue" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="IsAdultProduct" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="CertificateNumber" type="{}HundredString" minOccurs="0"/&gt;
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
    "makeAnOfferMinimumPercentage",
    "displayHeight",
    "displayLength",
    "displayWidth",
    "displayWeight",
    "theme",
    "additionalProductInformation",
    "originality",
    "gradeRating",
    "genre",
    "authenticatedBy",
    "gradedBy",
    "modelYear",
    "estatePeriod",
    "sizeName",
    "isVeryHighValue",
    "isAdultProduct",
    "certificateNumber"
})
@XmlRootElement(name = "AdvertisementCollectibles")
public class AdvertisementCollectibles {

    @XmlElement(name = "MakeAnOfferMinimumPercentage")
    @XmlSchemaType(name = "integer")
    protected Integer makeAnOfferMinimumPercentage;
    @XmlElement(name = "DisplayHeight")
    protected LengthDimension displayHeight;
    @XmlElement(name = "DisplayLength")
    protected LengthDimension displayLength;
    @XmlElement(name = "DisplayWidth")
    protected LengthDimension displayWidth;
    @XmlElement(name = "DisplayWeight")
    protected WeightDimension displayWeight;
    @XmlElement(name = "Theme")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String theme;
    @XmlElement(name = "AdditionalProductInformation")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String additionalProductInformation;
    @XmlElement(name = "Originality")
    @XmlSchemaType(name = "string")
    protected Originality originality;
    @XmlElement(name = "GradeRating")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String gradeRating;
    @XmlElement(name = "Genre")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String genre;
    @XmlElement(name = "AuthenticatedBy")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String authenticatedBy;
    @XmlElement(name = "GradedBy")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String gradedBy;
    @XmlElement(name = "ModelYear")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger modelYear;
    @XmlElement(name = "EstatePeriod")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String estatePeriod;
    @XmlElement(name = "SizeName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String sizeName;
    @XmlElement(name = "IsVeryHighValue")
    protected Boolean isVeryHighValue;
    @XmlElement(name = "IsAdultProduct")
    protected Boolean isAdultProduct;
    @XmlElement(name = "CertificateNumber")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String certificateNumber;

    /**
     * Gets the value of the makeAnOfferMinimumPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMakeAnOfferMinimumPercentage() {
        return makeAnOfferMinimumPercentage;
    }

    /**
     * Sets the value of the makeAnOfferMinimumPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMakeAnOfferMinimumPercentage(Integer value) {
        this.makeAnOfferMinimumPercentage = value;
    }

    /**
     * Gets the value of the displayHeight property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getDisplayHeight() {
        return displayHeight;
    }

    /**
     * Sets the value of the displayHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setDisplayHeight(LengthDimension value) {
        this.displayHeight = value;
    }

    /**
     * Gets the value of the displayLength property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getDisplayLength() {
        return displayLength;
    }

    /**
     * Sets the value of the displayLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setDisplayLength(LengthDimension value) {
        this.displayLength = value;
    }

    /**
     * Gets the value of the displayWidth property.
     * 
     * @return
     *     possible object is
     *     {@link LengthDimension }
     *     
     */
    public LengthDimension getDisplayWidth() {
        return displayWidth;
    }

    /**
     * Sets the value of the displayWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthDimension }
     *     
     */
    public void setDisplayWidth(LengthDimension value) {
        this.displayWidth = value;
    }

    /**
     * Gets the value of the displayWeight property.
     * 
     * @return
     *     possible object is
     *     {@link WeightDimension }
     *     
     */
    public WeightDimension getDisplayWeight() {
        return displayWeight;
    }

    /**
     * Sets the value of the displayWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link WeightDimension }
     *     
     */
    public void setDisplayWeight(WeightDimension value) {
        this.displayWeight = value;
    }

    /**
     * Gets the value of the theme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Sets the value of the theme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTheme(String value) {
        this.theme = value;
    }

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
     * Gets the value of the originality property.
     * 
     * @return
     *     possible object is
     *     {@link Originality }
     *     
     */
    public Originality getOriginality() {
        return originality;
    }

    /**
     * Sets the value of the originality property.
     * 
     * @param value
     *     allowed object is
     *     {@link Originality }
     *     
     */
    public void setOriginality(Originality value) {
        this.originality = value;
    }

    /**
     * Gets the value of the gradeRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGradeRating() {
        return gradeRating;
    }

    /**
     * Sets the value of the gradeRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGradeRating(String value) {
        this.gradeRating = value;
    }

    /**
     * Gets the value of the genre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the value of the genre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenre(String value) {
        this.genre = value;
    }

    /**
     * Gets the value of the authenticatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticatedBy() {
        return authenticatedBy;
    }

    /**
     * Sets the value of the authenticatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticatedBy(String value) {
        this.authenticatedBy = value;
    }

    /**
     * Gets the value of the gradedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGradedBy() {
        return gradedBy;
    }

    /**
     * Sets the value of the gradedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGradedBy(String value) {
        this.gradedBy = value;
    }

    /**
     * Gets the value of the modelYear property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getModelYear() {
        return modelYear;
    }

    /**
     * Sets the value of the modelYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setModelYear(BigInteger value) {
        this.modelYear = value;
    }

    /**
     * Gets the value of the estatePeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstatePeriod() {
        return estatePeriod;
    }

    /**
     * Sets the value of the estatePeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstatePeriod(String value) {
        this.estatePeriod = value;
    }

    /**
     * Gets the value of the sizeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSizeName() {
        return sizeName;
    }

    /**
     * Sets the value of the sizeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSizeName(String value) {
        this.sizeName = value;
    }

    /**
     * Gets the value of the isVeryHighValue property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVeryHighValue() {
        return isVeryHighValue;
    }

    /**
     * Sets the value of the isVeryHighValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVeryHighValue(Boolean value) {
        this.isVeryHighValue = value;
    }

    /**
     * Gets the value of the isAdultProduct property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAdultProduct() {
        return isAdultProduct;
    }

    /**
     * Sets the value of the isAdultProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAdultProduct(Boolean value) {
        this.isAdultProduct = value;
    }

    /**
     * Gets the value of the certificateNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * Sets the value of the certificateNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateNumber(String value) {
        this.certificateNumber = value;
    }

}
