package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public interface RowHeadTransformer {

	public RowHead transform(Taxon taxon);

}
