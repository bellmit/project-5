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
 * <p>Java class for HeadsetStyleValue.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HeadsetStyleValue"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="over-the-ear"/&gt;
 *     &lt;enumeration value="behind-the-ear"/&gt;
 *     &lt;enumeration value="in-the-ear"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HeadsetStyleValue")
@XmlEnum
public enum HeadsetStyleValue {

    @XmlEnumValue("over-the-ear")
    OVER___THE___EAR("over-the-ear"),
    @XmlEnumValue("behind-the-ear")
    BEHIND___THE___EAR("behind-the-ear"),
    @XmlEnumValue("in-the-ear")
    IN___THE___EAR("in-the-ear");
    private final String value;

    HeadsetStyleValue(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HeadsetStyleValue fromValue(String v) {
        for (HeadsetStyleValue c: HeadsetStyleValue.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
