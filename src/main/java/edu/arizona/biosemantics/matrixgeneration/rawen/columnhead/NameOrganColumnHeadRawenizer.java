package edu.arizona.biosemantics.matrixgeneration.rawen.columnhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public class NameOrganColumnHeadRawenizer implements ColumnHeadRawenizer {

	@Override
	public ColumnHead transform(Character character) {
		if(!character.getStructureIdentifier().getStructureName().equals("whole_organism")) 
			return new ColumnHead(
					character.getName() + " " + character.getConnector() + " " + character.getStructureIdentifier().getDisplayName(), character);
		return new ColumnHead(character.getName(), character);
	}

}
