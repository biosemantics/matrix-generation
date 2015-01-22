package edu.arizona.biosemantics.matrixgeneration.rawen.columnhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;

public interface ColumnHeadRawenizer {

	public ColumnHead transform(Character character);

}
