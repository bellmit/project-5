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
 * <p>Java class for SpeedClassRatingTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SpeedClassRatingTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="class_10"/&gt;
 *     &lt;enumeration value="class_2"/&gt;
 *     &lt;enumeration value="class_4"/&gt;
 *     &lt;enumeration value="class_6"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SpeedClassRatingTypeValues")
@XmlEnum
public enum SpeedClassRatingTypeValues {

    @XmlEnumValue("class_10")
    CLASS___10("class_10"),
    @XmlEnumValue("class_2")
    CLASS___2("class_2"),
    @XmlEnumValue("class_4")
    CLASS___4("class_4"),
    @XmlEnumValue("class_6")
    CLASS___6("class_6");
    private final String value;

    SpeedClassRatingTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SpeedClassRatingTypeValues fromValue(String v) {
        for (SpeedClassRatingTypeValues c: SpeedClassRatingTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
