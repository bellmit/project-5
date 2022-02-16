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
 * <p>Java class for BindingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BindingType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="perfect"/&gt;
 *     &lt;enumeration value="saddle_stitch"/&gt;
 *     &lt;enumeration value="side_stitch"/&gt;
 *     &lt;enumeration value="case"/&gt;
 *     &lt;enumeration value="plastic_comb"/&gt;
 *     &lt;enumeration value="three_ring"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BindingType")
@XmlEnum
public enum BindingType {

    @XmlEnumValue("perfect")
    PERFECT("perfect"),
    @XmlEnumValue("saddle_stitch")
    SADDLE___STITCH("saddle_stitch"),
    @XmlEnumValue("side_stitch")
    SIDE___STITCH("side_stitch"),
    @XmlEnumValue("case")
    CASE("case"),
    @XmlEnumValue("plastic_comb")
    PLASTIC___COMB("plastic_comb"),
    @XmlEnumValue("three_ring")
    THREE___RING("three_ring");
    private final String value;

    BindingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BindingType fromValue(String v) {
        for (BindingType c: BindingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
