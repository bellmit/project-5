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
 * <p>Java class for PositionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PositionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="left"/&gt;
 *     &lt;enumeration value="left_front"/&gt;
 *     &lt;enumeration value="left_rear"/&gt;
 *     &lt;enumeration value="left_inner"/&gt;
 *     &lt;enumeration value="left_outer"/&gt;
 *     &lt;enumeration value="left_center"/&gt;
 *     &lt;enumeration value="left_upper"/&gt;
 *     &lt;enumeration value="left_lower"/&gt;
 *     &lt;enumeration value="left_inside"/&gt;
 *     &lt;enumeration value="left_outside"/&gt;
 *     &lt;enumeration value="right"/&gt;
 *     &lt;enumeration value="right_front"/&gt;
 *     &lt;enumeration value="right_rear"/&gt;
 *     &lt;enumeration value="right_inner"/&gt;
 *     &lt;enumeration value="right_outer"/&gt;
 *     &lt;enumeration value="right_center"/&gt;
 *     &lt;enumeration value="right_upper"/&gt;
 *     &lt;enumeration value="right_lower"/&gt;
 *     &lt;enumeration value="right_inside"/&gt;
 *     &lt;enumeration value="right_outside"/&gt;
 *     &lt;enumeration value="front"/&gt;
 *     &lt;enumeration value="front_inner"/&gt;
 *     &lt;enumeration value="front_outer"/&gt;
 *     &lt;enumeration value="front_center"/&gt;
 *     &lt;enumeration value="front_upper"/&gt;
 *     &lt;enumeration value="front_lower"/&gt;
 *     &lt;enumeration value="front_inside"/&gt;
 *     &lt;enumeration value="front_outside"/&gt;
 *     &lt;enumeration value="rear"/&gt;
 *     &lt;enumeration value="rear_inner"/&gt;
 *     &lt;enumeration value="rear_outer"/&gt;
 *     &lt;enumeration value="rear_center"/&gt;
 *     &lt;enumeration value="rear_upper"/&gt;
 *     &lt;enumeration value="rear_lower"/&gt;
 *     &lt;enumeration value="rear_inside"/&gt;
 *     &lt;enumeration value="rear_outside"/&gt;
 *     &lt;enumeration value="inner"/&gt;
 *     &lt;enumeration value="outer"/&gt;
 *     &lt;enumeration value="upper"/&gt;
 *     &lt;enumeration value="lower"/&gt;
 *     &lt;enumeration value="top"/&gt;
 *     &lt;enumeration value="bottom"/&gt;
 *     &lt;enumeration value="inside"/&gt;
 *     &lt;enumeration value="inside_center"/&gt;
 *     &lt;enumeration value="outside"/&gt;
 *     &lt;enumeration value="intermediate"/&gt;
 *     &lt;enumeration value="driveline"/&gt;
 *     &lt;enumeration value="front_left_inner"/&gt;
 *     &lt;enumeration value="front_left_outer"/&gt;
 *     &lt;enumeration value="front_right_inner"/&gt;
 *     &lt;enumeration value="front_right_outer"/&gt;
 *     &lt;enumeration value="rear_left_inner"/&gt;
 *     &lt;enumeration value="rear_left_outer"/&gt;
 *     &lt;enumeration value="rear_right_inner"/&gt;
 *     &lt;enumeration value="rear_right_outer"/&gt;
 *     &lt;enumeration value="front_left_upper"/&gt;
 *     &lt;enumeration value="front_left_lower"/&gt;
 *     &lt;enumeration value="front_right_upper"/&gt;
 *     &lt;enumeration value="front_right_lower"/&gt;
 *     &lt;enumeration value="rear_left_upper"/&gt;
 *     &lt;enumeration value="rear_left_lower"/&gt;
 *     &lt;enumeration value="rear_right_lower"/&gt;
 *     &lt;enumeration value="rear_right_upper"/&gt;
 *     &lt;enumeration value="left_intermediate"/&gt;
 *     &lt;enumeration value="right_intermediate"/&gt;
 *     &lt;enumeration value="bottom_left"/&gt;
 *     &lt;enumeration value="bottom_right"/&gt;
 *     &lt;enumeration value="top_left"/&gt;
 *     &lt;enumeration value="top_right"/&gt;
 *     &lt;enumeration value="front_left"/&gt;
 *     &lt;enumeration value="front_right"/&gt;
 *     &lt;enumeration value="rear_left"/&gt;
 *     &lt;enumeration value="rear_right"/&gt;
 *     &lt;enumeration value="center"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PositionType")
@XmlEnum
public enum PositionType {

