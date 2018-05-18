package edu.arizona.biosemantics.matrixgeneration.rawen.columnhead;

import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public class NameOrganColumnHeadRawenizer implements ColumnHeadRawenizer {

	@Override
	public ColumnHead transform(Character character) {
		String name = character.getName();
		//if(!character.getBearerStructureIdentifier().getStructureName().equals("whole_organism")){ //whole_organism hide/show later in etcsite matrixGenerationService
			name = character.getName() + " " + character.getConnector() + " " + character.getBearerStructureIdentifier().getDisplayName();
		//}else
		//	name = character.getName();		
		return new ColumnHead(name, character, this);
	}

}
