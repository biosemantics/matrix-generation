package edu.arizona.biosemantics.matrixgeneration.model.complete;

public class AbsentPresentCharacter extends Character {
	
	private StructureIdentifier quantifiedStructure;
	
	public AbsentPresentCharacter(StructureIdentifier bearedStructure, StructureIdentifier bearerStructure,
			Object provenance) {
		super("presence of " + bearedStructure.getDisplayName(), "at", bearerStructure, provenance);
		this.quantifiedStructure = bearedStructure;
	}

	public StructureIdentifier getQuantifiedStructure() {
		return quantifiedStructure;
	}

}
