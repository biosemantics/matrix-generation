package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

public class SimpleCellValueTransformer implements CellValueTransformer {

	@Override
	public CellValue transform(Value value) {
		return new CellValue(value.getValue());
	}

	

}
