package edu.arizona.biosemantics.matrixgeneration.log;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * StringifyInjectionAspect specifies IPrintables and adds them a toString
 * method
 * 
 * @author rodenhausen
 */
public aspect StringifyInjectionAspect {

	@XmlTransient
	@Transient
	@JsonIgnore
	private IPrintable IPrintable.thisObject;

	/**
	 * toString method is defined for IPrintables
	 */
	public String IPrintable.toString() {
		return ObjectStringifier.getInstance().stringify(thisObject);
	}

	/**
	 * Pointcut specification for object construction of an IPrintable
	 * 
	 * @param object
	 */
	pointcut objectConstruction(IPrintable object) :
		initialization(IPrintable+.new(..)) && this(object);

	/**
	 * Advice for after object construction lets an IPrintable have a reference
	 * of his own for use in toString()
	 * 
	 * @param object
	 */
	after(IPrintable object) : objectConstruction(object) {
		object.thisObject = object;
	}

	/**
	 * IPrintables are specified
	 */

	declare parents : edu.arizona.biosemantics.matrixgeneration.* implements IPrintable;
	declare parents : edu.arizona.biosemantics.matrixgeneration.io..* implements IPrintable;
	declare parents : edu.arizona.biosemantics.matrixgeneration.model..* implements IPrintable;
	declare parents : edu.arizona.biosemantics.matrixgeneration.transform..* implements IPrintable;

}