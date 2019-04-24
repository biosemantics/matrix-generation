package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.arizona.biosemantics.matrixgeneration.model.complete.AttributeCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class FixConstraintedStructuresTransformer implements Transformer {

	private LinkedList<Searcher> searchers;
	private Map<String, Boolean> searchCache = new HashMap<String, Boolean>();

	@Inject
	public FixConstraintedStructuresTransformer(TaxonGroup taxonGroup) {
		searchers = new LinkedList<Searcher>();
		
		HashSet<String> entityOntologyNames = new HashSet<String>();
		HashSet<String> qualityOntologyNames = new HashSet<String>();

		for(Ontology ontology : TaxonGroupOntology.getEntityOntologies(taxonGroup)){ 
			entityOntologyNames.add(ontology.name());
		}
		
		/*for(Ontology ontology : TaxonGroupOntology.getExtraQualityOntologies(taxonGroup)){ 
			qualityOntologyNames.add(ontology.name());
		}*/
		searchers.add(new FileSearcher(entityOntologyNames, qualityOntologyNames, Configuration.ontologyDirectory, Configuration.wordNetDirectory, true));
	}

	@Override
	public void transform(Matrix matrix) {
		for(Taxon taxon : matrix.getTaxa()) {
			for(Structure structure : taxon.getStructures()) {
				StructureIdentifier oldStructureIdentifier = new StructureIdentifier(structure);
				fixStructure(structure);
				StructureIdentifier newStructureIdentifier = new StructureIdentifier(structure);
				//if(!oldStructureIdentifier.equals(newStructureIdentifier))
				matrix.updateStructure(structure, oldStructureIdentifier,  newStructureIdentifier, taxon);
			}
		}
	}

	private void fixStructure(Structure structure) {	
		StructureIdentifier structureIdentifier = new StructureIdentifier(structure);

		// old
		// restore order because we only want to search the terms in ontology in order they appeared in text
		// e.g. rarely secondary leaf apex. rarely, secondary and leaf are is_modifier characters to apex structure
		// then we only want to search for, the following, and in that order:
		// rarely secondary leaf apex
		// secondary leaf apex
		// leaf apex
		// apex

		// new
		//simpler search, search only the qualified value closest to structure
		LinkedHashMap<Character, Values> characterModifierValues = new LinkedHashMap<Character, Values>();
		Value lastModifierValue = null;
		Character lastModifierCharacter = null;
		for(Character character : structure.getCharacters()) {
			if(!character.getName().equals("quantity")) {
				Values values = structure.getCharacterValues(character);
				
                //          old: include all modifiers  
				//			for(Value value : values) {
				//				if(value.getIsModifier()) { 
				//					if(!characterModifierValues.containsKey(character))
				//						characterModifierValues.put(character, new Values());
				//					characterModifierValues.get(character).add(value);
				//
				//				}
				//			}

				//new : find qualified modifiers

				for(Value value : values) {
					if(value.getIsModifier())
						lastModifierValue = value;
				}

				if(lastModifierValue!=null && lastModifierValue.getValue()!=null && !lastModifierValue.getValue().contains(" - ")){
					lastModifierCharacter = character;
				}
			}
		}
		
		if(lastModifierCharacter!=null && lastModifierValue!=null){
			if(!characterModifierValues.containsKey(lastModifierCharacter))
				characterModifierValues.put(lastModifierCharacter, new Values());
			characterModifierValues.get(lastModifierCharacter).add(lastModifierValue);
		}

		while(!characterModifierValues.isEmpty()){//only one values. 
			String searchString = "";
			for(Values modifierValues : characterModifierValues.values()) {
				//searchString += modifierValues.getCombinedText(" ") + " ";
				searchString = modifierValues.getCombinedText(" ") + " "; //new: keep only the last modifier, which is the closest to the structure in original description
			}

			searchString = (searchString + structureIdentifier.getDisplayName()).trim();
			log(LogLevel.DEBUG, "Search for " + searchString + " (Is_modifier characters " + characterModifierValues.keySet().size() + ")");

			if(!searchString.isEmpty()) {
				if(searchCache.containsKey(searchString) && searchCache.get(searchString)) {
					updateStructure(structure, characterModifierValues);
					return;
				}

				for(Searcher searcher : searchers) {
					log(LogLevel.DEBUG, "Try searcher " + searcher);
					List<OntologyEntry> ontologyEntries = new LinkedList<OntologyEntry>();
					try {
						ontologyEntries = searcher.getEntityEntries(searchString, "", "");
						if(!ontologyEntries.isEmpty()){
							log(LogLevel.DEBUG, searcher +" found ontology match for " + searchString);
						}
					} catch(Throwable t) {
						log(LogLevel.ERROR, "Searcher failed! ", t);
					}

                    //elongate lower pedicel => match score 1.0, label:  lower region and (part_of some pedicel)

					boolean match = ontologyMatchSuccess(searchString, ontologyEntries);
					searchCache.put(searchString, match);
					if(match) { //check what has been matched!!
						log(LogLevel.DEBUG, "Found ontology match for " + searchString);
						updateStructure(structure, characterModifierValues); //use modifier as structure constraint
						return;
					}
				}
			}
			characterModifierValues.remove(characterModifierValues.keySet().iterator().next());
		}
	}

	private boolean ontologyMatchSuccess(String searchString, List<OntologyEntry> ontologyEntries){
		for(OntologyEntry entry: ontologyEntries){
			if(entry.getLabel().compareTo(searchString)==0){// //elongate lower pedicel => match score 1.0, label:  lower region and (part_of some pedicel)
				return true;
			}
		}
		return false;
	}
	private void updateStructure(Structure structure,
			LinkedHashMap<Character, Values> matchedCharacterModifierValues) {
		//Structure result = structure.clone();

		String constraintAddition = "";
		for(Character matchedCharacter : matchedCharacterModifierValues.keySet()) {
			Values values = structure.getCharacterValues(matchedCharacter);
			if(values != null) {
				Values toRemove = matchedCharacterModifierValues.get(matchedCharacter);
				constraintAddition += toRemove.getCombinedText(" ") + " ";

				values.removeAll(toRemove);
				if(values.isEmpty()) {
					structure.removeCharacterValues(matchedCharacter);
				}
			}
		}
		constraintAddition = constraintAddition.trim();
		if(structure.getConstraint() == null){
			structure.setConstraint(constraintAddition);
			log(LogLevel.DEBUG, "Structure "+structure.getName() + " constraint is now "+constraintAddition);
		}
		else {
			structure.setConstraint(constraintAddition + " " + structure.getConstraint());
		}

	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
