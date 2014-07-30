package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Taxon {
	
	public enum Rank {
		LIFE(0), SUPERDOMAIN(1), DOMAIN(1), SUBDOMAIN(2), SUPERKINGDOM(3), KINGDOM(
				4), SUBKINGDOM(5), SUPERPHYLUM(6), PHYLUM(7), SUBPHYLUM(8), SUPERCLASS(
				9), CLASS(10), SUBCLASS(11), SUPERORDER(12), ORDER(13), SUBORDER(
				14), SUPERFAMILY(15), FAMILY(16), SUBFAMILY(17), SUPERTRIBE(18), TRIBE(
				19), SUBTRIBE(20), SUPERGENUS(21), GENUS(22), SUBGENUS(23), SUPERSECTION(
				24), SECTION(25), SUBSECTION(26), SUPERSERIES(27), SERIES(28), SUBSERIES(
				29), SUPERSPECIES(30), SPECIES(31), SUBSPECIES(32), SUPERVARIETY(
				33), VARIETY(34), SUBVARIETAS(35), SUPERFORMA(36), FORMA(37), SUBFORMA(
				38), SUPERGROUP(39), GROUP(40), SUBGROUP(41), UNRANKED(42);

		private int id;

		Rank() {
		}

		Rank(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public static boolean isValidParentChild(Rank parent, Rank child) {
			int parentRankId = parent == null ? -1 : parent.getId();
			int childRankId = child == null ? -1 : child.getId();
			// special case group allows children of itself as it is the lowest
			// rank
			if (parent != null && child != null && parent.equals(UNRANKED)
					&& child.equals(UNRANKED))
				return true;
			return parentRankId < childRankId;
		}

		public static boolean equalOrBelowGenus(Rank rank) {
			return rank.getId() >= GENUS.getId();
		}

		public static boolean aboveGenus(Rank rank) {
			return rank.getId() < GENUS.getId();
		}
	}

	private String author;
	private String year;
	
	private TaxonName taxonName;
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

	public TaxonName getTaxonName() {
		return taxonName;
	}
	
	public void setTaxonName(TaxonName taxonName) {
		this.taxonName = taxonName;
	}

}
