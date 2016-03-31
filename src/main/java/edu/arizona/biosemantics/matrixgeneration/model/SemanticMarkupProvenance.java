package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.io.complete.in.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;

public class SemanticMarkupProvenance extends Provenance {

	private Taxon taxon;
	private Character character;

	public SemanticMarkupProvenance(Taxon taxon, Character character) {
		super(SemanticMarkupReader.class);
		this.taxon = taxon;
		this.character = character;
	}

	public Taxon getTaxon() {
		return taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}
	
	@Override
	public String toString() {
		return this.source.getSimpleName() + "; " + taxon.getTaxonIdentification().getDisplayName() + "; " + character.getDisplayName();
	}

}
