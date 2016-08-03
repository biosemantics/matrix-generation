package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.Provenance;
import edu.arizona.biosemantics.matrixgeneration.model.complete.AbsentPresentCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Relation;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public class AbsentPresentFromRelationsTranformer implements Transformer {

	private Set<String> presentRelations;
	private Set<String> absentRelations;

	@Inject
	public AbsentPresentFromRelationsTranformer(
			@Named("PresentRelation")Set<String> presentRelations, 
			@Named("AbsentRelation")Set<String> absentRelations) {
		this.presentRelations = presentRelations;
		this.absentRelations = absentRelations;
	}

	@Override
	public void transform(Matrix matrix) {		
		for(Taxon taxon : matrix.getTaxa()) {
			generateFromRelations(matrix, taxon);
		}
	}

	private void generateFromRelations(Matrix matrix, Taxon taxon) {
		for(Relation relation : taxon.getRelations()) {
			if(isPresentRelation(relation)) {
				Character character = createPresentCharacter(relation);
				if(character != null) {
					log(LogLevel.DEBUG, "Create from presence of relation character: " + character.toString());
					matrix.addCharacter(character);
					Structure structure = matrix.getStructure(character.getBearerStructureIdentifier(), taxon);
					if(structure == null) {
						StructureIdentifier identifier = character.getBearerStructureIdentifier();
						structure = new Structure(identifier.getStructureName(), 
								identifier.getStructureConstraint(), identifier.getStructureOntologyId());
						matrix.addStructure(structure, taxon);
						
					}
					log(LogLevel.DEBUG, "Set present for: " + taxon.toString());
					structure.addCharacterValue(character, new Value("present", new Provenance(this.getClass())));
				}
			}
			if(isAbsentRelation(relation)) {
				Character character = createAbsentCharacter(relation);
				if(character != null) {
					log(LogLevel.DEBUG, "Create from presence of relation character: " + character.toString());
					matrix.addCharacter(character);
					Structure structure = matrix.getStructure(character.getBearerStructureIdentifier(), taxon);
					log(LogLevel.DEBUG, "Set absent for: " + taxon.toString());
					structure.addCharacterValue(character, new Value("absent", new Provenance(this.getClass())));
				}
			}
		}
	}

	private Character createAbsentCharacter(Relation relation) {
		if(relation.getTo() != null && relation.getFrom() != null) {
			Character character = new AbsentPresentCharacter(new StructureIdentifier(relation.getTo()), 
					new StructureIdentifier(relation.getFrom()), this);
			return character;
		}
		return null;
	}

	private Character createPresentCharacter(Relation relation) {
		if(relation.getTo() != null && relation.getFrom() != null) {
			Character character = new AbsentPresentCharacter(new StructureIdentifier(relation.getTo()), 
					new StructureIdentifier(relation.getFrom()), this);
			return character;
		}
		return null;
	}

	private boolean isAbsentRelation(Relation relation) {
		return absentRelations.contains(relation.getName());
	}

	private boolean isPresentRelation(Relation relation) {
		return presentRelations.contains(relation.getName());
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
