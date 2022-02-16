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
 * <p>Java class for InterfaceTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InterfaceTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="eide"/&gt;
 *     &lt;enumeration value="fibre_channel"/&gt;
 *     &lt;enumeration value="ide"/&gt;
 *     &lt;enumeration value="pci_x_16"/&gt;
 *     &lt;enumeration value="pci_x_4"/&gt;
 *     &lt;enumeration value="pci_x_8"/&gt;
 *     &lt;enumeration value="sas"/&gt;
 *     &lt;enumeration value="sata_1_5_gb"/&gt;
 *     &lt;enumeration value="sata_3_0_gb"/&gt;
 *     &lt;enumeration value="sata_6_0_gb"/&gt;
 *     &lt;enumeration value="scsi"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "InterfaceTypeValues")
@XmlEnum
public enum InterfaceTypeValues {

    @XmlEnumValue("eide")
    EIDE("eide"),
    @XmlEnumValue("fibre_channel")
    FIBRE___CHANNEL("fibre_channel"),
    @XmlEnumValue("ide")
    IDE("ide"),
    @XmlEnumValue("pci_x_16")
    PCI___X___16("pci_x_16"),
    @XmlEnumValue("pci_x_4")
    PCI___X___4("pci_x_4"),
    @XmlEnumValue("pci_x_8")
    PCI___X___8("pci_x_8"),
    @XmlEnumValue("sas")
    SAS("sas"),
    @XmlEnumValue("sata_1_5_gb")
    SATA___1___5___GB("sata_1_5_gb"),
    @XmlEnumValue("sata_3_0_gb")
    SATA___3___0___GB("sata_3_0_gb"),
    @XmlEnumValue("sata_6_0_gb")
    SATA___6___0___GB("sata_6_0_gb"),
    @XmlEnumValue("scsi")
    SCSI("scsi");
    private final String value;

    InterfaceTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InterfaceTypeValues fromValue(String v) {
        for (InterfaceTypeValues c: InterfaceTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}