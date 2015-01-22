package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import edu.arizona.biosemantics.matrixgeneration.model.complete.AbsentPresentCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public class AbsentPresentFromBiologicalEntitiesTransformer implements Transformer {

	@Override
	public void transform(Matrix matrix) {		
		for(Taxon taxon : matrix.getTaxa()) {
			generateFromBiologicalEntities(matrix, taxon);
		}
	}
	
	private void generateFromBiologicalEntities(Matrix matrix, Taxon taxon) {
		for(Structure structure : taxon.getStructuresWithoutWholeOrganism()) {
			Character character = createPresentCharacter(taxon, structure);
			if(character != null) {
				matrix.addCharacter(character);
				structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
				structure.addCharacterValue(character, new Value("present"));
			}
		}
	}
	
	private Character createPresentCharacter(Taxon taxon, Structure structure) {
		Character character = new AbsentPresentCharacter("quantity of " + structure.getName(), "at", 
				new StructureIdentifier(taxon.getWholeOrganism()));
		return character;
	}

}
