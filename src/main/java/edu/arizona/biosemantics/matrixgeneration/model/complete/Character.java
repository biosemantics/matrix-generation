package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class Character implements Comparable<Character>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> generationProvenance = new LinkedList<String>();
	private String name;
	private String connector;
	private StructureIdentifier bearerStructure;
	
	public Character(String name, String connector, StructureIdentifier bearerStructure, 
			Object generationProvenance) {
		this.name = name;
		this.connector = connector;
		this.bearerStructure = bearerStructure;
		this.addGenerationProvenance(generationProvenance);
	}

	public String getName() {
		return name;
	}
	
	public String getConnector() {
		return connector;
	}

	public StructureIdentifier getBearerStructureIdentifier() {
		return bearerStructure;
	}
	
	public void addGenerationProvenance(Object generationProvenance) {
		if(generationProvenance instanceof String)
			this.generationProvenance.add((String)generationProvenance);
		else
			this.generationProvenance.add(generationProvenance.getClass().getSimpleName());
	}
	
	public List<String> getGenerationProvenance() {
		return generationProvenance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bearerStructure == null) ? 0 : bearerStructure.hashCode());
		result = prime * result
				+ ((connector == null) ? 0 : connector.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (bearerStructure == null) {
			if (other.bearerStructure != null)
				return false;
		} else if (!bearerStructure.equals(other.bearerStructure))
			return false;
		if (connector == null) {
			if (other.connector != null)
				return false;
		} else if (!connector.equals(other.connector))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Character character) {
		return (this.bearerStructure.toString() + " " + this.name + " " + this.connector).compareTo(
				character.bearerStructure.toString() + " " + character.name + " " + character.connector);
	}
	
	public String getDisplayName() {
		return name + " " + connector + " " + bearerStructure.getDisplayName();
	}
	
	public String toString() {
		return this.name + " [" + connector + "] " + this.getBearerStructureIdentifier().toString();
	}


	
}