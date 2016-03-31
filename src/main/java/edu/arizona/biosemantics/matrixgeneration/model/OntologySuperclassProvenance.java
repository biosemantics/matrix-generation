package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.OntologySuperclassInheritanceTransformer;

public class OntologySuperclassProvenance extends Provenance {

	private StructureIdentifier sourceStructure;

	public OntologySuperclassProvenance(StructureIdentifier sourceStructure) {
		super(OntologySuperclassInheritanceTransformer.class);
		this.sourceStructure = sourceStructure;
	}

	public StructureIdentifier getSourceStructure() {
		return sourceStructure;
	}

	public void setSourceStructure(StructureIdentifier sourceStructure) {
		this.sourceStructure = sourceStructure;
	}
	
}

