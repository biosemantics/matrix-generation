package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.NotApplicableCellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class TaxonomyAncestorInheritanceTransformer implements Transformer {

	private String cellValueSeparator;
	
	@Inject
	public TaxonomyAncestorInheritanceTransformer(@Named("CellValueSeparator") String cellValueSeparator) {
		this.cellValueSeparator = cellValueSeparator;
	}
	
	@Override
	public void transform(Matrix matrix) {
		for(RowHead rowHead : matrix.getLeafRowHeads()) {
			log(LogLevel.DEBUG, "Start from leaf " + rowHead.getValue());
			propagateToAncestors(matrix, rowHead);
		}
	}
	
	private void propagateToAncestors(Matrix matrix, RowHead rowHead) {
		RowHead parent = rowHead.getParent();
		if(parent != null) {
			log(LogLevel.DEBUG, "Propagate from " + rowHead.getValue() + " to ancestor: " + parent.getValue());
			for(ColumnHead columnHead : matrix.getColumnHeads()) { 
				CellValue newCellValue = determineParentCellValue(matrix, parent, columnHead);
				log(LogLevel.DEBUG, "Propagate from " + rowHead.getValue() + " to ancestor: " + parent.getValue() + ": "
						+ "Propagate for column: " + columnHead.getValue() + ",\t new value: " + newCellValue.getText() + ",\t old value: " + 
						matrix.getCellValue(rowHead, columnHead).getText());
				matrix.setCellValue(parent, columnHead, newCellValue);
			}

			//recursively inherit character values
			propagateToAncestors(matrix, rowHead.getParent());
		}
	}

	private CellValue determineParentCellValue(Matrix matrix, RowHead rowHead, ColumnHead columnHead) {
		Set<String> allValues = new HashSet<String>();
		for(RowHead child : rowHead.getChildren()) {
			CellValue value = matrix.getCellValue(child, columnHead);
			if(value instanceof NotApplicableCellValue)
				return new NotApplicableCellValue(this);
			allValues.addAll(Arrays.asList(value.getText().split(Pattern.quote(cellValueSeparator))));
		}
		StringBuilder valueBuilder = new StringBuilder();
		for(String value : allValues) {
			valueBuilder.append(value + cellValueSeparator);
		}
		String newValue = valueBuilder.toString();
		
		return new CellValue(newValue.substring(0, newValue.length() - cellValueSeparator.length()), (Value)null, this);
	}

	//if all children of parent contain the structure and have the character set the same
	/*private Value determineParentValue(Taxon parent, Structure structure, Character character) {
		Set<Value> allValues = new HashSet<Value>();
		for(Taxon child : parent.getChildren()) {
			if(child.containsStructure(structure.getName())) {
				Structure childStructure = child.getStructure(structure.getName());
				if(childStructure.containsCharacter(character)) {
					Value value = childStructure.getCharacterValue(character);
					allValues.add(value);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		
		Value value = new Value();
	}*/

}
