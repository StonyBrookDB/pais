//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.05 at 01:52:36 PM EST 
//


package edu.emory.cci.pais.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Ellipse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ellipse">
 *   &lt;complexContent>
 *     &lt;extension base="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}GeometricShape">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="centerX" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="centerY" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="radiusX" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="radiusY" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ellipse")
public class Ellipse
    extends GeometricShape
{

    @XmlAttribute(name = "centerX")
    protected Double centerX;
    @XmlAttribute(name = "centerY")
    protected Double centerY;
    @XmlAttribute(name = "radiusX")
    protected Double radiusX;
    @XmlAttribute(name = "radiusY")
    protected Double radiusY;

    /**
     * Gets the value of the centerX property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getCenterX() {
        return centerX;
    }

    /**
     * Sets the value of the centerX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setCenterX(Double value) {
        this.centerX = value;
    }

    /**
     * Gets the value of the centerY property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getCenterY() {
        return centerY;
    }

    /**
     * Sets the value of the centerY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setCenterY(Double value) {
        this.centerY = value;
    }

    /**
     * Gets the value of the radiusX property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRadiusX() {
        return radiusX;
    }

    /**
     * Sets the value of the radiusX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRadiusX(Double value) {
        this.radiusX = value;
    }

    /**
     * Gets the value of the radiusY property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRadiusY() {
        return radiusY;
    }

    /**
     * Sets the value of the radiusY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRadiusY(Double value) {
        this.radiusY = value;
    }

}
