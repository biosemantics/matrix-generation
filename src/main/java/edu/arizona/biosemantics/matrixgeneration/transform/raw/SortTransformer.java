package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;

public class SortTransformer implements Transformer {

	@Override
	public void transform(Matrix rawMatrix) {
		rawMatrix.sortAlphabetically();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
