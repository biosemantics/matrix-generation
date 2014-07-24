package edu.arizona.biosemantics.matrixgeneration;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.matrixgeneration.io.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.NexusWriter;
import edu.arizona.biosemantics.matrixgeneration.io.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.SDDWriter;
import edu.arizona.biosemantics.matrixgeneration.io.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.InheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.NormalizeUnitsTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.SplitRangeValuesTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.Transformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.NormalizeUnitsTransformer.Unit;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.ByChoiceCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.CellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.ColumnHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.CombinedCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.NameOrganColumnHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RangeValueByChoiceCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RawMatrixTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RowHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.SimpleCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.TaxonomyRowHeadTransformer;

public class Main {

	private Set<String> frequencyModifiers = new HashSet<String>(Arrays.asList(new String[] { "frequently", "rarely", "often" }));
	private Set<String> negationModifiers = new HashSet<String>(Arrays.asList(new String[] { "not" }));
	private Set<String> comparisonModifiers = new HashSet<String>(Arrays.asList(new String[] { "than" } ));
	
	private List<String> prependModifierPatterns;
	private List<String> appendModifierPatterns;
	private String inputDir;
	private String outputFile;
	
	public Main(String inputDir, String outputFile) {
		this.inputDir = inputDir;
		this.outputFile = outputFile;
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
		Reader reader = new SemanticMarkupReader(new File(inputDir));
		Matrix matrix = reader.read();
		//System.out.println("read matrix: " + matrix.getTaxaCount() + " taxa, " + matrix.getCharactersCount() + " characters.\n" + matrix.toString());
		
		Transformer inherit = new InheritanceTransformer();
		inherit.transform(matrix);
		
		//Transformer switchUnits = new NormalizeUnitsTransformer(Unit.mm);
		//switchUnits.transform(matrix);
		
		//Transformer splitRangeValues = new SplitRangeValuesTransformer();
		//splitRangeValues.transform(matrix);
		
		//System.out.println("transformed matrix: " + matrix.getTaxaCount() + " taxa, " + matrix.getCharactersCount() + " characters.\n " + matrix.toString());
		
		List<ByChoiceCellValueTransformer> byChoiceCellValueTransformers = new LinkedList<ByChoiceCellValueTransformer>();
		byChoiceCellValueTransformers.add(new RangeValueByChoiceCellValueTransformer(prependModifierPatterns, appendModifierPatterns));
		CellValueTransformer cellValueTransformer = new CombinedCellValueTransformer(byChoiceCellValueTransformers, 
				new SimpleCellValueTransformer(prependModifierPatterns, appendModifierPatterns));
		ColumnHeadTransformer columnHeadTransformer = new NameOrganColumnHeadTransformer();
		RowHeadTransformer rowHeadTransformer = new TaxonomyRowHeadTransformer();
		//CellValueTransformer cellValueTransformer = new SimpleCellValueTransformer();
		
		RawMatrixTransformer rawMatrixTransformer = new RawMatrixTransformer(columnHeadTransformer,
				rowHeadTransformer, cellValueTransformer);
		RawMatrix rawMatrix = rawMatrixTransformer.transform(matrix);
		
		//System.out.println("raw matrix: " + rawMatrix.getRowCount() + " rows, " + rawMatrix.getColumnCount() + " columns.\n " + rawMatrix.toString());
		Writer writer = new CSVWriter(new File(outputFile));
		//Writer writer = new SDDWriter(new File("matrix.sdd"));
		//Writer writer = new NexusWriter(new File("matrix.nxs"));
		writer.write(rawMatrix);
	}
	
	public static void main(String[] args) throws Exception {
		Main main = new Main("input", "matrix.csv");
		main.run();
	}
	
}
