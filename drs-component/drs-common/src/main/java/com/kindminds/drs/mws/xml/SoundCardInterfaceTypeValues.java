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
 * <p>Java class for SoundCardInterfaceTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SoundCardInterfaceTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="express_card"/&gt;
 *     &lt;enumeration value="firewire_1600"/&gt;
 *     &lt;enumeration value="firewire_3200"/&gt;
 *     &lt;enumeration value="firewire_400"/&gt;
 *     &lt;enumeration value="firewire_800"/&gt;
 *     &lt;enumeration value="firewire_esata"/&gt;
 *     &lt;enumeration value="pci"/&gt;
 *     &lt;enumeration value="pci_x_1"/&gt;
 *     &lt;enumeration value="pci_x_16"/&gt;
 *     &lt;enumeration value="pci_x_4"/&gt;
 *     &lt;enumeration value="pci_x_8"/&gt;
 *     &lt;enumeration value="pcmcia"/&gt;
 *     &lt;enumeration value="usb1.0"/&gt;
 *     &lt;enumeration value="usb1.1"/&gt;
 *     &lt;enumeration value="usb2.0"/&gt;
 *     &lt;enumeration value="usb3.0"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SoundCardInterfaceTypeValues")
@XmlEnum
public enum SoundCardInterfaceTypeValues {

    @XmlEnumValue("express_card")
    EXPRESS___CARD("express_card"),
    @XmlEnumValue("firewire_1600")
    FIREWIRE___1600("firewire_1600"),
    @XmlEnumValue("firewire_3200")
    FIREWIRE___3200("firewire_3200"),
    @XmlEnumValue("firewire_400")
    FIREWIRE___400("firewire_400"),
    @XmlEnumValue("firewire_800")
    FIREWIRE___800("firewire_800"),
    @XmlEnumValue("firewire_esata")
    FIREWIRE___ESATA("firewire_esata"),
    @XmlEnumValue("pci")
    PCI("pci"),
    @XmlEnumValue("pci_x_1")
    PCI___X___1("pci_x_1"),
    @XmlEnumValue("pci_x_16")
    PCI___X___16("pci_x_16"),
    @XmlEnumValue("pci_x_4")
    PCI___X___4("pci_x_4"),
    @XmlEnumValue("pci_x_8")
    PCI___X___8("pci_x_8"),
    @XmlEnumValue("pcmcia")
    PCMCIA("pcmcia"),
    @XmlEnumValue("usb1.0")
    USB_1___0("usb1.0"),
    @XmlEnumValue("usb1.1")
    USB_1___1("usb1.1"),
    @XmlEnumValue("usb2.0")
    USB_2___0("usb2.0"),
    @XmlEnumValue("usb3.0")
    USB_3___0("usb3.0");
    private final String value;

    SoundCardInterfaceTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SoundCardInterfaceTypeValues fromValue(String v) {
        for (SoundCardInterfaceTypeValues c: SoundCardInterfaceTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
