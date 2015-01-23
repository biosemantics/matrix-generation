package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.util.HashSet;

public class Values extends HashSet<Value> implements Cloneable {
	
	public Values() {
		super();
	}
	
	public Values(Value source) {
		super();
		this.add(source);
	}

	@Override
	public Values clone() {
		Values values = new Values();
		for(Value value : this) {
			values.add(value.clone());
		}
		return values;
	}
	
	public Value getRepresentative() {
		return this.iterator().next();
	}

	public String getCombinedText() {
		StringBuilder result = new StringBuilder();
		for(Value value : this) {
			result.append(value.getValue() + " | ");
		}
		String r = result.toString();
		return r.substring(0, r.length() - 3);
	}

}
