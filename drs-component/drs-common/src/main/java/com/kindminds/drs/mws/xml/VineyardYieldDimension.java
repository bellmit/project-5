//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for VineyardYieldDimension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VineyardYieldDimension"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;&gt;Dimension"&gt;
 *       &lt;attribute name="unitOfMeasure" use="required" type="{}VineyardYieldUnitOfMeasure" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VineyardYieldDimension", propOrder = {
    "value"
})
public class VineyardYieldDimension {

    @XmlValue
    protected BigDecimal value;
    @XmlAttribute(name = "unitOfMeasure", required = true)
    protected VineyardYieldUnitOfMeasure unitOfMeasure;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link VineyardYieldUnitOfMeasure }
     *     
     */
    public VineyardYieldUnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link VineyardYieldUnitOfMeasure }
     *     
     */
    public void setUnitOfMeasure(VineyardYieldUnitOfMeasure value) {
        this.unitOfMeasure = value;
    }

}
