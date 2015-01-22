package edu.arizona.biosemantics.matrixgeneration.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import edu.arizona.biosemantics.matrixgeneration.io.complete.in.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.complete.in.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.out.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.raw.out.Writer;
import edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue.CellValueRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue.AggregatedCellValueRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue.RangeValueCellValueRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.cellvalue.SimpleCellValueRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.columnhead.ColumnHeadRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.columnhead.NameOrganColumnHeadRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.rowhead.RowHeadRawenizer;
import edu.arizona.biosemantics.matrixgeneration.rawen.rowhead.TaxonomyRowHeadRawenizer;
import edu.arizona.biosemantics.matrixgeneration.run.IRun;
import edu.arizona.biosemantics.matrixgeneration.run.MatrixGenerationRun;

public class RunConfig extends BaseConfig {

	public RunConfig() throws IOException {
		super();
	}
	
	// Keywords
	private Set<String> presentRelation = new HashSet<String>(
			Arrays.asList(new String[] { "with", "attach", "include", "attached", "included", "part_of", "part of"}));
	private Set<String> absentRelation = new HashSet<String>(
			Arrays.asList(new String[] { "without", "lack of", "devoid of", "missing", "misses" }));
	private Set<String> frequencyModifiers = new HashSet<String>(Arrays.asList(new String[] { "frequently", "rarely", "often" }));
	private Set<String> negationModifiers = new HashSet<String>(Arrays.asList(new String[] { "not" }));
	private Set<String> comparisonModifiers = new HashSet<String>(Arrays.asList(new String[] { "than" } ));
	private Collection<String> frequencyModifierPatterns = createFrequencyModifierPatterns();
	private Collection<String> negationModifierPatterns = createNegationModifierPatterns();
	private Collection<String> comparisonModifierPatterns = createComparisonModifierPatterns();

	// IO
	private String inputDirectory = "in";
	private String outputFile = "out.csv";
	private Class<? extends Reader> reader = SemanticMarkupReader.class;
	private Class<? extends Writer> writer = CSVWriter.class;
	
	// Processing
	private List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer> completeTransformers = 
			new LinkedList<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer>();
	private List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer> rawTransformers = 
			new LinkedList<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer>();
	
	// Processing: Complete -> Raw
	private Class<? extends ColumnHeadRawenizer> columnHeadRawenizer = NameOrganColumnHeadRawenizer.class;
	private Class<? extends RowHeadRawenizer> rowHeadRawenizer = TaxonomyRowHeadRawenizer.class;
	private Class<? extends CellValueRawenizer> cellValueRawenizer = AggregatedCellValueRawenizer.class;
	private Class<? extends CellValueRawenizer> defaultCellValueRawenizer = SimpleCellValueRawenizer.class;
	private List<CellValueRawenizer> cellValueRawenizers = createCellValueRawenizers();

	@Override
	protected void configure() {
		super.configure();
		
		//try {
			bind(IRun.class).to(MatrixGenerationRun.class);
			
			// IO
			bind(String.class).annotatedWith(Names.named("InputDirectory")).toInstance(inputDirectory);
			bind(String.class).annotatedWith(Names.named("OutputFile")).toInstance(outputFile);
			bind(Reader.class).to(reader).in(Singleton.class);
			bind(Writer.class).to(writer).in(Singleton.class);
			
			// Processing
			bind(new TypeLiteral<List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer>>() {}).toInstance(
					completeTransformers );
			bind(new TypeLiteral<List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer>>() {}).toInstance(
					rawTransformers );
			
			// Rawenize
			bind(ColumnHeadRawenizer.class).to(columnHeadRawenizer);
			bind(RowHeadRawenizer.class).to(rowHeadRawenizer);
			bind(CellValueRawenizer.class).to(cellValueRawenizer);
			bind(new TypeLiteral<List<CellValueRawenizer>>() {}).toInstance(cellValueRawenizers);
			bind(CellValueRawenizer.class).annotatedWith(Names.named("DefaultCellValueRawenizer")).to(defaultCellValueRawenizer);			
			
			// Keywords
			bind(new TypeLiteral<Collection<String>>() {}).annotatedWith(Names.named("FrequencyModifierPatterns")).toInstance(
					frequencyModifierPatterns );
			bind(new TypeLiteral<Collection<String>>() {}).annotatedWith(Names.named("NegationModifierPatterns")).toInstance(
					negationModifierPatterns );
			bind(new TypeLiteral<Collection<String>>() {}).annotatedWith(Names.named("ComparisonModifierPatterns")).toInstance(
					comparisonModifierPatterns );
			bind(new TypeLiteral<Set<String>>() {}).annotatedWith(Names.named("PresentRelation")).toInstance(
					presentRelation );
			bind(new TypeLiteral<Set<String>>() {}).annotatedWith(Names.named("AbsentRelation")).toInstance(
					absentRelation );
			
		//} catch(IOException e) {
		//	log(LogLevel.ERROR, "Exception loading configuration", e);
		//	throw new IllegalArgumentException();
		//}
	}

	private List<CellValueRawenizer> createCellValueRawenizers() {
		List<CellValueRawenizer> cellValueRawenizers = new LinkedList<CellValueRawenizer>();
		cellValueRawenizers.add(new RangeValueCellValueRawenizer(frequencyModifierPatterns, comparisonModifierPatterns));
		return cellValueRawenizers;
	}
	
	private Collection<String> createFrequencyModifierPatterns() {
		Set<String> result = new HashSet<String>();
		for(String frequencyModifier : frequencyModifiers) {
			result.add("^" + frequencyModifier + "$");
		}
		return result;
	}
	
	private Collection<String> createNegationModifierPatterns() {
		Set<String> result = new HashSet<String>();
		for(String negationModifier : negationModifiers) {
			result.add("^" + negationModifier + "$");
		}
		return result;
	}
	
	private Collection<String> createComparisonModifierPatterns() {
		Set<String> result = new HashSet<String>();
		for(String comparisonModifier : comparisonModifiers) {
			result.add("^" + comparisonModifier + "$");
		}
		return result;
	}

	
	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public void setReader(Class<? extends Reader> reader) {
		this.reader = reader;
	}

	public void setWriter(Class<? extends Writer> writer) {
		this.writer = writer;
	}

	public List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer> getCompleteTransformers() {
		return completeTransformers;
	}

	public void setCompleteTransformers(
			List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer> completeTransformers) {
		this.completeTransformers = completeTransformers;
	}

	public List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer> getRawTransformers() {
		return rawTransformers;
	}

	public void setRawTransformers(
			List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer> rawTransformers) {
		this.rawTransformers = rawTransformers;
	}
}
