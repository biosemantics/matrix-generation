package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.arizona.biosemantics.matrixgeneration.model.Provenance;

public class Value implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;

	private String value;
	private String type;
	private String constraint;
	private String charType;
	private String constraintId;
	private String from;
	private String fromInclusive;
	private String fromUnit;
	private String modifier;
	private String geographicalConstraint;
	private String inBrackets;
	private String organConstraint;
	private String otherConstraint;
	private String parallelismConstraint;
	private String taxonConstraint;
	private String to;
	private String toInclusive;
	private String toUnit;
	private String upperRestricted;
	private String unit;
	private String ontologyId;
	private String provenance;
	private String notes;
	private boolean isModifier = false;

	private List<Provenance> generationProvenance = new LinkedList<Provenance>();
	
	public Value(String value, Provenance generationProvenance) {
		this.value = value;
		this.addGenerationProvenance(generationProvenance);
	}
	
	public Value(String value, List<Provenance> generationProvenance) {
		this.value = value;
		this.addGenerationProvenance(generationProvenance);
	}
	
	public String getValue() {
		return value;
	}

	public void setCharType(String charType) {
		this.charType = charType;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public void setConstraintId(String constraintId) {
		this.constraintId = constraintId;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setFromInclusive(String fromInclusive) {
		this.fromInclusive = fromInclusive;
	}

	public void setFromUnit(String fromUnit) {
		this.fromUnit = fromUnit;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public void setGeographicalConstraint(String geographicalConstraint) {
		this.geographicalConstraint = geographicalConstraint;
	}

	public void setInBrackets(String inBrackets) {
		this.inBrackets = inBrackets;
	}

	public void setOrganConstraint(String organConstraint) {
		this.organConstraint = organConstraint;
	}

	public void setOtherConstraint(String otherConstraint) {
		this.otherConstraint = otherConstraint;
	}

	public void setParallelismConstraint(String parallelismConstraint) {
		this.parallelismConstraint = parallelismConstraint;
	}

	public void setTaxonConstraint(String taxonConstraint) {
		this.taxonConstraint = taxonConstraint;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setToInclusive(String toInclusive) {
		this.toInclusive = toInclusive;
	}

	public void setToUnit(String toUnit) {
		this.toUnit = toUnit;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpperRestricted(String upperRestricted) {
		this.upperRestricted = upperRestricted;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setOntologyId(String ontologyId) {
		this.ontologyId = ontologyId;
	}

	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getType() {
		return type;
	}

	public String getConstraint() {
		return constraint;
	}

	public String getCharType() {
		return charType;
	}

	@JsonIgnore
	public String getConstraintId() {
		return constraintId;
	}

	public String getFrom() {
		return from;
	}

	public String getFromInclusive() {
		return fromInclusive;
	}

	public String getFromUnit() {
		return fromUnit;
	}

	public String getModifier() {
		return modifier;
	}

	@JsonIgnore
	public String getGeographicalConstraint() {
		return geographicalConstraint;
	}

	@JsonIgnore
	public String getInBrackets() {
		return inBrackets;
	}

	@JsonIgnore
	public String getOrganConstraint() {
		return organConstraint;
	}

	@JsonIgnore
	public String getOtherConstraint() {
		return otherConstraint;
	}

	@JsonIgnore
	public String getParallelismConstraint() {
		return parallelismConstraint;
	}

	@JsonIgnore
	public String getTaxonConstraint() {
		return taxonConstraint;
	}

	public String getTo() {
		return to;
	}

	public String getToInclusive() {
		return toInclusive;
	}

	public String getToUnit() {
		return toUnit;
	}

	public String getUpperRestricted() {
		return upperRestricted;
	}

	public String getUnit() {
		return unit;
	}

	@JsonIgnore
	public String getOntologyId() {
		return ontologyId;
	}

	@JsonIgnore
	public String getProvenance() {
		return provenance;
	}

	@JsonIgnore
	public String getNotes() {
		return notes;
	}

	@Override
	public Value clone() {
		Value value = new Value(this.value, new ArrayList<Provenance>(this.generationProvenance));
		value.setCharType(this.charType);
		value.setConstraint(this.constraint);
		value.setConstraintId(this.constraintId);
		value.setFrom(this.from);
		value.setFromInclusive(this.fromInclusive);
		value.setFromUnit(this.fromUnit);
		value.setGeographicalConstraint(this.geographicalConstraint);
		value.setInBrackets(this.inBrackets);
		value.setModifier(this.modifier);
		value.setNotes(this.notes);
		value.setOntologyId(this.ontologyId);
		value.setOrganConstraint(this.organConstraint);
		value.setOtherConstraint(this.organConstraint);
		value.setParallelismConstraint(this.parallelismConstraint);
		value.setProvenance(this.provenance);
		value.setTaxonConstraint(this.taxonConstraint);
		value.setTo(this.to);
		value.setToInclusive(this.toInclusive);
		value.setToUnit(this.toUnit);
		value.setType(this.type);
		value.setUnit(this.unit);
		value.setUpperRestricted(this.upperRestricted);
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((charType == null) ? 0 : charType.hashCode());
		result = prime * result
				+ ((constraint == null) ? 0 : constraint.hashCode());
		result = prime * result
				+ ((constraintId == null) ? 0 : constraintId.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result
				+ ((fromInclusive == null) ? 0 : fromInclusive.hashCode());
		result = prime * result
				+ ((fromUnit == null) ? 0 : fromUnit.hashCode());
		result = prime
				* result
				+ ((geographicalConstraint == null) ? 0
						: geographicalConstraint.hashCode());
		result = prime * result
				+ ((inBrackets == null) ? 0 : inBrackets.hashCode());
		result = prime * result
				+ ((modifier == null) ? 0 : modifier.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
				+ ((ontologyId == null) ? 0 : ontologyId.hashCode());
		result = prime * result
				+ ((organConstraint == null) ? 0 : organConstraint.hashCode());
		result = prime * result
				+ ((otherConstraint == null) ? 0 : otherConstraint.hashCode());
		result = prime
				* result
				+ ((parallelismConstraint == null) ? 0 : parallelismConstraint
						.hashCode());
		result = prime * result
				+ ((provenance == null) ? 0 : provenance.hashCode());
		result = prime * result
				+ ((taxonConstraint == null) ? 0 : taxonConstraint.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result
				+ ((toInclusive == null) ? 0 : toInclusive.hashCode());
		result = prime * result + ((toUnit == null) ? 0 : toUnit.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result
				+ ((upperRestricted == null) ? 0 : upperRestricted.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value other = (Value) obj;
		if (charType == null) {
			if (other.charType != null)
				return false;
		} else if (!charType.equals(other.charType))
			return false;
		if (constraint == null) {
			if (other.constraint != null)
				return false;
		} else if (!constraint.equals(other.constraint))
			return false;
		if (constraintId == null) {
			if (other.constraintId != null)
				return false;
		} else if (!constraintId.equals(other.constraintId))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (fromInclusive == null) {
			if (other.fromInclusive != null)
				return false;
		} else if (!fromInclusive.equals(other.fromInclusive))
			return false;
		if (fromUnit == null) {
			if (other.fromUnit != null)
				return false;
		} else if (!fromUnit.equals(other.fromUnit))
			return false;
		if (geographicalConstraint == null) {
			if (other.geographicalConstraint != null)
				return false;
		} else if (!geographicalConstraint.equals(other.geographicalConstraint))
			return false;
		if (inBrackets == null) {
			if (other.inBrackets != null)
				return false;
		} else if (!inBrackets.equals(other.inBrackets))
			return false;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (ontologyId == null) {
			if (other.ontologyId != null)
				return false;
		} else if (!ontologyId.equals(other.ontologyId))
			return false;
		if (organConstraint == null) {
			if (other.organConstraint != null)
				return false;
		} else if (!organConstraint.equals(other.organConstraint))
			return false;
		if (otherConstraint == null) {
			if (other.otherConstraint != null)
				return false;
		} else if (!otherConstraint.equals(other.otherConstraint))
			return false;
		if (parallelismConstraint == null) {
			if (other.parallelismConstraint != null)
				return false;
		} else if (!parallelismConstraint.equals(other.parallelismConstraint))
			return false;
		if (provenance == null) {
			if (other.provenance != null)
				return false;
		} else if (!provenance.equals(other.provenance))
			return false;
		if (taxonConstraint == null) {
			if (other.taxonConstraint != null)
				return false;
		} else if (!taxonConstraint.equals(other.taxonConstraint))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (toInclusive == null) {
			if (other.toInclusive != null)
				return false;
		} else if (!toInclusive.equals(other.toInclusive))
			return false;
		if (toUnit == null) {
			if (other.toUnit != null)
				return false;
		} else if (!toUnit.equals(other.toUnit))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (upperRestricted == null) {
			if (other.upperRestricted != null)
				return false;
		} else if (!upperRestricted.equals(other.upperRestricted))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	public void addGenerationProvenance(Provenance generationProvenance) {
		this.generationProvenance.add(generationProvenance);
	}
	
	public void addGenerationProvenance(List<Provenance> generationProvenance) {
		this.generationProvenance.addAll(generationProvenance);
	}
	
	public List<Provenance> getGenerationProvenance() {
		return generationProvenance;
	}

	public void setIsModifier(boolean isModifier) {
		this.isModifier = isModifier;
	}
	
	public boolean getIsModifier() {
		return isModifier;
	}
	
	public String toString(){
		return this.value;
	}
}

