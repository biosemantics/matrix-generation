package edu.arizona.biosemantics.matrixgeneration.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import edu.arizona.biosemantics.matrixgeneration.io.complete.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.complete.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.raw.Writer;
import edu.arizona.biosemantics.matrixgeneration.run.IRun;
import edu.arizona.biosemantics.matrixgeneration.run.MatrixGenerationRun;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.complete.SomeTransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.complete.TransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RawTransformStrategy;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.SomeRawTransformStrategy;
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

public class RunConfig extends BaseConfig {

	public RunConfig() throws IOException {
		super();
	}

	// IO
	private String inputDirectory = "in";
	private String outputFile = "out.csv";
	private Class<? extends Reader> reader = SemanticMarkupReader.class;
	private Class<? extends Writer> writer = CSVWriter.class;
	
	// Processing
	private Class<? extends TransformStrategy> transformStrategy = SomeTransformStrategy.class;
	private Class<? extends RawTransformStrategy> rawTransformStrategy = SomeRawTransformStrategy.class;
	private boolean inheritValues = false;
	private boolean generateAbsentPresent = false;
	private boolean inferCharactersFromOntologies = false;
	
	// Processing: Complete -> Raw
	private Class<? extends ColumnHeadTransformer> columnHeadTransformer = NameOrganColumnHeadTransformer.class;
	private Class<? extends RowHeadTransformer> rowHeadTransformer = TaxonomyRowHeadTransformer.class;
	private Class<? extends CellValueTransformer> cellValueTransformer = CombinedCellValueTransformer.class;
	private Class<? extends CellValueTransformer> defaultCellValueTransformer = SimpleCellValueTransformer.class;
	private List<ByChoiceCellValueTransformer> byChoiceCellValueTransformers = createByChoiceCellValueTransformers();
	private List<AddColumn> addColumns = createAddColumns();
	
	// Keywords
	private Set<String> presentRelation = new HashSet<String>(
			Arrays.asList(new String[] { "with", "attach", "include", "attached", "included" }));
	private Set<String> absentRelation = new HashSet<String>(
			Arrays.asList(new String[] { "without", "lack of", "devoid of", "missing", "misses" }));
	private Set<String> frequencyModifiers = new HashSet<String>(Arrays.asList(new String[] { "frequently", "rarely", "often" }));
	private Set<String> negationModifiers = new HashSet<String>(Arrays.asList(new String[] { "not" }));
	private Set<String> comparisonModifiers = new HashSet<String>(Arrays.asList(new String[] { "than" } ));
	private List<String> prependModifierPatterns = createPrependModifierPatterns();
	private List<String> appendModifierPatterns = createAppendModifierPatterns();

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
			bind(TransformStrategy.class).to(transformStrategy).in(Singleton.class);
			bind(RawTransformStrategy.class).to(rawTransformStrategy).in(Singleton.class);
			bind(Boolean.class).annotatedWith(Names.named("InheritValues")).toInstance(inheritValues);
			bind(Boolean.class).annotatedWith(Names.named("GenerateAbsentPresent")).toInstance(generateAbsentPresent);
			bind(Boolean.class).annotatedWith(Names.named("InferCharactersFromOntologies")).toInstance(inferCharactersFromOntologies);
			
			// Processing: Complete -> Raw
			bind(ColumnHeadTransformer.class).to(columnHeadTransformer);
			bind(RowHeadTransformer.class).to(rowHeadTransformer);
			bind(CellValueTransformer.class).to(cellValueTransformer);
			bind(new TypeLiteral<List<ByChoiceCellValueTransformer>>() {}).toInstance(byChoiceCellValueTransformers );
			bind(CellValueTransformer.class).annotatedWith(Names.named("DefaultCellValueTransformer")).to(defaultCellValueTransformer);			
			bind(new TypeLiteral<List<AddColumn>>() {}).toInstance(addColumns);
			
			// Keywords
			bind(new TypeLiteral<List<String>>() {}).annotatedWith(Names.named("PrependModifierPatterns")).toInstance(
					prependModifierPatterns );
			bind(new TypeLiteral<List<String>>() {}).annotatedWith(Names.named("AppendModifierPatterns")).toInstance(
					appendModifierPatterns );
			bind(new TypeLiteral<Set<String>>() {}).annotatedWith(Names.named("PresentRelation")).toInstance(
					presentRelation );
			bind(new TypeLiteral<Set<String>>() {}).annotatedWith(Names.named("AbsentRelation")).toInstance(
					absentRelation );
			
		//} catch(IOException e) {
		//	log(LogLevel.ERROR, "Exception loading configuration", e);
		//	throw new IllegalArgumentException();
		//}
	}

	private List<AddColumn> createAddColumns() {
		List<AddColumn> addColumns = new LinkedList<AddColumn>();
		addColumns.add(new AddSourceColumn());
		return addColumns;
	}

	private List<ByChoiceCellValueTransformer> createByChoiceCellValueTransformers() {
		List<ByChoiceCellValueTransformer> byChoiceCellValueTransformers = new LinkedList<ByChoiceCellValueTransformer>();
		byChoiceCellValueTransformers.add(new RangeValueByChoiceCellValueTransformer(prependModifierPatterns, appendModifierPatterns));
		return byChoiceCellValueTransformers;
	}

	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setInheritValues(boolean inheritValues) {
		this.inheritValues = inheritValues;
	}

	public void setGenerateAbsentPresent(boolean generateAbsentPresent) {
		this.generateAbsentPresent = generateAbsentPresent;
	}

	public void setInferCharactersFromOntologies(
			boolean inferCharactersFromOntologies) {
		this.inferCharactersFromOntologies = inferCharactersFromOntologies;
	}

	public void setPresentRelation(Set<String> presentRelation) {
		this.presentRelation = presentRelation;
	}

	public void setAbsentRelation(Set<String> absentRelation) {
		this.absentRelation = absentRelation;
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

	public void setFrequencyModifiers(Set<String> frequencyModifiers) {
		this.frequencyModifiers = frequencyModifiers;
	}

	public void setNegationModifiers(Set<String> negationModifiers) {
		this.negationModifiers = negationModifiers;
	}

	public void setComparisonModifiers(Set<String> comparisonModifiers) {
		this.comparisonModifiers = comparisonModifiers;
	}

	public void setColumnHeadTransformer(
			Class<? extends ColumnHeadTransformer> columnHeadTransformer) {
		this.columnHeadTransformer = columnHeadTransformer;
	}

	public void setRowHeadTransformer(
			Class<? extends RowHeadTransformer> rowHeadTransformer) {
		this.rowHeadTransformer = rowHeadTransformer;
	}

	public void setCellValueTransformer(
			Class<? extends CellValueTransformer> cellValueTransformer) {
		this.cellValueTransformer = cellValueTransformer;
	}

	public void setDefaultCellValueTransformer(
			Class<? extends CellValueTransformer> defaultCellValueTransformer) {
		this.defaultCellValueTransformer = defaultCellValueTransformer;
	}

	public void setByChoiceCellValueTransformers(
			List<ByChoiceCellValueTransformer> byChoiceCellValueTransformers) {
		this.byChoiceCellValueTransformers = byChoiceCellValueTransformers;
	}

	public void setPrependModifierPatterns(List<String> prependModifierPatterns) {
		this.prependModifierPatterns = prependModifierPatterns;
	}

	public void setAppendModifierPatterns(List<String> appendModifierPatterns) {
		this.appendModifierPatterns = appendModifierPatterns;
	}

	public void setAddColumns(List<AddColumn> addColumns) {
		this.addColumns = addColumns;
	}	
		
	
}
