package edu.arizona.biosemantics.matrixgeneration.rawen.rowhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public interface RowHeadRawenizer {

	public RowHead transform(Taxon taxon);

}
