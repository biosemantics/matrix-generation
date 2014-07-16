package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class RawMatrixTransformer {
	
	private ColumnHeadTransformer columnHeadTransformer;
	private RowHeadTransformer rowHeadTransformer;
	private CellValueTransformer cellValueTransformer;

	public RawMatrixTransformer(ColumnHeadTransformer columnHeadTransformer, RowHeadTransformer rowHeadTransformer,
			CellValueTransformer cellValueTransformer) {
		this.columnHeadTransformer = columnHeadTransformer;
		this.rowHeadTransformer = rowHeadTransformer;
		this.cellValueTransformer = cellValueTransformer;
	}

	public RawMatrix transform(Matrix matrix) {
		List<RowHead> rowHeads = new LinkedList<RowHead>();
		List<ColumnHead> columnHeads = new LinkedList<ColumnHead>();
		List<List<CellValue>> cellValues = new LinkedList<List<CellValue>>();
		
		for(Taxon taxon : matrix.getTaxa()) {
			RowHead rowHead = rowHeadTransformer.transform(taxon);
			rowHeads.add(rowHead);
		}
		List<Character> characters = new ArrayList<Character>(matrix.getCharacters());
		Collections.sort(characters);
		for(Character character : characters) {
			ColumnHead columnHead = columnHeadTransformer.transform(character);
			columnHeads.add(columnHead);
		}
		for(Taxon taxon : matrix.getTaxa()) {
			List<CellValue> taxonsCellValues = new LinkedList<CellValue>();
			cellValues.add(taxonsCellValues);
			for(Character character : characters) {		
				CellValue cellValue = new CellValue("");
				String structureName = character.getStructureName();
				Structure structure = taxon.getStructure(structureName);
				if(structure != null) {
					Value value = structure.getCharacterValue(character);
					if(structure.containsCharacterValue(character))
						cellValue = cellValueTransformer.transform(value);
				}
				taxonsCellValues.add(cellValue);
			}
		}
		
		return new RawMatrix(rowHeads, columnHeads, cellValues);
	}

}
