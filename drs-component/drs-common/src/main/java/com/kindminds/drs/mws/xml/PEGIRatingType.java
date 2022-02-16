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
 * <p>Java class for PEGIRatingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PEGIRatingType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ages_3_and_over"/&gt;
 *     &lt;enumeration value="ages_7_and_over"/&gt;
 *     &lt;enumeration value="ages_12_and_over"/&gt;
 *     &lt;enumeration value="ages_16_and_over"/&gt;
 *     &lt;enumeration value="ages_18_and_over"/&gt;
 *     &lt;enumeration value="unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PEGIRatingType")
@XmlEnum
public enum PEGIRatingType {

    @XmlEnumValue("ages_3_and_over")
    AGES___3___AND___OVER("ages_3_and_over"),
    @XmlEnumValue("ages_7_and_over")
    AGES___7___AND___OVER("ages_7_and_over"),
    @XmlEnumValue("ages_12_and_over")
    AGES___12___AND___OVER("ages_12_and_over"),
    @XmlEnumValue("ages_16_and_over")
    AGES___16___AND___OVER("ages_16_and_over"),
    @XmlEnumValue("ages_18_and_over")
    AGES___18___AND___OVER("ages_18_and_over"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    PEGIRatingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PEGIRatingType fromValue(String v) {
        for (PEGIRatingType c: PEGIRatingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
