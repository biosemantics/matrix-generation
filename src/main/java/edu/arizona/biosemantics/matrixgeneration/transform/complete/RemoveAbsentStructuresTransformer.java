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
 * TODO: Have to be more careful with this transformation. There can be cases where isAbsentRelation which leads to removal of structure (without indication), 
 * but at the same time the structure still is present and described (indication of valve). that then leads to it not be found in the matrix for that taxon anymore.
 * E.g. 
 *          <text>cup...without indication of valves,...</text>
         <biological_entity id="o90" name="cup" name_original="cup" type="structure"/>
         <biological_entity id="o91" name="indication" name_original="indication" type="structure" />
         <biological_entity id="o92" name="valve" name_original="valves" ontologyid="http://purl.obolibrary.org/obo/PO_0025228" type="structure" />
         <relation from="o90" id="r16" name="without" negation="false" to="o91" />
         <relation from="o91" id="r17" name="part_of" negation="false" to="o92" />
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
