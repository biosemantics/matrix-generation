package edu.arizona.biosemantics.matrixgeneration.model.complete;

public class AbsentPresentCharacter extends Character {
	
	private static final long serialVersionUID = 1L;
	
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
