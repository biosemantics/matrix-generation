package edu.arizona.biosemantics.matrixgeneration.model.complete;

import java.io.Serializable;


/**
 * a sentence in the description.
 * The statement element in the semanticMarkupOutput XML file
 *
 */
public class Statement implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String text;
	
	public Statement(String id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
