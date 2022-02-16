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
 *         &lt;element name="ProductType"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element ref="{}Coin"/&gt;
 *                   &lt;element ref="{}CollectibleCoins"/&gt;
 *                   &lt;element ref="{}Bullion"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
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
    "productType"
})
@XmlRootElement(name = "Coins")
public class Coins {

    @XmlElement(name = "ProductType", required = true)
    protected Coins.ProductType productType;

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link Coins.ProductType }
     *     
     */
    public Coins.ProductType getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Coins.ProductType }
     *     
     */
    public void setProductType(Coins.ProductType value) {
        this.productType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element ref="{}Coin"/&gt;
     *         &lt;element ref="{}CollectibleCoins"/&gt;
     *         &lt;element ref="{}Bullion"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "coin",
        "collectibleCoins",
        "bullion"
    })
    public static class ProductType {

        @XmlElement(name = "Coin")
        protected Coin coin;
        @XmlElement(name = "CollectibleCoins")
        protected CollectibleCoins collectibleCoins;
        @XmlElement(name = "Bullion")
        protected Bullion bullion;

        /**
         * Gets the value of the coin property.
         * 
         * @return
         *     possible object is
         *     {@link Coin }
         *     
         */
        public Coin getCoin() {
            return coin;
        }

        /**
         * Sets the value of the coin property.
         * 
         * @param value
         *     allowed object is
         *     {@link Coin }
         *     
         */
        public void setCoin(Coin value) {
            this.coin = value;
        }

        /**
         * Gets the value of the collectibleCoins property.
         * 
         * @return
         *     possible object is
         *     {@link CollectibleCoins }
         *     
         */
        public CollectibleCoins getCollectibleCoins() {
            return collectibleCoins;
        }

        /**
         * Sets the value of the collectibleCoins property.
         * 
         * @param value
         *     allowed object is
         *     {@link CollectibleCoins }
         *     
         */
        public void setCollectibleCoins(CollectibleCoins value) {
            this.collectibleCoins = value;
        }

        /**
         * Gets the value of the bullion property.
         * 
         * @return
         *     possible object is
         *     {@link Bullion }
         *     
         */
        public Bullion getBullion() {
            return bullion;
        }

        /**
         * Sets the value of the bullion property.
         * 
         * @param value
         *     allowed object is
         *     {@link Bullion }
         *     
         */
        public void setBullion(Bullion value) {
            this.bullion = value;
        }

    }

}
