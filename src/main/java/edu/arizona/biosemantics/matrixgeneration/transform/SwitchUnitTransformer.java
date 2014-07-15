package edu.arizona.biosemantics.matrixgeneration.transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

/**
 * 4. allow the user to set the unit (m or mm etc.) to be used.
 * @author rodenhausen
 */
public class SwitchUnitTransformer implements Transformer {

	private Map<String, Double> units = new HashMap<String, Double>();
	private String targetUnit;
	
	public SwitchUnitTransformer(String targetUnit) {
		initUnits();
		this.targetUnit = targetUnit;
	}
	
	//make sure for UTF8 encoding
	private void initUnits() {
		units.put("ym", 10.0);
		units.put("mm", 1.0);
		units.put("cm", 0.1);
		units.put("dm", 0.01);
		units.put("m", 0.001);
		units.put("km", 0.000001);
	}

	@Override
	public void transform(Matrix matrix) {
		/*for(Taxon taxon : matrix.getTaxa()) {
			for(Character character : matrix.getCharacters()) {
				if(taxon.containsStructure(character.getStructureName()) {
					Structure structure = taxon.getStructure(character.getStructureName());
					structure.getValue(character);
				}
			}
		}*/
		Double targetUnitFactor = units.get(targetUnit);
		for(Value value : matrix.getValues()) {
			if(isNumeric(value.getValue())) {
				Double doubleValue = Double.parseDouble(value.getValue());
				if(units.containsKey(value.getUnit())) {
					Double baseUnitFactor = units.get(value.getUnit());
					value.setValue(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setUnit(targetUnit);
				}
			}
			if((isNumeric(value.getFrom()))) {
				Double doubleValue = Double.parseDouble(value.getFrom());
				if(units.containsKey(value.getFromUnit())) {
					Double baseUnitFactor = units.get(value.getFromUnit());
					value.setFrom(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setFromUnit(targetUnit);
				}
			}
			if((isNumeric(value.getTo()))) {
				Double doubleValue = Double.parseDouble(value.getTo());
				if(units.containsKey(value.getToUnit())) {
					Double baseUnitFactor = units.get(value.getToUnit());
					value.setTo(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setToUnit(targetUnit);
				}
			}
			if((isNumeric(value.getFromInclusive()))) {
				Double doubleValue = Double.parseDouble(value.getFromInclusive());
				if(units.containsKey(value.getFromUnit())) {
					Double baseUnitFactor = units.get(value.getFromUnit());
					value.setFromInclusive(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setFromUnit(targetUnit);
				}
			}
			if((isNumeric(value.getToInclusive()))) {
				Double doubleValue = Double.parseDouble(value.getToInclusive());
				if(units.containsKey(value.getToUnit())) {
					Double baseUnitFactor = units.get(value.getToUnit());
					value.setToInclusive(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setToUnit(targetUnit);
				}
			}
		}
	}

	/**
	 * normalize somewhere 0.1 and 0,1 ? 
	 * @param value
	 * @return
	 */
	private boolean isNumeric(String value) {
		// TODO Auto-generated method stub
		return false;
	}
}
