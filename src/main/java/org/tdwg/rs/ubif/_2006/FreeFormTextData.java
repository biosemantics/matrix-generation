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
import javax.xml.bind.annotation.XmlType;


/**
 * # Derived from AbstractCharSummaryData to be used for unconstrained text ("Text-fields")
 * 
 * <p>Java class for FreeFormTextData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FreeFormTextData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}AbstractCharSummaryData">
 *       &lt;sequence>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}ModifierRefWithDataGroup"/>
 *         &lt;element name="Content" type="{http://rs.tdwg.org/UBIF/2006/}LongStringL" maxOccurs="unbounded"/>
 *         &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}SpecificExtension" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FreeFormTextData", propOrder = {
    "modifier",
    "content",
    "nextVersion"
})
public class FreeFormTextData
    extends AbstractCharSummaryData
{

    @XmlElement(name = "Modifier")
    protected List<ModifierRefWithData> modifier;
    @XmlElement(name = "Content", required = true)
    protected List<LongStringL> content;
    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;

    /**
     * Gets the value of the modifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ModifierRefWithData }
     * 
     * 
     */
    public List<ModifierRefWithData> getModifier() {
        if (modifier == null) {
            modifier = new ArrayList<ModifierRefWithData>();
        }
        return this.modifier;
    }

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LongStringL }
     * 
     * 
     */
    public List<LongStringL> getContent() {
        if (content == null) {
            content = new ArrayList<LongStringL>();
        }
        return this.content;
    }

    /**
     * Gets the value of the nextVersion property.
     * 
     * @return
     *     possible object is
     *     {@link VersionExtension }
     *     
     */
    public VersionExtension getNextVersion() {
        return nextVersion;
    }

    /**
     * Sets the value of the nextVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionExtension }
     *     
     */
    public void setNextVersion(VersionExtension value) {
        this.nextVersion = value;
    }

}
