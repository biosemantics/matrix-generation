package edu.arizona.biosemantics.matrixgeneration.run;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.io.complete.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.complete.TransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RawTransformStrategy;

public class MatrixGenerationRun extends AbstractRun {

	private String inputDirectory;
	private String outputFile;
	private boolean inheritValues;
	private boolean generateAbsentPresent;
	private boolean inferCharactersFromOntologies;
	private Reader reader;
	private Writer writer;
	private TransformStrategy transformStrategy;
	private RawTransformStrategy rawMatrixTransformStrategy;

	@Inject
	public MatrixGenerationRun(@Named("GuiceModuleFile")String guiceModuleFile,
			@Named("InputDirectory") String inputDirectory, @Named("OutputFile") String outputFile, 
			@Named("InheritValues") boolean inheritValues, @Named("GenerateAbsentPresent") boolean generateAbsentPresent, 
			@Named("InferCharactersFromOntologies") boolean inferCharactersFromOntologies, 
			Reader reader, Writer writer, TransformStrategy transformStrategy, RawTransformStrategy rawMatrixTransformStrategy) {
		super(guiceModuleFile);
		this.inputDirectory = inputDirectory;
		this.outputFile = outputFile;
		this.inheritValues = inheritValues;
		this.generateAbsentPresent = generateAbsentPresent;
		this.inferCharactersFromOntologies = inferCharactersFromOntologies;
		
		this.reader = reader;
		this.writer = writer;
		this.transformStrategy = transformStrategy;
		this.rawMatrixTransformStrategy = rawMatrixTransformStrategy;
	}

	@Override
	protected void doRun() throws Throwable {
		log(LogLevel.INFO, "Start Matrix Generation");
		log(LogLevel.INFO, "Input directory: " + inputDirectory);
		log(LogLevel.INFO, "Output file: " + outputFile);
		log(LogLevel.INFO, "Inherit values: " + inheritValues);
		log(LogLevel.INFO, "Generate Absent Present: " + generateAbsentPresent);
		log(LogLevel.INFO, "Infer Characters From Ontologies: " + inferCharactersFromOntologies);
		
		Matrix matrix = reader.read();
		log(LogLevel.INFO, "Read matrix. Taxa: " + matrix.getTaxaCount() + ", Characters: " + matrix.getCharactersCount());
		
		transformStrategy.transform(matrix);
		log(LogLevel.INFO, "Matrix successfully transformed. Taxa: " + matrix.getTaxaCount() + 
				", Characters: " + matrix.getCharactersCount());
				
		RawMatrix rawMatrix = rawMatrixTransformStrategy.transform(matrix);
		log(LogLevel.INFO, "Raw matrix successfully transformed. Rows: " + rawMatrix.getRowCount() + 
				", Columns: " + rawMatrix.getColumnCount());
		
		writer.write(rawMatrix);
		log(LogLevel.INFO, "Matrix successfully writen");
	}

}
