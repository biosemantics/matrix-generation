package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.OntologySubclassInheritanceTransformer;

public class OntologySubclassProvenance extends Provenance {

	private StructureIdentifier sourceStructure;

	public OntologySubclassProvenance(StructureIdentifier sourceStructure) {
		super(OntologySubclassInheritanceTransformer.class);
		this.sourceStructure = sourceStructure;
	}

	public StructureIdentifier getSourceStructure() {
		return sourceStructure;
	}

	public void setSourceStructure(StructureIdentifier sourceStructure) {
		this.sourceStructure = sourceStructure;
	}
	
	@Override
	public String toString() {
		return this.source.getSimpleName() + "; " + sourceStructure.getDisplayName();
	}

}
