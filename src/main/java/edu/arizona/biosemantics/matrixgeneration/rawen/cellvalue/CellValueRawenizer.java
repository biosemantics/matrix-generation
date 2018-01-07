package edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

public interface CellValueRawenizer {

	public CellValue rawenize(Value value);

	public boolean isRawenizable(Value value);

}
