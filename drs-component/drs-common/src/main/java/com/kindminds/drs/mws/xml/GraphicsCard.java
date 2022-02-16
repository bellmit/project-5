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
 *         &lt;element name="GraphicsCardDescription" type="{}MediumStringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="GraphicsCoprocessor" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="GraphicsProcessorManufacturer" type="{}StringNotNull" minOccurs="0"/&gt;
 *         &lt;element name="GraphicsCardRamSize" type="{}MemorySizeDimension" minOccurs="0"/&gt;
 *         &lt;element name="GraphicsCardInterface" type="{}TwentyStringNotNull" minOccurs="0"/&gt;
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
    "graphicsCardDescription",
    "graphicsCoprocessor",
    "graphicsProcessorManufacturer",
    "graphicsCardRamSize",
    "graphicsCardInterface"
})
@XmlRootElement(name = "GraphicsCard")
public class GraphicsCard {

    @XmlElement(name = "GraphicsCardDescription")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String graphicsCardDescription;
    @XmlElement(name = "GraphicsCoprocessor")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String graphicsCoprocessor;
    @XmlElement(name = "GraphicsProcessorManufacturer")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String graphicsProcessorManufacturer;
    @XmlElement(name = "GraphicsCardRamSize")
    protected MemorySizeDimension graphicsCardRamSize;
    @XmlElement(name = "GraphicsCardInterface")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String graphicsCardInterface;

    /**
     * Gets the value of the graphicsCardDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphicsCardDescription() {
        return graphicsCardDescription;
    }

    /**
     * Sets the value of the graphicsCardDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphicsCardDescription(String value) {
        this.graphicsCardDescription = value;
    }

    /**
     * Gets the value of the graphicsCoprocessor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphicsCoprocessor() {
        return graphicsCoprocessor;
    }

    /**
     * Sets the value of the graphicsCoprocessor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphicsCoprocessor(String value) {
        this.graphicsCoprocessor = value;
    }

    /**
     * Gets the value of the graphicsProcessorManufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphicsProcessorManufacturer() {
        return graphicsProcessorManufacturer;
    }

    /**
     * Sets the value of the graphicsProcessorManufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphicsProcessorManufacturer(String value) {
        this.graphicsProcessorManufacturer = value;
    }

    /**
     * Gets the value of the graphicsCardRamSize property.
     * 
     * @return
     *     possible object is
     *     {@link MemorySizeDimension }
     *     
     */
    public MemorySizeDimension getGraphicsCardRamSize() {
        return graphicsCardRamSize;
    }

    /**
     * Sets the value of the graphicsCardRamSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemorySizeDimension }
     *     
     */
    public void setGraphicsCardRamSize(MemorySizeDimension value) {
        this.graphicsCardRamSize = value;
    }

    /**
     * Gets the value of the graphicsCardInterface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphicsCardInterface() {
        return graphicsCardInterface;
    }

    /**
     * Sets the value of the graphicsCardInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphicsCardInterface(String value) {
        this.graphicsCardInterface = value;
    }

}