package edu.arizona.biosemantics.matrixgeneration.model.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Character;

public class ColumnHead {

	private String value;
	private Character source;
	
	public ColumnHead(String value, Character source) {
		this.value = value;
		this.source = source;
	}
	
	public String getValue() {
		return value;
	}

	public Character getSource() {
		return source;
	}
	
	public boolean hasCharacterSource() {
		return source != null;
	}
	

}
