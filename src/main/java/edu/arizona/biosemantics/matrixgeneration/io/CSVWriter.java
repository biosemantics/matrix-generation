package edu.arizona.biosemantics.matrixgeneration.io;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.RankData;
import edu.arizona.biosemantics.matrixgeneration.model.Structure;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.Character;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

public class CSVWriter implements Writer {

	private File file;

	public CSVWriter(File file) {
		this.file = file;
	}
	
	@Override
	public void write(Matrix matrix) throws Exception {
		Set<Character> characters = matrix.getCharacters();
		List<Taxon> rootTaxa = matrix.getTaxa();
		
		au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(new FileWriter(file));
		String[] line = new String[characters.size() + 1];
		int i=0;
		line[i++] = "Taxon Concept";
		for(Character character : characters)
			line[i++] = character.toString();
		csvWriter.writeNext(line);
		for(Taxon taxon : rootTaxa)
			writeTaxon(csvWriter, taxon, characters, new LinkedList<RankData>());
		csvWriter.flush();
		csvWriter.close();
	}

	private void writeTaxon(au.com.bytecode.opencsv.CSVWriter csvWriter, Taxon taxon, Set<Character> characters, 
			LinkedList<RankData> rankDatas) {
		rankDatas.offer(taxon.getRankData());
		String name = "";
		for(RankData rankData : rankDatas) {
			name += rankData.getRank()+ "=" + rankData.getName() + "_" + rankData.getAuthor();
		}
		name += "_" + taxon.getAuthor() + "_" + taxon.getYear();
		String[] line = new String[characters.size() + 1];
		int i=0;
		line[i++] = name;
		for(Character character : characters) {
			String structureName = character.getStructureName();
			Structure structure = taxon.getStructure(structureName);
			if(structure != null) {
				Value value = structure.getCharacterValue(character);
				if(structure.containsCharacterValue(character))
					line[i++] = value.getValue();
				else
					line[i++] = "";
			} else {
				line[i++] = "";
			}
		}
		csvWriter.writeNext(line);
		
		for(Taxon child : taxon.getChildren())
			writeTaxon(csvWriter, child, characters, rankDatas);
		
		rankDatas.pollLast();
	}

}
