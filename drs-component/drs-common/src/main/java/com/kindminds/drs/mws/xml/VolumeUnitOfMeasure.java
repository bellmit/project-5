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
 * <p>Java class for VolumeUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VolumeUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="cubic-cm"/&gt;
 *     &lt;enumeration value="cubic-ft"/&gt;
 *     &lt;enumeration value="cubic-in"/&gt;
 *     &lt;enumeration value="cubic-m"/&gt;
 *     &lt;enumeration value="cubic-yd"/&gt;
 *     &lt;enumeration value="cup"/&gt;
 *     &lt;enumeration value="fluid-oz"/&gt;
 *     &lt;enumeration value="gallon"/&gt;
 *     &lt;enumeration value="liter"/&gt;
 *     &lt;enumeration value="milliliter"/&gt;
 *     &lt;enumeration value="ounce"/&gt;
 *     &lt;enumeration value="pint"/&gt;
 *     &lt;enumeration value="quart"/&gt;
 *     &lt;enumeration value="liters"/&gt;
 *     &lt;enumeration value="deciliters"/&gt;
 *     &lt;enumeration value="centiliters"/&gt;
 *     &lt;enumeration value="milliliters"/&gt;
 *     &lt;enumeration value="microliters"/&gt;
 *     &lt;enumeration value="nanoliters"/&gt;
 *     &lt;enumeration value="picoliters"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VolumeUnitOfMeasure")
@XmlEnum
public enum VolumeUnitOfMeasure {

    @XmlEnumValue("cubic-cm")
    CUBIC___CM("cubic-cm"),
    @XmlEnumValue("cubic-ft")
    CUBIC___FT("cubic-ft"),
    @XmlEnumValue("cubic-in")
    CUBIC___IN("cubic-in"),
    @XmlEnumValue("cubic-m")
    CUBIC___M("cubic-m"),
    @XmlEnumValue("cubic-yd")
    CUBIC___YD("cubic-yd"),
    @XmlEnumValue("cup")
    CUP("cup"),
    @XmlEnumValue("fluid-oz")
    FLUID___OZ("fluid-oz"),
    @XmlEnumValue("gallon")
    GALLON("gallon"),
    @XmlEnumValue("liter")
    LITER("liter"),
    @XmlEnumValue("milliliter")
    MILLILITER("milliliter"),
    @XmlEnumValue("ounce")
    OUNCE("ounce"),
    @XmlEnumValue("pint")
    PINT("pint"),
    @XmlEnumValue("quart")
    QUART("quart"),
    @XmlEnumValue("liters")
    LITERS("liters"),
    @XmlEnumValue("deciliters")
    DECILITERS("deciliters"),
    @XmlEnumValue("centiliters")
    CENTILITERS("centiliters"),
    @XmlEnumValue("milliliters")
    MILLILITERS("milliliters"),
    @XmlEnumValue("microliters")
    MICROLITERS("microliters"),
    @XmlEnumValue("nanoliters")
    NANOLITERS("nanoliters"),
    @XmlEnumValue("picoliters")
    PICOLITERS("picoliters");
    private final String value;

    VolumeUnitOfMeasure(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VolumeUnitOfMeasure fromValue(String v) {
        for (VolumeUnitOfMeasure c: VolumeUnitOfMeasure.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
