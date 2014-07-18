package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.Value;

public interface ByChoiceCellValueTransformer extends CellValueTransformer {
		
		public boolean isTransformable(Value value);
		
	}