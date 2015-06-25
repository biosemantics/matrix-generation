package edu.arizona.biosemantics.matrixgeneration.model.raw;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public class NotApplicableCellValue extends CellValue {
	
	private static final long serialVersionUID = 1L;

	public NotApplicableCellValue(Object generationProvenance) {
		super("", (Value)null, generationProvenance);
	}

}
