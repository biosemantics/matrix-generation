package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class FixConstraintedStructuresTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getTaxa()) {
			for(Structure structure : taxon.getStructures()) {
	
				Structure fixedStructure = isFixStructure(structure);			
				if(fixedStructure != null) {
					matrix.removeStructure(structure, taxon);
					matrix.addStructure(fixedStructure, taxon);
				}
			}
		}
	}

	private Structure isFixStructure(Structure structure) {
		for(Character character : structure.getCharacters()) {
			Values values = structure.getCharacterValues(character);
			for(Value value : values) {
				if(value.getIsModifier()) {
					
				}
			}
		}
		
		return null;
	}

}
