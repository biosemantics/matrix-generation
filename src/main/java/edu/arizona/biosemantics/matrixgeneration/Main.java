package edu.arizona.biosemantics.matrixgeneration;

import java.io.File;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.io.complete.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.complete.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.SerializeWriter;
import edu.arizona.biosemantics.matrixgeneration.io.raw.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.complete.SomeTransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.complete.TransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RawTransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.SomeRawTransformStrategy;

public class Main {
	
	private String inputDir;
	private String outputFile;
	private boolean inheritValues;
	private boolean generateAbsentPresent;
	private boolean inferCharactersFromOntologies;
	
	
	public Main(String inputDir, String outputFile, boolean inheritValues, boolean generateAbsentPresent, 
			boolean inferCharactersFromOntologies) {
		this.inputDir = inputDir;
		this.outputFile = outputFile;
		this.inheritValues = inheritValues;
		this.generateAbsentPresent = generateAbsentPresent;
		this.inferCharactersFromOntologies = inferCharactersFromOntologies;
	}

	public void run() throws Exception {
		log(LogLevel.INFO, "Start Matrix Generation");
		log(LogLevel.INFO, "Input dir: " + inputDir);
		log(LogLevel.INFO, "Output file: " + outputFile);
		log(LogLevel.INFO, "Inherit values: " + inheritValues);
		log(LogLevel.INFO, "Generate Absent Present: " + generateAbsentPresent);
		inferCharactersFromOntologies = false;
		log(LogLevel.INFO, "Infer Characters From Ontologies: " + inferCharactersFromOntologies);
		
		Reader reader = new SemanticMarkupReader(new File(inputDir));
		Matrix matrix = reader.read();
		log(LogLevel.INFO, "Read matrix. Taxa: " + matrix.getTaxaCount() + ", Characters: " + matrix.getCharactersCount());
		
		TransformStrategy transformStratey = new SomeTransformStrategy(inheritValues, generateAbsentPresent, inferCharactersFromOntologies);
		transformStratey.transform(matrix);
		
		log(LogLevel.INFO, "Matrix successfully transformed. Taxa: " + matrix.getTaxaCount() + 
				", Characters: " + matrix.getCharactersCount());
				
		RawTransformStrategy rawMatrixTransformer = new SomeRawTransformStrategy();
		RawMatrix rawMatrix = rawMatrixTransformer.transform(matrix);
		
		log(LogLevel.INFO, "Raw matrix successfully transformed. Rows: " + rawMatrix.getRowCount() + 
				", Columns: " + rawMatrix.getColumnCount());
		
		//System.out.println("raw matrix: " + rawMatrix.getRowCount() + " rows, " + rawMatrix.getColumnCount() + " columns.\n " + rawMatrix.toString());
		//Writer writer = new CSVWriter(new File(outputFile));
		Writer writer = new SerializeWriter(new File(outputFile));
		//Writer writer = new SDDWriter(new File("matrix.sdd"));
		//Writer writer = new NexusWriter(new File("matrix.nxs"));
		writer.write(rawMatrix);
		log(LogLevel.INFO, "Matrix successfully writen");
	}
	
	public static void main(String[] args) throws Exception {
		Main main = new Main(args[0], args[1], Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]));
		main.run();
	} 
	
}
