package edu.arizona.biosemantics.matrixgeneration.io;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;

public interface Reader {

	public Matrix read() throws Exception;
	
}
