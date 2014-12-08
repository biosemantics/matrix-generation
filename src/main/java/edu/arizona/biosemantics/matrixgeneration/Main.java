package edu.arizona.biosemantics.matrixgeneration;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.io.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.raw.SerializeWriter;
import edu.arizona.biosemantics.matrixgeneration.io.raw.Writer;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.AbsentPresentTranformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.InheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.OntologyInferenceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.Transformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RawMatrixTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.addcolumn.AddColumn;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.addcolumn.AddSourceColumn;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue.ByChoiceCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue.CellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue.CombinedCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue.RangeValueByChoiceCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.cellvalue.SimpleCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.columnhead.ColumnHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.columnhead.NameOrganColumnHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.rowhead.RowHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.rowhead.TaxonomyRowHeadTransformer;

public class Main {

	private Set<String> frequencyModifiers = new HashSet<String>(Arrays.asList(new String[] { "frequently", "rarely", "often" }));
	private Set<String> negationModifiers = new HashSet<String>(Arrays.asList(new String[] { "not" }));
	private Set<String> comparisonModifiers = new HashSet<String>(Arrays.asList(new String[] { "than" } ));
	private Set<String> presentRelation = new HashSet<String>(
			Arrays.asList(new String[] { "with", "attach", "include", "attached", "included" }));
	private Set<String> absentRelation = new HashSet<String>(
			Arrays.asList(new String[] { "without", "lack of", "devoid of", "missing", "misses" }));
	
	private List<String> prependModifierPatterns;
	private List<String> appendModifierPatterns;
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
		prependModifierPatterns = createPrependModifierPatterns();
		appendModifierPatterns = createAppendModifierPatterns();
	}
	
	private List<String> createAppendModifierPatterns() {
		List<String> result = new LinkedList<String>();
		for(String comparisonModifier : comparisonModifiers) {
			result.add("^" + comparisonModifier + " .*$");
		}
		return result;
	}

	private List<String> createPrependModifierPatterns() {
		List<String> result = new LinkedList<String>();
		for(String frequencyModifier : frequencyModifiers) {
			result.add("^" + frequencyModifier + "$");
		}
		for(String negationModifier : negationModifiers) {
			result.add("^" + negationModifier + "$");
		}
		return result;
	}

	public void run() throws Exception {
		log(LogLevel.INFO, "Start Matrix Generation");
		log(LogLevel.INFO, "Input dir: " + inputDir);
		log(LogLevel.INFO, "Output file: " + outputFile);
		log(LogLevel.INFO, "Inherit values: " + inheritValues);
		log(LogLevel.INFO, "Generate Absent Present: " + generateAbsentPresent);
		log(LogLevel.INFO, "Infer Characters From Ontologies: " + inferCharactersFromOntologies);
		
		Reader reader = new SemanticMarkupReader(new File(inputDir));
		Matrix matrix = reader.read();
		log(LogLevel.INFO, "Read matrix. Taxa: " + matrix.getTaxaCount() + ", Characters: " + matrix.getCharactersCount());
		
		if(inheritValues) {
			Transformer inherit = new InheritanceTransformer();
			inherit.transform(matrix);
		}
		if(generateAbsentPresent) {
			Transformer generateAbsentPresent = new AbsentPresentTranformer(presentRelation, absentRelation);
			generateAbsentPresent.transform(matrix);
		}
		
		if(inferCharactersFromOntologies) {
			Transformer ontologyInferenceTransformer = new OntologyInferenceTransformer();
			ontologyInferenceTransformer.transform(matrix);
		}
		
		//Transformer switchUnits = new NormalizeUnitsTransformer(Unit.mm);
		//switchUnits.transform(matrix);
		
		//Transformer splitRangeValues = new SplitRangeValuesTransformer();
		//splitRangeValues.transform(matrix);
		
		//System.out.println("transformed matrix: " + \n " + matrix.toString());
		log(LogLevel.INFO, "Matrix successfully transformed. Taxa: " + matrix.getTaxaCount() + 
				", Characters: " + matrix.getCharactersCount());
		
		List<ByChoiceCellValueTransformer> byChoiceCellValueTransformers = new LinkedList<ByChoiceCellValueTransformer>();
		byChoiceCellValueTransformers.add(new RangeValueByChoiceCellValueTransformer(prependModifierPatterns, appendModifierPatterns));
		CellValueTransformer cellValueTransformer = new CombinedCellValueTransformer(byChoiceCellValueTransformers, 
				new SimpleCellValueTransformer(prependModifierPatterns, appendModifierPatterns));
		ColumnHeadTransformer columnHeadTransformer = new NameOrganColumnHeadTransformer();
		RowHeadTransformer rowHeadTransformer = new TaxonomyRowHeadTransformer();
		//CellValueTransformer cellValueTransformer = new SimpleCellValueTransformer();
		
		List<AddColumn> addColumns = new LinkedList<AddColumn>();
		addColumns.add(new AddSourceColumn());
		RawMatrixTransformer rawMatrixTransformer = new RawMatrixTransformer(columnHeadTransformer,
				rowHeadTransformer, cellValueTransformer, addColumns);
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
