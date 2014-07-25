package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Column;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class RawMatrixTransformer {
	
	private ColumnHeadTransformer columnHeadTransformer;
	private RowHeadTransformer rowHeadTransformer;
	private CellValueTransformer cellValueTransformer;
	private List<AddColumn> addColumns;

	public RawMatrixTransformer(ColumnHeadTransformer columnHeadTransformer, RowHeadTransformer rowHeadTransformer,
			CellValueTransformer cellValueTransformer, List<AddColumn> addColumns) {
		this.columnHeadTransformer = columnHeadTransformer;
		this.rowHeadTransformer = rowHeadTransformer;
		this.cellValueTransformer = cellValueTransformer;
		this.addColumns = addColumns;
	}

	public RawMatrix transform(Matrix matrix) {
		List<RowHead> rootRowHeads = new LinkedList<RowHead>();
		List<ColumnHead> columnHeads = new LinkedList<ColumnHead>();
		Map<RowHead, List<CellValue>> cellValues = new HashMap<RowHead, List<CellValue>>();
		
		for(Taxon rootTaxon : matrix.getRootTaxa()) {
			RowHead rowHead = rowHeadTransformer.transform(rootTaxon);
			rootRowHeads.add(rowHead);
			createDescendantRowHeads(rootTaxon, rowHead);			
		}
		List<Character> characters = new ArrayList<Character>(matrix.getCharacters());
		Collections.sort(characters);
		for(Character character : characters) {
			ColumnHead columnHead = columnHeadTransformer.transform(character);
			columnHeads.add(columnHead);
		}
		for(RowHead rowHead : rootRowHeads) {
			addRowsAndDescendantsCellValues(rowHead, characters, cellValues);
		}
		
		List<RowHead> rowHeads = getRowHeads(rootRowHeads);
		for(AddColumn addColumn : addColumns) {
			Column column = addColumn.getColumn(matrix, rowHeads);
			columnHeads.add(0, column.getColumnHead());
			Map<RowHead, CellValue> values = column.getCellValues();
			for(RowHead key : values.keySet()) {
				cellValues.get(key).add(0, values.get(key));
			}
		}
		
		return new RawMatrix(rootRowHeads, columnHeads, cellValues);
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

	private void addRowsAndDescendantsCellValues(RowHead rowHead, List<Character> characters, Map<RowHead, List<CellValue>> cellValues) {
		Taxon taxon = rowHead.getSource();
		List<CellValue> taxonsCellValues = new LinkedList<CellValue>();
		cellValues.put(rowHead, taxonsCellValues);
		for(Character character : characters) {		
			CellValue cellValue = new CellValue("", null);
			String structureName = character.getStructureName();
			Structure structure = taxon.getStructure(structureName);
			if(structure != null) {
				Value value = structure.getCharacterValue(character);
				if(structure.containsCharacterValue(character))
					cellValue = cellValueTransformer.transform(value);
			}
			taxonsCellValues.add(cellValue);
		}
		for(RowHead child : rowHead.getChildren()) {
			addRowsAndDescendantsCellValues(child, characters, cellValues);
		}
	}

	private void createDescendantRowHeads(Taxon taxon, RowHead rowHead) {
		for(Taxon child : taxon.getChildren()) {
			RowHead childRowHead = rowHeadTransformer.transform(child);
			rowHead.addChild(childRowHead);
			createDescendantRowHeads(child, childRowHead);
		}
	}

}
