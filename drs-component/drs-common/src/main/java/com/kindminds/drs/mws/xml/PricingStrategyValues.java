//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PricingStrategyValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PricingStrategyValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="bulk_by_uom"/&gt;
 *     &lt;enumeration value="catch_by_uom"/&gt;
 *     &lt;enumeration value="produce_by_uom"/&gt;
 *     &lt;enumeration value="produce_by_each"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PricingStrategyValues")
@XmlEnum
public enum PricingStrategyValues {

    @XmlEnumValue("bulk_by_uom")
    BULK___BY___UOM("bulk_by_uom"),
    @XmlEnumValue("catch_by_uom")
    CATCH___BY___UOM("catch_by_uom"),
    @XmlEnumValue("produce_by_uom")
    PRODUCE___BY___UOM("produce_by_uom"),
    @XmlEnumValue("produce_by_each")
    PRODUCE___BY___EACH("produce_by_each");
    private final String value;

    PricingStrategyValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PricingStrategyValues fromValue(String v) {
        for (PricingStrategyValues c: PricingStrategyValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
