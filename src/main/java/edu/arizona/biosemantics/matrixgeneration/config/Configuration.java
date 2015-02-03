package edu.arizona.biosemantics.matrixgeneration.config;

import java.io.IOException;
import java.util.Properties;

import edu.arizona.biosemantics.common.log.Logger;

public class Configuration {

	private final static Logger logger = Logger.getLogger(Configuration.class);
	
	public static String projectVersion;
	public static String ontologyDirectory;
	public static String wordNetDirectory;
	
	public static String rdfPrefixStructure;
	public static String rdfPrefixCharacter;
	public static String rdfPrefixProperty;
	public static String rdfPrefixModifier;
	public static String rdfPrefixConstraint;
	public static String rdfPrefixDescription;
	public static String rdfSyntax;
	public static String rdfBiol;
	
	static {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			properties.load(loader.getResourceAsStream("edu/arizona/biosemantics/matrixgeneration/static.properties"));
			projectVersion = properties.getProperty("project.version");
			rdfPrefixStructure = properties.getProperty("rdfPrefixSturcture");
			rdfPrefixCharacter = properties.getProperty("rdfPrefixCharacter");
			rdfPrefixProperty = properties.getProperty("rdfPrefixProperty");
			rdfPrefixModifier = properties.getProperty("rdfPrefixModifier");
			rdfPrefixConstraint = properties.getProperty("rdfPrefixConstraint");
			rdfPrefixDescription = properties.getProperty("rdfPrefixDescription");
			rdfSyntax = properties.getProperty("rdfSyntax");
			rdfBiol = properties.getProperty("rdfBiol");
			
			properties.load(loader.getResourceAsStream("edu/arizona/biosemantics/matrixgeneration/config.properties"));
			ontologyDirectory = properties.getProperty("ontologyDirectory");
			wordNetDirectory = properties.getProperty("wordNetDirectory");
		} catch (IOException e) {
			logger.error("Couldn't read configuration", e);
		}
	}
}
