package edu.arizona.biosemantics.matrixgeneration.io;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon;

public interface Reader {

	/**
	 * Reads the root taxa
	 * @return
	 * @throws Exception
	 */
	public List<Taxon> read() throws Exception;
	
}
