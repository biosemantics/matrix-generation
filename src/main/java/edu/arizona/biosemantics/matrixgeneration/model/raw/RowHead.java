package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.util.LinkedHashSet;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon;

public class RowHead {

	private Taxon source;
	private String value;
	private LinkedHashSet<RowHead> children = new LinkedHashSet<RowHead>();
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
