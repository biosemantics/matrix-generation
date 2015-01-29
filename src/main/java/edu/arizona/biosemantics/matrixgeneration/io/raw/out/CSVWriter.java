package edu.arizona.biosemantics.matrixgeneration.io.raw.out;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.CellValue;
import edu.arizona.biosemantics.matrixgeneration.model.raw.ColumnHead;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RowHead;

public class CSVWriter implements Writer {

	private String outputFile;
	private boolean outputProvenance;

	@Inject
	public CSVWriter(@Named("OutputFile") String outputFile, @Named("Output_Provenance") boolean outputProvenance) {
		this.outputFile = outputFile;
		this.outputProvenance = outputProvenance;
	}
	
	@Override
	public void write(Matrix matrix) throws IOException {
		Map<RowHead, List<CellValue>> cellValues = matrix.getCellValues();
		List<ColumnHead> columnHeads = matrix.getColumnHeads();
		List<RowHead> rowHeads = matrix.getRowHeads();
		int columns = columnHeads.size() + 1;
		int rows = rowHeads.size() + 1;
		
		try (au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(new FileWriter(new File(outputFile)))) {
			List<String[]> lines = new ArrayList<String[]>(rows);
			String[] line = new String[columns];
			int column=0;
			line[column++] = "Taxon Concept";
			for(ColumnHead columnHead : columnHeads) {
				String value = columnHead.getValue();
				if(outputProvenance)
					value += " [" + //not interesting at this time: StringUtils.join(columnHead.getGenerationProvenance(), ", ") + " / " + 
									StringUtils.join(columnHead.getSource().getGenerationProvenance(), ", ") + "]";
				line[column++] = value;
			}
			lines.add(line);
			
			for(RowHead rowHead : rowHeads) {
				line = new String[columnHeads.size() + 1];
				column=0;
				line[column++] = rowHead.getValue();
				for(CellValue cellValue : cellValues.get(rowHead)) {
					String value = cellValue.getText();
					
					String singleValueProvenance = "";
					for(Value v : cellValue.getSource()) {
						if(v != null) {
							String valueString = v.getValue();
							if(valueString == null) {
								if(v.getFrom() != null && v.getTo() != null)
									valueString = v.getFrom() + " - " + v.getTo();
								else {
									log(LogLevel.WARN, "There is an unexpected value");
								}
							}
							singleValueProvenance += valueString + " " + StringUtils.join(v.getGenerationProvenance(), ", ") + "; ";
						}
					}
					if(outputProvenance)
						value += " [" + StringUtils.join(cellValue.getGenerationProvenance(), ", ") + " / " + 
								singleValueProvenance + "]";
					line[column++] = value;
				}
				lines.add(line);
			}
			csvWriter.writeAll(lines);
			csvWriter.flush();
		}
	}

}
