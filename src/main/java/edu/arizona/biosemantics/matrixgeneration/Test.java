package edu.arizona.biosemantics.matrixgeneration;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws Throwable {
		String[] startArgs = { "-input", "input", "-output", "MatrixTest3.csv"
				, "-up_taxonomy_inheritance"
				, "-down_taxonomy_inheritance"
				, "-presence_relation"
				, "-presence_entity"
				, "-up_ontology_inheritance"
				, "-down_ontology_inheritance"
				, "-remove_attributes"
				//, "-remove_single_states"
				, "-output_format", "csv"
				, "-taxon_group", "PORIFERA"
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
