package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;

public interface Transformer {

	public void transform(Matrix rawMatrix);
	
}
