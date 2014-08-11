//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.05 at 01:52:36 PM EST 
//


package edu.emory.cci.pais.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImageReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImageReference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subject" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Subject" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="specimen" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Specimen" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="anatomicEntityCollection" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}AnatomicEntity" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="equipment" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Equipment" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="region" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Region" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageReference", propOrder = {
    "subject",
    "specimen",
    "anatomicEntityCollection",
    "equipment",
    "region"
})
@XmlSeeAlso({
    DICOMImageReference.class,
    MicroscopyImageReference.class
})
public class ImageReference {

    protected ImageReference.Subject subject;
    protected ImageReference.Specimen specimen;
    protected ImageReference.AnatomicEntityCollection anatomicEntityCollection;
    protected ImageReference.Equipment equipment;
    protected ImageReference.Region region;
    @XmlAttribute(name = "id")
    protected BigInteger id;

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link ImageReference.Subject }
     *     
     */
    public ImageReference.Subject getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageReference.Subject }
     *     
     */
    public void setSubject(ImageReference.Subject value) {
        this.subject = value;
    }

    /**
     * Gets the value of the specimen property.
     * 
     * @return
     *     possible object is
     *     {@link ImageReference.Specimen }
     *     
     */
    public ImageReference.Specimen getSpecimen() {
        return specimen;
    }

    /**
     * Sets the value of the specimen property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageReference.Specimen }
     *     
     */
    public void setSpecimen(ImageReference.Specimen value) {
        this.specimen = value;
    }

    /**
     * Gets the value of the anatomicEntityCollection property.
     * 
     * @return
     *     possible object is
     *     {@link ImageReference.AnatomicEntityCollection }
     *     
     */
    public ImageReference.AnatomicEntityCollection getAnatomicEntityCollection() {
        return anatomicEntityCollection;
    }

    /**
     * Sets the value of the anatomicEntityCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageReference.AnatomicEntityCollection }
     *     
     */
    public void setAnatomicEntityCollection(ImageReference.AnatomicEntityCollection value) {
        this.anatomicEntityCollection = value;
    }

    /**
     * Gets the value of the equipment property.
     * 
     * @return
     *     possible object is
     *     {@link ImageReference.Equipment }
     *     
     */
    public ImageReference.Equipment getEquipment() {
        return equipment;
    }

    /**
     * Sets the value of the equipment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageReference.Equipment }
     *     
     */
    public void setEquipment(ImageReference.Equipment value) {
        this.equipment = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link ImageReference.Region }
     *     
     */
    public ImageReference.Region getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageReference.Region }
     *     
     */
    public void setRegion(ImageReference.Region value) {
        this.region = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}AnatomicEntity" maxOccurs="unbounded" minOccurs="0"/>
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
        "anatomicEntity"
    })
    public static class AnatomicEntityCollection {

        @XmlElement(name = "AnatomicEntity")
        protected List<AnatomicEntity> anatomicEntity;

        /**
         * Gets the value of the anatomicEntity property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the anatomicEntity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnatomicEntity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AnatomicEntity }
         * 
         * 
         */
        public List<AnatomicEntity> getAnatomicEntity() {
            if (anatomicEntity == null) {
                anatomicEntity = new ArrayList<AnatomicEntity>();
            }
            return this.anatomicEntity;
        }

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
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Equipment" minOccurs="0"/>
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
        "equipment"
    })
    public static class Equipment {

        @XmlElement(name = "Equipment")
        protected edu.emory.cci.pais.model.Equipment equipment;

        /**
         * Gets the value of the equipment property.
         * 
         * @return
         *     possible object is
         *     {@link edu.emory.cci.pais.model.Equipment }
         *     
         */
        public edu.emory.cci.pais.model.Equipment getEquipment() {
            return equipment;
        }

        /**
         * Sets the value of the equipment property.
         * 
         * @param value
         *     allowed object is
         *     {@link edu.emory.cci.pais.model.Equipment }
         *     
         */
        public void setEquipment(edu.emory.cci.pais.model.Equipment value) {
            this.equipment = value;
        }

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
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Region" minOccurs="0"/>
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
        "region"
    })
    public static class Region {

        @XmlElement(name = "Region")
        protected edu.emory.cci.pais.model.Region region;

        /**
         * Gets the value of the region property.
         * 
         * @return
         *     possible object is
         *     {@link edu.emory.cci.pais.model.Region }
         *     
         */
        public edu.emory.cci.pais.model.Region getRegion() {
            return region;
        }

        /**
         * Sets the value of the region property.
         * 
         * @param value
         *     allowed object is
         *     {@link edu.emory.cci.pais.model.Region }
         *     
         */
        public void setRegion(edu.emory.cci.pais.model.Region value) {
            this.region = value;
        }

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
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Specimen" minOccurs="0"/>
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
        "specimen"
    })
    public static class Specimen {

        @XmlElement(name = "Specimen")
        protected edu.emory.cci.pais.model.Specimen specimen;

        /**
         * Gets the value of the specimen property.
         * 
         * @return
         *     possible object is
         *     {@link edu.emory.cci.pais.model.Specimen }
         *     
         */
        public edu.emory.cci.pais.model.Specimen getSpecimen() {
            return specimen;
        }

        /**
         * Sets the value of the specimen property.
         * 
         * @param value
         *     allowed object is
         *     {@link edu.emory.cci.pais.model.Specimen }
         *     
         */
        public void setSpecimen(edu.emory.cci.pais.model.Specimen value) {
            this.specimen = value;
        }

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
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Subject" minOccurs="0"/>
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
        "subject"
    })
    public static class Subject {

        @XmlElement(name = "Subject")
        protected edu.emory.cci.pais.model.Subject subject;

        /**
         * Gets the value of the subject property.
         * 
         * @return
         *     possible object is
         *     {@link edu.emory.cci.pais.model.Subject }
         *     
         */
        public edu.emory.cci.pais.model.Subject getSubject() {
            return subject;
        }

        /**
         * Sets the value of the subject property.
         * 
         * @param value
         *     allowed object is
         *     {@link edu.emory.cci.pais.model.Subject }
         *     
         */
        public void setSubject(edu.emory.cci.pais.model.Subject value) {
            this.subject = value;
        }

    }

}
