package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import edu.arizona.biosemantics.matrixgeneration.model.complete.AttributeCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public class AttributeCharacterRemoverTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(Character character : matrix.getCharacters()) {
			if(character instanceof AttributeCharacter) 
				matrix.removeCharacter(character);
		}
	}

}
