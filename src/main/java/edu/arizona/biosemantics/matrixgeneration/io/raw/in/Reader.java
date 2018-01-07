package edu.arizona.biosemantics.matrixgeneration.io.raw.in;

import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;

public interface Reader {

	public Matrix read() throws Exception;
	
}
