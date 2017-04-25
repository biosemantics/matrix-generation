package edu.arizona.biosemantics.matrixgeneration;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws Throwable {
		/*String[] startArgs = { "-input", "in", "-output", "MatrixTest.csv"
				//, "-up_taxonomy_inheritance"
				, "-down_taxonomy_inheritance"
				, "-presence_relation"
				, "-presence_entity"
				, "-up_ontology_inheritance"
				, "-down_ontology_inheritance"
				//, "-remove_attributes"
				//, "-remove_single_states"
				, "-output_format", "csv"
				, "-taxon_group", "PLANT"
				};*/
		String[] startArgs = {
			//"-input", "C:/Users/hongcui/Documents/trash/CathyTaxonComparisonDebug/FiveRanks/",
			"-input", "C:/Users/hongcui/Documents/trash/CathyTaxonComparisonDebug/Cathy/",
			"-output", "Test.csv",
			"-up_ontology_inheritance",
			"-down_ontology_inheritance",
			"-taxon_group", "PLANT",
			"-output_format", "csv"
		};
		CLIMain.main(startArgs);
	}
	
	/*public static void main(String[] args) throws Throwable {
		String[] startArgs = { "-input", "mc", "-output", "Matrix.csv"
				//, "-up_taxonomy_inheritance"
				//, "-down_taxonomy_inheritance"
				//, "-presence_relation"
				//, "-presence_entity"
				, "-up_ontology_inheritance"
				//, "-down_ontology_inheritance"
				//, "-remove_attributes"
				//, "-remove_single_states"
				};
		CLIMain.main(startArgs);
	}*/
	
}
