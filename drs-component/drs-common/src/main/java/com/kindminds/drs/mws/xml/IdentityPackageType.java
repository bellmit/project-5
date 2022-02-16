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
 * <p>Java class for IdentityPackageType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IdentityPackageType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="bulk"/&gt;
 *     &lt;enumeration value="frustration_free"/&gt;
 *     &lt;enumeration value="traditional"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "IdentityPackageType")
@XmlEnum
public enum IdentityPackageType {

    @XmlEnumValue("bulk")
    BULK("bulk"),
    @XmlEnumValue("frustration_free")
    FRUSTRATION___FREE("frustration_free"),
    @XmlEnumValue("traditional")
    TRADITIONAL("traditional");
    private final String value;

    IdentityPackageType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IdentityPackageType fromValue(String v) {
        for (IdentityPackageType c: IdentityPackageType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
