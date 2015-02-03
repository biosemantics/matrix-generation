package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.ontology.search.FileSearcher;
import edu.arizona.biosemantics.common.ontology.search.Searcher;
import edu.arizona.biosemantics.common.ontology.search.TaxonGroupOntology;
import edu.arizona.biosemantics.common.ontology.search.model.Ontology;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry;
import edu.arizona.biosemantics.common.ontology.search.model.OntologyEntry.Type;
import edu.arizona.biosemantics.matrixgeneration.config.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class FixConstraintedStructuresTransformer implements Transformer {

	private LinkedList<Searcher> searchers;
	private Map<String, Boolean> searchCache = new HashMap<String, Boolean>();
	
	@Inject
	public FixConstraintedStructuresTransformer(TaxonGroup taxonGroup) {
		searchers = new LinkedList<Searcher>();

		for(Ontology ontology : TaxonGroupOntology.getOntologies(taxonGroup)) 
			searchers.add(new FileSearcher(ontology, Configuration.ontologyDirectory, Configuration.wordNetDirectory));
	}
	
	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getTaxa()) {
			for(Structure structure : taxon.getStructures()) {
	
				Structure fixedStructure = isFixStructure(structure);			
				if(fixedStructure != null) {
					matrix.removeStructure(structure, taxon);
					matrix.addStructure(fixedStructure, taxon);
				}
			}
		}
	}

	private Structure isFixStructure(Structure structure) {	
		StructureIdentifier structureIdentifier = new StructureIdentifier(structure);
		
		//restore order because we only want to search the terms in ontology in order they appeared in text
		// e.g. rarely secondary leaf apex. rarely, secondary and leaf are is_modifier characters to apex structure
		// then we only want to search for, the following, and in that order:
		// rarely secondary leaf apex
		// secondary leaf apex
		// leaf apex
		// apex
		LinkedHashMap<Character, Values> characterModifierValues = new LinkedHashMap<Character, Values>();
		
		/*for(Character character : structure.getCharacters()) {
			for(Value value : structure.getCharacterValues(character)) {
				log(LogLevel.DEBUG, "value: " + value.getIsModifier());
				System.out.println(value.getIsModifier());
			}
		}*/
		
		for(Character character : structure.getCharacters()) {
			Values values = structure.getCharacterValues(character);
			
			for(Value value : values) {
				if(value.getIsModifier()) {
					if(!characterModifierValues.containsKey(character))
						characterModifierValues.put(character, new Values());
					characterModifierValues.get(character).add(value);
				}
			}
		}
		
		while(!characterModifierValues.isEmpty()) {
			String searchString = "";
			for(Values modifierValues : characterModifierValues.values()) {
				searchString += modifierValues.getCombinedText(" ") + " ";
			}
			searchString = (searchString + structureIdentifier.getDisplayName()).trim();
			log(LogLevel.DEBUG, "Search for " + searchString + " (Is_modifier characters " + characterModifierValues.keySet().size() + ")");
			
			if(!searchString.isEmpty()) {
				if(searchCache.containsKey(searchString) && searchCache.get(searchString))
					return createNewStructure(structure, characterModifierValues);
				
				for(Searcher searcher : searchers) {
					log(LogLevel.DEBUG, "Try searcher " + searcher);
					List<OntologyEntry> ontologyEntries = searcher.getEntries(searchString, Type.ENTITY);
					
					searchCache.put(searchString, !ontologyEntries.isEmpty());
					if(!ontologyEntries.isEmpty()) {
						log(LogLevel.DEBUG, "Found ontology match for " + searchString);
						return createNewStructure(structure, characterModifierValues);
					}
				}
			}
			characterModifierValues.remove(characterModifierValues.keySet().iterator().next());
		}
		
		return null;
	}

	private Structure createNewStructure(Structure structure,
			LinkedHashMap<Character, Values> matchedCharacterModifierValues) {
		Structure result = structure.clone();
		
		String constraintAddition = "";
		for(Character matchedCharacter : matchedCharacterModifierValues.keySet()) {
			Values values = result.getCharacterValues(matchedCharacter);
			if(values != null) {
				Values toRemove = matchedCharacterModifierValues.get(matchedCharacter);
				constraintAddition += toRemove.getCombinedText(" ") + " ";
				
				values.removeAll(matchedCharacterModifierValues.get(matchedCharacter));
				if(values.isEmpty()) {
					result.removeCharacterValues(matchedCharacter);
				}
			}
		}
		constraintAddition = constraintAddition.trim();
		
		if(result.getConstraint() == null)
			result.setConstraint(constraintAddition);
		else 
			result.setConstraint(constraintAddition + " " + result.getConstraint());
		
		return result;
	}

}
