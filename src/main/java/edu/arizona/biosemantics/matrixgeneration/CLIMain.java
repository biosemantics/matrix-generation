package edu.arizona.biosemantics.matrixgeneration;

import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.matrixgeneration.config.RunConfig;
import edu.arizona.biosemantics.matrixgeneration.run.IRun;

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
		options.addOption("i", "input", true, "input directory");
		options.addOption("o", "output", true, "output file");
		options.addOption("h", "inherit characters", false, 
				"inherit characters from parent to child");
		options.addOption("p", "generate absent/present", false, 
				"generate absent present characters from structure relationships");
		options.addOption("x", "infer characters from ontology", false, 
				"infer characters from ontology using superclass/subclass relationships");
		options.addOption("h", "help", false, "shows the help");
				
		try {
			config = new RunConfig();
		} catch(IOException e) {
			log(LogLevel.ERROR, "Couldn't instantiate default config", e);
			throw e;
		}
			
		try {
		    CommandLine commandLine = parser.parse( options, args );
		    if(commandLine.hasOption("h")) {
		    	HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "what is this?", options );
				return;
		    }
		    if(!commandLine.hasOption("i")) {
		    	log(LogLevel.ERROR, "You have to specify an input directory");
		    	throw new IllegalArgumentException();
		    }
		    if(!commandLine.hasOption("o")) {
		    	log(LogLevel.ERROR, "You have to specify an output file or directory");
		    	throw new IllegalArgumentException();
		    }
		    if(commandLine.hasOption("h")) {
		    	config.setInheritValues(true);
		    }
		    if(commandLine.hasOption("p")) {
		    	config.setGenerateAbsentPresent(true);
		    }
			if(commandLine.hasOption("x")) {
				config.setInferCharactersFromOntologies(true);
			}
		    
		} catch(ParseException e) {
			log(LogLevel.ERROR, "Problem parsing parameters", e);
		}
	}
	
}
