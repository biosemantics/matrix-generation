package edu.arizona.biosemantics.matrixgeneration.transform;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Character;

/**
 * 5. allow the user to decide whether the character states of a higher taxon should be inherited by the lower taxa.
 * @author rodenhausen
 */
public class InheritanceTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getTaxa()) {
			inheritCharacterValues(taxon);
		}
	}

	private void inheritCharacterValues(Taxon taxon) {
		//propagate character values to children 
		for(Structure structure : taxon.getStructures()) {
			for(Taxon child : taxon.getChildren()) {
				if(!child.containsStructure(structure.getName()))
					child.addStructure(structure.clone());
				
				Structure childStructure = child.getStructure(structure.getName());
				for(Character character : structure.getCharacters()) {		
					if(!childStructure.containsCharacter(character)) {
						childStructure.setCharacterValue(character, structure.getCharacterValue(character).clone());
					}
				}
			}
		}	
				
		//recursively inherit character values
		for(Taxon child : taxon.getChildren()) 
			inheritCharacterValues(child);
	}

}
