package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class TaxonomyDescendantInheritanceTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getRootTaxa()) {
			log(LogLevel.DEBUG, "Start from root taxon " + taxon.toString());
			propagateToDescendants(taxon);
		}
	}
	
	private void propagateToDescendants(Taxon taxon) {
		//propagate character values to children 
		for(Structure structure : taxon.getStructures()) {
			for(Taxon child : taxon.getChildren()) {
				log(LogLevel.DEBUG, "Propagate from " + taxon.toString() + " to descendant: " + child.toString());
				if(!child.containsStructure(structure.getName()))
					child.addStructure(structure.clone(false));
								
				Set<Structure> childStructures = child.getStructures(structure.getName());
				for(Structure childStructure : childStructures) {
					for(Character character : structure.getCharacters()) {		
						if(!childStructure.containsCharacter(character)) {
							Values newValues = structure.getCharacterValues(character).clone();
							for(Value value : newValues)
								value.addGenerationProvenance(this);
							log(LogLevel.DEBUG, "Propagate from " + taxon.toString() + " to descendant: " + child.toString() + ": "
									+ "Propagate for character: " + character.toString() + ",\t new value: " + newValues.getCombinedText(" | ") + ",\t old value: " + 
									structure.getCharacterValues(character).getCombinedText(" | "));
							childStructure.setCharacterValues(character, newValues);
						}
					}
				}
			}
		}	
				
		//recursively inherit character values
		for(Taxon child : taxon.getChildren()) 
			propagateToDescendants(child);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
