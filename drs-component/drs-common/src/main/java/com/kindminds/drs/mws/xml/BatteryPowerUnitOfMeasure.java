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
 * <p>Java class for BatteryPowerUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BatteryPowerUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="milliamp_hours"/&gt;
 *     &lt;enumeration value="amp_hours"/&gt;
 *     &lt;enumeration value="volt_amperes"/&gt;
 *     &lt;enumeration value="watt_hours"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BatteryPowerUnitOfMeasure")
@XmlEnum
public enum BatteryPowerUnitOfMeasure {

    @XmlEnumValue("milliamp_hours")
    MILLIAMP___HOURS("milliamp_hours"),
    @XmlEnumValue("amp_hours")
    AMP___HOURS("amp_hours"),
    @XmlEnumValue("volt_amperes")
    VOLT___AMPERES("volt_amperes"),
    @XmlEnumValue("watt_hours")
    WATT___HOURS("watt_hours");
    private final String value;

    BatteryPowerUnitOfMeasure(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BatteryPowerUnitOfMeasure fromValue(String v) {
        for (BatteryPowerUnitOfMeasure c: BatteryPowerUnitOfMeasure.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
