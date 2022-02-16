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
 * <p>Java class for ResolutionUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResolutionUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="megapixels"/&gt;
 *     &lt;enumeration value="pixels"/&gt;
 *     &lt;enumeration value="lines_per_inch"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ResolutionUnitOfMeasure")
@XmlEnum
public enum ResolutionUnitOfMeasure {

    @XmlEnumValue("megapixels")
    MEGAPIXELS("megapixels"),
    @XmlEnumValue("pixels")
    PIXELS("pixels"),
    @XmlEnumValue("lines_per_inch")
    LINES___PER___INCH("lines_per_inch");
    private final String value;

    ResolutionUnitOfMeasure(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResolutionUnitOfMeasure fromValue(String v) {
        for (ResolutionUnitOfMeasure c: ResolutionUnitOfMeasure.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
