package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.NotApplicableCellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class RemoveNotApplicableValuesOnlyColumnsTransformer implements Transformer {

	@Override
	public void transform(Matrix rawMatrix) {
		List<ColumnHead> iteratable = new ArrayList<ColumnHead>(rawMatrix.getColumnHeads());
		for(ColumnHead columnHead : iteratable) {
			if(isRemoveColumn(columnHead, rawMatrix)) {
				log(LogLevel.DEBUG, "Remove not applicable values column: " + columnHead.getValue());
				rawMatrix.removeColumn(columnHead);
			}
		}	
	}

	private boolean isRemoveColumn(ColumnHead columnHead, Matrix rawMatrix) {
		for(RowHead rowHead : rawMatrix.getRowHeads()) {
			CellValue cellValue = rawMatrix.getCellValue(rowHead, columnHead);
			if(!(cellValue instanceof NotApplicableCellValue)) 
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
