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
 * <p>Java class for VideoFormatType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VideoFormatType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ac-3"/&gt;
 *     &lt;enumeration value="dolby"/&gt;
 *     &lt;enumeration value="thx"/&gt;
 *     &lt;enumeration value="pal"/&gt;
 *     &lt;enumeration value="ntsc"/&gt;
 *     &lt;enumeration value="bw"/&gt;
 *     &lt;enumeration value="color"/&gt;
 *     &lt;enumeration value="subtitled"/&gt;
 *     &lt;enumeration value="dubbed"/&gt;
 *     &lt;enumeration value="closed-captioned"/&gt;
 *     &lt;enumeration value="import"/&gt;
 *     &lt;enumeration value="remastered"/&gt;
 *     &lt;enumeration value="widescreen"/&gt;
 *     &lt;enumeration value="hi-fidelity"/&gt;
 *     &lt;enumeration value="collectors_edition"/&gt;
 *     &lt;enumeration value="silent"/&gt;
 *     &lt;enumeration value="directors_cut"/&gt;
 *     &lt;enumeration value="full_screen"/&gt;
 *     &lt;enumeration value="anamorphic"/&gt;
 *     &lt;enumeration value="surround"/&gt;
 *     &lt;enumeration value="dts_stereo"/&gt;
 *     &lt;enumeration value="dvd_video"/&gt;
 *     &lt;enumeration value="vhs"/&gt;
 *     &lt;enumeration value="vhs_c"/&gt;
 *     &lt;enumeration value="hybrid_sacd"/&gt;
 *     &lt;enumeration value="digital_sound"/&gt;
 *     &lt;enumeration value="deluxe_edition"/&gt;
 *     &lt;enumeration value="special_extended_version"/&gt;
 *     &lt;enumeration value="special_limited_edition"/&gt;
 *     &lt;enumeration value="mono"/&gt;
 *     &lt;enumeration value="dual_disc"/&gt;
 *     &lt;enumeration value="value_price"/&gt;
 *     &lt;enumeration value="multisystem"/&gt;
 *     &lt;enumeration value="hd_dvd"/&gt;
 *     &lt;enumeration value="blu_ray"/&gt;
 *     &lt;enumeration value="umd"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VideoFormatType")
@XmlEnum
public enum VideoFormatType {

    @XmlEnumValue("ac-3")
    AC___3("ac-3"),
    @XmlEnumValue("dolby")
    DOLBY("dolby"),
    @XmlEnumValue("thx")
    THX("thx"),
    @XmlEnumValue("pal")
    PAL("pal"),
    @XmlEnumValue("ntsc")
    NTSC("ntsc"),
    @XmlEnumValue("bw")
    BW("bw"),
    @XmlEnumValue("color")
    COLOR("color"),
    @XmlEnumValue("subtitled")
    SUBTITLED("subtitled"),
    @XmlEnumValue("dubbed")
    DUBBED("dubbed"),
    @XmlEnumValue("closed-captioned")
    CLOSED___CAPTIONED("closed-captioned"),
    @XmlEnumValue("import")
    IMPORT("import"),
    @XmlEnumValue("remastered")
    REMASTERED("remastered"),
    @XmlEnumValue("widescreen")
    WIDESCREEN("widescreen"),
    @XmlEnumValue("hi-fidelity")
    HI___FIDELITY("hi-fidelity"),
    @XmlEnumValue("collectors_edition")
    COLLECTORS___EDITION("collectors_edition"),
    @XmlEnumValue("silent")
    SILENT("silent"),
    @XmlEnumValue("directors_cut")
    DIRECTORS___CUT("directors_cut"),
    @XmlEnumValue("full_screen")
    FULL___SCREEN("full_screen"),
    @XmlEnumValue("anamorphic")
    ANAMORPHIC("anamorphic"),
    @XmlEnumValue("surround")
    SURROUND("surround"),
    @XmlEnumValue("dts_stereo")
    DTS___STEREO("dts_stereo"),
    @XmlEnumValue("dvd_video")
    DVD___VIDEO("dvd_video"),
    @XmlEnumValue("vhs")
    VHS("vhs"),
    @XmlEnumValue("vhs_c")
    VHS___C("vhs_c"),
    @XmlEnumValue("hybrid_sacd")
    HYBRID___SACD("hybrid_sacd"),
    @XmlEnumValue("digital_sound")
    DIGITAL___SOUND("digital_sound"),
    @XmlEnumValue("deluxe_edition")
    DELUXE___EDITION("deluxe_edition"),
    @XmlEnumValue("special_extended_version")
    SPECIAL___EXTENDED___VERSION("special_extended_version"),
    @XmlEnumValue("special_limited_edition")
    SPECIAL___LIMITED___EDITION("special_limited_edition"),
    @XmlEnumValue("mono")
    MONO("mono"),
    @XmlEnumValue("dual_disc")
    DUAL___DISC("dual_disc"),
    @XmlEnumValue("value_price")
    VALUE___PRICE("value_price"),
    @XmlEnumValue("multisystem")
    MULTISYSTEM("multisystem"),
    @XmlEnumValue("hd_dvd")
    HD___DVD("hd_dvd"),
    @XmlEnumValue("blu_ray")
    BLU___RAY("blu_ray"),
    @XmlEnumValue("umd")
    UMD("umd");
    private final String value;

    VideoFormatType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VideoFormatType fromValue(String v) {
        for (VideoFormatType c: VideoFormatType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
