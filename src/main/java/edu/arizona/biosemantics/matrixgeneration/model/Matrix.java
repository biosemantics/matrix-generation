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
	private List<Taxon> taxa;
	/* all characters */
	private Map<Character, Character> characters;
	
	public Matrix(List<Taxon> taxa, Map<Character, Character> characters) {
		this.taxa = taxa;
		this.characters = characters;
	}

	public List<Taxon> getTaxa() {
		return Collections.unmodifiableList(taxa);
	}

	public Set<Character> getCharacters() {
		return Collections.unmodifiableSet(characters.keySet());
	}

	@JsonIgnore
	public Collection<Value> getValues() {
		List<Value> values = new LinkedList<Value>();
		for(Taxon taxon : taxa) {
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
		
}
