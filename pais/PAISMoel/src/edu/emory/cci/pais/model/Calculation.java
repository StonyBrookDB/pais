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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Calculation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Calculation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="calculationResult" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}CalculationResult" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="provenance" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Provenance" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codeValue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codeMeaning" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codingSchemeDesignator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codingSchemeVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unitOfMeasure" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Calculation", propOrder = {
    "calculationResult",
    "provenance"
})
public class Calculation {

    protected Calculation.CalculationResult calculationResult;
    protected Calculation.Provenance provenance;
    @XmlAttribute(name = "id")
    protected BigInteger id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "codeValue")
    protected String codeValue;
    @XmlAttribute(name = "codeMeaning")
    protected String codeMeaning;
    @XmlAttribute(name = "codingSchemeDesignator")
    protected String codingSchemeDesignator;
    @XmlAttribute(name = "codingSchemeVersion")
    protected String codingSchemeVersion;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "unitOfMeasure")
    protected String unitOfMeasure;

    /**
     * Gets the value of the calculationResult property.
     * 
     * @return
     *     possible object is
     *     {@link Calculation.CalculationResult }
     *     
     */
    public Calculation.CalculationResult getCalculationResult() {
        return calculationResult;
    }

    /**
     * Sets the value of the calculationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Calculation.CalculationResult }
     *     
     */
    public void setCalculationResult(Calculation.CalculationResult value) {
        this.calculationResult = value;
    }

    /**
     * Gets the value of the provenance property.
     * 
     * @return
     *     possible object is
     *     {@link Calculation.Provenance }
     *     
     */
    public Calculation.Provenance getProvenance() {
        return provenance;
    }

    /**
     * Sets the value of the provenance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Calculation.Provenance }
     *     
     */
    public void setProvenance(Calculation.Provenance value) {
        this.provenance = value;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the codeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * Sets the value of the codeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeValue(String value) {
        this.codeValue = value;
    }

    /**
     * Gets the value of the codeMeaning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeMeaning() {
        return codeMeaning;
    }

    /**
     * Sets the value of the codeMeaning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeMeaning(String value) {
        this.codeMeaning = value;
    }

    /**
     * Gets the value of the codingSchemeDesignator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodingSchemeDesignator() {
        return codingSchemeDesignator;
    }

    /**
     * Sets the value of the codingSchemeDesignator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodingSchemeDesignator(String value) {
        this.codingSchemeDesignator = value;
    }

    /**
     * Gets the value of the codingSchemeVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodingSchemeVersion() {
        return codingSchemeVersion;
    }

    /**
     * Sets the value of the codingSchemeVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodingSchemeVersion(String value) {
        this.codingSchemeVersion = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
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
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}CalculationResult" minOccurs="0"/>
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
        "calculationResult"
    })
    public static class CalculationResult {

        @XmlElement(name = "CalculationResult")
        protected edu.emory.cci.pais.model.CalculationResult calculationResult;

        /**
         * Gets the value of the calculationResult property.
         * 
         * @return
         *     possible object is
         *     {@link edu.emory.cci.pais.model.CalculationResult }
         *     
         */
        public edu.emory.cci.pais.model.CalculationResult getCalculationResult() {
            return calculationResult;
        }

        /**
         * Sets the value of the calculationResult property.
         * 
         * @param value
         *     allowed object is
         *     {@link edu.emory.cci.pais.model.CalculationResult }
         *     
         */
        public void setCalculationResult(edu.emory.cci.pais.model.CalculationResult value) {
            this.calculationResult = value;
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
     *         &lt;element ref="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}Provenance" minOccurs="0"/>
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
        "provenance"
    })
    public static class Provenance {

        @XmlElement(name = "Provenance")
        protected edu.emory.cci.pais.model.Provenance provenance;

        /**
         * Gets the value of the provenance property.
         * 
         * @return
         *     possible object is
         *     {@link edu.emory.cci.pais.model.Provenance }
         *     
         */
        public edu.emory.cci.pais.model.Provenance getProvenance() {
            return provenance;
        }

        /**
         * Sets the value of the provenance property.
         * 
         * @param value
         *     allowed object is
         *     {@link edu.emory.cci.pais.model.Provenance }
         *     
         */
        public void setProvenance(edu.emory.cci.pais.model.Provenance value) {
            this.provenance = value;
        }

    }

}
