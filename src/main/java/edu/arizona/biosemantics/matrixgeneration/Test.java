package edu.arizona.biosemantics.matrixgeneration;

import java.util.Arrays;
import java.util.regex.Pattern;

import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.io.complete.in.MicroPIESemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.complete.in.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Matrix;

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
//		Matrix matrix = new MicroPIESemanticMarkupReader("C:/micropie/output").read();
//		System.out.println("Read matrix:\n Taxa: " + matrix.getTaxaCount() + "\n Characters: " + matrix.getCharactersCount() + 
//				"\n Values: " + matrix.getSetCharacterValues());
		/**/
		String[] startArgs = {
			//"-input", "C:/Users/hongcui/Documents/trash/CathyTaxonComparisonDebug/FiveRanks/",
			//"-input", "C:/Users/hongcui/Documents/trash/CathyTaxonComparisonDebug/Cathy/",
			//"-output", "Test.csv",
			//"-input", "C:/micropie/output",//76,00_output_by_TC_task_56,C:/etcsitebase/etcsite/data/users/1/000   C:/micropie/output
			//"-output", "C:/micropie/outputser/MicroPIETest.csv",
			

			"-input", "C:/Users/hongcui/Documents/research/AuthorOntology/Data/Descriptions and Parsing/Carex_from_FNA_no_Keys_Term_Reviewed/out",
			
			//"C:/Users/hongcui/Lorena/Farjon_parsed0712_hong_local_parse_enhance",
			//"-output", "C:/Users/hongcui/Documents/etcsite/data/matrixGeneration/71/matrix.ser",
			"-output", "C:/Users/hongcui/Documents/research/AuthorOntology/Data/Descriptions and Parsing/Carex_from_FNA_no_Keys_Term_Reviewed/matrix.ser",
			//"C:/Users/hongcui/Lorena/Farjon_Matrix_etc_site/Farjon_matrix_local_enhance_mg_no_inference_no_inheritance.ser",
			//"-up_ontology_inheritance",
			//"-down_ontology_inheritance",
			//"-presence_relation",
			//"-presence_entity",
			"-taxon_group", "PLANT",//PLANT, PROKARYOTES
			"-output_format", "serialize" //serialize,csv //Matrix.ser, then run MatrixGenerationService.main to get final matrix 8/13/2018 
			//"-output_format", "csv" 
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
