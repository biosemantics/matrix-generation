package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.CombinedCellValueTransformer.ByChoiceCellValueTransformer;

public class RangeValueByChoiceCellValueTransformer implements ByChoiceCellValueTransformer {

	@Override
	public CellValue transform(Value value) {
		String from = value.getFrom() == null ? value.getFromInclusive() : value.getFrom();
		String to = value.getTo() == null ? value.getToInclusive() : value.getTo();
		return new CellValue(from + value.getFromUnit() + " - " + to + value.getToUnit());
	}

	@Override
	public boolean isTransformable(Value value) {
		return value.getCharType() != null && value.getCharType().equals("range_value");
	}


}
