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
 * <p>Java class for CurrentUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CurrentUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="mA"/&gt;
 *     &lt;enumeration value="A"/&gt;
 *     &lt;enumeration value="amps"/&gt;
 *     &lt;enumeration value="picoamps"/&gt;
 *     &lt;enumeration value="microamps"/&gt;
 *     &lt;enumeration value="milliamps"/&gt;
 *     &lt;enumeration value="kiloamps"/&gt;
 *     &lt;enumeration value="nanoamps"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CurrentUnitOfMeasure")
@XmlEnum
public enum CurrentUnitOfMeasure {

    @XmlEnumValue("mA")
    M_A("mA"),
    A("A"),
    @XmlEnumValue("amps")
    AMPS("amps"),
    @XmlEnumValue("picoamps")
    PICOAMPS("picoamps"),
    @XmlEnumValue("microamps")
    MICROAMPS("microamps"),
    @XmlEnumValue("milliamps")
    MILLIAMPS("milliamps"),
    @XmlEnumValue("kiloamps")
    KILOAMPS("kiloamps"),
    @XmlEnumValue("nanoamps")
    NANOAMPS("nanoamps");
    private final String value;

    CurrentUnitOfMeasure(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CurrentUnitOfMeasure fromValue(String v) {
        for (CurrentUnitOfMeasure c: CurrentUnitOfMeasure.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
