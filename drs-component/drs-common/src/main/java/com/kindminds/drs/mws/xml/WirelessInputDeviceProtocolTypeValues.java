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
 * <p>Java class for WirelessInputDeviceProtocolTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="WirelessInputDeviceProtocolTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="2.4_ghz_radio_frequency"/&gt;
 *     &lt;enumeration value="5.8_ghz_radio_frequency"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "WirelessInputDeviceProtocolTypeValues")
@XmlEnum
public enum WirelessInputDeviceProtocolTypeValues {

    @XmlEnumValue("2.4_ghz_radio_frequency")
    VALUE_1("2.4_ghz_radio_frequency"),
    @XmlEnumValue("5.8_ghz_radio_frequency")
    VALUE_2("5.8_ghz_radio_frequency");
    private final String value;

    WirelessInputDeviceProtocolTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WirelessInputDeviceProtocolTypeValues fromValue(String v) {
        for (WirelessInputDeviceProtocolTypeValues c: WirelessInputDeviceProtocolTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
