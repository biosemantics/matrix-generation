package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.TaxonomyDescendantInheritanceTransformer;

public class TaxonomyDescendantInheritanceProvenance extends Provenance {
	
	private Taxon sourceTaxon;
	private Structure sourceStructure;
	private Character sourceCharacter;

	public TaxonomyDescendantInheritanceProvenance(Taxon sourceTaxon, Structure sourceStructure, Character sourceCharacter) {
		super(TaxonomyDescendantInheritanceTransformer.class);
		this.sourceTaxon = sourceTaxon;
		this.sourceStructure = sourceStructure;
		this.sourceCharacter = sourceCharacter;
	}

	public Taxon getSourceTaxon() {
		return sourceTaxon;
	}

	public void setSourceTaxon(Taxon sourceTaxon) {
		this.sourceTaxon = sourceTaxon;
	}

	public Structure getSourceStructure() {
		return sourceStructure;
	}

	public void setSourceStructure(Structure sourceStructure) {
		this.sourceStructure = sourceStructure;
	}

	public Character getSourceCharacter() {
		return sourceCharacter;
	}

	public void setSourceCharacter(Character sourceCharacter) {
		this.sourceCharacter = sourceCharacter;
	}
	
	

}
