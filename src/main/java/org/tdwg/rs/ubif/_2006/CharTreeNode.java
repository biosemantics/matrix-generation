//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.18 at 10:21:36 AM MST 
//


package org.tdwg.rs.ubif._2006;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * Inner nodes (or terminal nodes if no characters follow).
 * 
 * <p>Java class for CharTree_Node complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CharTree_Node">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}CharTree_AbstractNode">
 *       &lt;sequence>
 *         &lt;element name="DescriptiveConcept" type="{http://rs.tdwg.org/UBIF/2006/}DescriptiveConceptRef" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://rs.tdwg.org/UBIF/2006/}LocalInstanceID"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CharTree_Node", propOrder = {
    "descriptiveConcept"
})
public class CharTreeNode
    extends CharTreeAbstractNode
{

    @XmlElement(name = "DescriptiveConcept")
    protected DescriptiveConceptRef descriptiveConcept;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;
    @XmlAttribute(name = "debuglabel")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String debuglabel;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the descriptiveConcept property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptiveConceptRef }
     *     
     */
    public DescriptiveConceptRef getDescriptiveConcept() {
        return descriptiveConcept;
    }

    /**
     * Sets the value of the descriptiveConcept property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptiveConceptRef }
     *     
     */
    public void setDescriptiveConcept(DescriptiveConceptRef value) {
        this.descriptiveConcept = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the debuglabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebuglabel() {
        return debuglabel;
    }

    /**
     * Sets the value of the debuglabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebuglabel(String value) {
        this.debuglabel = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
