//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.06.10 at 11:40:51 PM CST 
//


package com.kindminds.drs.mws.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ForUseWith" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="film-cameras"/&gt;
 *               &lt;enumeration value="digital-cameras"/&gt;
 *               &lt;enumeration value="camcorders"/&gt;
 *               &lt;enumeration value="telescopes"/&gt;
 *               &lt;enumeration value="microscopes"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="AccessoryType" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="c-mounts"/&gt;
 *               &lt;enumeration value="lens-scope-converters"/&gt;
 *               &lt;enumeration value="lens-to-camera-adapters"/&gt;
 *               &lt;enumeration value="remote-lens-controllers"/&gt;
 *               &lt;enumeration value="extenders"/&gt;
 *               &lt;enumeration value="series-vii-adapters"/&gt;
 *               &lt;enumeration value="t-mounts"/&gt;
 *               &lt;enumeration value="tripod-adapters"/&gt;
 *               &lt;enumeration value="lens-boards"/&gt;
 *               &lt;enumeration value="bayonets"/&gt;
 *               &lt;enumeration value="lens-hoods"/&gt;
 *               &lt;enumeration value="lens-supports"/&gt;
 *               &lt;enumeration value="rapid-focusing-levers"/&gt;
 *               &lt;enumeration value="shutters"/&gt;
 *               &lt;enumeration value="diopters"/&gt;
 *               &lt;enumeration value="mirror-scopes"/&gt;
 *               &lt;enumeration value="lens-caps-general"/&gt;
 *               &lt;enumeration value="lens-caps-up-to-48mm"/&gt;
 *               &lt;enumeration value="lens-caps-49mm"/&gt;
 *               &lt;enumeration value="lens-caps-52mm"/&gt;
 *               &lt;enumeration value="lens-caps-55mm"/&gt;
 *               &lt;enumeration value="lens-caps-58mm"/&gt;
 *               &lt;enumeration value="lens-caps-62mm"/&gt;
 *               &lt;enumeration value="lens-caps-67mm"/&gt;
 *               &lt;enumeration value="lens-caps-72mm"/&gt;
 *               &lt;enumeration value="lens-caps-77mm"/&gt;
 *               &lt;enumeration value="lens-caps-82mm"/&gt;
 *               &lt;enumeration value="lens-caps-86mm"/&gt;
 *               &lt;enumeration value="lens-caps-95mm"/&gt;
 *               &lt;enumeration value="lens-caps-other-sizes"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "forUseWith",
    "accessoryType"
})
@XmlRootElement(name = "LensAccessory")
public class LensAccessory {

    @XmlElement(name = "ForUseWith")
    protected String forUseWith;
    @XmlElement(name = "AccessoryType")
    protected String accessoryType;

    /**
     * Gets the value of the forUseWith property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForUseWith() {
        return forUseWith;
    }

    /**
     * Sets the value of the forUseWith property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForUseWith(String value) {
        this.forUseWith = value;
    }

    /**
     * Gets the value of the accessoryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessoryType() {
        return accessoryType;
    }

    /**
     * Sets the value of the accessoryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessoryType(String value) {
        this.accessoryType = value;
    }

}