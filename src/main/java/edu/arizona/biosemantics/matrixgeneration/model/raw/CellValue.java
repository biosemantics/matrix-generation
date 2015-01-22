package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class CellValue implements Serializable {

	private Values source;
	private String text;
	private Set<String> containedValues = new HashSet<String>();
	
	public CellValue(String text, Values source) {
		this.text = text;
		this.containedValues.add(text);
		this.source = source;
	}
	
	public CellValue(String text, Value source) {
		this.text = text;
		this.containedValues.add(text);
		this.source = new Values(source);
	}
	
	public CellValue(String text, Collection<String> containedValues, Values source) {
		this.text = text;
		this.containedValues.addAll(containedValues);
		this.source = source;
	}
	
	public CellValue(String text, Collection<String> containedValues, Value source) {
		this.text = text;
		this.containedValues.addAll(containedValues);
		this.source = new Values(source);
	}

	public Values getSource() {
		return source;
	}

	public String getText() {
		return text;
	}

	public Set<String> getContainedValues() {
		return containedValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellValue other = (CellValue) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
