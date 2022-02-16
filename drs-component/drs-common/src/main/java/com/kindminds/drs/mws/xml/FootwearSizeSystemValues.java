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
 * <p>Java class for FootwearSizeSystemValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FootwearSizeSystemValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="us_footwear_size_system"/&gt;
 *     &lt;enumeration value="eu_footwear_size_system"/&gt;
 *     &lt;enumeration value="uk_footwear_size_system"/&gt;
 *     &lt;enumeration value="jp_footwear_size_system"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FootwearSizeSystemValues")
@XmlEnum
public enum FootwearSizeSystemValues {

    @XmlEnumValue("us_footwear_size_system")
    US___FOOTWEAR___SIZE___SYSTEM("us_footwear_size_system"),
    @XmlEnumValue("eu_footwear_size_system")
    EU___FOOTWEAR___SIZE___SYSTEM("eu_footwear_size_system"),
    @XmlEnumValue("uk_footwear_size_system")
    UK___FOOTWEAR___SIZE___SYSTEM("uk_footwear_size_system"),
    @XmlEnumValue("jp_footwear_size_system")
    JP___FOOTWEAR___SIZE___SYSTEM("jp_footwear_size_system");
    private final String value;

    FootwearSizeSystemValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FootwearSizeSystemValues fromValue(String v) {
        for (FootwearSizeSystemValues c: FootwearSizeSystemValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
