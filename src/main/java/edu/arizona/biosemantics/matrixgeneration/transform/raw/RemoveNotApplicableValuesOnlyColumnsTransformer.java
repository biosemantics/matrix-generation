package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.NotApplicableCellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class RemoveNotApplicableValuesOnlyColumnsTransformer implements Transformer {

	@Override
	public void transform(Matrix rawMatrix) {
		for(int columnId = 0; columnId < rawMatrix.getColumnCount(); columnId++) {
			if(isRemoveColumn(columnId, rawMatrix)) {
				log(LogLevel.DEBUG, "Remove not applicable values column: " + rawMatrix.getColumnHeads().get(columnId).getValue());
				rawMatrix.removeColumn(columnId);
			}
		}	
	}

	private boolean isRemoveColumn(int columnId, Matrix rawMatrix) {
		for(RowHead rowHead : rawMatrix.getRowHeads()) {
			CellValue cellValue = rawMatrix.getCellValue(rowHead, columnId);
			if(!(cellValue instanceof NotApplicableCellValue)) 
				return false;
		}
		return true;
	}

}
