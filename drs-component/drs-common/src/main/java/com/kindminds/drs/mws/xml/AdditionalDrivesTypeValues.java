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
 * <p>Java class for AdditionalDrivesTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdditionalDrivesTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="blu_ray"/&gt;
 *     &lt;enumeration value="blu_ray_re"/&gt;
 *     &lt;enumeration value="blu_ray_rom"/&gt;
 *     &lt;enumeration value="blu_ray_rw"/&gt;
 *     &lt;enumeration value="cd_rom"/&gt;
 *     &lt;enumeration value="cd_rw"/&gt;
 *     &lt;enumeration value="dvd"/&gt;
 *     &lt;enumeration value="dvd_cd_rw"/&gt;
 *     &lt;enumeration value="dvd_minus_rw"/&gt;
 *     &lt;enumeration value="dvd_plus_minus_rw"/&gt;
 *     &lt;enumeration value="dvd_plus_r"/&gt;
 *     &lt;enumeration value="dvd_plus_rw"/&gt;
 *     &lt;enumeration value="dvd_r"/&gt;
 *     &lt;enumeration value="dvd_ram"/&gt;
 *     &lt;enumeration value="dvd_rom"/&gt;
 *     &lt;enumeration value="dvd_rw"/&gt;
 *     &lt;enumeration value="floppy"/&gt;
 *     &lt;enumeration value="ide_tape_drive"/&gt;
 *     &lt;enumeration value="jazz"/&gt;
 *     &lt;enumeration value="scsi_cdrom"/&gt;
 *     &lt;enumeration value="scsi_tape_drive"/&gt;
 *     &lt;enumeration value="thumb_drive"/&gt;
 *     &lt;enumeration value="zip"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AdditionalDrivesTypeValues")
@XmlEnum
public enum AdditionalDrivesTypeValues {

    @XmlEnumValue("blu_ray")
    BLU___RAY("blu_ray"),
    @XmlEnumValue("blu_ray_re")
    BLU___RAY___RE("blu_ray_re"),
    @XmlEnumValue("blu_ray_rom")
    BLU___RAY___ROM("blu_ray_rom"),
    @XmlEnumValue("blu_ray_rw")
    BLU___RAY___RW("blu_ray_rw"),
    @XmlEnumValue("cd_rom")
    CD___ROM("cd_rom"),
    @XmlEnumValue("cd_rw")
    CD___RW("cd_rw"),
    @XmlEnumValue("dvd")
    DVD("dvd"),
    @XmlEnumValue("dvd_cd_rw")
    DVD___CD___RW("dvd_cd_rw"),
    @XmlEnumValue("dvd_minus_rw")
    DVD___MINUS___RW("dvd_minus_rw"),
    @XmlEnumValue("dvd_plus_minus_rw")
    DVD___PLUS___MINUS___RW("dvd_plus_minus_rw"),
    @XmlEnumValue("dvd_plus_r")
    DVD___PLUS___R("dvd_plus_r"),
    @XmlEnumValue("dvd_plus_rw")
    DVD___PLUS___RW("dvd_plus_rw"),
    @XmlEnumValue("dvd_r")
    DVD___R("dvd_r"),
    @XmlEnumValue("dvd_ram")
    DVD___RAM("dvd_ram"),
    @XmlEnumValue("dvd_rom")
    DVD___ROM("dvd_rom"),
    @XmlEnumValue("dvd_rw")
    DVD___RW("dvd_rw"),
    @XmlEnumValue("floppy")
    FLOPPY("floppy"),
    @XmlEnumValue("ide_tape_drive")
    IDE___TAPE___DRIVE("ide_tape_drive"),
    @XmlEnumValue("jazz")
    JAZZ("jazz"),
    @XmlEnumValue("scsi_cdrom")
    SCSI___CDROM("scsi_cdrom"),
    @XmlEnumValue("scsi_tape_drive")
    SCSI___TAPE___DRIVE("scsi_tape_drive"),
    @XmlEnumValue("thumb_drive")
    THUMB___DRIVE("thumb_drive"),
    @XmlEnumValue("zip")
    ZIP("zip");
    private final String value;

    AdditionalDrivesTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AdditionalDrivesTypeValues fromValue(String v) {
        for (AdditionalDrivesTypeValues c: AdditionalDrivesTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
