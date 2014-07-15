package edu.arizona.biosemantics.matrixgeneration.model;

public class Value implements Cloneable {

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
	
	public Value(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
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

	public String getGeographicalConstraint() {
		return geographicalConstraint;
	}

	public String getInBrackets() {
		return inBrackets;
	}

	public String getOrganConstraint() {
		return organConstraint;
	}

	public String getOtherConstraint() {
		return otherConstraint;
	}

	public String getParallelismConstraint() {
		return parallelismConstraint;
	}

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

	public String getOntologyId() {
		return ontologyId;
	}

	public String getProvenance() {
		return provenance;
	}

	public String getNotes() {
		return notes;
	}
	
	@Override
	public Value clone() {
		Value value = new Value(this.value);
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
		
}
