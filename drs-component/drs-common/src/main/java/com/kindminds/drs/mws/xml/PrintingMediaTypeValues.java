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
 * <p>Java class for PrintingMediaTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrintingMediaTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="back_print_film"/&gt;
 *     &lt;enumeration value="banner_paper"/&gt;
 *     &lt;enumeration value="card_stock"/&gt;
 *     &lt;enumeration value="envelopes"/&gt;
 *     &lt;enumeration value="fabric"/&gt;
 *     &lt;enumeration value="glossy_film"/&gt;
 *     &lt;enumeration value="glossy_photo_paper"/&gt;
 *     &lt;enumeration value="high_resolution_paper"/&gt;
 *     &lt;enumeration value="labels"/&gt;
 *     &lt;enumeration value="paper_plain"/&gt;
 *     &lt;enumeration value="transparencies"/&gt;
 *     &lt;enumeration value="unknown"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PrintingMediaTypeValues")
@XmlEnum
public enum PrintingMediaTypeValues {

    @XmlEnumValue("back_print_film")
    BACK___PRINT___FILM("back_print_film"),
    @XmlEnumValue("banner_paper")
    BANNER___PAPER("banner_paper"),
    @XmlEnumValue("card_stock")
    CARD___STOCK("card_stock"),
    @XmlEnumValue("envelopes")
    ENVELOPES("envelopes"),
    @XmlEnumValue("fabric")
    FABRIC("fabric"),
    @XmlEnumValue("glossy_film")
    GLOSSY___FILM("glossy_film"),
    @XmlEnumValue("glossy_photo_paper")
    GLOSSY___PHOTO___PAPER("glossy_photo_paper"),
    @XmlEnumValue("high_resolution_paper")
    HIGH___RESOLUTION___PAPER("high_resolution_paper"),
    @XmlEnumValue("labels")
    LABELS("labels"),
    @XmlEnumValue("paper_plain")
    PAPER___PLAIN("paper_plain"),
    @XmlEnumValue("transparencies")
    TRANSPARENCIES("transparencies"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    PrintingMediaTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrintingMediaTypeValues fromValue(String v) {
        for (PrintingMediaTypeValues c: PrintingMediaTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
