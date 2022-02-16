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
 * <p>Java class for CustomerReturnPolicyValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CustomerReturnPolicyValues"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="NonReturnable"/&gt;
 *     &lt;enumeration value="Standard"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CustomerReturnPolicyValues")
@XmlEnum
public enum CustomerReturnPolicyValues {

    @XmlEnumValue("NonReturnable")
    NON_RETURNABLE("NonReturnable"),
    @XmlEnumValue("Standard")
    STANDARD("Standard");
    private final String value;

    CustomerReturnPolicyValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CustomerReturnPolicyValues fromValue(String v) {
        for (CustomerReturnPolicyValues c: CustomerReturnPolicyValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
