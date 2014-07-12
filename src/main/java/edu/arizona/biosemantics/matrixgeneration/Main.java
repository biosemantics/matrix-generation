package edu.arizona.biosemantics.matrixgeneration;

import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.io.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.Reader;
import edu.arizona.biosemantics.matrixgeneration.io.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.Writer;
import edu.arizona.biosemantics.matrixgeneration.model.Taxon;
import edu.arizona.biosemantics.matrixgeneration.transform.InheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.Transformer;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Reader reader = new SemanticMarkupReader();
		List<Taxon> taxa = reader.read();
		
		Transformer transformer = new InheritanceTransformer();
		transformer.transform(taxa);
		
		Writer writer = new CSVWriter();
		writer.write(taxa);
	}
	
}
