//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NeckSizeUnitOfMeasure.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NeckSizeUnitOfMeasure"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CM"/&gt;
 *     &lt;enumeration value="IN"/&gt;
 *     &lt;enumeration value="MM"/&gt;
 *     &lt;enumeration value="M"/&gt;
 *     &lt;enumeration value="FT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NeckSizeUnitOfMeasure")
@XmlEnum
public enum NeckSizeUnitOfMeasure {

    CM,
    IN,
    MM,
    M,
    FT;

    public String value() {
        return name();
    }

    public static NeckSizeUnitOfMeasure fromValue(String v) {
        return valueOf(v);
    }

}