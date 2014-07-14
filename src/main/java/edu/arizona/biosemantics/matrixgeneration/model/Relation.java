package edu.arizona.biosemantics.matrixgeneration.model;

public class Relation {

	private boolean negated;
	private String name;
	private Structure from;
	private Structure to;
	
	public boolean isNegated() {
		return negated;
	}
	public void setNegated(boolean negated) {
		this.negated = negated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Structure getFrom() {
		return from;
	}
	public void setFrom(Structure from) {
		this.from = from;
	}
	public Structure getTo() {
		return to;
	}
	public void setTo(Structure to) {
		this.to = to;
	}	

}
