package edu.arizona.biosemantics.matrixgeneration.rawen.columnhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public class NameOrganColumnHeadRawenizer implements ColumnHeadRawenizer {

	@Override
	public ColumnHead transform(Character character) {
		if(!character.getBearerStructureIdentifier().getStructureName().equals("whole_organism")) 
			return new ColumnHead(
					character.getName() + " " + character.getConnector() + " " + character.getBearerStructureIdentifier().getDisplayName(), character);
		return new ColumnHead(character.getName(), character);
	}

}
