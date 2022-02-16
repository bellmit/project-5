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
 *         &lt;element name="PaperType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="black-and-white"/&gt;
 *               &lt;enumeration value="color-negative"/&gt;
 *               &lt;enumeration value="color-reversal"/&gt;
 *               &lt;enumeration value="ra-chemistry"/&gt;
 *               &lt;enumeration value="other"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PaperBase" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="polyester-based"/&gt;
 *               &lt;enumeration value="fiber-based"/&gt;
 *               &lt;enumeration value="resin-coated"/&gt;
 *               &lt;enumeration value="other"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PaperSurface" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="glossy"/&gt;
 *               &lt;enumeration value="semi-glossy"/&gt;
 *               &lt;enumeration value="matt"/&gt;
 *               &lt;enumeration value="semi-matt"/&gt;
 *               &lt;enumeration value="pearl"/&gt;
 *               &lt;enumeration value="luster"/&gt;
 *               &lt;enumeration value="satin"/&gt;
 *               &lt;enumeration value="other"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PaperGrade" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="grade-0"/&gt;
 *               &lt;enumeration value="grade-1"/&gt;
 *               &lt;enumeration value="grade-2"/&gt;
 *               &lt;enumeration value="grade-3"/&gt;
 *               &lt;enumeration value="grade-4"/&gt;
 *               &lt;enumeration value="multigrade"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PaperSize" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="10x10"/&gt;
 *               &lt;enumeration value="10x12"/&gt;
 *               &lt;enumeration value="10x20"/&gt;
 *               &lt;enumeration value="11x14"/&gt;
 *               &lt;enumeration value="12x17"/&gt;
 *               &lt;enumeration value="16x20"/&gt;
 *               &lt;enumeration value="20x24"/&gt;
 *               &lt;enumeration value="20x30"/&gt;
 *               &lt;enumeration value="24x30"/&gt;
 *               &lt;enumeration value="3.5x5"/&gt;
 *               &lt;enumeration value="30x40"/&gt;
 *               &lt;enumeration value="4x5"/&gt;
 *               &lt;enumeration value="4x6"/&gt;
 *               &lt;enumeration value="5x7"/&gt;
 *               &lt;enumeration value="8.5x11"/&gt;
 *               &lt;enumeration value="8x10"/&gt;
 *               &lt;enumeration value="roll"/&gt;
 *               &lt;enumeration value="other"/&gt;
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
    "paperType",
    "paperBase",
    "paperSurface",
    "paperGrade",
    "paperSize"
})
@XmlRootElement(name = "PhotoPaper")
public class PhotoPaper {

    @XmlElement(name = "PaperType")
    protected String paperType;
    @XmlElement(name = "PaperBase")
    protected String paperBase;
    @XmlElement(name = "PaperSurface")
    protected String paperSurface;
    @XmlElement(name = "PaperGrade")
    protected String paperGrade;
    @XmlElement(name = "PaperSize")
    protected String paperSize;

    /**
     * Gets the value of the paperType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperType() {
        return paperType;
    }

    /**
     * Sets the value of the paperType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperType(String value) {
        this.paperType = value;
    }

    /**
     * Gets the value of the paperBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperBase() {
        return paperBase;
    }

    /**
     * Sets the value of the paperBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperBase(String value) {
        this.paperBase = value;
    }

    /**
     * Gets the value of the paperSurface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperSurface() {
        return paperSurface;
    }

    /**
     * Sets the value of the paperSurface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperSurface(String value) {
        this.paperSurface = value;
    }

    /**
     * Gets the value of the paperGrade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperGrade() {
        return paperGrade;
    }

    /**
     * Sets the value of the paperGrade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperGrade(String value) {
        this.paperGrade = value;
    }

    /**
     * Gets the value of the paperSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaperSize() {
        return paperSize;
    }

    /**
     * Sets the value of the paperSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaperSize(String value) {
        this.paperSize = value;
    }

}
