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
 *         &lt;element name="MaterialFabric" type="{}HundredString" maxOccurs="3" minOccurs="0"/&gt;
 *         &lt;element name="ImportDesignation" type="{}HundredString" minOccurs="0"/&gt;
 *         &lt;element name="CountryAsLabeled" type="{}CountryOfOriginType" minOccurs="0"/&gt;
 *         &lt;element name="FurDescription" type="{}HundredString" minOccurs="0"/&gt;
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
    "materialFabric",
    "importDesignation",
    "countryAsLabeled",
    "furDescription"
})
@XmlRootElement(name = "AdditionalProductInformation")
public class AdditionalProductInformation {

    @XmlElement(name = "MaterialFabric")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected List<String> materialFabric;
    @XmlElement(name = "ImportDesignation")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String importDesignation;
    @XmlElement(name = "CountryAsLabeled")
    protected String countryAsLabeled;
    @XmlElement(name = "FurDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String furDescription;

    /**
     * Gets the value of the materialFabric property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the materialFabric property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaterialFabric().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMaterialFabric() {
        if (materialFabric == null) {
            materialFabric = new ArrayList<String>();
        }
        return this.materialFabric;
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
     * Gets the value of the countryAsLabeled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryAsLabeled() {
        return countryAsLabeled;
    }

    /**
     * Sets the value of the countryAsLabeled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryAsLabeled(String value) {
        this.countryAsLabeled = value;
    }

    /**
     * Gets the value of the furDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFurDescription() {
        return furDescription;
    }

    /**
     * Sets the value of the furDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFurDescription(String value) {
        this.furDescription = value;
    }

}