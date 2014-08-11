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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MicroscopyImageReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MicroscopyImageReference">
 *   &lt;complexContent>
 *     &lt;extension base="{gme://caCORE.caCORE/3.2/edu.emory.cci.pais}ImageReference">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="resolution" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="zaxisResolution" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="zaxisCoordinate" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MicroscopyImageReference")
@XmlSeeAlso({
    WholeSlideImageReference.class,
    TMAImageReference.class
})
public class MicroscopyImageReference
    extends ImageReference
{

    @XmlAttribute(name = "resolution")
    protected Double resolution;
    @XmlAttribute(name = "zaxisResolution")
    protected Double zaxisResolution;
    @XmlAttribute(name = "zaxisCoordinate")
    protected Double zaxisCoordinate;

    /**
     * Gets the value of the resolution property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getResolution() {
        return resolution;
    }

    /**
     * Sets the value of the resolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setResolution(Double value) {
        this.resolution = value;
    }

    /**
     * Gets the value of the zaxisResolution property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getZaxisResolution() {
        return zaxisResolution;
    }

    /**
     * Sets the value of the zaxisResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setZaxisResolution(Double value) {
        this.zaxisResolution = value;
    }

    /**
     * Gets the value of the zaxisCoordinate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getZaxisCoordinate() {
        return zaxisCoordinate;
    }

    /**
     * Sets the value of the zaxisCoordinate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setZaxisCoordinate(Double value) {
        this.zaxisCoordinate = value;
    }

}