    @XmlEnumValue("left")
    LEFT("left"),
    @XmlEnumValue("left_front")
    LEFT___FRONT("left_front"),
    @XmlEnumValue("left_rear")
    LEFT___REAR("left_rear"),
    @XmlEnumValue("left_inner")
    LEFT___INNER("left_inner"),
    @XmlEnumValue("left_outer")
    LEFT___OUTER("left_outer"),
    @XmlEnumValue("left_center")
    LEFT___CENTER("left_center"),
    @XmlEnumValue("left_upper")
    LEFT___UPPER("left_upper"),
    @XmlEnumValue("left_lower")
    LEFT___LOWER("left_lower"),
    @XmlEnumValue("left_inside")
    LEFT___INSIDE("left_inside"),
    @XmlEnumValue("left_outside")
    LEFT___OUTSIDE("left_outside"),
    @XmlEnumValue("right")
    RIGHT("right"),
    @XmlEnumValue("right_front")
    RIGHT___FRONT("right_front"),
    @XmlEnumValue("right_rear")
    RIGHT___REAR("right_rear"),
    @XmlEnumValue("right_inner")
    RIGHT___INNER("right_inner"),
    @XmlEnumValue("right_outer")
    RIGHT___OUTER("right_outer"),
    @XmlEnumValue("right_center")
    RIGHT___CENTER("right_center"),
    @XmlEnumValue("right_upper")
    RIGHT___UPPER("right_upper"),
    @XmlEnumValue("right_lower")
    RIGHT___LOWER("right_lower"),
    @XmlEnumValue("right_inside")
    RIGHT___INSIDE("right_inside"),
    @XmlEnumValue("right_outside")
    RIGHT___OUTSIDE("right_outside"),
    @XmlEnumValue("front")
    FRONT("front"),
    @XmlEnumValue("front_inner")
    FRONT___INNER("front_inner"),
    @XmlEnumValue("front_outer")
    FRONT___OUTER("front_outer"),
    @XmlEnumValue("front_center")
    FRONT___CENTER("front_center"),
    @XmlEnumValue("front_upper")
    FRONT___UPPER("front_upper"),
    @XmlEnumValue("front_lower")
    FRONT___LOWER("front_lower"),
    @XmlEnumValue("front_inside")
    FRONT___INSIDE("front_inside"),
    @XmlEnumValue("front_outside")
    FRONT___OUTSIDE("front_outside"),
    @XmlEnumValue("rear")
    REAR("rear"),
    @XmlEnumValue("rear_inner")
    REAR___INNER("rear_inner"),
    @XmlEnumValue("rear_outer")
    REAR___OUTER("rear_outer"),
    @XmlEnumValue("rear_center")
    REAR___CENTER("rear_center"),
    @XmlEnumValue("rear_upper")
    REAR___UPPER("rear_upper"),
    @XmlEnumValue("rear_lower")
    REAR___LOWER("rear_lower"),
    @XmlEnumValue("rear_inside")
    REAR___INSIDE("rear_inside"),
    @XmlEnumValue("rear_outside")
    REAR___OUTSIDE("rear_outside"),
    @XmlEnumValue("inner")
    INNER("inner"),
    @XmlEnumValue("outer")
    OUTER("outer"),
    @XmlEnumValue("upper")
    UPPER("upper"),
    @XmlEnumValue("lower")
    LOWER("lower"),
    @XmlEnumValue("top")
    TOP("top"),
    @XmlEnumValue("bottom")
    BOTTOM("bottom"),
    @XmlEnumValue("inside")
    INSIDE("inside"),
    @XmlEnumValue("inside_center")
    INSIDE___CENTER("inside_center"),
    @XmlEnumValue("outside")
    OUTSIDE("outside"),
    @XmlEnumValue("intermediate")
    INTERMEDIATE("intermediate"),
    @XmlEnumValue("driveline")
    DRIVELINE("driveline"),
    @XmlEnumValue("front_left_inner")
    FRONT___LEFT___INNER("front_left_inner"),
    @XmlEnumValue("front_left_outer")
    FRONT___LEFT___OUTER("front_left_outer"),
    @XmlEnumValue("front_right_inner")
    FRONT___RIGHT___INNER("front_right_inner"),
    @XmlEnumValue("front_right_outer")
    FRONT___RIGHT___OUTER("front_right_outer"),
    @XmlEnumValue("rear_left_inner")
    REAR___LEFT___INNER("rear_left_inner"),
    @XmlEnumValue("rear_left_outer")
    REAR___LEFT___OUTER("rear_left_outer"),
    @XmlEnumValue("rear_right_inner")
    REAR___RIGHT___INNER("rear_right_inner"),
    @XmlEnumValue("rear_right_outer")
    REAR___RIGHT___OUTER("rear_right_outer"),
    @XmlEnumValue("front_left_upper")
    FRONT___LEFT___UPPER("front_left_upper"),
    @XmlEnumValue("front_left_lower")
    FRONT___LEFT___LOWER("front_left_lower"),
    @XmlEnumValue("front_right_upper")
    FRONT___RIGHT___UPPER("front_right_upper"),
    @XmlEnumValue("front_right_lower")
    FRONT___RIGHT___LOWER("front_right_lower"),
    @XmlEnumValue("rear_left_upper")
    REAR___LEFT___UPPER("rear_left_upper"),
    @XmlEnumValue("rear_left_lower")
    REAR___LEFT___LOWER("rear_left_lower"),
    @XmlEnumValue("rear_right_lower")
    REAR___RIGHT___LOWER("rear_right_lower"),
    @XmlEnumValue("rear_right_upper")
    REAR___RIGHT___UPPER("rear_right_upper"),
    @XmlEnumValue("left_intermediate")
    LEFT___INTERMEDIATE("left_intermediate"),
    @XmlEnumValue("right_intermediate")
    RIGHT___INTERMEDIATE("right_intermediate"),
    @XmlEnumValue("bottom_left")
    BOTTOM___LEFT("bottom_left"),
    @XmlEnumValue("bottom_right")
    BOTTOM___RIGHT("bottom_right"),
    @XmlEnumValue("top_left")
    TOP___LEFT("top_left"),
    @XmlEnumValue("top_right")
    TOP___RIGHT("top_right"),
    @XmlEnumValue("front_left")
    FRONT___LEFT("front_left"),
    @XmlEnumValue("front_right")
    FRONT___RIGHT("front_right"),
    @XmlEnumValue("rear_left")
    REAR___LEFT("rear_left"),
    @XmlEnumValue("rear_right")
    REAR___RIGHT("rear_right"),
    @XmlEnumValue("center")
    CENTER("center");
    private final String value;

    PositionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PositionType fromValue(String v) {
        for (PositionType c: PositionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}