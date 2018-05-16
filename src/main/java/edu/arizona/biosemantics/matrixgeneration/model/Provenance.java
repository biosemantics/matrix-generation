package edu.arizona.biosemantics.matrixgeneration.model;

import java.io.Serializable;
//import com.google.gwt.user.client.rpc.IsSerializable;

public class Provenance implements Serializable/*, IsSerializable*/ {

	protected Class<?> source;
	
	public Provenance(Class<?> source) {
		this.source = source;
	}

	public Class<?> getSource() {
		return source;
	}

	public void setSource(Class<?> source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return this.source.getSimpleName();
	}
	
}
