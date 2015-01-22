package edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ModifierExtractor {

	private List<String> antiPatterns = new LinkedList<String>();

	public ModifierExtractor(Collection<String> frequencyModifiersPatterns, Collection<String> comparisonModifierPatterns) {
		this.antiPatterns.addAll(frequencyModifiersPatterns);
		this.antiPatterns.addAll(comparisonModifierPatterns);
	}
	
	public String extract(String modifier) {
		String result = "";
		String[] parts = modifier.split(";");
		for(String part : parts) {
			part = part.trim();
			boolean addPart = true;
			for(String antiPattern : antiPatterns) {
				if(part.matches(antiPattern)) {
					addPart = false;
					break;
				}
			}
			if(addPart)
				result += part + ";";
		}
		if(result.length() > 0)
			return result.substring(0, result.length() - 1);
		return result;
	}
	
}
