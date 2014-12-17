package edu.arizona.biosemantics.matrixgeneration.transform.raw.addcolumn;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Column;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public interface AddColumn {

	Column getColumn(Matrix matrix, List<RowHead> rowHeads);
	
}
