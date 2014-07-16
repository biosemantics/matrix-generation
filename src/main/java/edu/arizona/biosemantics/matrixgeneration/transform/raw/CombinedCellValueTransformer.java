package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

/**
 * 6. allow the user to decide how to represent  range numerical values, for example either as  
 * length = "2-5 cm" or min_length = 2cm, max_length=5cm.
 * @author rodenhausen
 */
public class CombinedCellValueTransformer implements CellValueTransformer {

	public static interface ByChoiceCellValueTransformer extends CellValueTransformer {
		
		public boolean isTransformable(Value value);
		
	}

	private List<ByChoiceCellValueTransformer> transformers;
	private CellValueTransformer defaultTransformer;
	
	public CombinedCellValueTransformer(List<ByChoiceCellValueTransformer> transformers, 
			CellValueTransformer defaultTransformer) {
		this.transformers = transformers;
		this.defaultTransformer = defaultTransformer;
	}

	@Override
	public CellValue transform(Value value) {
		for(ByChoiceCellValueTransformer transformer : transformers) {
			if(transformer.isTransformable(value)) {
				return transformer.transform(value);
			}
		}
		return defaultTransformer.transform(value);
	}

}
