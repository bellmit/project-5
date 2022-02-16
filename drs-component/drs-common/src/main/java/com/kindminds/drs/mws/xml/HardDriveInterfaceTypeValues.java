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
 * <p>Java class for HardDriveInterfaceTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HardDriveInterfaceTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ata"/&gt;
 *     &lt;enumeration value="ata100"/&gt;
 *     &lt;enumeration value="ata133"/&gt;
 *     &lt;enumeration value="ata_2"/&gt;
 *     &lt;enumeration value="ata_3"/&gt;
 *     &lt;enumeration value="ata_4"/&gt;
 *     &lt;enumeration value="ata_5"/&gt;
 *     &lt;enumeration value="atapi"/&gt;
 *     &lt;enumeration value="dma"/&gt;
 *     &lt;enumeration value="eide"/&gt;
 *     &lt;enumeration value="eio"/&gt;
 *     &lt;enumeration value="esata"/&gt;
 *     &lt;enumeration value="esdi"/&gt;
 *     &lt;enumeration value="ethernet"/&gt;
 *     &lt;enumeration value="ethernet_100base_t"/&gt;
 *     &lt;enumeration value="ethernet_100base_tx"/&gt;
 *     &lt;enumeration value="ethernet_10_100_1000"/&gt;
 *     &lt;enumeration value="ethernet_10base_t"/&gt;
 *     &lt;enumeration value="fast_scsi"/&gt;
 *     &lt;enumeration value="fast_wide_scsi"/&gt;
 *     &lt;enumeration value="fata"/&gt;
 *     &lt;enumeration value="fc_al"/&gt;
 *     &lt;enumeration value="fc_al_2"/&gt;
 *     &lt;enumeration value="fdd"/&gt;
 *     &lt;enumeration value="fibre_channel"/&gt;
 *     &lt;enumeration value="firewire"/&gt;
 *     &lt;enumeration value="ide"/&gt;
 *     &lt;enumeration value="ieee_1284"/&gt;
 *     &lt;enumeration value="ieee_1394b"/&gt;
 *     &lt;enumeration value="iscsi"/&gt;
 *     &lt;enumeration value="lvds"/&gt;
 *     &lt;enumeration value="pc_card"/&gt;
 *     &lt;enumeration value="pci_express_x16"/&gt;
 *     &lt;enumeration value="pci_express_x4"/&gt;
 *     &lt;enumeration value="pci_express_x8"/&gt;
 *     &lt;enumeration value="raid"/&gt;
 *     &lt;enumeration value="scsi"/&gt;
 *     &lt;enumeration value="serial_ata"/&gt;
 *     &lt;enumeration value="serial_ata150"/&gt;
 *     &lt;enumeration value="serial_ata300"/&gt;
 *     &lt;enumeration value="serial_ata600"/&gt;
 *     &lt;enumeration value="serial_scsi"/&gt;
 *     &lt;enumeration value="solid_state"/&gt;
 *     &lt;enumeration value="ssa"/&gt;
 *     &lt;enumeration value="st412"/&gt;
 *     &lt;enumeration value="ultra2_scsi"/&gt;
 *     &lt;enumeration value="ultra2_wide_scsi"/&gt;
 *     &lt;enumeration value="ultra3_scsi"/&gt;
 *     &lt;enumeration value="ultra_160_scsi"/&gt;
 *     &lt;enumeration value="ultra_320_scsi"/&gt;
 *     &lt;enumeration value="ultra_ata"/&gt;
 *     &lt;enumeration value="ultra_scsi"/&gt;
 *     &lt;enumeration value="ultra_wide_scsi"/&gt;
 *     &lt;enumeration value="unknown"/&gt;
 *     &lt;enumeration value="usb"/&gt;
 *     &lt;enumeration value="usb_1.1"/&gt;
 *     &lt;enumeration value="usb_2.0"/&gt;
 *     &lt;enumeration value="usb_2.0_3.0"/&gt;
 *     &lt;enumeration value="usb_3.0"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HardDriveInterfaceTypeValues")
@XmlEnum
public enum HardDriveInterfaceTypeValues {

