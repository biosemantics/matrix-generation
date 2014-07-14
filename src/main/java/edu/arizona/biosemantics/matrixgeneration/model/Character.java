package edu.arizona.biosemantics.matrixgeneration.model;

public class Character {

	private String name;
	
	public Character(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}	
	
	@Override
	public boolean equals(Object object) {
		if(object != null && object instanceof Character) {
			Character character = (Character)object;
			return this.name.equals(character.name);
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
