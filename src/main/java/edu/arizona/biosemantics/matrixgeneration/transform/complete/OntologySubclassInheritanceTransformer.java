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

public class OntologySubclassInheritanceTransformer implements Transformer {

	private OntologyAccess ontologyAccess;
	private OWLOntologyManager owlOntologyManager;
	private OWLDataFactory owlDataFactory;

	public OntologySubclassInheritanceTransformer() {
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
				log(LogLevel.INFO, "Infer from base character " + character.toString());
				log(LogLevel.INFO, "Infer from base structure " + structureIdentifier.toString());
				Set<Structure> inferedSubclassStructures = getSubclassStructures(structureIdentifier);
				
				for(Structure inferedSubclassStructure : inferedSubclassStructures) {
					log(LogLevel.INFO, "Infered subclass " + inferedSubclassStructure.toString());
					
					for(Taxon taxon : matrix.getTaxa())
						if(matrix.getStructure(structureIdentifier, taxon) != null) 
							matrix.addStructure(inferedSubclassStructure, taxon);
					
					Character inferedCharacter = new AbsentPresentCharacter(new StructureIdentifier(inferedSubclassStructure), 
							bearerIdentifier);
					log(LogLevel.DEBUG, "Create from infered subclass character: " + inferedCharacter.toString());
					matrix.addCharacter(inferedCharacter);
					
					for(Taxon taxon : matrix.getTaxa()) 
						if(isAbsent(character, taxon, matrix)) {
							setAbsent(inferedCharacter, taxon, matrix);	
							log(LogLevel.DEBUG, "Set absent for taxon: " + taxon.toString());
						}
				}
			}
		}
	}

	private void setAbsent(Character inferedCharacter, Taxon taxon, Matrix matrix) {
		Structure inferedStructure = matrix.getStructure(inferedCharacter.getBearerStructureIdentifier(), taxon);
		inferedStructure.addCharacterValue(inferedCharacter, new Value("absent"));
	}

	private boolean isAbsent(Character character, Taxon taxon, Matrix matrix) {
		if(matrix.hasStructure(character.getBearerStructureIdentifier(), taxon)) {
			Structure structure = matrix.getStructure(character.getBearerStructureIdentifier(), taxon);
			if(structure.containsCharacter(character)) {
				Values values = structure.getCharacterValues(character);
				for(Value value : values) 
					if(value.getValue().trim().equals("absent"))
						return true;
			}
		}
		return false;
	}
	
	/*public static void main(String[] args) throws OWLOntologyCreationException {
		OntologyInferenceTransformer tf = new OntologyInferenceTransformer();
		tf.getStructuresWherePartOf(new StructureIdentifier("", "", "http://purl.obolibrary.org/obo/PO_0005708"));
	}*/
	
	private Set<Structure> getSubclassStructures(StructureIdentifier structure) {
		Set<Structure> result = new HashSet<Structure>();
		if(structure.hasStructureOntologyId()) {
			OWLClass subclass = owlDataFactory.getOWLClass(IRI.create(structure.getStructureOntologyId()));
			Set<OWLClass> ancestors = ontologyAccess.getAncestors(subclass);
			for(OWLClass ancestor : ancestors) {
				result.add(new Structure(ontologyAccess.getLabel(ancestor), "", ancestor.getIRI().toString()));
			}
		}
		return result;
	}
}
