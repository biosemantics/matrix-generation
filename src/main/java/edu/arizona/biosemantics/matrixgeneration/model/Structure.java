package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.arizona.biosemantics.matrixgeneration.log.LogLevel;

public class Structure implements Cloneable {

	private String name;
	private String constraint;
	private String alterName;
	private String constraintid;
	private String geographicalConstraint;
	private String id;
	private String inBracket;
	private String inBrackets;
	private String parallelismConstraint;
	private String taxonConstraint;
	private String ontologyId;
	private String provenance;
	private String notes;
	private String nameOriginal;
	
	private LinkedHashMap<Character, Value> values = new LinkedHashMap<Character, Value>();

	public boolean containsCharacter(Character character) {
		return values.containsKey(character);
	}

	public Value getCharacterValue(Character character) {
		return values.get(character);
	}

	public Value setCharacterValue(Character character, Value value) {
		if(values.containsKey(character))
			log(LogLevel.WARN, "Structure " + this.getName() + " already contains a value for character " + character.getName() + 
					". Tried to set:\n" + value.toString() + "\nwhere\n" + values.get(character).toString() + "\nwas already set.");
		return values.put(character, value);
	}

	public Value removeCharacterValue(Character character) {
		return values.remove(character);
	}

	public Set<Character> getCharacters() {
		return values.keySet();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public String getAlterName() {
		return alterName;
	}

	public void setAlterName(String alterName) {
		this.alterName = alterName;
	}

	public String getConstraintid() {
		return constraintid;
	}

	public void setConstraintid(String constraintid) {
		this.constraintid = constraintid;
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

	public String getInBracket() {
		return inBracket;
	}

	public void setInBracket(String inBracket) {
		this.inBracket = inBracket;
	}

	public String getInBrackets() {
		return inBrackets;
	}

	public void setInBrackets(String inBrackets) {
		this.inBrackets = inBrackets;
	}

	public String getParallelismConstraint() {
		return parallelismConstraint;
	}

	public void setParallelismConstraint(String parallelismConstraint) {
		this.parallelismConstraint = parallelismConstraint;
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

	public String getNameOriginal() {
		return nameOriginal;
	}

	public void setNameOriginal(String nameOriginal) {
		this.nameOriginal = nameOriginal;
	}

	public boolean containsCharacterValue(Character character) {
		return values.containsKey(character);
	}
	
	public LinkedHashMap<Character, Value> getValues() {
		return values;
	}

	@Override
	public Structure clone() {
		Structure structure = new Structure();
		structure.setName(this.name);
		structure.setAlterName(this.alterName);
		structure.setConstraint(this.constraint);
		structure.setConstraintid(this.constraintid);
		structure.setGeographicalConstraint(this.geographicalConstraint);
		structure.setId(this.id);
		structure.setInBracket(this.inBracket);
		structure.setInBrackets(this.inBrackets);
		structure.setNameOriginal(this.nameOriginal);
		structure.setName(this.name);
		structure.setParallelismConstraint(this.parallelismConstraint);
		structure.setNotes(this.notes);
		structure.setOntologyId(this.ontologyId);
		structure.setProvenance(this.provenance);
		structure.setTaxonConstraint(this.taxonConstraint);
		for(Character character : values.keySet()) {
			structure.setCharacterValue(character, this.getCharacterValue(character).clone());
		}
		return structure;
	}

}
