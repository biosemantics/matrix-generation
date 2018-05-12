package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.List;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;


public class TransformerStrategy implements Transformer {

	private List<Transformer> transformers;

	public TransformerStrategy(List<Transformer> transformers) {
		this.transformers = transformers;
	}
	
	public void transform(Matrix matrix) {
		for(Transformer transformer : transformers) {
			log(LogLevel.DEBUG, "Transform using " + transformer.getClass());
			transformer.transform(matrix);
			log(LogLevel.INFO, "Matrix successfully transformed using "+transformer+" .\n Taxa: " + matrix.getTaxaCount() + 
					"\n Characters: " + matrix.getCharactersCount() + "\n Values: " + matrix.getSetCharacterValues());
			
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
