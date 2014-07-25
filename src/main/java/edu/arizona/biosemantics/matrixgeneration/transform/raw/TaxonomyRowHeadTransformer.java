package edu.arizona.biosemantics.matrixgeneration.transform.raw;

import edu.arizona.biosemantics.matrixgeneration.model.RankData;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.TaxonName;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class TaxonomyRowHeadTransformer implements RowHeadTransformer {

	@Override
	public RowHead transform(Taxon taxon) {
		TaxonName taxonName = taxon.getTaxonName();
		String name = "";
		for(RankData rankData : taxonName.getRankData()) {
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
		
		name += ":author=" + taxonName.getAuthor() + ",date=" + taxonName.getDate();
		return new RowHead(name, taxon);
	}

}
