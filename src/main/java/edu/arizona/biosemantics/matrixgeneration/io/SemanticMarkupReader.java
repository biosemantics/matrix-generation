package edu.arizona.biosemantics.matrixgeneration.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import edu.arizona.biosemantics.matrixgeneration.model.Character.StructureIdentifier;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Relation;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon.Rank;
import edu.arizona.biosemantics.matrixgeneration.model.RankData;
import edu.arizona.biosemantics.matrixgeneration.model.TaxonName;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class SemanticMarkupReader implements Reader {
	
	private File inputDirectory;
	private SAXBuilder saxBuilder = new SAXBuilder();
	private XPathFactory xpathFactory = XPathFactory.instance();
	private XPathExpression<Element> sourceXpath = 
			xpathFactory.compile("/bio:treatment/meta/source", Filters.element(), null, 
					Namespace.getNamespace("bio", "http://www.github.com/biosemantics"));
	private XPathExpression<Element> taxonIdentificationXpath = 
			xpathFactory.compile("/bio:treatment/taxon_identification[@status='ACCEPTED']/taxon_name", Filters.element(), null,
					Namespace.getNamespace("bio", "http://www.github.com/biosemantics"));
	private XPathExpression<Element> statementXpath = 
			xpathFactory.compile("/bio:treatment/description[@type='morphology']/statement", Filters.element(), null, 
					Namespace.getNamespace("bio", "http://www.github.com/biosemantics"));

	public SemanticMarkupReader(File inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	@Override
	public Matrix read() throws Exception {
		Map<Character, Character> characters = new HashMap<Character, Character>();
		
		Map<String, Structure> idStructureMap = new HashMap<String, Structure>();
		Map<String, Relation> idRelationMap = new HashMap<String, Relation>();
		
		List<TaxonName> taxonNames = new LinkedList<TaxonName>();
		Map<RankData, Taxon> rankTaxaMap = new HashMap<RankData, Taxon>();
		
		Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap = 
				new HashMap<StructureIdentifier, Map<Taxon, List<Structure>>>();
		Map<Taxon, File> sourceFilesMap = new HashMap<Taxon, File>();
		
		readPlainData(idStructureMap, idRelationMap, characters, taxonNames, rankTaxaMap, structureIdTaxonStructuresMap, sourceFilesMap);
		List<Taxon> rootTaxa = createTaxaHierarchy(taxonNames, rankTaxaMap);
		
		return new Matrix(rootTaxa, characters, structureIdTaxonStructuresMap, sourceFilesMap, rankTaxaMap);
	}

	private List<Taxon> createTaxaHierarchy(List<TaxonName> taxonNames, 
			Map<RankData, Taxon> rankTaxaMap) {		
		List<Taxon> rootTaxa = new LinkedList<Taxon>();
		for(TaxonName taxonName : taxonNames) {
			LinkedList<RankData> rankData = taxonName.getRankData();
			Taxon taxon = rankTaxaMap.get(rankData.getLast());
			if(rankData.size() == 1)
				rootTaxa.add(taxon);
			if(rankData.size() > 1) {
				int parentRankIndex = rankData.size() - 2;
				Taxon parentTaxon = null;
				while(parentTaxon == null && parentRankIndex >= 0) {
					RankData parentRankData = rankData.get(parentRankIndex);
					parentTaxon = rankTaxaMap.get(parentRankData);
					parentRankIndex--;
				}
				if(parentTaxon == null)
					rootTaxa.add(taxon);
				else
					parentTaxon.addChild(taxon);
			}
		}
		return rootTaxa;
	}

	private void readPlainData(Map<String, Structure> idStructureMap, Map<String, Relation> idRelationMap, Map<Character, Character> characters, 
			List<TaxonName> taxonNames, Map<RankData, Taxon> rankTaxaMap, Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap, 
			Map<Taxon, File> sourceFilesMap) throws JDOMException, IOException {		
		HashMap<RankData, RankData> rankDataInstances = new HashMap<RankData, RankData>();
		for(File file : inputDirectory.listFiles()) {
			if(file.isFile()) {
				Document document = saxBuilder.build(file);
				Element sourceElement = sourceXpath.evaluateFirst(document);
				String author = sourceElement.getChildText("author");
				String date = sourceElement.getChildText("date");
				
				LinkedList<RankData> rankDatas = createRankDatas(document, rankDataInstances);
				TaxonName taxonName = new TaxonName(rankDatas, author, date);
				taxonNames.add(taxonName);
				
				Taxon taxon = createTaxon(document, idStructureMap, idRelationMap, characters, taxonName, structureIdTaxonStructuresMap);
				sourceFilesMap.put(taxon, file);
 				rankTaxaMap.put(taxonName.getRankData().getLast(), taxon);
			}
		}
	}

	private Taxon createTaxon(Document document, Map<String, Structure> idStructureMap, 
			Map<String, Relation> idRelationMap, Map<Character, Character> characters, TaxonName taxonName, 
			Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap) {
		Taxon taxon = new Taxon();
		taxon.setTaxonName(taxonName);
		StringBuilder descriptionBuilder = new StringBuilder();
		for (Element statement : statementXpath.evaluate(document)) {
			String text = statement.getChild("text").getText();
			descriptionBuilder.append(text + ". ");
			
			List<Element> structures = statement.getChildren("structure");
			for(Element structure : structures) {
				taxon.addStructure(createStructure(structure, idStructureMap, characters, taxon, structureIdTaxonStructuresMap));
			}
			
			List<Element> relations = statement.getChildren("relation");
			for(Element relation : relations) {
				taxon.addRelation(createRelation(relation, idStructureMap, idRelationMap));
			}
		}
		taxon.setDescription(descriptionBuilder.toString().trim());
		return taxon;
	}

	private LinkedList<RankData> createRankDatas(Document document, HashMap<RankData, RankData> rankDataInstances) {
		LinkedList<RankData> rankDatas = new LinkedList<RankData>();
		for(Element taxonName : taxonIdentificationXpath.evaluate(document)) {
			String rank = taxonName.getAttributeValue("rank");
			String date = taxonName.getAttributeValue("date");
			String authority = taxonName.getAttributeValue("authority");
			String name = taxonName.getText();
			RankData rankData = new RankData(authority, date, Rank.valueOf(rank.toUpperCase()), name);
			if(!rankDataInstances.containsKey(rankData))
				rankDataInstances.put(rankData, rankData);
			rankDatas.add(rankDataInstances.get(rankData));
		}
		Collections.sort(rankDatas);
		
		for(int i=1; i<rankDatas.size(); i++) {
			rankDatas.get(i).setParent(rankDatas.get(0));
		}
		return rankDatas;
	}

	private Relation createRelation(Element relation, Map<String, Structure> idStructureMap, Map<String, Relation> idRelationMap) {
		Relation result = new Relation();
		String fromId = relation.getAttributeValue("from");
		String toId = relation.getAttributeValue("to");
		Structure fromStructure = idStructureMap.get(fromId);
		Structure toStructure = idStructureMap.get(toId);
		String id = relation.getAttributeValue("id");
		idRelationMap.put(id, result);
		String name = relation.getAttributeValue("name");
		String negated = relation.getAttributeValue("negation");
		String alterName = relation.getAttributeValue("alter_name");
		String geographicalConstraint = relation.getAttributeValue("geographical_constraint");
		String inBrackets = relation.getAttributeValue("in_brackets");
		String modifier = relation.getAttributeValue("modifier");
		String notes = relation.getAttributeValue("notes");
		String ontologyId = relation.getAttributeValue("ontologyid");
		String organConstraint = relation.getAttributeValue("organ_constraint");
		String parallelismConstraint = relation.getAttributeValue("parallelism_constraint");
		String provenance = relation.getAttributeValue("provenance");
		String taxonConstraint = relation.getAttributeValue("taxon_constraint");
		
		result.setAlterName(alterName);
		result.setFrom(fromStructure);
		result.setGeographicalConstraint(geographicalConstraint);
		result.setId(id);
		result.setInBrackets(inBrackets);
		result.setModifier(modifier);
		result.setName(name);
		result.setNegated(Boolean.valueOf(negated));
		result.setNotes(notes);
		result.setOntologyId(ontologyId);
		result.setOrganConstraint(organConstraint);
		result.setParallelismConstriant(parallelismConstraint);
		result.setProvenance(provenance);
		result.setTaxonConstraint(taxonConstraint);
		result.setTo(toStructure);
		return result;
	}

	private Structure createStructure(Element structure, Map<String, Structure> idStructureMap, Map<Character, Character> characters, 
			Taxon taxon, Map<StructureIdentifier, Map<Taxon, List<Structure>>> structureIdTaxonStructuresMap) {
		Structure result = new Structure();
		String id = structure.getAttributeValue("id");
		idStructureMap.put(id, result);
		
		result.setName(structure.getAttributeValue("name"));
		result.setAlterName(structure.getAttributeValue("alter_name"));
		result.setConstraint(structure.getAttributeValue("constraint"));
		result.setConstraintid(structure.getAttributeValue("constraintid"));
		result.setGeographicalConstraint(structure.getAttributeValue("geographical_constraint"));
		result.setId(id);
		result.setInBracket(structure.getAttributeValue("in_bracket"));
		result.setInBrackets(structure.getAttributeValue("in_brackets"));
		result.setNameOriginal(structure.getAttributeValue("name_original"));
		result.setParallelismConstraint(structure.getAttributeValue("paralellism_constraint"));
		result.setNotes(structure.getAttributeValue("notes"));
		result.setOntologyId(structure.getAttributeValue("ontologyid"));
		result.setProvenance(structure.getAttributeValue("provenance"));
		result.setTaxonConstraint(structure.getAttributeValue("taxon_constraint"));
		
		for(Element characterElement : structure.getChildren("character")) {

			String v = characterElement.getAttributeValue("value");
			Value value = new Value(v);
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
						
			String name = characterElement.getAttributeValue("name");
			Character character = new Character(name, result.getName(), result.getConstraint());
			if(!characters.containsKey(character))
				characters.put(character, character);
			character = characters.get(character);
			
			result.setCharacterValue(character, value);
			
			if(!structureIdTaxonStructuresMap.containsKey(character.getStructureIdentifier()))
				structureIdTaxonStructuresMap.put(character.getStructureIdentifier(), new HashMap<Taxon, List<Structure>>());
			if(!structureIdTaxonStructuresMap.get(character.getStructureIdentifier()).containsKey(taxon))
				structureIdTaxonStructuresMap.get(character.getStructureIdentifier()).put(taxon, new LinkedList<Structure>());
			structureIdTaxonStructuresMap.get(character.getStructureIdentifier()).get(taxon).add(result);
		}
		
		return result;
	}

}
