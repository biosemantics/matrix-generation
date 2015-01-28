package edu.arizona.biosemantics.matrixgeneration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.config.RunConfig;
import edu.arizona.biosemantics.matrixgeneration.io.complete.in.SemanticMarkupReader;
import edu.arizona.biosemantics.matrixgeneration.io.raw.out.CSVWriter;
import edu.arizona.biosemantics.matrixgeneration.io.raw.out.SerializeWriter;
import edu.arizona.biosemantics.matrixgeneration.run.IRun;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.AbsentPresentFromBiologicalEntitiesTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.AbsentPresentFromRelationsTranformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.RemoveAttributeCharactersTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.NormalizeUnitsTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.NormalizeUnitsTransformer.Unit;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.OntologySuperclassInheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.OntologySubclassInheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.SplitRangeValuesTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.complete.TaxonomyDescendantInheritanceTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.AddSourceColumnTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RemoveNotApplicableValuesOnlyColumnsTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.RemoveSingleValueColumnsTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.SortTransformer;
import edu.arizona.biosemantics.matrixgeneration.transform.raw.TaxonomyAncestorInheritanceTransformer;

public class CLIMain {
	
	protected RunConfig config;

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		try {
			CLIMain cliMain = new CLIMain();
			cliMain.parse(args);
			cliMain.run();
		} catch(Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}

	protected void run() throws Throwable {
		log(LogLevel.DEBUG, "run using config:");
		log(LogLevel.DEBUG, config.toString());
		Injector injector = Guice.createInjector(config);
		IRun run = injector.getInstance(IRun.class);
		
		log(LogLevel.INFO, "running " + run.getDescription() + "...");
		try {
			run.run();
		} catch (Throwable t) {
			log(LogLevel.ERROR, "Problem to execute the run", t);
			throw t;
		}
	}

