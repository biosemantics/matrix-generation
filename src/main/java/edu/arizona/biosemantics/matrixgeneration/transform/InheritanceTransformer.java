package edu.arizona.biosemantics.matrixgeneration.transform;

import java.util.List;

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
		//possibly need to clone structure/value if they subsequently could be modified separately
		for(Structure structure : taxon.getStructures()) {
			if(structure.getCharacters().isEmpty()) {
				for(Taxon child : taxon.getChildren()) 
					if(!child.containsStructure(structure.getName()))
						child.addStructure(structure);
			}
			for(Character character : structure.getCharacters()) {		
				for(Taxon child : taxon.getChildren()) {
					if(child.containsStructure(structure.getName())) {
						Structure childStructure = child.getStructure(structure.getName());
						if(!childStructure.containsCharacter(character)) {
							childStructure.setCharacterValue(character, structure.getValue(character));
						}
					} else {
						child.addStructure(structure);
					}
				}
			}
		}	
				
		//recursively inherit character values
		for(Taxon child : taxon.getChildren()) 
			inheritCharacterValues(child);
	}

}
