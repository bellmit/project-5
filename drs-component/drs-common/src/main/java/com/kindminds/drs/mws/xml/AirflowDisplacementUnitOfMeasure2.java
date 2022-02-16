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
 * <p>Java class for AirflowDisplacementUnitOfMeasure2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AirflowDisplacementUnitOfMeasure2"&gt;
 *   &lt;restriction base="{}String"&gt;
 *     &lt;enumeration value="cubic_feet_per_minute"/&gt;
 *     &lt;enumeration value="CFM"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AirflowDisplacementUnitOfMeasure2")
@XmlEnum
public enum AirflowDisplacementUnitOfMeasure2 {

    @XmlEnumValue("cubic_feet_per_minute")
    CUBIC___FEET___PER___MINUTE("cubic_feet_per_minute"),
    CFM("CFM");
    private final String value;

    AirflowDisplacementUnitOfMeasure2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AirflowDisplacementUnitOfMeasure2 fromValue(String v) {
        for (AirflowDisplacementUnitOfMeasure2 c: AirflowDisplacementUnitOfMeasure2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}