package edu.arizona.biosemantics.matrixgeneration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.io.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.InheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.NormalizeUnitsTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.Transformer;
import edu.arizona.biosemantics.matrixgeneration.transform.matrix.NormalizeUnitsTransformer.Unit;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.CellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.ColumnHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.CombinedCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.CombinedCellValueTransformer.ByChoiceCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.NameOrganColumnHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RangeValueByChoiceCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RawMatrixTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RowHeadTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.SimpleCellValueTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.TaxonomyRowHeadTransformer;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Reader reader = new SemanticMarkupReader(new File("input"));
		Matrix matrix = reader.read();
		System.out.println("read matrix: \n" + matrix.toString());
		
		Transformer inherit = new InheritanceTransformer();
		inherit.transform(matrix);
		
		Transformer switchUnits = new NormalizeUnitsTransformer(Unit.mm);
		switchUnits.transform(matrix);
		
		System.out.println("transformed matrix: \n " + matrix.toString());
		
		/*List<ByChoiceCellValueTransformer> byChoiceCellValueTransformers = new LinkedList<ByChoiceCellValueTransformer>();
		byChoiceCellValueTransformers.add(new RangeValueByChoiceCellValueTransformer());
		CellValueTransformer cellValueTransformer = new CombinedCellValueTransformer(byChoiceCellValueTransformers, 
				new SimpleCellValueTransformer()); */
		ColumnHeadTransformer columnHeadTransformer = new NameOrganColumnHeadTransformer();
		RowHeadTransformer rowHeadTransformer = new TaxonomyRowHeadTransformer();
		CellValueTransformer cellValueTransformer = new SimpleCellValueTransformer();
		
		RawMatrixTransformer rawMatrixTransformer = new RawMatrixTransformer(columnHeadTransformer,
				rowHeadTransformer, cellValueTransformer);
		RawMatrix rawMatrix = rawMatrixTransformer.transform(matrix);
		
		System.out.println("raw matrix: \n " + rawMatrix.toString());
		Writer writer = new CSVWriter(new File("matrix.csv"));
		writer.write(rawMatrix);
	}
	
}
