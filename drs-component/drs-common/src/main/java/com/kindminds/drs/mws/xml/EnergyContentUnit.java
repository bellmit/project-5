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
 * <p>Java class for EnergyContentUnit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EnergyContentUnit"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="watt_hours"/&gt;
 *     &lt;enumeration value="kilowatt_hours"/&gt;
 *     &lt;enumeration value="kcal"/&gt;
 *     &lt;enumeration value="kj"/&gt;
 *     &lt;enumeration value="btu"/&gt;
 *     &lt;enumeration value="kilowatts"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EnergyContentUnit")
@XmlEnum
public enum EnergyContentUnit {

    @XmlEnumValue("watt_hours")
    WATT___HOURS("watt_hours"),
    @XmlEnumValue("kilowatt_hours")
    KILOWATT___HOURS("kilowatt_hours"),
    @XmlEnumValue("kcal")
    KCAL("kcal"),
    @XmlEnumValue("kj")
    KJ("kj"),
    @XmlEnumValue("btu")
    BTU("btu"),
    @XmlEnumValue("kilowatts")
    KILOWATTS("kilowatts");
    private final String value;

    EnergyContentUnit(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnergyContentUnit fromValue(String v) {
        for (EnergyContentUnit c: EnergyContentUnit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
