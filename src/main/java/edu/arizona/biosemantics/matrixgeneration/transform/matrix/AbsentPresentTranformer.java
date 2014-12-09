package edu.arizona.biosemantics.matrixgeneration.transform.matrix;

import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Relation;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class AbsentPresentTranformer implements Transformer {

	private Set<String> presentRelations;
	private Set<String> absentRelations;

	public AbsentPresentTranformer(Set<String> presentRelations, Set<String> absentRelations) {
		this.presentRelations = presentRelations;
		this.absentRelations = absentRelations;
	}

	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getTaxa()) {
			//System.out.println("taxon : " + taxon);
			for(Relation relation : taxon.getRelations()) {
				if(isPresentRelation(relation)) {
					Character character = createPresentCharacter(relation);
					if(character != null) {
						matrix.addCharacter(character);
						Structure structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
						structure.setCharacterValue(character, new Value("present"));
					}
				}
				if(isAbsentRelation(relation)) {
					Character character = createAbsentCharacter(relation);
					if(character != null) {
						matrix.addCharacter(character);
						Structure structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
						structure.setCharacterValue(character, new Value("absent"));
					}
				}
			}
		}
	}

	private Character createAbsentCharacter(Relation relation) {
		if(relation.getTo() != null) {
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
		if(relation.getTo() != null) {
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
