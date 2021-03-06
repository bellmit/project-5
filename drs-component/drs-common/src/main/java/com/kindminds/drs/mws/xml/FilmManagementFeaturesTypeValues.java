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
 * <p>Java class for FilmManagementFeaturesTypeValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FilmManagementFeaturesTypeValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="auto_film_advance"/&gt;
 *     &lt;enumeration value="auto_rewind"/&gt;
 *     &lt;enumeration value="midroll_rewind"/&gt;
 *     &lt;enumeration value="auto_film_load"/&gt;
 *     &lt;enumeration value="midroll_change"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FilmManagementFeaturesTypeValues")
@XmlEnum
public enum FilmManagementFeaturesTypeValues {

    @XmlEnumValue("auto_film_advance")
    AUTO___FILM___ADVANCE("auto_film_advance"),
    @XmlEnumValue("auto_rewind")
    AUTO___REWIND("auto_rewind"),
    @XmlEnumValue("midroll_rewind")
    MIDROLL___REWIND("midroll_rewind"),
    @XmlEnumValue("auto_film_load")
    AUTO___FILM___LOAD("auto_film_load"),
    @XmlEnumValue("midroll_change")
    MIDROLL___CHANGE("midroll_change");
    private final String value;

    FilmManagementFeaturesTypeValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FilmManagementFeaturesTypeValues fromValue(String v) {
        for (FilmManagementFeaturesTypeValues c: FilmManagementFeaturesTypeValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
