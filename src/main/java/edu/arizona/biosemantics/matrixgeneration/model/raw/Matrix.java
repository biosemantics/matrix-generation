package edu.arizona.biosemantics.matrixgeneration.model.raw;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Matrix implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<RowHead> rootRowHeads;
	private List<ColumnHead> columnHeads;
	private Map<RowHead, List<CellValue>> cellValues;
	private edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix source;
	
	public Matrix(List<RowHead> rootRowHeads, List<ColumnHead> columnHeads,
			Map<RowHead, List<CellValue>> cellValues, edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix source) {
		super();
		this.rootRowHeads = rootRowHeads;
		this.columnHeads = columnHeads;
		this.cellValues = cellValues;
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

	public edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix getSource() {
		return source;
	}

	public List<RowHead> getLeafRowHeads() {
		List<RowHead> leafRowHeads = new LinkedList<RowHead>();
		for(RowHead rootRowHead : rootRowHeads) {
			leafRowHeads.addAll(getLeafRowHeads(rootRowHead));
		}
		return leafRowHeads;
	}
	
	public List<RowHead> getLeafRowHeads(RowHead parent) {
		List<RowHead> result = new LinkedList<RowHead>();
		if(parent.getChildren().isEmpty()) {
			result.add(parent);
		} else {
			for(RowHead child : parent.getChildren())
				result.addAll(getLeafRowHeads(child));
		}
		return result;
	}

	public CellValue getCellValue(RowHead rowHead, ColumnHead columnHead) {
		int i = columnHeads.indexOf(columnHead);
		if(cellValues.containsKey(rowHead)) {
			return cellValues.get(rowHead).get(i);
		}
		return null;
	}
	
	public CellValue getCellValue(RowHead rowHead, int columnId) {
		if(cellValues.containsKey(rowHead)) {
			return cellValues.get(rowHead).get(columnId);
		}
		return null;
	}

	public void insertCellValue(RowHead rowHead, int columnId, CellValue cellValue) {
		if(cellValues.containsKey(rowHead)) {
			cellValues.get(rowHead).add(0, cellValue);
		}
	}

	public void insertColumnHead(int columnId, ColumnHead columnHead) {
		columnHeads.add(columnId, columnHead);
	}

	public void setCellValue(RowHead rowHead, int i, CellValue cellValue) {
		if(cellValues.containsKey(rowHead)) {
			cellValues.get(rowHead).set(i, cellValue);
		}
	}
	
	public void setCellValue(RowHead rowHead, ColumnHead columnHead, CellValue cellValue) {
		int columnId = columnHeads.indexOf(columnHead);
		setCellValue(rowHead, columnId, cellValue);
	}

	public void removeColumn(ColumnHead columnHead) {
		int i = columnHeads.indexOf(columnHead);
		this.removeColumn(i);
	}
	
	public void removeColumn(int columnId) {
		this.columnHeads.remove(columnId);
		for(RowHead rowHead : this.getRowHeads()) {
			cellValues.get(rowHead).remove(columnId);
		}
	}

	public int getApplicableCellValues() {
		int result = 0;
		for(RowHead rowHead : this.getRowHeads()) {
			for(CellValue cellValue : cellValues.get(rowHead)) {
				if(!(cellValue instanceof NotApplicableCellValue)) {
					result++;
				}
			}
		}
		return result;
	}

	public void sortAlphabetically() {
		//TODO: Sort taxa on each level..
		
		List<Column> columnList = new ArrayList<Column>(columnHeads.size());
		for(int i=0; i<columnHeads.size(); i++) {
			ColumnHead columnHead = columnHeads.get(i);
			Map<RowHead, CellValue> rowHeadValues = new HashMap<RowHead, CellValue>();
			for(RowHead rowHead : this.cellValues.keySet()) 
				rowHeadValues.put(rowHead, this.cellValues.get(rowHead).get(i));
			columnList.add(new Column(columnHead, rowHeadValues));
		}
		
		Collections.sort(columnList);
		
		for(RowHead rowHead : cellValues.keySet()) 
			cellValues.get(rowHead).clear();
		columnHeads.clear();
		for(int i=0; i<columnList.size(); i++){
			Column column = columnList.get(i);
			columnHeads.add(column.getColumnHead());
			
			for(RowHead rowHead : column.getCellValues().keySet()) {
				if(!cellValues.containsKey(rowHead))
					cellValues.put(rowHead, new ArrayList<CellValue>(columnList.size()));
				cellValues.get(rowHead).add(column.getCellValues().get(rowHead));
			}
		}
		
	}
	
	/*private class ColumnCellValues implements Comparable<ColumnCellValues> {
		private ColumnHead columnHead;
		private Map<RowHead, CellValue> rowHeadValues = new HashMap<RowHead, CellValue>();
		public ColumnCellValues(ColumnHead columnHead,
				Map<RowHead, CellValue> rowHeadValues) {
			super();
			this.columnHead = columnHead;
			this.rowHeadValues = rowHeadValues;
		}
		public ColumnHead getColumnHead() {
			return columnHead;
		}
		public Map<RowHead, CellValue> getRowHeadValues() {
			return rowHeadValues;
		}
		@Override
		public int compareTo(ColumnCellValues o) {
			return columnHead.getValue().compareTo(o.columnHead.getValue());
		}
	}*/
}
