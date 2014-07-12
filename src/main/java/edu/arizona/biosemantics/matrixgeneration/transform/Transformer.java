package edu.arizona.biosemantics.matrixgeneration.transform;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Taxon;

public interface Transformer {

	public void transform(List<Taxon> taxa);

}
