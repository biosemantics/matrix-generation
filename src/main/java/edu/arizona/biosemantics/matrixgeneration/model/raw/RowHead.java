package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;
import java.util.LinkedHashSet;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon;

public class RowHead implements Serializable {

	private Taxon source;
	private String value;
	private LinkedHashSet<RowHead> children = new LinkedHashSet<RowHead>();
	@JsonIgnore
	private RowHead parent;
	
	public RowHead(String value, Taxon source) {
		this.value = value;
		this.source = source;
	}
	
	public String getValue() {
		return value;
	}

	public void addChild(RowHead rowHead) {
		children.add(rowHead);
	}

	public Taxon getSource() {
		return source;
	}

	public LinkedHashSet<RowHead> getChildren() {
		return children;
	}

	public int getChildrenCount() {
		return children.size();
	}

	public void setParent(RowHead parent) {
		this.parent = parent;
	}
	
	public RowHead getParent() {
		return parent;
	}

}
