package edu.arizona.biosemantics.matrixgeneration.io;

import edu.arizona.biosemantics.matrixgeneration.model.Value;

/** StateFactory code before **/
public class ValueTypeDeterminer {
	
	public enum ValueType {
		SINGLETON_NUMERIC, SINGLETON_CATEGORICAL, RANGE_CATEGORICAL, RANGE_NUMERIC, EMPTY
	}

	public boolean isSingletonNumeric(Value value) {
		return determine(value).equals(ValueType.SINGLETON_NUMERIC);
	}

	public boolean isSingletonCategorical(Value value) {
		return determine(value).equals(ValueType.SINGLETON_CATEGORICAL);
	}

	public boolean isRangeCategorical(Value value) {
		return determine(value).equals(ValueType.RANGE_CATEGORICAL);
	}

	public boolean isRangeNumeric(Value value) {
		return determine(value).equals(ValueType.RANGE_NUMERIC);
	}

	public boolean isEmpty(Value value) {
		return determine(value).equals(ValueType.EMPTY);
	}

	public ValueType determine(Value value) {
		if (value.getValue() == null) {
			if (value.getFromUnit() != null && value.getToUnit() != null) {
				Double from = null, to = null;
				try {
					from = Double.parseDouble(value.getFrom());
					to = Double.parseDouble(value.getTo());
				} catch (NumberFormatException e) {
					System.out.println("Bad number:" + e.getMessage()
							+ " in value: " + value.toString());
					from = -1.0;
					to = -1.0;
				}
				return ValueType.RANGE_NUMERIC;
			} else {
				// Let's first check if it's a count, by trying to parse it as
				// an integer.
				Double from = null, to = null;
				try {
					from = Double.parseDouble(value.getFrom());
					if (value.getTo() != null)
						to = Double.parseDouble(value.getTo());
					else
						to = 9999.00;
					return ValueType.RANGE_NUMERIC;
				} catch (NumberFormatException e) {
					String sto;
					if (value.getTo() != null)
						sto = value.getTo();
					else
						sto = "9999.00";
					return ValueType.RANGE_CATEGORICAL;
				}
			}
		} else {
			if (value.getUnit() != null) {
				Double v = null;
				try {
					v = Double.parseDouble(value.getValue());
				} catch (NumberFormatException e) {
					System.out.println("Bad number:" + e.getMessage()
							+ " in value: " + value.toString());
					v = -1.0;
				}
				return ValueType.SINGLETON_CATEGORICAL;
			} else {
				// Need to check here for counts, too
				Double v = null;
				try {
					v = Double.parseDouble(value.getValue());
					return ValueType.SINGLETON_NUMERIC;
				} catch (NumberFormatException e) {
					if (value.getValue().equals("/"))
						return ValueType.EMPTY;
					else
						return ValueType.SINGLETON_CATEGORICAL;
				}
			}
		}
	}

	public boolean isSingleton(Value value) {
		ValueType result = determine(value);
		return result.equals(ValueType.SINGLETON_CATEGORICAL)
				|| result.equals(ValueType.SINGLETON_NUMERIC);
	}

	public boolean isRange(Value value) {
		ValueType result = determine(value);
		return result.equals(ValueType.RANGE_CATEGORICAL)
				|| result.equals(ValueType.RANGE_NUMERIC);
	}

}
