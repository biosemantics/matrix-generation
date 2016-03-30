package edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.matrixgeneration.model.Provenance;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

public class SimpleCellValueRawenizer implements CellValueRawenizer {

	private ModifierExtractor modifierExtractor;

	@Inject
	public SimpleCellValueRawenizer(
			@Named("FrequencyModifierPatterns")
			Collection<String> frequencyModifierPatterns,
			@Named("ComparisonModifierPatterns")
			Collection<String> comparisonModifierPatterns) {
		this.modifierExtractor = new ModifierExtractor(frequencyModifierPatterns, comparisonModifierPatterns);
	}
	
	@Override
	public CellValue rawenize(Value value) {
		String result = "";
		String v = value.getValue();
		if(v != null) {
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
			} */
			
			result += v;
			if(value.getUnit() != null)
				result += " " + value.getUnit();
			
			/* if(modifier != null) {
				for(String appendModifierPattern : appendModifierPatterns) {
					if(modifier.matches(appendModifierPattern)) {
						result += " " + modifier;
						break;
					}
				}
			} */
			
		}
		
		return new CellValue(result.trim(), value, new Provenance(this.getClass()));
	}

	@Override
	public boolean isRawenizable(Value value) {
		return true;
	}
}
