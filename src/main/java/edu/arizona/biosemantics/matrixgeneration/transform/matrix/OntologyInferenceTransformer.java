package edu.arizona.biosemantics.matrixgeneration.transform.matrix;

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
import edu.arizona.biosemantics.matrixgeneration.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class OntologyInferenceTransformer implements Transformer {

	private OntologyAccess ontologyAccess;
	private OWLOntologyManager owlOntologyManager;
	private OWLDataFactory owlDataFactory;

	public OntologyInferenceTransformer() {
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
		for(Character character : matrix.getCharacters()) {
			StructureIdentifier structure = character.getStructureIdentifier();
			//Set<StructureIdentifier> inferedBearerStructures = getStructuresWherePartOf(structure);
			Set<StructureIdentifier> inferedSuperclassStructures = getStructuresWhereSubclass(structure);
			
			/*for(StructureIdentifier inferedBearerStructure : inferedBearerStructures) {
			 	log(LogLevel.INFO, "Infered character from bearer structure " + inferedBearerStructure.getDisplayName() + " " + 
						inferedBearerStructure.getStructureOntologyId());
				Character inferedCharacter = new Character("quantity of " + 
						structure.getStructureName(), "at", 
						new StructureIdentifier(inferedBearerStructure.getStructureName(), "", 
								inferedBearerStructure.getStructureOntologyId()));
				matrix.addCharacter(inferedCharacter);
				for(Taxon taxon : matrix.getTaxa())
					if(isPresent(character, taxon, matrix))
						setPresent(inferedCharacter, taxon, matrix);
			}*/
			
			for(StructureIdentifier inferedSuperclassStructure : inferedSuperclassStructures) {
				log(LogLevel.INFO, "Infered character from superclass structure " + inferedSuperclassStructure.getDisplayName() + " " + 
						inferedSuperclassStructure.getStructureOntologyId());
				Character inferedCharacter = new Character("quantity", "of", 
						new StructureIdentifier(inferedSuperclassStructure.getStructureName(), "", inferedSuperclassStructure.getStructureOntologyId()));
				matrix.addCharacter(inferedCharacter);
				for(Taxon taxon : matrix.getTaxa())
					if(isPresent(character, taxon, matrix))
						setPresent(inferedCharacter, taxon, matrix);
			}
		}
	}

	private void setPresent(Character inferedCharacter, Taxon taxon, Matrix matrix) {
		Structure inferedStructure = matrix.getStructure(inferedCharacter.getStructureIdentifier(), taxon);
		inferedStructure.setCharacterValue(inferedCharacter, new Value("present"));
	}

	private boolean isPresent(Character character, Taxon taxon, Matrix matrix) {
		Structure structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
		Value value = structure.getCharacterValue(character);
		return value != null && !value.getValue().trim().isEmpty();
	}
	
	/*public static void main(String[] args) throws OWLOntologyCreationException {
		OntologyInferenceTransformer tf = new OntologyInferenceTransformer();
		tf.getStructuresWherePartOf(new StructureIdentifier("", "", "http://purl.obolibrary.org/obo/PO_0005708"));
	}*/

	private Set<StructureIdentifier> getStructuresWherePartOf(StructureIdentifier structure) {
		Set<StructureIdentifier> result = new HashSet<StructureIdentifier>();
		if(structure.hasStructureOntologyId()) {
			OWLClass part = owlDataFactory.getOWLClass(IRI.create(structure.getStructureOntologyId()));
			Set<OWLClass> bearers = ontologyAccess.getBearers(part);
			for(OWLClass bearer : bearers) {
				result.add(new StructureIdentifier(ontologyAccess.getLabel(bearer), "", bearer.getIRI().toString()));
			}
		}
		return result;
	}
	
	private Set<StructureIdentifier> getStructuresWhereSubclass(StructureIdentifier structure) {
		Set<StructureIdentifier> result = new HashSet<StructureIdentifier>();
		if(structure.hasStructureOntologyId()) {
			OWLClass subclass = owlDataFactory.getOWLClass(IRI.create(structure.getStructureOntologyId()));
			Set<OWLClass> ancestors = ontologyAccess.getAncestors(subclass);
			for(OWLClass ancestor : ancestors) {
				result.add(new StructureIdentifier(ontologyAccess.getLabel(ancestor), "", ancestor.getIRI().toString()));
			}
		}
		return result;
	}
}
