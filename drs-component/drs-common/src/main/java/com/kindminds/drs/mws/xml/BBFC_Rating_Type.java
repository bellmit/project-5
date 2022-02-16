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
 * <p>Java class for BBFC_Rating_Type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BBFC_Rating_Type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ages_12_and_over"/&gt;
 *     &lt;enumeration value="ages_15_and_over"/&gt;
 *     &lt;enumeration value="ages_18_and_over"/&gt;
 *     &lt;enumeration value="exempt"/&gt;
 *     &lt;enumeration value="parental_guidance"/&gt;
 *     &lt;enumeration value="to_be_announced"/&gt;
 *     &lt;enumeration value="universal"/&gt;
 *     &lt;enumeration value="universal_childrens"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BBFC_Rating_Type")
@XmlEnum
public enum BBFC_Rating_Type {

    @XmlEnumValue("ages_12_and_over")
    AGES___12___AND___OVER("ages_12_and_over"),
    @XmlEnumValue("ages_15_and_over")
    AGES___15___AND___OVER("ages_15_and_over"),
    @XmlEnumValue("ages_18_and_over")
    AGES___18___AND___OVER("ages_18_and_over"),
    @XmlEnumValue("exempt")
    EXEMPT("exempt"),
    @XmlEnumValue("parental_guidance")
    PARENTAL___GUIDANCE("parental_guidance"),
    @XmlEnumValue("to_be_announced")
    TO___BE___ANNOUNCED("to_be_announced"),
    @XmlEnumValue("universal")
    UNIVERSAL("universal"),
    @XmlEnumValue("universal_childrens")
    UNIVERSAL___CHILDRENS("universal_childrens");
    private final String value;

    BBFC_Rating_Type(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BBFC_Rating_Type fromValue(String v) {
        for (BBFC_Rating_Type c: BBFC_Rating_Type.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
