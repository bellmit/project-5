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
 * <p>Java class for TabletInputMethodTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TabletInputMethodTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="buttons"/&gt;
 *     &lt;enumeration value="dial"/&gt;
 *     &lt;enumeration value="handwriting_recognition"/&gt;
 *     &lt;enumeration value="keyboard"/&gt;
 *     &lt;enumeration value="keypad"/&gt;
 *     &lt;enumeration value="keypad_stroke"/&gt;
 *     &lt;enumeration value="keypad_stroke"/&gt;
 *     &lt;enumeration value="microphone"/&gt;
 *     &lt;enumeration value="touch_screen"/&gt;
 *     &lt;enumeration value="touch_screen_stylus_pen"/&gt;
 *     &lt;enumeration value="trackpoint_pointing_device"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TabletInputMethodTypeValues")
@XmlEnum
public enum TabletInputMethodTypeValues {

    @XmlEnumValue("buttons")
    BUTTONS("buttons"),
    @XmlEnumValue("dial")
    DIAL("dial"),
    @XmlEnumValue("handwriting_recognition")
    HANDWRITING___RECOGNITION("handwriting_recognition"),
    @XmlEnumValue("keyboard")
    KEYBOARD("keyboard"),
    @XmlEnumValue("keypad")
    KEYPAD("keypad"),
    @XmlEnumValue("keypad_stroke")
    KEYPAD___STROKE("keypad_stroke"),
    @XmlEnumValue("microphone")
    MICROPHONE("microphone"),
    @XmlEnumValue("touch_screen")
    TOUCH___SCREEN("touch_screen"),
    @XmlEnumValue("touch_screen_stylus_pen")
    TOUCH___SCREEN___STYLUS___PEN("touch_screen_stylus_pen"),
    @XmlEnumValue("trackpoint_pointing_device")
    TRACKPOINT___POINTING___DEVICE("trackpoint_pointing_device");
    private final String value;

    TabletInputMethodTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TabletInputMethodTypeValues fromValue(String v) {
        for (TabletInputMethodTypeValues c: TabletInputMethodTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
