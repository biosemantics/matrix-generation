package edu.arizona.biosemantics.matrixgeneration.io;

import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;

public interface Writer {

	public void write(RawMatrix rawMatrix) throws Exception;
	
}
