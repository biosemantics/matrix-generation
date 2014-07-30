package edu.arizona.biosemantics.matrixgeneration.io;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;

public interface Writer {

	public void write(Matrix matrix) throws Exception;
	
}
