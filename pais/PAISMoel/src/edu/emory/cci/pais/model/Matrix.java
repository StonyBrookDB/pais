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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Matrix complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Matrix">
 *   &lt;complexContent>
 *     &lt;extension base="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}CalculationResult">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="numberOfColumns" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="numberOfRows" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Matrix")
public class Matrix
    extends CalculationResult
{

    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "numberOfColumns")
    protected BigInteger numberOfColumns;
    @XmlAttribute(name = "numberOfRows")
    protected BigInteger numberOfRows;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the numberOfColumns property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * Sets the value of the numberOfColumns property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfColumns(BigInteger value) {
        this.numberOfColumns = value;
    }

    /**
     * Gets the value of the numberOfRows property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Sets the value of the numberOfRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfRows(BigInteger value) {
        this.numberOfRows = value;
    }

}
