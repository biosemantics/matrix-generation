package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public class NameOrganColumnHeadTransformer implements ColumnHeadTransformer {

	@Override
	public ColumnHead transform(Character character) {
		String structureString = 
				(character.getStructureConstraint() == null || character.getStructureConstraint().isEmpty()) ?
						character.getStructureName() : 
							character.getStructureConstraint() + " " + character.getStructureName();
		return new ColumnHead(character.getName() + " of " + structureString, character);
	}



}
