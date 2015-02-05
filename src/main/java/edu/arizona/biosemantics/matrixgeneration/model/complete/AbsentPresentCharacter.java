package edu.arizona.biosemantics.matrixgeneration.model.complete;

public class AbsentPresentCharacter extends Character {
	
	private StructureIdentifier bearedStructure;
	
	public AbsentPresentCharacter(StructureIdentifier bearedStructure, StructureIdentifier bearerStructure,
			Object provenance) {
		super("presence of " + bearedStructure.getDisplayName(), "at", bearerStructure, provenance);
		this.bearedStructure = bearedStructure;
	}

	public StructureIdentifier getBearedStructureIdentifier() {
		return bearedStructure;
	}

}