	protected void parse(String[] args) throws IOException {
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("input", "input", true, "input directory");
		options.addOption("output", "output", true, "output file");
		options.addOption("output_format", "output format", true, "select output format");
		options.addOption("up_taxonomy_inheritance", "inherit characters to taxonomy parents", false, 
				"inherit characters from child to parent");
		options.addOption("down_taxonomy_inheritance", "inherit characters to taxonomy children", false, 
				"inherit characters from parent to child");
		options.addOption("presence_relation", "generate absent/present from relations", false, 
				"generate absent present characters from structure relationships");
		options.addOption("presence_entity", "generate absent/present from biological entity presence", false, 
				"generate absent/present from biological entity presence");
		options.addOption("up_ontology_inheritance", "infer characters from ontology superclasses", false, 
				"infer characters from ontology using superclass relationships");
		options.addOption("down_ontology_inheritance", "infer characters from ontology subclasses", false, 
				"infer characters from ontology using subclass relationships");
		options.addOption("remove_attributes", "remove attribute characters", false, "remove attribute characters");
		options.addOption("remove_single_states", "remove single state characters", false, "remove single state characters");
		options.addOption("normalize_units", "normalize units", true, "normalize units");
		options.addOption("split_ranges", "split range values into separate characters", false, 
				"split range values into separate characters for extreme values");
		options.addOption("add_source", "add source file as column", false, 
				"add source file as column");
		options.addOption("help", "help", false, "shows the help");
				
		try {
			config = new RunConfig();
		} catch(IOException e) {
			log(LogLevel.ERROR, "Couldn't instantiate default config", e);
			throw e;
		}
			
		try {
		    CommandLine commandLine = parser.parse( options, args );
		    if(commandLine.hasOption("help")) {
		    	HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "what is this?", options );
				return;
		    }
		    if(!commandLine.hasOption("input")) {
		    	log(LogLevel.ERROR, "You have to specify an input directory");
		    	throw new IllegalArgumentException();
		    }
		    config.setInputDirectory(commandLine.getOptionValue("input"));
		    if(!commandLine.hasOption("output")) {
		    	log(LogLevel.ERROR, "You have to specify an output file or directory");
		    	throw new IllegalArgumentException();
		    }
		    config.setOutputFile(commandLine.getOptionValue("output"));
		    
		    //could determine order here based on parameters
		    List<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer> completeTransformers = 
		    		new LinkedList<edu.arizona.biosemantics.matrixgeneration.transform.complete.Transformer>();
		    List<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer> rawTransformers = 
		    		new LinkedList<edu.arizona.biosemantics.matrixgeneration.transform.raw.Transformer>();
		    
		    Injector injector = Guice.createInjector(config);
		    if(commandLine.hasOption("presence_relation")) {
			    completeTransformers.add(injector.getInstance(AbsentPresentFromRelationsTranformer.class));
		    }
		    if(commandLine.hasOption("presence_entity")) {
		    	completeTransformers.add(injector.getInstance(AbsentPresentFromBiologicalEntitiesTransformer.class));
		    }
		    if(commandLine.hasOption("up_ontology_inheritance")) {
				completeTransformers.add(injector.getInstance(OntologySubclassInheritanceTransformer.class));
		    }
		    if(commandLine.hasOption("down_ontology_inheritance")) {
				completeTransformers.add(injector.getInstance(OntologySuperclassInheritanceTransformer.class));
		    }
		    if(commandLine.hasOption("down_taxonomy_inheritance")) {
		    	completeTransformers.add(injector.getInstance(TaxonomyDescendantInheritanceTransformer.class));
		    }
		    if(commandLine.hasOption("up_taxonomy_inheritance")) {
		    	rawTransformers.add(injector.getInstance(TaxonomyAncestorInheritanceTransformer.class));
		    }
		    if(commandLine.hasOption("normalize_units")) {
		    	String unitString = commandLine.getOptionValue("normalize_units");
		    	try { 
		    		Unit unit = Unit.valueOf(unitString);
		    		NormalizeUnitsTransformer transformer = injector.getInstance(NormalizeUnitsTransformer.class);
		    		transformer.setUnit(unit);
			    	completeTransformers.add(transformer);
		    	} catch(Exception e) {
		    		log(LogLevel.ERROR, "Couldn't parse unit", e);
		    		throw new IllegalArgumentException(e);
		    	}
		    }
		    if(commandLine.hasOption("split_ranges")) {
		    	completeTransformers.add(injector.getInstance(SplitRangeValuesTransformer.class));
		    }
			if(commandLine.hasOption("remove_attributes")) {
				completeTransformers.add(injector.getInstance(RemoveAttributeCharactersTransformer.class));
			} 
			if(commandLine.hasOption("remove_single_states")) {
				rawTransformers.add(injector.getInstance(RemoveSingleValueColumnsTransformer.class));
			}
			if(commandLine.hasOption("add_source")) {
				rawTransformers.add(injector.getInstance(AddSourceColumnTransformer.class));
			}
			rawTransformers.add(injector.getInstance(RemoveNotApplicableValuesOnlyColumnsTransformer.class));
			rawTransformers.add(injector.getInstance(SortTransformer.class));
			
		    config.setCompleteTransformers(completeTransformers);
		    config.setRawTransformers(rawTransformers);
		    config.setReader(SemanticMarkupReader.class);
		    if(commandLine.hasOption("output_format")) {
		    	switch(commandLine.getOptionValue("output_format")) {
		    	case "csv":
		    		config.setWriter(CSVWriter.class);
		    		break;
		    	case "serialize":
		    		config.setWriter(SerializeWriter.class);
		    		break;
		    	default:
		    		throw new IllegalArgumentException("output format not supported");
		    	}
		    } else 
		    	config.setWriter(CSVWriter.class);
		    
		} catch(ParseException e) {
			log(LogLevel.ERROR, "Problem parsing parameters", e);
			throw new IllegalArgumentException(e);
		}
	}
	
}
