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
 *                   &lt;element ref="{}VideoDVD"/&gt;
 *                   &lt;element ref="{}VideoVHS"/&gt;
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
@XmlRootElement(name = "Video")
public class Video {

    @XmlElement(name = "ProductType", required = true)
    protected Video.ProductType productType;

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link Video.ProductType }
     *     
     */
    public Video.ProductType getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Video.ProductType }
     *     
     */
    public void setProductType(Video.ProductType value) {
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
     *         &lt;element ref="{}VideoDVD"/&gt;
     *         &lt;element ref="{}VideoVHS"/&gt;
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
        "videoDVD",
        "videoVHS"
    })
    public static class ProductType {

        @XmlElement(name = "VideoDVD")
        protected VideoDVD videoDVD;
        @XmlElement(name = "VideoVHS")
        protected VideoVHS videoVHS;

        /**
         * Gets the value of the videoDVD property.
         * 
         * @return
         *     possible object is
         *     {@link VideoDVD }
         *     
         */
        public VideoDVD getVideoDVD() {
            return videoDVD;
        }

        /**
         * Sets the value of the videoDVD property.
         * 
         * @param value
         *     allowed object is
         *     {@link VideoDVD }
         *     
         */
        public void setVideoDVD(VideoDVD value) {
            this.videoDVD = value;
        }

        /**
         * Gets the value of the videoVHS property.
         * 
         * @return
         *     possible object is
         *     {@link VideoVHS }
         *     
         */
        public VideoVHS getVideoVHS() {
            return videoVHS;
        }

        /**
         * Sets the value of the videoVHS property.
         * 
         * @param value
         *     allowed object is
         *     {@link VideoVHS }
         *     
         */
        public void setVideoVHS(VideoVHS value) {
            this.videoVHS = value;
        }

    }

}
