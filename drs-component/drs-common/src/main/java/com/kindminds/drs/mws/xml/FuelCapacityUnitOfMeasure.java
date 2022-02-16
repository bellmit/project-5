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
 * <p>Java class for FuelCapacityUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FuelCapacityUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="cubic_centimeters"/&gt;
 *     &lt;enumeration value="liter"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FuelCapacityUnitOfMeasure")
@XmlEnum
public enum FuelCapacityUnitOfMeasure {

    @XmlEnumValue("cubic_centimeters")
    CUBIC___CENTIMETERS("cubic_centimeters"),
    @XmlEnumValue("liter")
    LITER("liter");
    private final String value;

    FuelCapacityUnitOfMeasure(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FuelCapacityUnitOfMeasure fromValue(String v) {
        for (FuelCapacityUnitOfMeasure c: FuelCapacityUnitOfMeasure.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
