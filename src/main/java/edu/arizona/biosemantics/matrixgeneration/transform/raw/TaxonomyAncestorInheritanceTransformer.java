package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.NotApplicableCellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class TaxonomyAncestorInheritanceTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(RowHead rowHead : matrix.getLeafRowHeads()) {
			propagateToAncestors(matrix, rowHead);
		}
	}
	
	private void propagateToAncestors(Matrix matrix, RowHead rowHead) {
		RowHead parent = rowHead.getParent();
		if(parent != null) {
			for(int i=0; i<matrix.getColumnCount(); i++) { 
				CellValue value = matrix.getCellValue(rowHead, i);
				if(value instanceof NotApplicableCellValue) {
					CellValue newCellValue = determineParentCellValue(matrix, parent, i);
					matrix.setCellValue(parent, i, newCellValue);
				}
			}

		}

		//recursively inherit character values
		propagateToAncestors(matrix, rowHead.getParent());
	}

	private CellValue determineParentCellValue(Matrix matrix, RowHead rowHead, int columnId) {
		Set<String> allValues = new HashSet<String>();
		for(RowHead child : rowHead.getChildren()) {
			CellValue value = matrix.getCellValue(rowHead, columnId);
			if(value instanceof NotApplicableCellValue)
				return new NotApplicableCellValue();
			allValues.add(value.getText());
		}
		StringBuilder valueBuilder = new StringBuilder();
		for(String value : allValues) {
			valueBuilder.append(value + ",");
		}
		String newValue = valueBuilder.toString();
		
		return new CellValue(newValue.substring(0, newValue.length() - 1), (Value)null);
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
