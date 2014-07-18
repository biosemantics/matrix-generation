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
			name += rankData.getRank()+ "=" + rankData.getName() + "_" + rankData.getAuthor();
		}
		name += "_" + taxonName.getAuthor() + "_" + taxonName.getDate();
		return new RowHead(name, taxon);
	}

	

}
