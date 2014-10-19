package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;

public class Taxon implements Serializable {

	private String author;
	private String year;
	
	private TaxonIdentification taxonIdentification;
	private LinkedHashSet<Taxon> children = new LinkedHashSet<Taxon>();
	
	private LinkedHashMap<String, Structure> structures = new LinkedHashMap<String, Structure>();
	private LinkedHashMap<Relation, Relation> relations = new LinkedHashMap<Relation, Relation>();
	
	private String description;
	private File sourceFile;
	
	public LinkedHashSet<Taxon> getChildren() {
		return children;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean containsStructure(String name) {
		return structures.containsKey(name);
	}

	public void addStructure(Structure structure) {
		if(structures.containsKey(structure.getName())) {
			Structure targetStructure = structures.get(structure.getName());
			for(Character character : structure.getCharacters()) 
				targetStructure.setCharacterValue(character, structure.getCharacterValue(character));
		} else
			structures.put(structure.getName(), structure);
	}

	public Collection<Structure> getStructures() {
		return structures.values();
	}

	public Structure getStructure(String name) {
		return structures.get(name);
	}

	public void addChild(Taxon taxon) {
		children.add(taxon);
	}

	public void addRelation(Relation relation) {
		relations.put(relation, relation);
	}
	
	public Collection<Relation> getRelations() {
		return relations.values();
	}
	
	public Relation getRelation(Relation relation) {
		return relations.get(relation);
	}
	
	public boolean containsRelation(Relation relation) {
		return this.relations.containsKey(relation);
	}

	public TaxonIdentification getTaxonIdentification() {
		return taxonIdentification;
	}
	
	public void setTaxonIdentification(TaxonIdentification taxonIdentification) {
		this.taxonIdentification = taxonIdentification;
	}

}
