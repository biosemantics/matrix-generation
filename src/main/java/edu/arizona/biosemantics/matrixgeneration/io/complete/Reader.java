package edu.arizona.biosemantics.matrixgeneration.io.complete;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public interface Reader {

	public Matrix read() throws Exception;
	
}
