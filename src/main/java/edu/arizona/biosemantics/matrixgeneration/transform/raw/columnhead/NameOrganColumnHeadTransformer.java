package edu.arizona.biosemantics.matrixgeneration.transform.raw.columnhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public class NameOrganColumnHeadTransformer implements ColumnHeadTransformer {

	@Override
	public ColumnHead transform(Character character) {
		return new ColumnHead(character.getDisplayName(), character);
	}



}
