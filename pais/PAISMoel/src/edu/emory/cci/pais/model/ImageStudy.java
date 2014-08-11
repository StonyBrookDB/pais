//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.05 at 01:52:36 PM EST 
//


package edu.emory.cci.pais.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ImageStudy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImageStudy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="series" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}ImageSeries" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="instanceUID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="studyDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="studyTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageStudy", propOrder = {
    "series"
})
public class ImageStudy {

    protected ImageStudy.Series series;
    @XmlAttribute(name = "id")
    protected BigInteger id;
    @XmlAttribute(name = "instanceUID")
    protected String instanceUID;
    @XmlAttribute(name = "studyDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar studyDate;
    @XmlAttribute(name = "studyTime")
    protected String studyTime;

    /**
     * Gets the value of the series property.
     * 
     * @return
     *     possible object is
     *     {@link ImageStudy.Series }
     *     
     */
    public ImageStudy.Series getSeries() {
        return series;
    }

    /**
     * Sets the value of the series property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageStudy.Series }
     *     
     */
    public void setSeries(ImageStudy.Series value) {
        this.series = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the instanceUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceUID() {
        return instanceUID;
    }

    /**
     * Sets the value of the instanceUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceUID(String value) {
        this.instanceUID = value;
    }

    /**
     * Gets the value of the studyDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStudyDate() {
        return studyDate;
    }

    /**
     * Sets the value of the studyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStudyDate(XMLGregorianCalendar value) {
        this.studyDate = value;
    }

    /**
     * Gets the value of the studyTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyTime() {
        return studyTime;
    }

    /**
     * Sets the value of the studyTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyTime(String value) {
        this.studyTime = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}ImageSeries" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "imageSeries"
    })
    public static class Series {

        @XmlElement(name = "ImageSeries")
        protected ImageSeries imageSeries;

        /**
         * Gets the value of the imageSeries property.
         * 
         * @return
         *     possible object is
         *     {@link ImageSeries }
         *     
         */
        public ImageSeries getImageSeries() {
            return imageSeries;
        }

        /**
         * Sets the value of the imageSeries property.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageSeries }
         *     
         */
        public void setImageSeries(ImageSeries value) {
            this.imageSeries = value;
        }

    }

}
