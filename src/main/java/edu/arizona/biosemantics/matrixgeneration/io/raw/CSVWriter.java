package edu.arizona.biosemantics.matrixgeneration.io.raw;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class CSVWriter implements Writer {

	private File file;

	public CSVWriter(File file) {
		this.file = file;
	}
	
	@Override
	public void write(RawMatrix rawMatrix) throws Exception {
		Map<RowHead, List<CellValue>> cellValues = rawMatrix.getCellValues();
		List<ColumnHead> columnHeads = rawMatrix.getColumnHeads();
		List<RowHead> rowHeads = rawMatrix.getRowHeads();
		int columns = columnHeads.size() + 1;
		int rows = rowHeads.size() + 1;
		
		au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(new FileWriter(file));
		List<String[]> lines = new ArrayList<String[]>(rows);
		String[] line = new String[columns];
		int column=0;
		line[column++] = "Taxon Concept";
		for(ColumnHead columnHead : columnHeads) {
			line[column++] = columnHead.getValue();
		}
		lines.add(line);
		
		for(RowHead rowHead : rowHeads) {
			line = new String[columnHeads.size() + 1];
			column=0;
			line[column++] = rowHead.getValue();
			for(CellValue cellValue : cellValues.get(rowHead)) {
				line[column++] = cellValue.getValue();
			}
			lines.add(line);
		}
		csvWriter.writeAll(lines);
		csvWriter.flush();
		csvWriter.close();
	}

}
