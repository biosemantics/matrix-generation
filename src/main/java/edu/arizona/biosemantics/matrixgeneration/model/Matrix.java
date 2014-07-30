package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.arizona.biosemantics.matrixgeneration.model.Character.StructureIdentifier;

public class Matrix {

	/* contains the root taxa */
	private List<Taxon> rootTaxa;
	/* all characters */
	private Map<Character, Character> characters;
	/* <structure identifier, taxon> to taxon's structure map */
	private Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap;
	private Map<Taxon, File> sourceFiles;
	private Map<RankData, Taxon> rankDataMap;
	
	public Matrix(List<Taxon> rootTaxa, Map<Character, Character> characters, 
			Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap, Map<Taxon, File> sourceFiles, 
			Map<RankData, Taxon> rankTaxaMap) {
		this.rootTaxa = rootTaxa;
		this.characters = characters;
		this.structureIdTaxonStructuresMap = structureIdTaxonStructuresMap;
		this.sourceFiles = sourceFiles;
		this.rankDataMap = rankTaxaMap;
	}

	public List<Taxon> getRootTaxa() {
		return Collections.unmodifiableList(rootTaxa);
	}
	
	public List<Taxon> getTaxa() {
		List<Taxon> result = new LinkedList<Taxon>();
		for(Taxon taxon : rootTaxa) {
			addDescendants(taxon, result);
		}
		return result;
	}

	private void addDescendants(Taxon taxon, List<Taxon> result) {
		result.add(taxon);
		for(Taxon child : taxon.getChildren()) 
			addDescendants(child, result);
		}

	public Set<Character> getCharacters() {
		return Collections.unmodifiableSet(characters.keySet());
	}

	@JsonIgnore
	public Collection<Value> getValues() {
		List<Value> values = new LinkedList<Value>();
		for(Taxon taxon : getTaxa()) {
			for(Structure structure : taxon.getStructures()) {
				for(Character character : structure.getCharacters()) {
					values.add(structure.getCharacterValue(character));
				}
			}
		}
		return values;
	}

	public void removeCharacter(Character character) {
		characters.remove(character);
	}
	
	public Character addCharacter(Character character) {
		if(!characters.containsKey(character))
			characters.put(character, character);
		character = characters.get(character);
		return character;
	}

	public int getRootTaxaCount() {
		return rootTaxa.size();
	}
	
	public int getTaxaCount() {
		int count = 0;
		for(Taxon taxon : rootTaxa) {
			count += getDescendantsCount(taxon) + 1;
		}
		return count;
	}
	
	private int getDescendantsCount(Taxon taxon) {
		int count = 0;
		for(Taxon child : taxon.getChildren()) {
			count += getDescendantsCount(child);
		}
		return count + taxon.getChildren().size();
	}
	
	public int getCharactersCount() {
		return characters.size();
	}

	/*
	 * For now only return first structure. Unclear how to deal with multiple structures of same "name + constraint" (id)
	 * in terms of what to return in case a structure description of the specific taxon is needed
	 */
	public Structure getStructure(StructureIdentifier structureIdentifier, Taxon taxon) {
		if(structureIdTaxonStructuresMap.containsKey(structureIdentifier))
			if(structureIdTaxonStructuresMap.get(structureIdentifier).containsKey(taxon))
				if(!structureIdTaxonStructuresMap.get(structureIdentifier).get(taxon).isEmpty())
					return structureIdTaxonStructuresMap.get(structureIdentifier).get(taxon).get(0);
		return null;
	}
	
	public File getSourceFile(Taxon taxon) {
		return sourceFiles.get(taxon);
	}
	
	public Taxon getTaxon(RankData rankData) {
		return rankDataMap.get(rankData);
	}
	
	/* Unclear which is needed to be created when evaluating part_of relations, dummy implementations for now */
	/**
	 * Returns parent structure specific to taxon
	 * @param structre
	 * @return
	 */
	public Structure getParent(Structure structure) {
		return null;
	}
	/**
	 * Returns child structures specific to taxon
	 * @param structure
	 * @return
	 */
	public List<Structure> getChildren(Structure structure) {
		return new LinkedList<Structure>();
	}
	/**
	 * Returns parent structure of *merged by identifier* structure hierarchy
	 * @param structureIdentifier
	 * @return
	 */
	public StructureIdentifier getParent(StructureIdentifier structureIdentifier) {
		return null;
	}
	/**
	 * Returns child structures of *merged by identifier* structure hierarchy
	 * @param StructureIdentifier
	 * @return
	 */
	public List<StructureIdentifier> getChildren(StructureIdentifier StructureIdentifier) {
		return new LinkedList<StructureIdentifier>();
	}
		
}
