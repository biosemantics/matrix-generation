package edu.arizona.biosemantics.matrixgeneration.transform.raw.addcolumn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Column;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class AddSourceColumn implements AddColumn {

	@Override
	public Column getColumn(Matrix matrix, List<RowHead> rowHeads) {
		ColumnHead head = new ColumnHead("source file", null);
		Map<RowHead, CellValue> values = new HashMap<RowHead, CellValue>();
		
		for(RowHead rowHead : rowHeads) {
			values.put(rowHead, new CellValue(matrix.getSourceFile(rowHead.getSource()).getName(), null));
		}
		
		return new Column(head, values);
	}

}
