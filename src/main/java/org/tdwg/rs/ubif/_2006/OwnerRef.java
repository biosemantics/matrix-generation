//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.18 at 10:21:36 AM MST 
//


package org.tdwg.rs.ubif._2006;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Restriction of RichAgentRef to Owner roles only (contribution attributes prohibited).
 * 
 * <p>Java class for OwnerRef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OwnerRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://rs.tdwg.org/UBIF/2006/}RichAgentRef">
 *       &lt;attribute name="role" use="required" type="{http://rs.tdwg.org/UBIF/2006/}AgentOwnerRoleEnum" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OwnerRef")
public class OwnerRef
    extends RichAgentRef
{


}