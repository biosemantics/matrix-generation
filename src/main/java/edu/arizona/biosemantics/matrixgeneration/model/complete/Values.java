package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Values extends HashMap<Value, Value> implements Cloneable, Iterable<Value> {
	
	public Values() {
		super();
	}
	
	public Values(Value source) {
		super();
		this.add(source);
	}
	
	public void add(Value value) {
		if(this.containsKey(value)) {
			Value storedValue = this.get(value);
			storedValue.addGenerationProvenance(value.getGenerationProvenance());
		} else {
			super.put(value, value);
		}
	}
	
	public void addAll(Values values) {
		for(Value value : values.keySet()) {
			this.add(value);
		}
	}

	@Override
	public Values clone() {
		Values values = new Values();
		for(Value value : this.keySet()) {
			values.add(value.clone());
		}
		return values;
	}
	
	public Value getRepresentative() {
		return this.keySet().iterator().next();
	}

	public String getCombinedText() {
		StringBuilder result = new StringBuilder();
		for(Value value : this.keySet()) {
			result.append(value.getValue() + " | ");
		}
		String r = result.toString();
		return r.substring(0, r.length() - 3);
	}

	@Override
	public Iterator<Value> iterator() {
		return this.keySet().iterator();
	}

	public Collection<? extends Value> getAll() {
		return this.keySet();
	}

}
