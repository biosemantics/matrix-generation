package edu.arizona.biosemantics.matrixgeneration.model.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class CellValue {

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
