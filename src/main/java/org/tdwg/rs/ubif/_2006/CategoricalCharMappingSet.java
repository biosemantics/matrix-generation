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
 * Inner class, used only within CategoricalCharacter
 * 
 * <p>Java class for CategoricalCharMappingSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CategoricalCharMappingSet">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rs.tdwg.org/UBIF/2006/}Set">
 *       &lt;sequence>
 *         &lt;element name="Mapping" type="{http://rs.tdwg.org/UBIF/2006/}CategoricalCharMapping" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CategoricalCharMappingSet", propOrder = {
    "mapping"
})
public class CategoricalCharMappingSet
    extends Set
{

    @XmlElement(name = "Mapping", required = true)
    protected List<CategoricalCharMapping> mapping;

    /**
     * Gets the value of the mapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CategoricalCharMapping }
     * 
     * 
     */
    public List<CategoricalCharMapping> getMapping() {
        if (mapping == null) {
            mapping = new ArrayList<CategoricalCharMapping>();
        }
        return this.mapping;
    }

}