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
		//for(Character character : matrix.getCharacters()) {
			StructureIdentifier structure = character.getStructureIdentifier();
			//Set<StructureIdentifier> inferedBearerStructures = getStructuresWherePartOf(structure);
			Set<StructureIdentifier> inferedSubclassStructures = getStructuresWhereSubclass(structure);
			
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
			
			for(StructureIdentifier inferedSubclassStructure : inferedSubclassStructures) {
				log(LogLevel.INFO, "Infered character from subclass structure " + inferedSubclassStructure.getDisplayName() + " " + 
						inferedSubclassStructure.getStructureOntologyId());
				Character inferedCharacter = new Character("quantity", "of", 
						new StructureIdentifier(inferedSubclassStructure.getStructureName(), "", inferedSubclassStructure.getStructureOntologyId()));
				matrix.addCharacter(inferedCharacter);
				for(Taxon taxon : matrix.getTaxa())
					try {
						if(isAbsent(character, taxon, matrix))
							setAbsent(inferedCharacter, taxon, matrix);
					} catch(Throwable t) {
						log(LogLevel.ERROR, "Something needs to be fixed here", t);
					}
			}
		}
	}

	private void setAbsent(Character inferedCharacter, Taxon taxon, Matrix matrix) {
		Structure inferedStructure = matrix.getStructure(inferedCharacter.getStructureIdentifier(), taxon);
		inferedStructure.addCharacterValue(inferedCharacter, new Value("absent"));
	}

	private boolean isAbsent(Character character, Taxon taxon, Matrix matrix) {
		Structure structure = matrix.getStructure(character.getStructureIdentifier(), taxon);
		Values values = structure.getCharacterValues(character);
		for(Value value : values) 
			if(value.getValue().trim().equals("absent"))
				return true;
		return false;
	}
	
	/*public static void main(String[] args) throws OWLOntologyCreationException {
		OntologyInferenceTransformer tf = new OntologyInferenceTransformer();
		tf.getStructuresWherePartOf(new StructureIdentifier("", "", "http://purl.obolibrary.org/obo/PO_0005708"));
	}*/
	
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
