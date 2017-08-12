package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.taxonomy.RankData;
import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;

public class Matrix implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/* contains the root taxa */
	private List<Taxon> rootTaxa;
	/* all characters */
	private Map<Character, Character> characters;
	/* <structure identifier, taxon> to taxon's structure map */
	private Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap;
	private Map<Taxon, File> sourceFiles;
	private Map<TaxonIdentification, Taxon> rankDataMap;
	
	public Matrix(List<Taxon> rootTaxa, Map<Character, Character> characters, 
			Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap, Map<Taxon, File> sourceFiles, 
			Map<TaxonIdentification, Taxon> rankTaxaMap) {
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
		return new HashSet<Character>(characters.keySet());
	}

	@JsonIgnore
	public Collection<Value> getValues() {
		List<Value> values = new LinkedList<Value>();
		for(Taxon taxon : getTaxa()) {
			for(Structure structure : taxon.getStructures()) {
				for(Character character : structure.getCharacters()) {
					values.addAll(structure.getCharacterValues(character).getAll());
				}
			}
		}
		return values;
	}
	
	public int getSetCharacterValues() {
		int result = 0;
		for(Taxon taxon : getTaxa()) {
			for(Structure structure : taxon.getStructures()) {
				result += structure.getSetCharactersValuesCount();
			}
		}
		return result;
	}

	public void removeCharacter(Character character) {
		characters.remove(character);
		
		StructureIdentifier structureIdentifier = character.getBearerStructureIdentifier();
		boolean structureInUse = false;
		for(Taxon taxon : this.getTaxa()) {
			if(this.hasStructure(structureIdentifier, taxon)) {
				Structure structure = this.getStructure(structureIdentifier, taxon);
				structure.removeCharacterValues(character);
				if(structure.getSetCharactersValuesCount() == 0) {
					structureIdTaxonStructuresMap.get(structureIdentifier).remove(taxon);
				} else {
					structureInUse = true;
				}
			}
		}
		if(!structureInUse)
			structureIdTaxonStructuresMap.remove(structureIdentifier);
	}
	
	public Character addCharacter(Character character) {
		if(!characters.containsKey(character)) {
			characters.put(character, character);
		}
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
	
	public boolean hasStructure(StructureIdentifier structureIdentifier, Taxon taxon) {
		return this.getStructure(structureIdentifier, taxon) != null;
	}
	
	public void addStructure(Structure structure, Taxon taxon) {
		StructureIdentifier structureIdentifier = new StructureIdentifier(structure);
		if(!structureIdTaxonStructuresMap.containsKey(structureIdentifier)) {
			Map<Taxon, List<Structure>> taxonStructureMap = new HashMap<Taxon, List<Structure>>();
			structureIdTaxonStructuresMap.put(structureIdentifier, taxonStructureMap);
			if(!taxonStructureMap.containsKey(taxon))
				taxonStructureMap.put(taxon, new LinkedList<Structure>());
			taxonStructureMap.get(taxon).add(structure);
		} else {
			Map<Taxon, List<Structure>> taxonStructureMap = structureIdTaxonStructuresMap.get(structureIdentifier);
			if(!taxonStructureMap.containsKey(taxon))
				taxonStructureMap.put(taxon, new LinkedList<Structure>());
			taxonStructureMap.get(taxon).add(structure);
		}
	}
	
	public void removeStructure(Structure structure, Taxon taxon) {
		StructureIdentifier structureIdentifier = new StructureIdentifier(structure);
		if(structureIdTaxonStructuresMap.containsKey(structureIdentifier)) {
			Map<Taxon, List<Structure>> taxonStructureMap = structureIdTaxonStructuresMap.get(structureIdentifier);
			taxonStructureMap.remove(taxon);
			if(taxonStructureMap.isEmpty())
				structureIdTaxonStructuresMap.remove(structureIdentifier);
		}
	}
	
	public File getSourceFile(Taxon taxon) {
		return sourceFiles.get(taxon);
	}
	
	public Taxon getTaxon(RankData rankData) {
		return rankDataMap.get(rankData);
	}
	
	public List<Taxon> getLeafTaxa() {
		List<Taxon> leafTaxa = new LinkedList<Taxon>();
		for(Taxon rootTaxon : rootTaxa) {
			leafTaxa.addAll(getLeafTaxa(rootTaxon));
		}
		return leafTaxa;
	}
	
	public List<Taxon> getLeafTaxa(Taxon parent) {
		List<Taxon> result = new LinkedList<Taxon>();
		if(parent.getChildren().isEmpty()) {
			result.add(parent);
		} else {
			for(Taxon child : parent.getChildren())
				result.addAll(getLeafTaxa(child));
		}
		return result;
	}

	public Set<StructureIdentifier> getStructureIdentifiers() {
		return structureIdTaxonStructuresMap.keySet();
	}

	public void updateStructure(Structure structure, StructureIdentifier oldStructureIdentifier, StructureIdentifier newStructureIdentifier, Taxon taxon) {
		if(structureIdTaxonStructuresMap.containsKey(oldStructureIdentifier)) {
			Map<Taxon, List<Structure>> taxonStructuresMap = structureIdTaxonStructuresMap.get(oldStructureIdentifier);
			if(taxonStructuresMap.containsKey(taxon)) {
				List<Structure> taxonStructures = taxonStructuresMap.get(taxon);
				taxonStructures.remove(structure);
			}
		}
		
		if(!structureIdTaxonStructuresMap.containsKey(newStructureIdentifier)) 
			structureIdTaxonStructuresMap.put(newStructureIdentifier, new HashMap<Taxon, List<Structure>>());
		Map<Taxon, List<Structure>> taxonStructuresMap = structureIdTaxonStructuresMap.get(newStructureIdentifier);
		if(!taxonStructuresMap.containsKey(taxon))  
			taxonStructuresMap.put(taxon,  new LinkedList<Structure>());
		List<Structure> taxonStructures = taxonStructuresMap.get(taxon);
		taxonStructures.add(structure);
	}
	
}
