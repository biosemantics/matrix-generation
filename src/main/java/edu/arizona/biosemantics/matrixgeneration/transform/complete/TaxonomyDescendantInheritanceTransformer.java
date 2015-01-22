package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;

public class TaxonomyDescendantInheritanceTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getRootTaxa()) {
			propagateToDescendants(taxon);
		}
	}
	
	private void propagateToDescendants(Taxon taxon) {
		//propagate character values to children 
		for(Structure structure : taxon.getStructures()) {
			for(Taxon child : taxon.getChildren()) {
				if(!child.containsStructure(structure.getName()))
					child.addStructure(structure.clone());
								
				Set<Structure> childStructures = child.getStructures(structure.getName());
				for(Structure childStructure : childStructures) {
					for(Character character : structure.getCharacters()) {		
						if(!childStructure.containsCharacter(character)) {
							childStructure.setCharacterValues(character, structure.getCharacterValues(character).clone());
						}
					}
				}
			}
		}	
				
		//recursively inherit character values
		for(Taxon child : taxon.getChildren()) 
			propagateToDescendants(child);
	}

}
