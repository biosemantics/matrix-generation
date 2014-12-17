package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public class CellValue implements Serializable {

	private Value source;
	private String value;
	
	public CellValue(String value, Value source) {
		this.value = value;
		this.source = source;
	}

	public String getValue() {
		return value;
	}

	public Value getSource() {
		return source;
	}

	
}
