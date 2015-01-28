package edu.arizona.biosemantics.matrixgeneration.rawen.columnhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public class NameOrganColumnHeadRawenizer implements ColumnHeadRawenizer {

	@Override
	public ColumnHead transform(Character character) {
		String name = character.getName();
		if(!character.getBearerStructureIdentifier().getStructureName().equals("whole_organism")) 
			name = character.getName() + " " + character.getConnector() + " " + character.getBearerStructureIdentifier().getDisplayName();
		return new ColumnHead(name, character, this);
	}

}
