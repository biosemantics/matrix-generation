package edu.arizona.biosemantics.matrixgeneration.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

import edu.arizona.biosemantics.matrixgeneration.Configuration;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Relation;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class RDFWriter implements Writer {

	private final String propNS = Configuration.rdfPrefixProperty;
	private final String biolNS = Configuration.rdfBiol;
	private final String structureNS = Configuration.rdfPrefixStructure;
	private final String characterNS = Configuration.rdfPrefixCharacter;
	private final String modifierNS = Configuration.rdfPrefixModifier;
	private final String constraintNS = Configuration.rdfPrefixConstraint;
		
	private final Property hasSubstructure = new PropertyImpl(propNS+"#hasSubstructure");
	private final Property hasCharacter = new PropertyImpl(propNS+"#hasCharacter");
	private final Property hasState = new PropertyImpl(propNS+"#hasState");
	private final Property stateValue = new PropertyImpl(propNS+"#stateValue");
	private final Property stateValueFrom = new PropertyImpl(propNS+"#stateValueFrom");
	private final Property stateValueTo = new PropertyImpl(propNS+"#stateValueTo");
	private File file;
	private ValueTypeDeterminer valueTypeDeterminer = new ValueTypeDeterminer();
	
	public RDFWriter(File file) throws IOException {
		this.file = file;
	}
	
	@Override
	public void write(Matrix matrix) throws IOException {
		Model taxonModel = ModelFactory.createDefaultModel();
		OntModel biolModel = ModelFactory.createOntologyModel();
		OntModel descModel = ModelFactory.createOntologyModel();
		biolModel.read(biolNS);
		String xmlBase = "http://www.github.com/biosemantics/" + file.getName();
		taxonModel.setNsPrefix("", xmlBase);
//		taxonModel.setNsPrefix("biosem", "http://cs.umb.edu/biosemantics/");
//		taxonModel.setNsPrefix("biol", biolNS);
//		taxonModel.setNsPrefix("bsprop", propNS);
		
		for(Taxon taxon : matrix.getTaxa()) {
			addStructuresToModel(taxon, matrix, taxonModel, biolModel, descModel);
			addRelationsToModel(taxon, taxonModel, biolModel, descModel);
		}
		
		com.hp.hpl.jena.rdf.model.RDFWriter writer = taxonModel.getWriter("RDF/XML");
		writer.setProperty("allowBadURIs", "true");
		writer.setProperty("xmlbase", xmlBase);
		try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			writer.write(taxonModel, fileOutputStream, "XML/RDF");
			fileOutputStream.flush();
		}
	}

	/**
	 * Add a set of structures to an RDF model.
	 * @param descModel 
	 * @param biolModel 
	 * @param taxonModel
	 * @param structures
	 */
	private void addStructuresToModel(Taxon taxon, Matrix matrix, Model model, OntModel biolModel, OntModel descModel) {
		for(Structure structure : taxon.getStructures()) {
			Resource fromType = descModel.createResource(structureNS+"#"+structure.getName());
			Resource fromResource = model.createResource("#"+structure.getName(), fromType);
			if (structure.getName().equals("whole_organism")) {
				//the whole organism gets a taxonomy attached.
				Property hasTaxonomy = new PropertyImpl(biolNS+"#hasTaxonomy");
				//the taxonomy is of type purl.org/NET/biol/ns#Taxonomy
				Resource taxonomy = model.createResource("#taxonomy", biolModel.getResource(biolNS+"#Taxonomy")); 
				Property name = new PropertyImpl(biolNS+"#name");
				Property classification = new PropertyImpl(biolNS+"#"+taxon.getTaxonIdentification().getRankData().getLast().getRank().toString().toLowerCase());
				model.add(fromResource, hasTaxonomy, taxonomy);
				model.add(taxonomy, name, taxon.getTaxonIdentification().getDisplayName());
				model.add(taxonomy, classification, taxon.getTaxonIdentification().getDisplayName());
			}
			
			//TODO relation part_of and structure hierarchies are not implemented yet and would possibly be on the model not on raw
			Property property = new PropertyImpl(propNS+"#hasSubstructure");
			for(Structure childStructure : matrix.getChildren(structure)) {
				Resource toType = descModel.createResource(structureNS+"#" + childStructure.getDisplayName());
				Resource toResource = model.createResource("#" + childStructure.getDisplayName(), toType);
				model.add(fromResource, property, toResource);
			}
			addCharactersToModel(taxon, matrix, fromResource, structure, model, biolModel, descModel);
		}
	}
	
	public static String resolveFullCharacterName(String shortCharName,	Structure structure, Matrix matrix) {
		String structName = structure.getName();
		String charName = structName + "_" + shortCharName;
		Structure parent = matrix.getParent(structure);
		while(parent != null) {
			structName = parent.getName();
			if(!structName.equals("whole_organism"))
				charName = structName + "_" + charName;
			parent = matrix.getParent(parent);
		}
		return charName;
	}
	
	/**
	 * Add all of the characters of a structure to an RDF model.
	 * @param taxonModel Model to add relations to.
	 * @param subject The structure to add characters of
	 * @param map Map from character name to state
	 */
	@SuppressWarnings({ "rawtypes" })
	private void addCharactersToModel(Taxon taxon, Matrix matrix, Resource subject, Structure structure, 
			Model model, OntModel biolModel, OntModel descModel) {
		for(Character character : structure.getCharacters()) {
			String fullCharName = resolveFullCharacterName(character.getName(), structure, matrix);
			//first, define an RDF resource representing the character
			Resource characterType = descModel.createResource(characterNS+"#"+fullCharName);
			Resource characterDatum = model.createResource("#"+fullCharName, characterType);
			//now make a statement that the subject (structure) has this character
			subject.addProperty(hasCharacter, characterDatum);
			
			Value value = structure.getCharacterValue(character);
			
			//we'll also need a blank node for the state datum
			Resource stateDatum = model.createResource();
			//and also a statement that this character has a state
			characterDatum.addProperty(hasState, stateDatum);
			
			if(valueTypeDeterminer.isRange(value) || valueTypeDeterminer.isEmpty(value)) {	
				
				String from = value.getFrom();
				if(from == null || from.isEmpty())
					from = value.getFromInclusive();
				String to = value.getTo();
				if(to == null || to.isEmpty()) 
					to = value.getToInclusive();
				
				//place the state values as objects in a statement with the state datum
				Literal stateObjectFrom = model.createTypedLiteral(from);
				Statement stmtFrom = new StatementImpl(stateDatum, stateValueFrom, stateObjectFrom);
				model.add(stmtFrom);
				Literal stateObjectTo = model.createTypedLiteral(to);
				Statement stmtTo = new StatementImpl(stateDatum, stateValueTo, stateObjectTo);
				model.add(stmtTo);
				if(value.getModifier() != null && !value.getModifier().isEmpty()) {
					addModifier(model, value, characterDatum, hasState, stateDatum);
					addModifier(model, value, characterDatum, hasState, stateDatum);
				}
				if(value.getConstraint() != null && !value.getConstraint().isEmpty()) {
					addConstraint(model, value, characterDatum, hasState, stateDatum);
					addConstraint(model, value, characterDatum, hasState, stateDatum);
				}
				
				//circa March 2012, all numeric states are normalized to same units
//					if(state.getFromUnit() != null) {
//						Property unitPredicate =
//							new PropertyImpl(rdfProps.getProperty("prefix.property")
//									.concat(s + "_from_unit"));
//						taxonModel.add(subject, unitPredicate,
//								taxonModel.createTypedLiteral(state.getFromUnit()));
//					}
//					if(state.getToUnit() != null) {
//						Property unitPredicate =
//							new PropertyImpl(rdfProps.getProperty("prefix.property")
//									.concat(s + "_to_unit"));
//						taxonModel.add(subject, unitPredicate,
//								taxonModel.createTypedLiteral(state.getToUnit()));
//					}
			}
			else {
				//place the state values as objects in a statement with the state datum
				Literal stateObject = model.createTypedLiteral(value.getValue());
				Statement stmt = new StatementImpl(stateDatum, stateValue, stateObject);
				model.add(stmt);
				if(value.getModifier() != null && !value.getModifier().isEmpty())
					addModifier(model, value, characterDatum, hasState, stateDatum);
				if(value.getConstraint() != null && !value.getConstraint().isEmpty())
					addConstraint(model, value, characterDatum, hasState, stateDatum);
				
				//circa March 2012, all numeric states are normalized to same units
//					if(state.getFromUnit() != null) {
//						Property unitPredicate =
//							new PropertyImpl(rdfProps.getProperty("prefix.property")
//									.concat(s + "_unit"));
//						taxonModel.add(subject, unitPredicate,
//								taxonModel.createTypedLiteral(state.getFromUnit()));
//					}
			}
		}
	}

	/**
	 * We're viewing constraints as reified statments.  If a constraint id referring to another structure is present,
	 * we reify the reified statment, as well
	 * @param taxonModel
	 * @param statement The statement to reify.
	 * @param state
	 */
	private void addConstraint(Model taxonModel, Value value,
			Resource characterDatum, Property property, Resource stateDatum) {
		Property constraintPredicate = new PropertyImpl(constraintNS);
		Literal constraint = taxonModel.createTypedLiteral(value.getConstraint());
		Statement statement = new StatementImpl(characterDatum, property, stateDatum);
		ReifiedStatement reifStmt = taxonModel.createReifiedStatement(statement);
		Statement constraintStmt = taxonModel.createStatement(reifStmt, constraintPredicate, constraint);
		taxonModel.add(constraintStmt);
		
//		Statement stmt1 = new StatementImpl(reifStmt, constraintPredicate, constraint);
//		Structure constraintId = state.getConstraintId();
//
//		ReifiedStatement reifStmtAgain = taxonModel.createReifiedStatement(
//				rdfProps.getProperty("prefix.reified").
//				concat(reifStmt.getLocalName()).concat("_double_reified"), stmt1);
//		Property constrainedBy = new PropertyImpl(rdfProps.getProperty("prefix.constrained_by"));
//		Resource cId = taxonModel.createResource(rdfProps.getProperty("prefix.structure").concat(constraintId.getName()));
//		taxonModel.add(reifStmtAgain, constrainedBy, cId);
//		
//		if(constraintId.getConstraintType() != null) {
//			Property typeProp = new PropertyImpl(rdfProps.getProperty("prefix.constraint_type"));
//			Literal constraintType = taxonModel.createTypedLiteral(constraintId.getConstraintType());
//			taxonModel.add(cId, typeProp, constraintType);
//		}
//		taxonModel.remove(stmt1);
//		taxonModel.remove(statement);
//		
	}

	/**
	 * Attach a modifier to the RDF model.  This means making a reified statement, 
	 * for which 'modifier' is the predicate and the value of modifier is the object.
	 * @param taxonModel
	 * @param statement The statement to reify.
	 * @param state
	 */
	private void addModifier(Model taxonModel, Value value,
			Resource characterDatum, Property property, Resource stateDatum) {
		/*String key = "";
		if(state instanceof SingletonState)
			key = SingletonState.KEY;
		else
			key = RangeState.KEY_FROM;*/
		Property modifierPredicate = new PropertyImpl(modifierNS);
		Literal modifier = taxonModel.createTypedLiteral(value.getModifier());
//		String statementString = rdfProps.getProperty("prefix.reified").
//			concat(characterDatum.getLocalName()).
//			concat("_"+property.getLocalName()).
//			concat("_"+stateDatum.getLocalName());
		Statement statement = new StatementImpl(characterDatum, property, stateDatum); 
		Statement modifierStatement = new StatementImpl(
				taxonModel.createReifiedStatement(statement),
				modifierPredicate,
				modifier);
		taxonModel.add(modifierStatement);
//		taxonModel.remove(statement);
	}

	/**
	 * Adds all of the relations to the model.
	 * @param descModel 
	 * @param biolModel 
	 * @param taxonModel
	 * @param relations
	 */
	private void addRelationsToModel(Taxon taxon, Model model, OntModel biolModel, OntModel descModel) {
		for(Relation r : taxon.getRelations()) {
			Resource from = model.getResource("#"+((Structure)r.getFrom()).getName());
			Resource to = model.getResource("#"+((Structure)r.getTo()).getName());
			Property predicate = new PropertyImpl(propNS+"#"+r.getName());
			model.add(from, predicate, to);
		}
	}

}
