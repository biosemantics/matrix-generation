package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.arizona.biosemantics.matrixgeneration.log.LogLevel;

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
		if(values.containsKey(character))
			log(LogLevel.WARN, "Structure " + this.getName() + " already contains a value for character " + character.getName() + 
					". Tried to set:\n" + value.toString() + "\nwhere\n" + values.get(character).toString() + "\nwas already set.");
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

	public boolean containsCharacterValue(Character character) {
		return values.containsKey(character);
	}
	
	public LinkedHashMap<Character, Value> getValues() {
		return values;
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
