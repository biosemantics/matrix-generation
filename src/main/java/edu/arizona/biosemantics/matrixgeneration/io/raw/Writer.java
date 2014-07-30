package edu.arizona.biosemantics.matrixgeneration.io.raw;

import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;

public interface Writer {

	public void write(RawMatrix rawMatrix) throws Exception;
	
}
