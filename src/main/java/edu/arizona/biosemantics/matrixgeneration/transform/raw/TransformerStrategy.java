package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.List;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;

public class TransformerStrategy implements Transformer {

	private List<Transformer> transformers;

	public TransformerStrategy(List<Transformer> transformers) {
		this.transformers = transformers;
	}
	
	public void transform(Matrix rawMatrix) {
		for(Transformer transformer : transformers) {
			log(LogLevel.DEBUG, "Transform using " + transformer.getClass());
			transformer.transform(rawMatrix);
			log(LogLevel.INFO, "Raw matrix successfully transformed.\n Rows: " + rawMatrix.getRowCount() + 
					"\n Columns: " + rawMatrix.getColumnCount() + "\n Applicable cell values " + rawMatrix.getApplicableCellValues());
			
		}
	}

}
