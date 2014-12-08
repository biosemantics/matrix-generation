package edu.arizona.biosemantics.matrixgeneration.transform.matrix;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OntologyAccess {

	private Set<OWLOntology> ontologies;
	private OWLDataFactory owlDataFactory;
	private HashMap<OWLOntology, OWLReasoner> ontologyReasonerMap;
	private OWLObjectProperty partof;
	
	public OntologyAccess(Set<OWLOntology> ontologies) {
		this.ontologies = ontologies;
		
		OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
		owlDataFactory = owlOntologyManager.getOWLDataFactory();
		partof = owlDataFactory.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/BFO_0000050"));
		
		OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		ontologyReasonerMap = new HashMap<OWLOntology, OWLReasoner>();
		for(OWLOntology ontology : ontologies)
			ontologyReasonerMap.put(ontology, reasonerFactory.createReasoner(ontology));
	}
	
	public String getLabel(OWLClass owlClass) {
		OWLAnnotation label = (OWLAnnotation) this.getLabels(owlClass).toArray()[0];
		return this.getRefinedOutput(label.getValue().toString());
	}
	
	public Set<OWLAnnotation> getLabels(OWLClass c) {
		return this.getAnnotationByIRI(c, OWLRDFVocabulary.RDFS_LABEL.getIRI());
	}
	
	public Set<OWLAnnotation> getAnnotationByIRI(OWLClass owlClass, IRI iri) {
		Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
		for(OWLOntology ontology : ontologies){
			result.addAll(owlClass.getAnnotations(ontology, owlDataFactory.getOWLAnnotationProperty(iri)));
		}
		return result;
	}
	
	public Set<OWLClass> getAncestors(OWLClass owlClass) {
		Set<OWLClass> result = new HashSet<OWLClass>();
		for(OWLReasoner reasoner : ontologyReasonerMap.values()) {
			NodeSet<OWLClass> ancestorNodes = reasoner.getSuperClasses(owlClass, false);
			
			for(Node<OWLClass> ancestorNode : ancestorNodes) {
				OWLClass ancestor = ancestorNode.getRepresentativeElement();
				if(!ancestor.isBottomEntity() && !ancestor.isTopEntity())
					result.add(ancestor);
			}
		}
		return result;
	}
	
	public Set<OWLClass> getBearers(OWLClass owlClass) {
		Set<OWLClass> result = new HashSet<OWLClass>();
		for(OWLReasoner reasoner : ontologyReasonerMap.values()) {
			OWLClassExpression partOfClass = owlDataFactory.getOWLObjectSomeValuesFrom(partof, owlClass);
			NodeSet<OWLClass> bearerNodes = reasoner.getSubClasses(partOfClass, false);
			
			for(Node<OWLClass> bearerNode : bearerNodes) {
				OWLClass bearer = bearerNode.getRepresentativeElement();
				if(!bearer.isBottomEntity() && !bearer.isTopEntity())
					result.add(bearer);
			}
				
		}
		return result;
	}
	
	/**
	 * Remove the non-readable or non-meaningful characters in the retrieval
	 * from OWL API, and return the refined output.
	 *
	 * @param origin the origin??? what does it look like?
	 * @return the refined output ??? what does it look like??
	 */
	public String getRefinedOutput(String origin) {
		// Annotation(<http://www.geneontology.org/formats/oboInOwl#hasExactSynonym>
		// W)
		if (origin.startsWith("Annotation")) {
			origin = origin.replaceFirst("^Annotation.*>\\s+", "")
					.replaceFirst("\\)\\s*$", "").trim();
		}

		/*
		 * Remove the ^^xsd:string tail from the returned annotation value
		 */
		return origin.replaceAll("\\^\\^xsd:string", "").replaceAll("\"", "")
				.replaceAll("\\.", "").replaceFirst("@.*", ""); //remove lang info @en
	}
}
