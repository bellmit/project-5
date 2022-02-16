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
 * <p>Java class for CNC_Rating_Type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CNC_Rating_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="universal"/&gt;
 *     &lt;enumeration value="parental_guidance"/&gt;
 *     &lt;enumeration value="ages_12_and_over"/&gt;
 *     &lt;enumeration value="ages_14_and_over"/&gt;
 *     &lt;enumeration value="ages_16_and_over"/&gt;
 *     &lt;enumeration value="ages_18_and_over"/&gt;
 *     &lt;enumeration value="ages_18_and_over_x_rated"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CNC_Rating_Type")
@XmlEnum
public enum CNC_Rating_Type {

    @XmlEnumValue("universal")
    UNIVERSAL("universal"),
    @XmlEnumValue("parental_guidance")
    PARENTAL___GUIDANCE("parental_guidance"),
    @XmlEnumValue("ages_12_and_over")
    AGES___12___AND___OVER("ages_12_and_over"),
    @XmlEnumValue("ages_14_and_over")
    AGES___14___AND___OVER("ages_14_and_over"),
    @XmlEnumValue("ages_16_and_over")
    AGES___16___AND___OVER("ages_16_and_over"),
    @XmlEnumValue("ages_18_and_over")
    AGES___18___AND___OVER("ages_18_and_over"),
    @XmlEnumValue("ages_18_and_over_x_rated")
    AGES___18___AND___OVER___X___RATED("ages_18_and_over_x_rated");
    private final String value;

    CNC_Rating_Type(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CNC_Rating_Type fromValue(String v) {
        for (CNC_Rating_Type c: CNC_Rating_Type.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
