package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.NotApplicableCellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class RemoveSingleValueColumnsTransformer implements Transformer {

	@Override
	public void transform(Matrix rawMatrix) {
		for(int columnId = 0; columnId < rawMatrix.getColumnCount(); columnId++) {
			if(isRemoveColumn(columnId, rawMatrix)) {
				rawMatrix.removeColumn(columnId);
			}
		}	
	}

	private boolean isRemoveColumn(int columnId, Matrix rawMatrix) {
		Set<CellValue> cellValues = new HashSet<CellValue>();
		for(RowHead rowHead : rawMatrix.getRowHeads()) {
			CellValue cellValue = rawMatrix.getCellValue(rowHead, columnId);
			if(!(cellValue instanceof NotApplicableCellValue)) 
				cellValues.add(cellValue);
		}
		return cellValues.size() > 1;
	}

}
