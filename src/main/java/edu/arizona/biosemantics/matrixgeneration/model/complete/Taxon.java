package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;

public class Taxon implements Serializable {
	
	private TaxonIdentification taxonIdentification;
	private LinkedHashSet<Taxon> children = new LinkedHashSet<Taxon>();
	
	private Structure wholeOrganism;
	private LinkedHashMap<String, Set<Structure>> structures = new LinkedHashMap<String, Set<Structure>>();
	private LinkedHashMap<Relation, Relation> relations = new LinkedHashMap<Relation, Relation>();
	
	private String description;
	private File sourceFile;
	private Taxon parent;
	
	public Taxon() { }
	
	public LinkedHashSet<Taxon> getChildren() {
		return new LinkedHashSet<Taxon>(children);
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
		if(!structures.containsKey(structure.getName()))
			structures.put(structure.getName(), new HashSet<Structure>());
		if(!structure.getName().equals("whole_organism") || (structure.getName().equals("whole_organism") && structures.get(structure.getName()).isEmpty()))
			structures.get(structure.getName()).add(structure);
	}

	public Collection<Structure> getStructures() {
		Collection<Structure> result = new LinkedList<Structure>();
		for(Set<Structure> nameStructures : structures.values()) 
			result.addAll(nameStructures);
		return result;
	}

	public Set<Structure> getStructures(String name) {
		return structures.get(name);
	}

	public void addChild(Taxon taxon) {
		children.add(taxon);
		taxon.setParent(this);
	}

	private void setParent(Taxon parent) {
		this.parent = parent;
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

	public Structure getWholeOrganism() {
		return wholeOrganism;
	}
	
	public Collection<Structure> getStructuresWithoutWholeOrganism() {
		Collection<Structure> result = new LinkedList<Structure>();
		for(String name : structures.keySet()) 
			if(!name.equals(wholeOrganism.getName()))
				result.addAll(structures.get(name));
		return result;
	}

	public Taxon getParent() {
		return parent;
	}
	
	public String toString() {
		return this.taxonIdentification.getRankData().getLast().getRank().name() + ": " + 
				this.taxonIdentification.getRankData().getLast().getName();
	}

	public void setWholeOrganism(Structure wholeOrganism) {
		this.wholeOrganism = wholeOrganism;
		this.structures.put(wholeOrganism.getName(), new HashSet<Structure>());
		this.structures.get(wholeOrganism.getName()).add(wholeOrganism);
	}

	public File getSourceFile() {
		return sourceFile;
	}
	
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}


}
