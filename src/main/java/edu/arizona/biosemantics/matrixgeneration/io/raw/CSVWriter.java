package edu.arizona.biosemantics.matrixgeneration.io.raw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class CSVWriter implements Writer {

	private String outputFile;

	@Inject
	public CSVWriter(@Named("OutputFile") String outputFile) {
		this.outputFile = outputFile;
	}
	
	@Override
	public void write(RawMatrix rawMatrix) throws IOException {
		Map<RowHead, List<CellValue>> cellValues = rawMatrix.getCellValues();
		List<ColumnHead> columnHeads = rawMatrix.getColumnHeads();
		List<RowHead> rowHeads = rawMatrix.getRowHeads();
		int columns = columnHeads.size() + 1;
		int rows = rowHeads.size() + 1;
		
		try (au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(new FileWriter(new File(outputFile)))) {
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
		}
	}

}
