package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.util.Collections;
import java.util.List;

public class RawMatrix {

	private List<RowHead> rowHeads;
	private List<ColumnHead> columnHeads;
	private List<List<CellValue>> cellValues;
	
	public RawMatrix(List<RowHead> rowHeads, List<ColumnHead> columnHeads,
			List<List<CellValue>> cellValues) {
		super();
		this.rowHeads = Collections.unmodifiableList(rowHeads);
		this.columnHeads = Collections.unmodifiableList(columnHeads);
		this.cellValues = Collections.unmodifiableList(cellValues);
		if(rowHeads.size() != cellValues.size())
			throw new IllegalArgumentException("");
		for(List<CellValue> rowsCellValues : cellValues) {
			if(rowsCellValues.size() != columnHeads.size())
				throw new IllegalArgumentException("");
		}
	}
	public List<RowHead> getRowHeads() {
		return rowHeads;
	}
	public List<ColumnHead> getColumnHeads() {
		return columnHeads;
	}
	public List<List<CellValue>> getCellValues() {
		return cellValues;
	}
	public int getRowCount() {
		return rowHeads.size();
	}
	public int getColumnCount() {
		return columnHeads.size();
	}
}
