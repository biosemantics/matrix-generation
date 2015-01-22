package edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

public class RangeValueCellValueRawenizer implements CellValueRawenizer {


	private ModifierExtractor modifierExtractor;

	public RangeValueCellValueRawenizer(Collection<String> frequencyModifierPatterns, Collection<String> comparisonModifierPatterns) {
		this.modifierExtractor = new ModifierExtractor(frequencyModifierPatterns, comparisonModifierPatterns);
	}

	@Override
	public CellValue rawenize(Value value) {
		String result = "";
		String modifier = value.getModifier();
		
		if(modifier != null) {
			String extractedModifier = modifierExtractor.extract(modifier);
			result = extractedModifier + " ";
		}
		
		/*if(modifier != null) {
			for(String prependModifierPattern : prependModifierPatterns) {
				if(modifier.matches(prependModifierPattern)) {
					result += modifier + " ";
					break;
				}
			}
		}*/
		
		String from = value.getFrom() == null ? value.getFromInclusive() : value.getFrom();
		String to = value.getTo() == null ? value.getToInclusive() : value.getTo();
		
		result += from + (value.getFromUnit() == null ? "" : value.getFromUnit()) +
				" - " + to + (value.getToUnit() == null ? "" : value.getToUnit());
		
		/*if(modifier != null) {
			for(String appendModifierPattern : appendModifierPatterns) {
				if(modifier.matches(appendModifierPattern)) {
					result += " " + modifier;
					break;
				}
			}
		}*/
		
		return new CellValue(result.trim(), Arrays.asList(new String[]{ from, to }), value);
	}

	@Override
	public boolean isRawenizable(Value value) {
		return value.getCharType() != null && value.getCharType().equals("range_value");
	}


}
