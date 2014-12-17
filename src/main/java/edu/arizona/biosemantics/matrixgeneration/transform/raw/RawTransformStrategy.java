package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;

public interface RawTransformStrategy {

	RawMatrix transform(Matrix matrix);

}
