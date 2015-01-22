package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Column;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.addcolumn.AddColumn;

public class AddSourceColumnTransformer implements Transformer {

	private class AddSourceColumn implements AddColumn {
		@Override
		public Column getColumn(edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix matrix, List<RowHead> rowHeads) {
			ColumnHead head = new ColumnHead("source file", null);
			Map<RowHead, CellValue> values = new HashMap<RowHead, CellValue>();
			
			for(RowHead rowHead : rowHeads) {
				values.put(rowHead, new CellValue(matrix.getSourceFile(rowHead.getSource()).getName(), (Value)null));
			}
			
			return new Column(head, values);
		}
	}
	
	@Override
	public void transform(edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix matrix) {
		List<RowHead> rootRowHeads = matrix.getRootRowHeads();
		List<RowHead> rowHeads = getRowHeads(rootRowHeads);
		
		AddColumn addColumn = new AddSourceColumn();
		Column column = addColumn.getColumn(matrix.getSource(), rowHeads);
		matrix.insertColumnHead(0, column.getColumnHead());
		Map<RowHead, CellValue> values = column.getCellValues();
		for(RowHead key : values.keySet()) {
			matrix.insertCellValue(key, 0, values.get(key));
		}
	}
	
	private List<RowHead> getRowHeads(List<RowHead> rootRowHeads) {
		List<RowHead> result = new LinkedList<RowHead>();
		for(RowHead rowHead : rootRowHeads) {
			addRowHeadAndDescendants(rowHead, result);
		}
		return result;
	}
	
	private void addRowHeadAndDescendants(RowHead rowHead, List<RowHead> result) {
		result.add(rowHead);
		for(RowHead child : rowHead.getChildren()) {
			addRowHeadAndDescendants(child, result);
		}
	}

}
