package edu.arizona.biosemantics.matrixgeneration.io.complete.in;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public interface Reader {

	public Matrix read() throws Exception;
	
}
