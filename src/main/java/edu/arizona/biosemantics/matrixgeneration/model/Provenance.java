package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.Serializable;

public class Provenance implements Serializable {

	private Class<?> source;
	
	public Provenance(Class<?> source) {
		this.source = source;
	}

	public Class<?> getSource() {
		return source;
	}

	public void setSource(Class<?> source) {
		this.source = source;
	}
	
	
	
}
