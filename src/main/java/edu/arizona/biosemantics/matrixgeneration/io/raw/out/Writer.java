package edu.arizona.biosemantics.matrixgeneration.io.raw.out;

import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;

public interface Writer {

	public void write(Matrix rawMatrix) throws Exception;
	
}
