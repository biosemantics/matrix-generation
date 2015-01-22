package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;


public class TransformerStrategy implements Transformer {

	private List<Transformer> transformers;

	public TransformerStrategy(List<Transformer> transformers) {
		this.transformers = transformers;
	}
	
	public void transform(Matrix matrix) {
		for(Transformer transformer : transformers) {
			transformer.transform(matrix);
		}
	}
	
}
