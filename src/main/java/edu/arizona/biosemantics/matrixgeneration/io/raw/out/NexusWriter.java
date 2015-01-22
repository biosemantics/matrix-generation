package edu.arizona.biosemantics.matrixgeneration.io.raw.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class NexusWriter implements Writer {

	private File file;

	public NexusWriter(File file ) {
		this.file = file;
	}
	
	@Override
	public void write(Matrix rawMatrix) throws IOException  {
		Map<Integer, List<String>> columnStates = getColumnStates(rawMatrix);
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			writeMetaData(bw, rawMatrix, columnStates);
			writeCharacterLabels(bw, rawMatrix, columnStates);
			writeMatrix(bw, rawMatrix, columnStates);
			bw.flush();
		}
	}

	private void writeCharacterLabels(BufferedWriter bw, Matrix rawMatrix,
			Map<Integer, List<String>> columnStates) throws IOException {
		bw.append("charstatelabels\n");
		
		for(int i=0; i<rawMatrix.getColumnCount(); i++) {
			bw.append(i+1 + "\t" + this.normalizeAsNexusToken(rawMatrix.getColumnHeads().get(i).getValue()));
			if(!columnStates.get(i).isEmpty()) {
				String statesString = " / ";
				for(String state : columnStates.get(i)) {
					statesString += state + ", ";
				}
				bw.append(statesString.substring(0, statesString.length() - 2));
			}
			if(i < rawMatrix.getColumnCount() - 1)
				bw.append(",\n");
			else 
				bw.append(";\n");
		}
		bw.append("\n");
	}

	private void writeMatrix(BufferedWriter bw, Matrix rawMatrix, Map<Integer, List<String>> columnStates) throws IOException {
		bw.append("matrix\n");
		int maxRowHeadLength = getMaxRowHeadLength(rawMatrix);
		int valueStartIndex = maxRowHeadLength + 4;
		int valueEndIndex = valueStartIndex + rawMatrix.getColumnCount();
		writeCharacterIndex(bw, valueStartIndex, valueEndIndex);
		bw.append("\n");
		
		for(RowHead rowHead : rawMatrix.getRowHeads()) {
			String rowHeadValue = normalizeAsNexusToken(rowHead.getValue());
			bw.append(rowHeadValue);
			for(int i=rowHeadValue.length(); i < valueStartIndex; i++) {
				bw.append(" ");
			}
			List<CellValue> rowValues = rawMatrix.getCellValues().get(rowHead);
			for(int i=0; i<rowValues.size(); i++) {
				String state = rowValues.get(i).getText();
				bw.append(String.valueOf(columnStates.get(i).indexOf(state)));
			}
			bw.append("\n");
		}
		bw.append(";\n");
		bw.append("End;");
	}

	private void writeMetaData(BufferedWriter bw, Matrix rawMatrix, Map<Integer, List<String>> columnStates) throws IOException {
		bw.append("#nexus\n");
		int maxNumberOfStates = getMaxNumberOfStates(columnStates);
		
		bw.append("begin data;\n");
		bw.append("dimensions ntax=" + rawMatrix.getRowCount() + 
				" nchar=" + rawMatrix.getColumnCount() + ";\n");
		bw.append("format symbols=\"");
		String symbolsString = "";
		for(int i=0; i<maxNumberOfStates; i++) {
			symbolsString += " " + i;
		}
		bw.append(symbolsString.trim());
		bw.append("\" missing=? gap=- ;\n");
	}

	private void writeCharacterIndex(BufferedWriter bw, int valueStartIndex, int valueEndIndex) throws IOException {
		for(int line=0; line<2; line++) {
			bw.append("[");
			for(int i=1; i<valueStartIndex; i++) 
				bw.append(" ");
			int displayIndex = 10;
			int values = valueEndIndex - valueStartIndex;
			for(int i=1; i<=values; i++) {
				if(i==90)
					System.out.println("test");
				if(i % 10 == 0) {
					if(line==0) {
						bw.append(String.valueOf(displayIndex));
						i+=String.valueOf(displayIndex).length() - 1;
						displayIndex += 10;
					} else {
						bw.append(".");
					}
				} else {
					bw.append(" ");
				}
			}
			bw.append("]");
			bw.append("\n");
		}
	}

	private int getMaxRowHeadLength(Matrix rawMatrix) {
		int result = 0;
		for(RowHead rowHead : rawMatrix.getRowHeads()) {
			result = Math.max(result, normalizeAsNexusToken(rowHead.getValue()).length());
		}
		return result;
	}

	private String normalizeAsNexusToken(String value) {
		String normalized = value.replaceAll("\\s", "_");
		normalized = normalized.replaceAll("\\W", "_");
		return normalized;
	}

	private Map<Integer, List<String>> getColumnStates(Matrix rawMatrix) {
		Map<Integer, List<String>> columnStates = new HashMap<Integer, List<String>>();
		for(int i=0; i<rawMatrix.getColumnCount(); i++) {
			Set<String> states = new HashSet<String>();
			for(RowHead rowHead : rawMatrix.getRowHeads()) {
				states.add(rawMatrix.getCellValues().get(rowHead).get(i).getText());
			}
			List<String> statesList = new ArrayList<String>(states);
			Collections.sort(statesList);
			columnStates.put(i, statesList);
		}
		
		return columnStates;
	}

	private int getMaxNumberOfStates(Map<Integer, List<String>> columnStates) {
		int maxNumberOfStates = 0;
		for(int i=0; i<columnStates.size(); i++) {
			List<String> states = columnStates.get(i);	
			maxNumberOfStates = Math.max(states.size(), maxNumberOfStates);
		}
		return maxNumberOfStates;
	}

}
