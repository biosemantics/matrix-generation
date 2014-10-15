//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.18 at 10:21:36 AM MST 
//


package org.tdwg.rs.ubif._2006;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Scope expressed as taxon name, publication citation, plus specimen or geographical scope.
 * 
 * (Design note: The sequence of repeated scope elements, any of which may be missing may lead to empty elements using this type. However, the alternative design of a repeated choice, which may guarantee at least one scope, was rejected because it is not extensible.)
 * 
 * <p>Java class for ExtendedScopeSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtendedScopeSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}TaxonomicScopeSet">
 *       &lt;sequence>
 *         &lt;element name="Specimen" type="{http://rs.tdwg.org/UBIF/2006/}SpecimenRef" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GeographicArea" type="{http://rs.tdwg.org/UBIF/2006/}GeographicAreaRef" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedScopeSet", propOrder = {
    "specimen",
    "geographicArea"
})
@XmlSeeAlso({
    DescriptionScopeSet.class
})
public class ExtendedScopeSet
    extends TaxonomicScopeSet
{

    @XmlElement(name = "Specimen")
    protected List<SpecimenRef> specimen;
    @XmlElement(name = "GeographicArea")
    protected List<GeographicAreaRef> geographicArea;

    /**
     * Gets the value of the specimen property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specimen property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecimen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpecimenRef }
     * 
     * 
     */
    public List<SpecimenRef> getSpecimen() {
        if (specimen == null) {
            specimen = new ArrayList<SpecimenRef>();
        }
        return this.specimen;
    }

    /**
     * Gets the value of the geographicArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geographicArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeographicArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GeographicAreaRef }
     * 
     * 
     */
    public List<GeographicAreaRef> getGeographicArea() {
        if (geographicArea == null) {
            geographicArea = new ArrayList<GeographicAreaRef>();
        }
        return this.geographicArea;
    }

}
