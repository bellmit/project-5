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
 * <p>Java class for PrinterWirelessTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrinterWirelessTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="2.4_ghz_radio_frequency"/&gt;
 *     &lt;enumeration value="5.8_ghz_radio_frequency"/&gt;
 *     &lt;enumeration value="54g"/&gt;
 *     &lt;enumeration value="802_11_A"/&gt;
 *     &lt;enumeration value="802_11_AB"/&gt;
 *     &lt;enumeration value="802_11_ABG"/&gt;
 *     &lt;enumeration value="802_11_AG"/&gt;
 *     &lt;enumeration value="802_11_B"/&gt;
 *     &lt;enumeration value="802_11_BGN"/&gt;
 *     &lt;enumeration value="802_11_G"/&gt;
 *     &lt;enumeration value="802_11_G_108Mbps"/&gt;
 *     &lt;enumeration value="802_11_G_54Mbps"/&gt;
 *     &lt;enumeration value="802_11_N"/&gt;
 *     &lt;enumeration value="900_mhz_radio_frequency"/&gt;
 *     &lt;enumeration value="bluetooth"/&gt;
 *     &lt;enumeration value="dect"/&gt;
 *     &lt;enumeration value="dect_6.0"/&gt;
 *     &lt;enumeration value="infrared"/&gt;
 *     &lt;enumeration value="irda"/&gt;
 *     &lt;enumeration value="radio_frequency"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PrinterWirelessTypeValues")
@XmlEnum
public enum PrinterWirelessTypeValues {

    @XmlEnumValue("2.4_ghz_radio_frequency")
    VALUE_1("2.4_ghz_radio_frequency"),
    @XmlEnumValue("5.8_ghz_radio_frequency")
    VALUE_2("5.8_ghz_radio_frequency"),
    @XmlEnumValue("54g")
    VALUE_3("54g"),
    @XmlEnumValue("802_11_A")
    VALUE_4("802_11_A"),
    @XmlEnumValue("802_11_AB")
    VALUE_5("802_11_AB"),
    @XmlEnumValue("802_11_ABG")
    VALUE_6("802_11_ABG"),
    @XmlEnumValue("802_11_AG")
    VALUE_7("802_11_AG"),
    @XmlEnumValue("802_11_B")
    VALUE_8("802_11_B"),
    @XmlEnumValue("802_11_BGN")
    VALUE_9("802_11_BGN"),
    @XmlEnumValue("802_11_G")
    VALUE_10("802_11_G"),
    @XmlEnumValue("802_11_G_108Mbps")
    VALUE_11("802_11_G_108Mbps"),
    @XmlEnumValue("802_11_G_54Mbps")
    VALUE_12("802_11_G_54Mbps"),
    @XmlEnumValue("802_11_N")
    VALUE_13("802_11_N"),
    @XmlEnumValue("900_mhz_radio_frequency")
    VALUE_14("900_mhz_radio_frequency"),
    @XmlEnumValue("bluetooth")
    VALUE_15("bluetooth"),
    @XmlEnumValue("dect")
    VALUE_16("dect"),
    @XmlEnumValue("dect_6.0")
    VALUE_17("dect_6.0"),
    @XmlEnumValue("infrared")
    VALUE_18("infrared"),
    @XmlEnumValue("irda")
    VALUE_19("irda"),
    @XmlEnumValue("radio_frequency")
    VALUE_20("radio_frequency");
    private final String value;

    PrinterWirelessTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrinterWirelessTypeValues fromValue(String v) {
        for (PrinterWirelessTypeValues c: PrinterWirelessTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
