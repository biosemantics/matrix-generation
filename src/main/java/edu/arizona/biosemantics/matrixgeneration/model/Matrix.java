package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Matrix {

	/* contains the root taxa */
	private List<Taxon> taxa;
	/* all characters */
	private Set<Character> characters;
	
	public Matrix(List<Taxon> taxa, Set<Character> characters) {
		this.taxa = taxa;
		this.characters = characters;
	}

	public List<Taxon> getTaxa() {
		return taxa;
	}

	public Set<Character> getCharacters() {
		return characters;
	}
	
	@Override
	public String toString() {
		return "taxa: " + taxa + "\ncharacters: " + characters;
	}

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
		
}
