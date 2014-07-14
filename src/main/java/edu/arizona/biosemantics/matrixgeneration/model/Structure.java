package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Structure {

	private String name;
	private LinkedHashMap<Character, Value> values = new LinkedHashMap<Character, Value>();

	public boolean containsCharacter(Character character) {
		return values.containsKey(character);
	}

	public Value getValue(Character character) {
		return values.get(character);
	}

	public Value setCharacterValue(Character character, Value value) {
		return values.put(character, value);
	}

	public Value remove(Character character) {
		return values.remove(character);
	}

	public Set<Character> getCharacters() {
		return values.keySet();
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = name;
	}

}
