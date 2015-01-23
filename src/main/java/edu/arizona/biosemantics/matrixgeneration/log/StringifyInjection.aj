package edu.arizona.biosemantics.matrixgeneration.log;

import edu.arizona.biosemantics.common.log.AbstractStringifyInjection;
import edu.arizona.biosemantics.common.log.IPrintable;

public aspect StringifyInjection extends AbstractStringifyInjection {

	declare parents : edu.arizona.biosemantics.matrixgeneration.* implements IPrintable;
	declare parents : edu.arizona.biosemantics.matrixgeneration.io..* implements IPrintable;
	//declare parents : edu.arizona.biosemantics.matrixgeneration.model..* implements IPrintable;
	declare parents : edu.arizona.biosemantics.matrixgeneration.transform..* implements IPrintable;

}