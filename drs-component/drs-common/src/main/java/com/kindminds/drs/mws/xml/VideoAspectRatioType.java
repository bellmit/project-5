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
 * <p>Java class for VideoAspectRatioType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VideoAspectRatioType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="1.33:1"/&gt;
 *     &lt;enumeration value="1.37:1"/&gt;
 *     &lt;enumeration value="1.44:1"/&gt;
 *     &lt;enumeration value="1.55:1"/&gt;
 *     &lt;enumeration value="1.66:1"/&gt;
 *     &lt;enumeration value="1.75:1"/&gt;
 *     &lt;enumeration value="1.77:1"/&gt;
 *     &lt;enumeration value="1.78:1"/&gt;
 *     &lt;enumeration value="1.85:1"/&gt;
 *     &lt;enumeration value="2.20:1"/&gt;
 *     &lt;enumeration value="2.35:1"/&gt;
 *     &lt;enumeration value="2.40:1"/&gt;
 *     &lt;enumeration value="2.55:1"/&gt;
 *     &lt;enumeration value="2:1"/&gt;
 *     &lt;enumeration value="unknown_aspect_ratio"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VideoAspectRatioType")
@XmlEnum
public enum VideoAspectRatioType {

    @XmlEnumValue("1.33:1")
    VALUE_1("1.33:1"),
    @XmlEnumValue("1.37:1")
    VALUE_2("1.37:1"),
    @XmlEnumValue("1.44:1")
    VALUE_3("1.44:1"),
    @XmlEnumValue("1.55:1")
    VALUE_4("1.55:1"),
    @XmlEnumValue("1.66:1")
    VALUE_5("1.66:1"),
    @XmlEnumValue("1.75:1")
    VALUE_6("1.75:1"),
    @XmlEnumValue("1.77:1")
    VALUE_7("1.77:1"),
    @XmlEnumValue("1.78:1")
    VALUE_8("1.78:1"),
    @XmlEnumValue("1.85:1")
    VALUE_9("1.85:1"),
    @XmlEnumValue("2.20:1")
    VALUE_10("2.20:1"),
    @XmlEnumValue("2.35:1")
    VALUE_11("2.35:1"),
    @XmlEnumValue("2.40:1")
    VALUE_12("2.40:1"),
    @XmlEnumValue("2.55:1")
    VALUE_13("2.55:1"),
    @XmlEnumValue("2:1")
    VALUE_14("2:1"),
    @XmlEnumValue("unknown_aspect_ratio")
    VALUE_15("unknown_aspect_ratio");
    private final String value;

    VideoAspectRatioType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VideoAspectRatioType fromValue(String v) {
        for (VideoAspectRatioType c: VideoAspectRatioType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
