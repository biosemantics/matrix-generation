package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public class RawMatrix implements Serializable {

	private List<RowHead> rootRowHeads;
	private List<ColumnHead> columnHeads;
	private Map<RowHead, List<CellValue>> cellValues;
	private Matrix source;
	
	public RawMatrix(List<RowHead> rootRowHeads, List<ColumnHead> columnHeads,
			Map<RowHead, List<CellValue>> cellValues, Matrix source) {
		super();
		this.rootRowHeads = Collections.unmodifiableList(rootRowHeads);
		this.columnHeads = Collections.unmodifiableList(columnHeads);
		this.cellValues = Collections.unmodifiableMap(cellValues);
		this.source = source;
		if(getRowCount() != cellValues.size())
			throw new IllegalArgumentException("");
		for(List<CellValue> rowsCellValues : cellValues.values()) {
			if(rowsCellValues.size() != columnHeads.size())
				throw new IllegalArgumentException("");
		}
	}
	
	public List<RowHead> getRootRowHeads() {
		return rootRowHeads;
	}
	
	public List<RowHead> getRowHeads() {
		List<RowHead> result = new LinkedList<RowHead>();
		for(RowHead rootRowHead : rootRowHeads) {
			addRowHeadAndDescendants(rootRowHead, result);
		}
		return result;
	}
	
	private void addRowHeadAndDescendants(RowHead rowHead, List<RowHead> result) {
		result.add(rowHead);
		for(RowHead child : rowHead.getChildren()) {
			addRowHeadAndDescendants(child, result);
		}
	}
	
	public List<ColumnHead> getColumnHeads() {
		return columnHeads;
	}
	
	public Map<RowHead, List<CellValue>> getCellValues() {
		return cellValues;
	}
	
	public int getRowCount() {
		return getRowHeads().size();
	}
	
	public int getRootRowCount() {
		return rootRowHeads.size();
	}
	
	public int getColumnCount() {
		return columnHeads.size();
	}

	public Matrix getSource() {
		return source;
	}
	
	
}
