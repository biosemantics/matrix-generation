package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Matrix {

	/* contains the root taxa */
	private List<Taxon> rootTaxa;
	/* all characters */
	private Map<Character, Character> characters;
	
	public Matrix(List<Taxon> rootTaxa, Map<Character, Character> characters) {
		this.rootTaxa = rootTaxa;
		this.characters = characters;
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


		
}
