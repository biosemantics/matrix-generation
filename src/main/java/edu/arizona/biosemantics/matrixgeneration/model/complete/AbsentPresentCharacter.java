package edu.arizona.biosemantics.matrixgeneration.model.complete;

public class AbsentPresentCharacter extends Character {
	
	private StructureIdentifier quantifiedStructure;
	
	public AbsentPresentCharacter(StructureIdentifier quantifiedStructure, StructureIdentifier bearerStructure) {
		super("quantity of " + quantifiedStructure.getDisplayName(), "at", bearerStructure);
		this.quantifiedStructure = quantifiedStructure;
	}

	public StructureIdentifier getQuantifiedStructure() {
		return quantifiedStructure;
	}

}
