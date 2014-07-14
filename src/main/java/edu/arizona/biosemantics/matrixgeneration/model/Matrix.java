package edu.arizona.biosemantics.matrixgeneration.model;

import java.util.List;
import java.util.Set;

public class Matrix {

	/* contains the root taxa */
	private List<Taxon> taxa;
	/* all characters */
	private Set<Character> character;
	
	public Matrix(List<Taxon> taxa, Set<Character> character) {
		this.taxa = taxa;
		this.character = character;
	}

	public List<Taxon> getTaxa() {
		return taxa;
	}

	public Set<Character> getCharacter() {
		return character;
	}
		
}
