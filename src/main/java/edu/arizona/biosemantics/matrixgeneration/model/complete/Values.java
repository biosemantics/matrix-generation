package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class Values extends LinkedHashMap<Value, Value> implements Cloneable, Iterable<Value> {

	private static final long serialVersionUID = 1L;
	
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

	public String getCombinedText(String combiner) {
		StringBuilder result = new StringBuilder();
		for(Value value : this.keySet()) {
			result.append(value.getValue() + combiner);
		}
		String r = result.toString();
		return r.substring(0, r.length() - combiner.length());
	}

	@Override
	public Iterator<Value> iterator() {
		return this.keySet().iterator();
	}

	public Collection<? extends Value> getAll() {
		return this.keySet();
	}

	public void removeAll(Values values) {
		for(Value value : values)
			this.remove(value);
	}

}
