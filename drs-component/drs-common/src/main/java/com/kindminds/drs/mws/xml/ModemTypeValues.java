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
 * <p>Java class for ModemTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ModemTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="analog_modem"/&gt;
 *     &lt;enumeration value="data_fax_voice"/&gt;
 *     &lt;enumeration value="isdn_modem"/&gt;
 *     &lt;enumeration value="cable"/&gt;
 *     &lt;enumeration value="data_modem"/&gt;
 *     &lt;enumeration value="network_modem"/&gt;
 *     &lt;enumeration value="cellular"/&gt;
 *     &lt;enumeration value="digital"/&gt;
 *     &lt;enumeration value="unknown_modem_type"/&gt;
 *     &lt;enumeration value="csu"/&gt;
 *     &lt;enumeration value="dsl"/&gt;
 *     &lt;enumeration value="voice"/&gt;
 *     &lt;enumeration value="data_fax"/&gt;
 *     &lt;enumeration value="dsu"/&gt;
 *     &lt;enumeration value="winmodem"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ModemTypeValues")
@XmlEnum
public enum ModemTypeValues {

    @XmlEnumValue("analog_modem")
    ANALOG___MODEM("analog_modem"),
    @XmlEnumValue("data_fax_voice")
    DATA___FAX___VOICE("data_fax_voice"),
    @XmlEnumValue("isdn_modem")
    ISDN___MODEM("isdn_modem"),
    @XmlEnumValue("cable")
    CABLE("cable"),
    @XmlEnumValue("data_modem")
    DATA___MODEM("data_modem"),
    @XmlEnumValue("network_modem")
    NETWORK___MODEM("network_modem"),
    @XmlEnumValue("cellular")
    CELLULAR("cellular"),
    @XmlEnumValue("digital")
    DIGITAL("digital"),
    @XmlEnumValue("unknown_modem_type")
    UNKNOWN___MODEM___TYPE("unknown_modem_type"),
    @XmlEnumValue("csu")
    CSU("csu"),
    @XmlEnumValue("dsl")
    DSL("dsl"),
    @XmlEnumValue("voice")
    VOICE("voice"),
    @XmlEnumValue("data_fax")
    DATA___FAX("data_fax"),
    @XmlEnumValue("dsu")
    DSU("dsu"),
    @XmlEnumValue("winmodem")
    WINMODEM("winmodem");
    private final String value;

    ModemTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ModemTypeValues fromValue(String v) {
        for (ModemTypeValues c: ModemTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
