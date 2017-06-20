package edu.arizona.biosemantics.matrixgeneration.model.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Provenance;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public class NotApplicableCellValue extends CellValue {
	
	private static final long serialVersionUID = 1L;

	public NotApplicableCellValue(Provenance generationProvenance) {
		super("", (Value)null, generationProvenance);
	}

}
