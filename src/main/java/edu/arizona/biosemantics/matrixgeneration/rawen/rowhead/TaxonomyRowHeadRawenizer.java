package edu.arizona.biosemantics.matrixgeneration.rawen.rowhead;

import java.util.LinkedList;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.common.taxonomy.RankData;
import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class TaxonomyRowHeadRawenizer implements RowHeadRawenizer {

	@Override
	public RowHead transform(Taxon taxon) {
		TaxonIdentification taxonIdentification = taxon.getTaxonIdentification();
		String name = getBiologicalName(taxonIdentification);
		
				
		/*for(RankData rankData : taxonIdentification.getRankData()) {

			name += rankData.getRank()+ "=" + rankData.getName(); 
			if(rankData.getAuthor() != null && !rankData.getAuthor().isEmpty()) {
				name += ",authority=" + rankData.getAuthor();
			}
			if(rankData.getDate() != null && !rankData.getDate().isEmpty()) {
				name += ",date=" + rankData.getDate();
			}
			name += ";";
		}
		name = name.substring(0, name.length() - 1);
		
		name += ":author=" + taxonIdentification.getAuthor() + ",date=" + taxonIdentification.getDate();
		*/
		return new RowHead(name, taxon);
	}

	private String getBiologicalName(TaxonIdentification taxonIdentification) {
		LinkedList<RankData> rankDatas = new LinkedList<RankData>(taxonIdentification.getRankData());
		
		RankData last = rankDatas.getLast();
		if(Rank.aboveGenus(last.getRank()))
			return last.getName();
		
		String result = "";
		while(true) {
			if(rankDatas.isEmpty())
				break;
			RankData rankData = rankDatas.removeLast();
			if(Rank.aboveGenus(rankData.getRank()))
				break;
			else
				result = rankData.getName() + " " + result;
		}
		return result;
	}

}
