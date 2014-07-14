package edu.arizona.biosemantics.matrixgeneration;

import java.io.File;
import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.io.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.Matrix;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.transform.InheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.Transformer;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Reader reader = new SemanticMarkupReader(new File("input"));
		Matrix matrix = reader.read();
		System.out.println(matrix.toString());
		
		Transformer transformer = new InheritanceTransformer();
		transformer.transform(matrix);
		
		System.out.println("------ after transform -------");
		System.out.println(matrix.toString());
		Writer writer = new CSVWriter();
		writer.write(matrix);
	}
	
}
