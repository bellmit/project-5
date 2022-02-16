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
 * <p>Java class for MonitorConnectorsTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MonitorConnectorsTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="audio_video_port"/&gt;
 *     &lt;enumeration value="dmi"/&gt;
 *     &lt;enumeration value="ethernet"/&gt;
 *     &lt;enumeration value="gameport"/&gt;
 *     &lt;enumeration value="hdmi"/&gt;
 *     &lt;enumeration value="ieee_1394"/&gt;
 *     &lt;enumeration value="ps/2"/&gt;
 *     &lt;enumeration value="serial_interface"/&gt;
 *     &lt;enumeration value="usb2.0"/&gt;
 *     &lt;enumeration value="usb3.0"/&gt;
 *     &lt;enumeration value="vga"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MonitorConnectorsTypeValues")
@XmlEnum
public enum MonitorConnectorsTypeValues {

    @XmlEnumValue("audio_video_port")
    AUDIO___VIDEO___PORT("audio_video_port"),
    @XmlEnumValue("dmi")
    DMI("dmi"),
    @XmlEnumValue("ethernet")
    ETHERNET("ethernet"),
    @XmlEnumValue("gameport")
    GAMEPORT("gameport"),
    @XmlEnumValue("hdmi")
    HDMI("hdmi"),
    @XmlEnumValue("ieee_1394")
    IEEE___1394("ieee_1394"),
    @XmlEnumValue("ps/2")
    PS___2("ps/2"),
    @XmlEnumValue("serial_interface")
    SERIAL___INTERFACE("serial_interface"),
    @XmlEnumValue("usb2.0")
    USB_2___0("usb2.0"),
    @XmlEnumValue("usb3.0")
    USB_3___0("usb3.0"),
    @XmlEnumValue("vga")
    VGA("vga");
    private final String value;

    MonitorConnectorsTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MonitorConnectorsTypeValues fromValue(String v) {
        for (MonitorConnectorsTypeValues c: MonitorConnectorsTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}