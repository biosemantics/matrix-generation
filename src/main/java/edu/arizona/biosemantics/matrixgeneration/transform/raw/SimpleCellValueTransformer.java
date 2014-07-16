package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

public class SimpleCellValueTransformer implements CellValueTransformer {

	@Override
	public CellValue transform(Value value) {
		if(value.getValue() == null)
			return new CellValue("");
		if(value.getUnit() != null)
			return new CellValue(value.getValue() + " " + value.getUnit());
		return new CellValue(value.getValue());
	}

	

}
