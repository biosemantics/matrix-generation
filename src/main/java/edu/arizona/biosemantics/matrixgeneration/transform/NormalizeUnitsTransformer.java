package edu.arizona.biosemantics.matrixgeneration.transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Value;

/**
 * 4. allow the user to set the unit (m or mm etc.) to be used.
 * @author rodenhausen
 */
public class NormalizeUnitsTransformer implements Transformer {

	public static enum Unit {
		
		nm(100000.0), 
		µ(1000.0),
		µm(1000.0), 
		mm(1.0), 
		cm(0.1), 
		dm(0.01),
		m(0.001), 
		km(0.000001), 
		in(0.03937007874016), 
		ft(0.003280839895013), 
		yd(0.001093613298338);
		
		private Double toMMFactor;

		private Unit(Double toMMFactor) {
			this.toMMFactor = toMMFactor;
		}
		
		public Double getToMMFactor() {
			return toMMFactor;
		}
		
	}
	
	private Unit targetUnit;
	
	public NormalizeUnitsTransformer(Unit targetUnit) {
		this.targetUnit = targetUnit;
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
		Double targetUnitFactor = targetUnit.getToMMFactor();
		for(Value value : matrix.getValues()) {
			if(isNumeric(value.getValue())) {
				try {
					Double doubleValue = Double.parseDouble(normalizeNumeric(value.getValue()));
					try{
						Double baseUnitFactor = Unit.valueOf(value.getUnit()).getToMMFactor();
						value.setValue(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
						value.setUnit(targetUnit.toString());
					} catch(IllegalArgumentException e) {
						log(LogLevel.ERROR, "Can't convert unit", e);
					}
				} catch(NumberFormatException e) {
					log(LogLevel.ERROR, "Can't parse value", e);
				}
			}
			if((isNumeric(value.getFrom()))) {
				try{
					Double doubleValue = Double.parseDouble(normalizeNumeric(value.getFrom()));
					try{
						Double baseUnitFactor = Unit.valueOf(value.getFromUnit()).getToMMFactor();
						value.setFrom(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
						value.setFromUnit(targetUnit.toString());
					} catch(IllegalArgumentException e) {
						log(LogLevel.ERROR, "Can't convert from unit", e);
					}
				} catch(NumberFormatException e) {
					log(LogLevel.ERROR, "Can't parse from", e);
				}
			}
			if((isNumeric(value.getTo()))) {
				try{
					Double doubleValue = Double.parseDouble(normalizeNumeric(value.getTo()));
					try{
						Double baseUnitFactor = Unit.valueOf(value.getToUnit()).getToMMFactor();
						value.setTo(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
						value.setToUnit(targetUnit.toString());
					} catch(IllegalArgumentException e) {
						log(LogLevel.ERROR, "Can't convert to unit", e);
					}
				} catch(NumberFormatException e) {
					log(LogLevel.ERROR, "Can't parse to", e);
				}
			}
			if((isNumeric(value.getFromInclusive()))) {
				try{
					Double doubleValue = Double.parseDouble(normalizeNumeric(value.getFromInclusive()));
					try{
						Double baseUnitFactor = Unit.valueOf(value.getFromUnit()).getToMMFactor();
						value.setFromInclusive(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
						value.setFromUnit(targetUnit.toString());
					} catch(IllegalArgumentException e) {
						log(LogLevel.ERROR, "Can't convert from unit", e);
					}
				} catch(NumberFormatException e) {
					log(LogLevel.ERROR, "Can't parse from inclusive", e);
				}
			}
			if((isNumeric(value.getToInclusive()))) {
				try{
					Double doubleValue = Double.parseDouble(normalizeNumeric(value.getToInclusive()));
					try{
						Double baseUnitFactor = Unit.valueOf(value.getToUnit()).getToMMFactor();
						value.setToInclusive(String.valueOf(doubleValue * baseUnitFactor * targetUnitFactor));
						value.setToUnit(targetUnit.toString());
					} catch(IllegalArgumentException e) {
						log(LogLevel.ERROR, "Can't convert to unit", e);
					}
				} catch(NumberFormatException e) {
					log(LogLevel.ERROR, "Can't parse to inclusive", e);
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
		
		NormalizeUnitsTransformer t = new NormalizeUnitsTransformer(Unit.mm);
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
