package edu.arizona.biosemantics.matrixgeneration.io.complete.in;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;
import edu.arizona.biosemantics.matrixgeneration.model.SemanticMarkupProvenance;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Character;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Relation;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Statement;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.complete.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;

public class MicroPIESemanticMarkupReader extends SemanticMarkupReader{

	@Inject
	public MicroPIESemanticMarkupReader(@Named("InputDirectory") String inputDirectory) {
		super(inputDirectory);
	}

	@Override
	protected Taxon createTaxon(Document document, Map<String, Structure> idStructureMap, 
			Map<String, Relation> idRelationMap, Map<Character, Character> characters, TaxonIdentification taxonIdentification, 
			Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap, String wholeOrganismOntologyId) {
		Taxon taxon = new Taxon();
		taxon.setTaxonIdentification(taxonIdentification);
		StringBuilder descriptionBuilder = new StringBuilder();
		
		createWholeOrganism(wholeOrganismOntologyId, taxon, structureIdTaxonStructuresMap);
		for (Element statement : statementXpath.evaluate(document)) {
			String text = statement.getChild("text").getText();
			descriptionBuilder.append(text);//+ ". " micropie results contain dot.
			
			//create and add the statement
			Statement sent = new Statement(statement.getAttribute("id").getValue(),text);
			taxon.addStatement(sent);
			
			List<Element> bioEntities = statement.getChildren("biological_entity");
			for(Element bioentity : bioEntities) {
				if(bioentity.getAttribute("type") != null && bioentity.getAttributeValue("type").equals("structure")){
					this.createStructure(sent, bioentity, idStructureMap, characters, taxon, structureIdTaxonStructuresMap);
				}
			}
			
			List<Element> relations = statement.getChildren("relation");
			for(Element relation : relations) {
				taxon.addRelation(createRelation(relation, idStructureMap, idRelationMap));
			}
		}
		taxon.setDescription(descriptionBuilder.toString().trim());
		return taxon;
	}
	
	@Override
	protected Structure createStructure(Statement statement, Element structure, Map<String, Structure> idStructureMap, Map<Character, Character> characters, 
			Taxon taxon, Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap) {
		Structure wholeOgranism = taxon.getWholeOrganism();
		StructureIdentifier structureIdentifier = new StructureIdentifier(wholeOgranism);
		for(Element characterElement : structure.getChildren("character")) {
			String name = characterElement.getAttributeValue("name");
			Character character = createCharacter(taxon, structureIdentifier, name);
			if(!characters.containsKey(character))
				characters.put(character, character);
			character = characters.get(character);
			
			String v = characterElement.getAttributeValue("value");
			Value value = new Value(v, new SemanticMarkupProvenance(taxon, character));
			value.setCharType(characterElement.getAttributeValue("char_type"));
			value.setConstraint(characterElement.getAttributeValue("constraint"));
			value.setConstraintId(characterElement.getAttributeValue("constraintid"));
			value.setFrom(characterElement.getAttributeValue("from"));
			value.setFromInclusive(characterElement.getAttributeValue("from_inclusive"));
			value.setFromUnit(characterElement.getAttributeValue("from_unit"));
			value.setModifier(characterElement.getAttributeValue("modifier"));
			value.setGeographicalConstraint(characterElement.getAttributeValue("geographical_constraint"));
			value.setInBrackets(characterElement.getAttributeValue("in_brackets"));
			value.setOrganConstraint(characterElement.getAttributeValue("organ_constraint"));
			value.setOtherConstraint(characterElement.getAttributeValue("other_constraint"));
			value.setParallelismConstraint(characterElement.getAttributeValue("parallelism_constraint"));
			value.setTaxonConstraint(characterElement.getAttributeValue("taxon_constraint"));
			value.setTo(characterElement.getAttributeValue("to"));
			value.setToInclusive(characterElement.getAttributeValue("to_inclusive"));
			value.setToUnit(characterElement.getAttributeValue("to_unit"));
			value.setType(characterElement.getAttributeValue("type"));
			value.setUpperRestricted(characterElement.getAttributeValue("upper_restricted"));
			value.setUnit(characterElement.getAttributeValue("unit"));
			value.setValue(characterElement.getAttributeValue("value"));
			value.setOntologyId(characterElement.getAttributeValue("ontologyid"));
			value.setProvenance(characterElement.getAttributeValue("provenance"));
			value.setNotes(characterElement.getAttributeValue("notes"));
			value.setStatement(statement);
			System.out.println("create value:["+value.getValue()+"] ====> sources:"+statement.getText());
			boolean isModifier = false;
			try {
				isModifier = Boolean.parseBoolean(characterElement.getAttributeValue("is_modifier"));
			} catch(Exception e) {	} 
			value.setIsModifier(isModifier);

			wholeOgranism.addCharacterValue(character, value);
		}
	
		return wholeOgranism;
	}
	
}
