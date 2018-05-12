package edu.arizona.biosemantics.matrixgeneration.run;

import java.io.File;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.io.complete.in.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.out.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.rawen.MatrixRawenizer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer;

public class MatrixGenerationRun extends AbstractRun {

	private String inputDirectory;
	private String outputFile;
	private Reader reader;
	private Writer writer;
	private List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer> completeTransformers;
	private MatrixRawenizer matrixRawenizer;
	private List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer> rawTransformers;

	@Inject
	public MatrixGenerationRun(@Named("GuiceModuleFile")String guiceModuleFile,
			@Named("InputDirectory") String inputDirectory, @Named("OutputFile") String outputFile, 
			Reader reader, Writer writer, 
			List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer> completeTransformers, 
			MatrixRawenizer matrixRawenizer,
			List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer> rawTransformers) {
		super(guiceModuleFile);
		this.inputDirectory = inputDirectory;
		this.outputFile = outputFile;
		
		this.reader = reader;
		this.writer = writer;
		this.completeTransformers = completeTransformers;
		this.matrixRawenizer = matrixRawenizer;
		this.rawTransformers = rawTransformers;
	}

	@Override
	protected void doRun() throws Throwable {
		log(LogLevel.INFO, "Start Matrix Generation");
		log(LogLevel.INFO, "Input directory: " + inputDirectory);
		log(LogLevel.INFO, "Output file: " + outputFile);
		log(LogLevel.INFO, "Complete Transformers: " + completeTransformers);
		log(LogLevel.INFO, "Raw Transformers: " + rawTransformers);
		
		Matrix matrix = reader.read();//?? different whole_organism structure in taxon vs. idtaxonstructure map! the structure in taxon has empty values!
		log(LogLevel.INFO, "Read matrix:\n Taxa: " + matrix.getTaxaCount() + "\n Characters: " + matrix.getCharactersCount() + 
				"\n Values: " + matrix.getSetCharacterValues());
		
		edu.arizona.biosemantics.matrixgeneration.transform.complete.TransformerStrategy completeTransformStrategy = 
				new edu.arizona.biosemantics.matrixgeneration.transform.complete.TransformerStrategy(completeTransformers);
		edu.arizona.biosemantics.matrixgeneration.transform.raw.TransformerStrategy rawTransformStrategy = 
				new edu.arizona.biosemantics.matrixgeneration.transform.raw.TransformerStrategy(rawTransformers);
		
		completeTransformStrategy.transform(matrix);
		log(LogLevel.INFO, "Matrix successfully transformed.\n Taxa: " + matrix.getTaxaCount() + 
				"\n Characters: " + matrix.getCharactersCount() + "\n Values: " + matrix.getSetCharacterValues());
		
		edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix rawMatrix = matrixRawenizer.convert(matrix);
		log(LogLevel.INFO, "Matrix rawenized.\n Rows: " + rawMatrix.getRowCount() + "\n Columns: " + rawMatrix.getColumnCount() + 
				"\n Applicable cell values " + rawMatrix.getApplicableCellValues());
		
		
		rawTransformStrategy.transform(rawMatrix);
		log(LogLevel.INFO, "Raw matrix successfully transformed.\n Rows: " + rawMatrix.getRowCount() + 
				"\n Columns: " + rawMatrix.getColumnCount() + "\n Applicable cell values " + rawMatrix.getApplicableCellValues());
		
		writer.write(rawMatrix);
		log(LogLevel.INFO, "Matrix successfully writen");
	}

}
