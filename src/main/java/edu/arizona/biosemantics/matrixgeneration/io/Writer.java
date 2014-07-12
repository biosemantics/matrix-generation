package edu.arizona.biosemantics.matrixgeneration.io;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon;

public interface Writer {

	public void write(List<Taxon> taxa) throws Exception;
	
}
