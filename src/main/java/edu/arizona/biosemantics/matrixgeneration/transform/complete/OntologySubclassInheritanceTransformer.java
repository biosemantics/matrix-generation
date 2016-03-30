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
import edu.arizona.biosemantics.matrixgeneration.model.Provenance;
import edu.arizona.biosemantics.matrixgeneration.model.complete.AbsentPresentCharacter;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Values;

/**
 * OntologySubclassInheritanceTransformer looks at AbsentPresentCharacters.
 * For each AbsentPresentCharacter look at ontology subclasses of the beared structures A.
 * Add a AbsentPresentCharacter with found subclasses as beared structures B.
 * If a beared structure A is absent in a taxon set the AbsentPresentCharacter of beared structures B
 * also to absent.
 * 
 * Reasoning: If flower is absent, petal must be absent too.
 * @author Thomas
 */
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
				StructureIdentifier bearerStructure = character.getBearerStructureIdentifier();
				StructureIdentifier bearedStructure = ((AbsentPresentCharacter)character).getBearedStructureIdentifier();
				log(LogLevel.INFO, "Infer from base character " + character.toString());
				log(LogLevel.INFO, "Infer from beared structure " + bearedStructure.toString());
				Set<Structure> inferredSubclassStructures = getSubclassStructures(bearedStructure);
				
				for(Structure inferredSubclassStructure : inferredSubclassStructures) {
					log(LogLevel.INFO, "Inferred subclass " + inferredSubclassStructure.toString());
					
					for(Taxon taxon : matrix.getTaxa())
						if(matrix.getStructure(bearedStructure, taxon) != null) 
							matrix.addStructure(inferredSubclassStructure, taxon);
					
					Character inferredCharacter = new AbsentPresentCharacter(new StructureIdentifier(inferredSubclassStructure), 
							bearerStructure, this);
					log(LogLevel.DEBUG, "Create from inferred subclass character: " + inferredCharacter.toString());
					matrix.addCharacter(inferredCharacter);
					
					for(Taxon taxon : matrix.getTaxa()) 
						if(isAbsent(character, taxon, matrix)) {
							log(LogLevel.DEBUG, "Set absent for taxon: " + taxon.toString());
							setAbsent(inferredCharacter, taxon, matrix);	
						}
				}
			}
		}
	}

	private void setAbsent(Character inferredCharacter, Taxon taxon, Matrix matrix) {
		Structure bearerStructure = matrix.getStructure(inferredCharacter.getBearerStructureIdentifier(), taxon);
		bearerStructure.addCharacterValue(inferredCharacter, new Value("absent", new Provenance(this.getClass())));
	}

	private boolean isAbsent(Character character, Taxon taxon, Matrix matrix) {
		if(matrix.hasStructure(character.getBearerStructureIdentifier(), taxon)) {
			Structure bearerStructure = matrix.getStructure(character.getBearerStructureIdentifier(), taxon);
			if(bearerStructure.containsCharacter(character)) {
				Values values = bearerStructure.getCharacterValues(character);
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
			Set<OWLClass> descendants = ontologyAccess.getDescendants(subclass);
			for(OWLClass descendant : descendants) {
				String name = ontologyAccess.getLabel(descendant);
				if(name != null)
					result.add(new Structure(name, null, descendant.getIRI().toString()));
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
