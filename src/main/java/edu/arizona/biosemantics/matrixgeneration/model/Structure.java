package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Structure implements Cloneable {

	private String name;
	private LinkedHashMap<Character, Value> values = new LinkedHashMap<Character, Value>();

	public boolean containsCharacter(Character character) {
		return values.containsKey(character);
	}

	public Value getCharacterValue(Character character) {
		return values.get(character);
	}

	public Value setCharacterValue(Character character, Value value) {
		return values.put(character, value);
	}

	public Value removeCharacterValue(Character character) {
		return values.remove(character);
	}

	public Set<Character> getCharacters() {
		return values.keySet();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + ": " + values;
	}

	public boolean containsCharacterValue(Character character) {
		return values.containsKey(character);
	}
	
	@Override
	public Structure clone() {
		Structure structure = new Structure();
		structure.setName(this.name);
		for(Character character : values.keySet()) {
			structure.setCharacterValue(character, this.getCharacterValue(character).clone());
		}
		return structure;
	}

}
