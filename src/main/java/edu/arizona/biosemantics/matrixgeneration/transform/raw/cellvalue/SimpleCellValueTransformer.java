package edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;

public class SimpleCellValueTransformer implements CellValueTransformer {

	private List<String> prependModifierPatterns;
	private List<String> appendModifierPatterns;

	public SimpleCellValueTransformer(
			List<String> prependModifierPatterns,
			List<String> appendModifierPatterns) {
		this.prependModifierPatterns = prependModifierPatterns;
		this.appendModifierPatterns = appendModifierPatterns;
	}
	
	@Override
	public CellValue transform(Value value) {
		String result = "";
		String v = value.getValue();
		if(v != null) {
			String modifier = value.getModifier();
			if(modifier != null) {
				for(String prependModifierPattern : prependModifierPatterns) {
					if(modifier.matches(prependModifierPattern)) {
						result += modifier + " ";
						break;
					}
				}
			}
			
			result += v;
			if(value.getUnit() != null)
				result += " " + value.getUnit();
			
			if(modifier != null) {
				for(String appendModifierPattern : appendModifierPatterns) {
					if(modifier.matches(appendModifierPattern)) {
						result += " " + modifier;
						break;
					}
				}
			}
		}
		return new CellValue(result.trim(), value);
	}

}
