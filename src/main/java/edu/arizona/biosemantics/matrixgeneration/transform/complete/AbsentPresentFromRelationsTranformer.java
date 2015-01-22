package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

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
					matrix.addCharacter(character);
					Structure structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
					structure.addCharacterValue(character, new Value("present"));
				}
			}
			if(isAbsentRelation(relation)) {
				Character character = createAbsentCharacter(relation);
				if(character != null) {
					matrix.addCharacter(character);
					Structure structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
					structure.addCharacterValue(character, new Value("absent"));
				}
			}
		}
	}

	private Character createAbsentCharacter(Relation relation) {
		if(relation.getTo() != null && relation.getFrom() != null) {
			String toStructure = (relation.getTo().getConstraint() == null || 
					relation.getTo().getConstraint().isEmpty()) ? relation.getTo().getName() : 
						relation.getTo().getConstraint() + " " + relation.getTo().getName();
			Character character = new Character("quantity of " + 
					toStructure, "at", 
					new StructureIdentifier(relation.getFrom().getName(), relation.getFrom().getConstraint(), 
							relation.getFrom().getOntologyId()));
			return character;
		}
		return null;
	}

	private Character createPresentCharacter(Relation relation) {
		if(relation.getTo() != null && relation.getFrom() != null) {
			String toStructure = (relation.getTo().getConstraint() == null || 
					relation.getTo().getConstraint().isEmpty()) ? relation.getTo().getName() : 
						relation.getTo().getConstraint() + " " + relation.getTo().getName();
			Character character = new Character("quantity of " + 
					toStructure, "at",
					new StructureIdentifier(relation.getFrom().getName(), relation.getFrom().getConstraint(), 
							relation.getFrom().getOntologyId()));
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

}
