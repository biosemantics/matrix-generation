package edu.arizona.biosemantics.matrixgeneration.io.complete;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public interface Writer {

	public void write(Matrix matrix) throws Exception;
	
}
