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
 * <p>Java class for EuEfficiencyClassType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EuEfficiencyClassType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="a"/&gt;
 *     &lt;enumeration value="b"/&gt;
 *     &lt;enumeration value="c"/&gt;
 *     &lt;enumeration value="d"/&gt;
 *     &lt;enumeration value="e"/&gt;
 *     &lt;enumeration value="f"/&gt;
 *     &lt;enumeration value="g"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EuEfficiencyClassType")
@XmlEnum
public enum EuEfficiencyClassType {

    @XmlEnumValue("a")
    A("a"),
    @XmlEnumValue("b")
    B("b"),
    @XmlEnumValue("c")
    C("c"),
    @XmlEnumValue("d")
    D("d"),
    @XmlEnumValue("e")
    E("e"),
    @XmlEnumValue("f")
    F("f"),
    @XmlEnumValue("g")
    G("g");
    private final String value;

    EuEfficiencyClassType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EuEfficiencyClassType fromValue(String v) {
        for (EuEfficiencyClassType c: EuEfficiencyClassType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
