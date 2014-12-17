package edu.arizona.biosemantics.matrixgeneration.transform.raw.rowhead;

import edu.arizona.biosemantics.common.taxonomy.RankData;
import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class TaxonomyRowHeadTransformer implements RowHeadTransformer {

	@Override
	public RowHead transform(Taxon taxon) {
		TaxonIdentification taxonIdentification = taxon.getTaxonIdentification();
		String name = "";
		for(RankData rankData : taxonIdentification.getRankData()) {
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
		return new RowHead(name, taxon);
	}

}
