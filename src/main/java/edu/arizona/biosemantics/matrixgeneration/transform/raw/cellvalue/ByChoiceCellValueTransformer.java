package edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public interface ByChoiceCellValueTransformer extends CellValueTransformer {
		
		public boolean isTransformable(Value value);
		
	}