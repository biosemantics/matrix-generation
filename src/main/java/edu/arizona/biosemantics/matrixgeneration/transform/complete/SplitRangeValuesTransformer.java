package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class SplitRangeValuesTransformer implements Transformer { 
	
	//have to go through all values of a character, and decide if single one of values is range
	//then split character in two. the ones who did not have range, will have min and max the same value..
	@Override
	public void transform(Matrix matrix) {
		List<Character> toSplitCharacters = new LinkedList<Character>();
		for(Character character : matrix.getCharacters()) {
			for(Taxon taxon : matrix.getTaxa()) {
				Set<Structure> structures = taxon.getStructures(character.getStructureIdentifier().getStructureName());
				for(Structure structure : structures) {
					if(structure != null) {
						Values values = structure.getCharacterValues(character);
						for(Value value : values) {
							if(value != null) {
								if(value.getCharType() != null && value.getCharType().equals("range_value")) {
									toSplitCharacters.add(character);
									continue;
								}
							}
						}
					}
				}
			}
		}
		
		for(Character character : toSplitCharacters) {
			Character maxCharacter = new Character(character.getName() + "_max", "of", character.getStructureIdentifier());
			Character minCharacter = new Character(character.getName() + "_min", "of", character.getStructureIdentifier());
			matrix.addCharacter(maxCharacter);
			matrix.addCharacter(minCharacter);
			
			for(Taxon taxon : matrix.getTaxa()) {
				Set<Structure> structures = taxon.getStructures(character.getStructureIdentifier().getStructureName());
				for(Structure structure : structures) {
					if(structure != null) {
						Values values = structure.getCharacterValues(character);
						for(Value value : values) {
							if(value != null) {
								structure.removeCharacterValues(character);
								
								Value maxValue = value.clone();
								maxValue.setValue(value.getTo() == null ? value.getToInclusive() : value.getTo());
								maxValue.setUnit(value.getToUnit());
								maxValue.setCharType(null);
								maxValue.setTo(null);
								maxValue.setToInclusive(null);
								maxValue.setFrom(null);
								maxValue.setFromInclusive(null);
								maxValue.setFromUnit(null);
								maxValue.setToUnit(null);
								
								Value minValue = value.clone();
								minValue.setValue(value.getFrom() == null ? value.getFromInclusive() : value.getFrom());
								minValue.setUnit(value.getFromUnit());
								minValue.setCharType(null);
								minValue.setTo(null);
								minValue.setToInclusive(null);
								minValue.setFrom(null);
								minValue.setFromInclusive(null);
								minValue.setFromUnit(null);
								minValue.setToUnit(null);
								
								structure.addCharacterValue(minCharacter, minValue);
								structure.addCharacterValue(maxCharacter, maxValue);
							}
						}
					}
				}
			}
			matrix.removeCharacter(character);
		}
	}

}
