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

}
