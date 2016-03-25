package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.AttributeCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

public class RemoveAttributeCharactersTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(Character character : matrix.getCharacters()) {
			if(character instanceof AttributeCharacter) {
				log(LogLevel.DEBUG, "Remove attribute character: " + character.toString());
				matrix.removeCharacter(character);
			}
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
