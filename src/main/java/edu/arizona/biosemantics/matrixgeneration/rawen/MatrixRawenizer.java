package edu.arizona.biosemantics.matrixgeneration.rawen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Column;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.NotApplicableCellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;
import edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue.CellValueRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.columnhead.ColumnHeadRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.rowhead.RowHeadRawenizer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.addcolumn.AddColumn;

public class MatrixRawenizer {

	private ColumnHeadRawenizer columnHeadRawenizer;
	private RowHeadRawenizer rowHeadRawenizer;
	private CellValueRawenizer cellValueRawenizer;

	@Inject
	public MatrixRawenizer(ColumnHeadRawenizer columnHeadRawenizer,
			RowHeadRawenizer rowHeadRawenizer,
			CellValueRawenizer cellValueRawenizer) {
		this.columnHeadRawenizer = columnHeadRawenizer;
		this.rowHeadRawenizer = rowHeadRawenizer;
		this.cellValueRawenizer = cellValueRawenizer;
	}
	
	public edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix convert(edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix matrix) {
		List<RowHead> rootRowHeads = new LinkedList<RowHead>();
		List<ColumnHead> columnHeads = new LinkedList<ColumnHead>();
		Map<RowHead, List<CellValue>> cellValues = new HashMap<RowHead, List<CellValue>>();
		
		for(Taxon rootTaxon : matrix.getRootTaxa()) {
			RowHead rowHead = rowHeadRawenizer.transform(rootTaxon);
			rootRowHeads.add(rowHead);
			createDescendantRowHeads(rootTaxon, rowHead);	
		}
		List<Character> characters = new ArrayList<Character>(matrix.getCharacters());
		Collections.sort(characters);
		for(Character character : characters) {
			ColumnHead columnHead = columnHeadRawenizer.transform(character);
			columnHeads.add(columnHead);
		}
		for(RowHead rowHead : rootRowHeads) {
			addRowsAndDescendantsCellValues(rowHead, characters, cellValues);
		}
		
		return new edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix(rootRowHeads, columnHeads, cellValues, matrix);
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
			List<CellValue> characterValues = new LinkedList<CellValue>();
			
			String structureName = character.getStructureIdentifier().getStructureName();
			Set<Structure> structures = taxon.getStructures(structureName);
			if(structures != null) {
				Values values = null;
				for(Structure structure : structures) {
					Values newValues = structure.getCharacterValues(character);
					values = combineValues(values, newValues);
				}
				if(values != null)
					for(Value value : values) {
						if(value.getNotes() != null && value.getNotes().contains("[duplicate value]")) {
							// ignore duplicate values due to range values have added their 
							// extreme values as separate characters
						} else {
							CellValue cellValue = cellValueRawenizer.rawenize(value);
							characterValues.add(cellValue);
						}
					}
			}
			
			if(characterValues.isEmpty())
				characterValues.add(new NotApplicableCellValue());
			
			taxonsCellValues.add(combineCellValues(characterValues));
		}
		
		for(RowHead child : rowHead.getChildren()) {
			addRowsAndDescendantsCellValues(child, characters, cellValues);
		}
	}
	
	private CellValue combineCellValues(List<CellValue> cellValues) {
		String result = "";
		Values sources = new Values();
		
		Set<String> containedValues = new HashSet<String>();
		for(CellValue cellValue : cellValues) {
			if(!containedValues.contains(cellValue.getText())) {
				result += cellValue.getText() + " | ";
				sources.addAll(cellValue.getSource());
				containedValues.addAll(cellValue.getContainedValues());
			}
		}
		
		return new CellValue(result.substring(0, result.length() - 3).trim(), containedValues, sources);
	}

	private Values combineValues(Values values, Values newValues) {
		if(newValues != null) {
			if(values == null) 
				return newValues;
			values.addAll(newValues);
		}
		return values;
	}

	private void createDescendantRowHeads(Taxon taxon, RowHead rowHead) {
		for(Taxon child : taxon.getChildren()) {
			RowHead childRowHead = rowHeadRawenizer.transform(child);
			rowHead.addChild(childRowHead);
			childRowHead.setParent(rowHead);
			createDescendantRowHeads(child, childRowHead);
		}
	}
}
