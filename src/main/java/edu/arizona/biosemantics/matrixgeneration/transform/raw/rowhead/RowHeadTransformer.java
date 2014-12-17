package edu.arizona.biosemantics.matrixgeneration.transform.raw.rowhead;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public interface RowHeadTransformer {

	public RowHead transform(Taxon taxon);

}
