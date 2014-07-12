package edu.arizona.biosemantics.matrixgeneration.model;

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

	public enum Level {
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

		Level() {
		}

		Level(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public static boolean isValidParentChild(Level parent, Level child) {
			int parentLevelId = parent == null ? -1 : parent.getId();
			int childLevelId = child == null ? -1 : child.getId();
			// special case group allows children of itself as it is the lowest
			// rank
			if (parent != null && child != null && parent.equals(UNRANKED)
					&& child.equals(UNRANKED))
				return true;
			return parentLevelId < childLevelId;
		}

		public static boolean equalOrBelowGenus(Level level) {
			return level.getId() >= GENUS.getId();
		}

		public static boolean aboveGenus(Level level) {
			return level.getId() < GENUS.getId();
		}
	}

	private String name;
	private String author;
	private String year;
	
	public Level level;
	public LinkedHashSet<Taxon> children = new LinkedHashSet<Taxon>();
	private LinkedHashMap<String, Structure> structures = new LinkedHashMap<String, Structure>();
	
	private String description;
	
	public LinkedHashSet<Taxon> getChildren() {
		return children;
	}

	public void setChildren(LinkedHashSet<Taxon> children) {
		this.children = children;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
		structures.put(structure.getName(), structure);
	}

	public Collection<Structure> getStructures() {
		return structures.values();
	}

	public Structure getStructure(String name) {
		return structures.get(name);
	}

}
