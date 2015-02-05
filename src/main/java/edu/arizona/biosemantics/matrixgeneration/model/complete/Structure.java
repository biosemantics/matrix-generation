package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;

public class Structure implements Cloneable, Serializable {

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
	
	private LinkedHashMap<Character, Values> values = new LinkedHashMap<Character, Values>();

	public Structure() { 	}
	
	public Structure(String name) {
		this.name = name == null ? null : name.trim().isEmpty() ? null : name.trim();
	}
	
	public Structure(String name, String ontologyId) {
		this.name = name == null ? null : name.trim().isEmpty() ? null : name.trim();
		this.ontologyId = ontologyId == null ? null : ontologyId.trim().isEmpty() ? null : ontologyId.trim();
	}
	
	public Structure(String name, String constraint, String ontologyId) {
		this.name = name == null ? null : name.trim().isEmpty() ? null : name.trim();
		this.constraint = constraint == null ? null : constraint.trim().isEmpty() ? null : constraint.trim();
		this.ontologyId = ontologyId == null ? null : ontologyId.trim().isEmpty() ? null : ontologyId.trim();
	}

	public boolean containsCharacter(Character character) {
		return values.containsKey(character);
	}

	public Values getCharacterValues(Character character) {
		return values.get(character);
	}
	
	public int getSetCharactersValuesCount() {
		return values.size();
	}
	
	public void addCharacterValue(Character character, Value value) {
		if(values.containsKey(character))
			log(LogLevel.WARN, "Structure " + this.getName() + " already contains a value for character " + character.getName() + 
					". Set:\n" + value.toString() + "\nwhere\n" + values.get(character).toString() + "\nwas already set.");
		if(!values.containsKey(character))
			values.put(character, new Values());
		values.get(character).add(value);
	}

	public void setCharacterValues(Character character, Values values) {
		this.values.put(character, values);
	}

	public Values removeCharacterValues(Character character) {
		return values.remove(character);
	}

	public LinkedHashSet<Character> getCharacters() {
		return new LinkedHashSet<Character>(values.keySet());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim().isEmpty() ? null : name.trim();
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint == null ? null : constraint.trim().isEmpty() ? null : constraint.trim();
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
		this.ontologyId = ontologyId == null ? null : ontologyId.trim().isEmpty() ? null : ontologyId.trim();
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
	
	public LinkedHashMap<Character, Values> getValues() {
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
			structure.setCharacterValues(character, this.getCharacterValues(character).clone());
		}
		return structure;
	}

	public String getDisplayName() {
		String result = "";
		if(constraint != null)
			result += constraint + " ";
		if(name != null)
			result += name;
		return result.trim();
	}
	
	public String toString() {
		return new StructureIdentifier(this).toString();
	}

}
