package edu.arizona.biosemantics.matrixgeneration.model;

import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.TaxonomyAncestorInheritanceTransformer;

public class TaxonomyAncestorInheritanceProvenance extends Provenance {

	private RowHead sourceRowHead;
	private ColumnHead sourceColumnHead;

	public TaxonomyAncestorInheritanceProvenance(RowHead sourceRowHead, ColumnHead sourceColumnHead) {
		super(TaxonomyAncestorInheritanceTransformer.class);
		this.sourceRowHead = sourceRowHead;
		this.sourceColumnHead = sourceColumnHead;		
	}

	public RowHead getSourceRowHead() {
		return sourceRowHead;
	}

	public void setSourceRowHead(RowHead sourceRowHead) {
		this.sourceRowHead = sourceRowHead;
	}

	public ColumnHead getSourceColumnHead() {
		return sourceColumnHead;
	}

	public void setSourceColumnHead(ColumnHead sourceColumnHead) {
		this.sourceColumnHead = sourceColumnHead;
	}

}
