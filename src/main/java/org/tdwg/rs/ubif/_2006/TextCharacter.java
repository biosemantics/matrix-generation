//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.18 at 10:21:36 AM MST 
//


package org.tdwg.rs.ubif._2006;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * # Derived from AbstractCharacterDefinition to be used in instance documents (non-abstract type). In coded descriptions, these characters only support a Text element for unconstrained text.
 * 
 * For applications not capable to analyze unconstrained natural language, text data can not be used in identification.
 * 
 * <p>Java class for TextCharacter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TextCharacter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}AbstractCharacterDefinition">
 *       &lt;group ref="{http://rs.tdwg.org/UBIF/2006/}SpecificExtension" minOccurs="0"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TextCharacter", propOrder = {
    "nextVersion"
})
public class TextCharacter
    extends AbstractCharacterDefinition
{

    @XmlElement(name = "NextVersion")
    protected VersionExtension nextVersion;

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
