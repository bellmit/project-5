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
 * <p>Java class for SourceCountryCodeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SourceCountryCodeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="JP"/&gt;
 *     &lt;enumeration value="US"/&gt;
 *     &lt;enumeration value="GB"/&gt;
 *     &lt;enumeration value="DE"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SourceCountryCodeValues")
@XmlEnum
public enum SourceCountryCodeValues {

    JP("JP"),
    US("US"),
    GB("GB"),
    DE("DE"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    SourceCountryCodeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SourceCountryCodeValues fromValue(String v) {
        for (SourceCountryCodeValues c: SourceCountryCodeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}