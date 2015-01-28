package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;

public class ColumnHead implements Serializable, Comparable<ColumnHead> {

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

	@Override
	public int compareTo(ColumnHead c) {
		return value.compareTo(c.value);
	}
	

}