    @XmlEnumValue("ata")
    ATA("ata"),
    @XmlEnumValue("ata100")
    ATA_100("ata100"),
    @XmlEnumValue("ata133")
    ATA_133("ata133"),
    @XmlEnumValue("ata_2")
    ATA___2("ata_2"),
    @XmlEnumValue("ata_3")
    ATA___3("ata_3"),
    @XmlEnumValue("ata_4")
    ATA___4("ata_4"),
    @XmlEnumValue("ata_5")
    ATA___5("ata_5"),
    @XmlEnumValue("atapi")
    ATAPI("atapi"),
    @XmlEnumValue("dma")
    DMA("dma"),
    @XmlEnumValue("eide")
    EIDE("eide"),
    @XmlEnumValue("eio")
    EIO("eio"),
    @XmlEnumValue("esata")
    ESATA("esata"),
    @XmlEnumValue("esdi")
    ESDI("esdi"),
    @XmlEnumValue("ethernet")
    ETHERNET("ethernet"),
    @XmlEnumValue("ethernet_100base_t")
    ETHERNET___100_BASE___T("ethernet_100base_t"),
    @XmlEnumValue("ethernet_100base_tx")
    ETHERNET___100_BASE___TX("ethernet_100base_tx"),
    @XmlEnumValue("ethernet_10_100_1000")
    ETHERNET___10___100___1000("ethernet_10_100_1000"),
    @XmlEnumValue("ethernet_10base_t")
    ETHERNET___10_BASE___T("ethernet_10base_t"),
    @XmlEnumValue("fast_scsi")
    FAST___SCSI("fast_scsi"),
    @XmlEnumValue("fast_wide_scsi")
    FAST___WIDE___SCSI("fast_wide_scsi"),
    @XmlEnumValue("fata")
    FATA("fata"),
    @XmlEnumValue("fc_al")
    FC___AL("fc_al"),
    @XmlEnumValue("fc_al_2")
    FC___AL___2("fc_al_2"),
    @XmlEnumValue("fdd")
    FDD("fdd"),
    @XmlEnumValue("fibre_channel")
    FIBRE___CHANNEL("fibre_channel"),
    @XmlEnumValue("firewire")
    FIREWIRE("firewire"),
    @XmlEnumValue("ide")
    IDE("ide"),
    @XmlEnumValue("ieee_1284")
    IEEE___1284("ieee_1284"),
    @XmlEnumValue("ieee_1394b")
    IEEE___1394_B("ieee_1394b"),
    @XmlEnumValue("iscsi")
    ISCSI("iscsi"),
    @XmlEnumValue("lvds")
    LVDS("lvds"),
    @XmlEnumValue("pc_card")
    PC___CARD("pc_card"),
    @XmlEnumValue("pci_express_x16")
    PCI___EXPRESS___X_16("pci_express_x16"),
    @XmlEnumValue("pci_express_x4")
    PCI___EXPRESS___X_4("pci_express_x4"),
    @XmlEnumValue("pci_express_x8")
    PCI___EXPRESS___X_8("pci_express_x8"),
    @XmlEnumValue("raid")
    RAID("raid"),
    @XmlEnumValue("scsi")
    SCSI("scsi"),
    @XmlEnumValue("serial_ata")
    SERIAL___ATA("serial_ata"),
    @XmlEnumValue("serial_ata150")
    SERIAL___ATA_150("serial_ata150"),
    @XmlEnumValue("serial_ata300")
    SERIAL___ATA_300("serial_ata300"),
    @XmlEnumValue("serial_ata600")
    SERIAL___ATA_600("serial_ata600"),
    @XmlEnumValue("serial_scsi")
    SERIAL___SCSI("serial_scsi"),
    @XmlEnumValue("solid_state")
    SOLID___STATE("solid_state"),
    @XmlEnumValue("ssa")
    SSA("ssa"),
    @XmlEnumValue("st412")
    ST_412("st412"),
    @XmlEnumValue("ultra2_scsi")
    ULTRA_2___SCSI("ultra2_scsi"),
    @XmlEnumValue("ultra2_wide_scsi")
    ULTRA_2___WIDE___SCSI("ultra2_wide_scsi"),
    @XmlEnumValue("ultra3_scsi")
    ULTRA_3___SCSI("ultra3_scsi"),
    @XmlEnumValue("ultra_160_scsi")
    ULTRA___160___SCSI("ultra_160_scsi"),
    @XmlEnumValue("ultra_320_scsi")
    ULTRA___320___SCSI("ultra_320_scsi"),
    @XmlEnumValue("ultra_ata")
    ULTRA___ATA("ultra_ata"),
    @XmlEnumValue("ultra_scsi")
    ULTRA___SCSI("ultra_scsi"),
    @XmlEnumValue("ultra_wide_scsi")
    ULTRA___WIDE___SCSI("ultra_wide_scsi"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown"),
    @XmlEnumValue("usb")
    USB("usb"),
    @XmlEnumValue("usb_1.1")
    USB___1___1("usb_1.1"),
    @XmlEnumValue("usb_2.0")
    USB___2___0("usb_2.0"),
    @XmlEnumValue("usb_2.0_3.0")
    USB___2___0___3___0("usb_2.0_3.0"),
    @XmlEnumValue("usb_3.0")
    USB___3___0("usb_3.0");
    private final String value;

    HardDriveInterfaceTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HardDriveInterfaceTypeValues fromValue(String v) {
        for (HardDriveInterfaceTypeValues c: HardDriveInterfaceTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}