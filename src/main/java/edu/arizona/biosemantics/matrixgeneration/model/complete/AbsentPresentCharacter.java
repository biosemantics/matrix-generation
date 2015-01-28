package edu.arizona.biosemantics.matrixgeneration.model.complete;

public class AbsentPresentCharacter extends Character {
	
	private StructureIdentifier quantifiedStructure;
	
	public AbsentPresentCharacter(StructureIdentifier bearedStructure, StructureIdentifier bearerStructure) {
		super("presence of " + bearedStructure.getDisplayName(), "at", bearerStructure);
		this.quantifiedStructure = bearedStructure;
	}

	public StructureIdentifier getQuantifiedStructure() {
		return quantifiedStructure;
	}

}
