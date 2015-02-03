package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.AbsentPresentCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Relation;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

/**
 * "flower without large petal", usually in markup looks as follows
 * <structure name="flower"> .. </structure>
 * <structure name="petal">
 *    <character type="size" value="large"/>
 * </structure>
 * <relation from.. to.. "without/>
 * 
 * In the matrix, we usually don't want to see attribute characters such as "size of petal" = large if the text actually expressed that it is absent
 * We usually only want to know "presence of large petal of/at flower" = absent.
 * 
 * This transformer intents to take care of this situation
 * @author rodenhausen
 *
 */
public class RemoveAbsentStructuresTransformer implements Transformer {

	private Set<String> absentRelations;

	@Inject
	public RemoveAbsentStructuresTransformer(@Named("AbsentRelation")Set<String> absentRelations) {
		this.absentRelations = absentRelations;
	}

	@Override
	public void transform(Matrix matrix) {		
		for(Taxon taxon : matrix.getTaxa()) {
			removeFromRelations(matrix, taxon);
		}
	}

	private void removeFromRelations(Matrix matrix, Taxon taxon) {
		for(Relation relation : taxon.getRelations()) {
			if(isAbsentRelation(relation)) {
				Structure absentStructure = relation.getTo();
				if(absentStructure != null) {
					log(LogLevel.DEBUG, "Remove structure " + absentStructure.getDisplayName() + " from taxon " + taxon.getTaxonIdentification().getDisplayName() + ". "
							+ "Responsible relation " + relation.getName());
					matrix.removeStructure(absentStructure, taxon);
				}
			}
		}
	}

	private boolean isAbsentRelation(Relation relation) {
		return absentRelations.contains(relation.getName());
	}
}
