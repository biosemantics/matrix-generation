package edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

/**
 * 6. allow the user to decide how to represent  range numerical values, for example either as  
 * length = "2-5 cm" or min_length = 2cm, max_length=5cm.
 * @author rodenhausen
 */
public class AggregatedCellValueRawenizer implements CellValueRawenizer {
	
	private List<CellValueRawenizer> cellValueRawenizers;
	private CellValueRawenizer defaultCellValueRawenizer;
	
	@Inject
	public AggregatedCellValueRawenizer(List<CellValueRawenizer> cellValueRawenizers, 
			@Named("DefaultCellValueRawenizer") CellValueRawenizer defaultCellValueRawenizer) {
		this.cellValueRawenizers = cellValueRawenizers;
		this.defaultCellValueRawenizer = defaultCellValueRawenizer;
	}

	@Override
	public CellValue rawenize(Value value) {
		for(CellValueRawenizer rawenizer : cellValueRawenizers) {
			if(rawenizer.isRawenizable(value)) {
				return rawenizer.rawenize(value);
			}
		}
		return defaultCellValueRawenizer.rawenize(value);
	}

	@Override
	public boolean isRawenizable(Value value) {
		if(defaultCellValueRawenizer.isRawenizable(value))
			return true;
		for(CellValueRawenizer rawenizer : cellValueRawenizers) 
			if(rawenizer.isRawenizable(value)) 
				return true;
		return false;
	}

}
