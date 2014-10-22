package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.Serializable;

public class Relation implements Cloneable, Serializable {

	private boolean negated;
	private String name;
	private String fromId;
	private Structure from;
	private String toId;
	private Structure to;
	private String alterName;
	private String geographicalConstraint;
	private String id;
	private String inBrackets;
	private String modifier;
	private String organConstraint;
	private String parallelismConstriant;
	private String taxonConstraint;
	private String ontologyId;
	private String provenance;
	private String notes;
	
	public boolean isNegated() {
		return negated;
	}
	public void setNegated(boolean negated) {
		this.negated = negated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Structure getFrom() {
		return from;
	}
	public void setFrom(Structure from) {
		this.from = from;
	}
	public Structure getTo() {
		return to;
	}
	public void setTo(Structure to) {
		this.to = to;
	}
	public String getAlterName() {
		return alterName;
	}
	public void setAlterName(String alterName) {
		this.alterName = alterName;
	}
	public String getGeographicalConstraint() {
		return geographicalConstraint;
	}
	public void setGeographicalConstraint(String geographicalConstraint) {
		this.geographicalConstraint = geographicalConstraint;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInBrackets() {
		return inBrackets;
	}
	public void setInBrackets(String inBrackets) {
		this.inBrackets = inBrackets;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getOrganConstraint() {
		return organConstraint;
	}
	public void setOrganConstraint(String organConstraint) {
		this.organConstraint = organConstraint;
	}
	public String getParallelismConstriant() {
		return parallelismConstriant;
	}
	public void setParallelismConstriant(String parallelismConstriant) {
		this.parallelismConstriant = parallelismConstriant;
	}
	public String getTaxonConstraint() {
		return taxonConstraint;
	}
	public void setTaxonConstraint(String taxonConstraint) {
		this.taxonConstraint = taxonConstraint;
	}
	public String getOntologyId() {
		return ontologyId;
	}
	public void setOntologyId(String ontologyId) {
		this.ontologyId = ontologyId;
	}
	public String getProvenance() {
		return provenance;
	}
	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public Relation clone() {
		Relation relation = new Relation();
		relation.setAlterName(this.alterName);
		relation.setFrom(this.from);
		relation.setGeographicalConstraint(this.geographicalConstraint);
		relation.setId(this.id);
		relation.setInBrackets(this.inBrackets);
		relation.setModifier(this.modifier);
		relation.setName(this.name);
		relation.setNegated(this.negated);
		relation.setNotes(this.notes);
		relation.setOntologyId(this.ontologyId);
		relation.setOrganConstraint(this.organConstraint);
		relation.setParallelismConstriant(this.parallelismConstriant);
		relation.setProvenance(this.provenance);
		relation.setTaxonConstraint(this.taxonConstraint);
		relation.setTo(this.to);
		relation.setFromId(this.fromId);
		relation.setToId(this.toId);
		return relation;
	}
}
