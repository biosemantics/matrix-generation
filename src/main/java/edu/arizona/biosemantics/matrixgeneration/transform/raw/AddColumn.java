package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Column;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public interface AddColumn {

	Column getColumn(Matrix matrix, List<RowHead> rowHeads);
	
}
