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
	
	//area too?
	private void initUnits() {
		units.put("nm", 1000000.0);
		units.put("µ", 1000.0);
		units.put("µm", 1000.0);
		units.put("mm", 1.0);
		units.put("cm", 0.1);
		units.put("dm", 0.01);
		units.put("m", 0.001);
		units.put("km", 0.000001);
		
		units.put("in", 0.03937007874016 );
		units.put("ft", 0.003280839895013 );
		units.put("yd", 0.001093613298338);
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
				Double doubleValue = Double.parseDouble(normalizeNumeric(value.getValue()));
				if(units.containsKey(value.getUnit())) {
					Double baseUnitFactor = units.get(value.getUnit());
					value.setValue(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setUnit(targetUnit);
				}
			}
			if((isNumeric(value.getFrom()))) {
				Double doubleValue = Double.parseDouble(normalizeNumeric(value.getFrom()));
				if(units.containsKey(value.getFromUnit())) {
					Double baseUnitFactor = units.get(value.getFromUnit());
					value.setFrom(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setFromUnit(targetUnit);
				}
			}
			if((isNumeric(value.getTo()))) {
				Double doubleValue = Double.parseDouble(normalizeNumeric(value.getTo()));
				if(units.containsKey(value.getToUnit())) {
					Double baseUnitFactor = units.get(value.getToUnit());
					value.setTo(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setToUnit(targetUnit);
				}
			}
			if((isNumeric(value.getFromInclusive()))) {
				Double doubleValue = Double.parseDouble(normalizeNumeric(value.getFromInclusive()));
				if(units.containsKey(value.getFromUnit())) {
					Double baseUnitFactor = units.get(value.getFromUnit());
					value.setFromInclusive(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setFromUnit(targetUnit);
				}
			}
			if((isNumeric(value.getToInclusive()))) {
				Double doubleValue = Double.parseDouble(normalizeNumeric(value.getToInclusive()));
				if(units.containsKey(value.getToUnit())) {
					Double baseUnitFactor = units.get(value.getToUnit());
					value.setToInclusive(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
					value.setToUnit(targetUnit);
				}
			}
		}
	}

	private String normalizeNumeric(String value) {
		return value.replaceAll(",", ".");
	}
	
	/**
	 * normalize somewhere 0.1 and 0,1 ? 
	 * @param value
	 * @return
	 */
	private boolean isNumeric(String value) {
		String exponential = "[[E[\\+\\-]?]\\d+]?$";
		// "+.3"; case or "-111.577" case
		if(value.matches("^[\\+\\-]\\d*[\\.,]\\d+" + exponential))
			return true;
		// "+1", "-4" case
		if(value.matches("^[\\+\\-]\\d+" + exponential))
			return true;
		// ".3" case or "111.577" case
		if(value.matches("^\\d*[\\.,]\\d+" + exponential))
			return true;
		// "123" case
		if(value.matches("^\\d" + exponential))
			return true;
		return false;
	}
	
	
	public static void main(String[] args) {
		
		String test = "E+";
		System.out.println(test.matches("^[E[\\+\\-]?]?$"));
		
		String a = "+.3";
		String b = "-.2";
		
		String c = ".4";
		String d = "4";
		String e = "1,3";
		String f = "-1.4";
		
		String g = ".";
		String h = "-";
		
		String i = "1.4E+";// "1.4E+03";
		String j = ".4E+3";
		String k = ".4E3";
		String l = ".4E-5";
		
		SwitchUnitTransformer t = new SwitchUnitTransformer("mm");
		System.out.println(t.isNumeric(a));
		System.out.println(t.isNumeric(b));
		System.out.println(t.isNumeric(c));
		System.out.println(t.isNumeric(d));
		System.out.println(t.isNumeric(e));
		System.out.println(t.isNumeric(f));
		System.out.println(t.isNumeric(g));
		System.out.println(t.isNumeric(h));
		System.out.println(t.isNumeric(i));
		System.out.println(t.isNumeric(j));
		System.out.println(t.isNumeric(k));
		System.out.println(t.isNumeric(l));
		
	}
}
