package edu.arizona.biosemantics.matrixgeneration.transform.complete;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.ontology.search.OntologyAccess;
import edu.arizona.biosemantics.matrixgeneration.config.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.complete.AbsentPresentCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

public class OntologySuperclassInheritanceTransformer implements Transformer {

	private OntologyAccess ontologyAccess;
	private OWLOntologyManager owlOntologyManager;
	private OWLDataFactory owlDataFactory;

	public OntologySuperclassInheritanceTransformer() {
		Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
		owlOntologyManager = OWLManager.createOWLOntologyManager();
		owlDataFactory = owlOntologyManager.getOWLDataFactory();
		
		File ontologyDirectory = new File(Configuration.ontologyDirectory);
		for(File ontologyFile : ontologyDirectory.listFiles()) {
			try {
				OWLOntology ontology = owlOntologyManager.loadOntologyFromOntologyDocument(ontologyFile);
				ontologies.add(ontology);
			} catch (OWLOntologyCreationException e) {
				log(LogLevel.ERROR, "Couldn't load ontology " + ontologyFile.getAbsolutePath(), e);
			}
		}
		ontologyAccess = new OntologyAccess(ontologies);
	}
	
	@Override
	public void transform(Matrix matrix) {
		Set<Character> iteratable = new HashSet<Character>(matrix.getCharacters());
		for(Character character : iteratable) {
			if(character instanceof AbsentPresentCharacter) {
				StructureIdentifier bearerIdentifier = character.getBearerStructureIdentifier();
				StructureIdentifier structureIdentifier = ((AbsentPresentCharacter)character).getQuantifiedStructure();
				//Set<StructureIdentifier> inferedBearerStructures = getStructuresWherePartOf(structure);
				Set<Structure> inferedSuperclassStructures = getSuperclassStructures(structureIdentifier);
				
				for(Structure inferredSuperclassStructure : inferedSuperclassStructures) {
					log(LogLevel.INFO, "Infered character from subclass structure " + inferredSuperclassStructure.getDisplayName() + " " + 
							inferredSuperclassStructure.getOntologyId());
					
					for(Taxon taxon : matrix.getTaxa())
						if(matrix.getStructure(structureIdentifier, taxon) != null) 
							matrix.addStructure(inferredSuperclassStructure, taxon);
					
					Character inferedCharacter = new AbsentPresentCharacter(new StructureIdentifier(inferredSuperclassStructure), 
							bearerIdentifier);
					log(LogLevel.DEBUG, "Create from infered superclass character: " + inferedCharacter.toString());
					matrix.addCharacter(inferedCharacter);
					
					for(Taxon taxon : matrix.getTaxa()) 
						if(isPresent(character, taxon, matrix)) {
							log(LogLevel.DEBUG, "Set present for taxon: " + taxon.toString());
							setPresent(inferedCharacter, taxon, matrix);
						}
				}
			}
		}
	}

	private void setPresent(Character inferedCharacter, Taxon taxon, Matrix matrix) {
		Structure inferedStructure = matrix.getStructure(inferedCharacter.getBearerStructureIdentifier(), taxon);
		inferedStructure.addCharacterValue(inferedCharacter, new Value("present"));
	}

	private boolean isPresent(Character character, Taxon taxon, Matrix matrix) {
		if(matrix.hasStructure(character.getBearerStructureIdentifier(), taxon)) {
			Structure structure = matrix.getStructure(character.getBearerStructureIdentifier(), taxon);
			if(structure.containsCharacterValue(character)) {
				Values values = structure.getCharacterValues(character);
				for(Value value : values) 
					if(value.getValue().trim().equals("present"))
						return true;
			}
		}
		return false;
	}

	private Set<Structure> getStructuresWherePartOf(StructureIdentifier structure) {
		Set<Structure> result = new HashSet<Structure>();
		if(structure.hasStructureOntologyId()) {
			OWLClass part = owlDataFactory.getOWLClass(IRI.create(structure.getStructureOntologyId()));
			Set<OWLClass> bearers = ontologyAccess.getBearers(part);
			for(OWLClass bearer : bearers) {
				result.add(new Structure(ontologyAccess.getLabel(bearer), null, bearer.getIRI().toString()));
			}
		}
		return result;
	}
	
	private Set<Structure> getSuperclassStructures(StructureIdentifier structure) {
		Set<Structure> result = new HashSet<Structure>();
		if(structure.hasStructureOntologyId()) {
			OWLClass subclass = owlDataFactory.getOWLClass(IRI.create(structure.getStructureOntologyId()));
			Set<OWLClass> descendants = ontologyAccess.getDescendants(subclass);
			for(OWLClass descendant : descendants) {
				result.add(new Structure(ontologyAccess.getLabel(descendant), null, descendant.getIRI().toString()));
			}
		}
		return result;
	}
}
