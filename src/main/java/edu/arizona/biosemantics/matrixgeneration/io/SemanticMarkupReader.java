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
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon.Rank;
import edu.arizona.biosemantics.matrixgeneration.model.RankData;
import edu.arizona.biosemantics.matrixgeneration.model.TaxonName;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class SemanticMarkupReader implements Reader {
	
	private File inputDirectory;

	public SemanticMarkupReader(File inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	@Override
	public Matrix read() throws Exception {
		Set<Character> characters = new HashSet<Character>();
		List<TaxonName> taxonNames = new LinkedList<TaxonName>();
		Map<RankData, Taxon> rankTaxaMap = new HashMap<RankData, Taxon>();
		readPlainData(characters, taxonNames, rankTaxaMap);
		List<Taxon> taxa = createTaxaHierarchy(taxonNames, rankTaxaMap);
		return new Matrix(taxa, characters);
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

	private void readPlainData(Set<Character> characters, List<TaxonName> taxonNames, Map<RankData, Taxon> rankTaxaMap) throws JDOMException, IOException {
		SAXBuilder saxBuilder = new SAXBuilder();
		XPathFactory xpathFactory = XPathFactory.instance();
		XPathExpression<Element> sourceXpath = 
				xpathFactory.compile("/treatment/meta/source", Filters.element());
		XPathExpression<Element> taxonIdentificationXpath = 
				xpathFactory.compile("/treatment/taxon_identification[@status='ACCEPTED']/taxon_name", Filters.element());
		XPathExpression<Element> statementXpath = 
				xpathFactory.compile("/treatment/description[@type='morphology']/statement", Filters.element());
		
		HashMap<RankData, RankData> rankDataInstances = new HashMap<RankData, RankData>();
		for(File file : inputDirectory.listFiles()) {
			if(file.isFile()) {
				Taxon taxon = new Taxon();
				
				Document document = saxBuilder.build(file);
				Element sourceElement = sourceXpath.evaluateFirst(document);
				String author = sourceElement.getChild("author").getText();
				String date = sourceElement.getChild("date").getText();
				taxon.setAuthor(author);
				taxon.setYear(date);
				
				LinkedList<RankData> rankDatas = new LinkedList<RankData>();
				for(Element taxonName : taxonIdentificationXpath.evaluate(document)) {
					String rank = taxonName.getAttributeValue("rank");
					String authority = taxonName.getAttributeValue("authority");
					String name = taxonName.getText();
					RankData rankData = new RankData(authority, Rank.valueOf(rank.toUpperCase()), name);
					if(!rankDataInstances.containsKey(rankData))
						rankDataInstances.put(rankData, rankData);
					rankDatas.add(rankDataInstances.get(rankData));
				}
				Collections.sort(rankDatas);
				
				TaxonName taxonName = new TaxonName(rankDatas, author, date);
				taxonNames.add(taxonName);
				
				StringBuilder descriptionBuilder = new StringBuilder();
				for (Element statement : statementXpath.evaluate(document)) {
					String text = statement.getChild("text").getText();
					descriptionBuilder.append(text + ". ");
					
					List<Element> structures = statement.getChildren("structure");
					for(Element structure : structures) {
						taxon.addStructure(createStructure(structure, characters));
					}
				}
				taxon.setDescription(descriptionBuilder.toString().trim());
				rankTaxaMap.put(taxonName.getRankData().getLast(), taxon);
				taxon.setRankData(taxonName.getRankData().getLast());
			}
		}
	}

	private Structure createStructure(Element structure, Set<Character> characters) {
		Structure result = new Structure();
		result.setName(structure.getAttributeValue("name"));
		
		for(Element characterElement : structure.getChildren("character")) {
			String modifier = characterElement.getAttributeValue("modifier");
			String name = characterElement.getAttributeValue("name");
			String value = characterElement.getAttributeValue("value");
			
			Character character = new Character(name);
			characters.add(character);
			
			//TODO: What to do with modifier?
			String fullValue = modifier != null ? modifier + " " + value : value;
			result.setCharacterValue(character, new Value(fullValue));
		}
		
		return result;
	}

}
