package edu.arizona.biosemantics.matrixgeneration;

import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private static Configuration instance;
	
	public static Configuration getInstance() throws IOException {
		if(instance == null)
			instance = new Configuration();
		return instance;
	}

	private String projectVersion;
	private String rdfPrefixStructure;
	private String rdfPrefixCharacter;
	private String rdfPrefixProperty;
	private String rdfPrefixModifier;
	private String rdfPrefixConstraint;
	private String rdfPrefixDescription;
	private String rdfSyntax;
	private String rdfBiol;
	
	public Configuration() throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		properties.load(loader.getResourceAsStream("config.properties"));
		this.projectVersion = properties.getProperty("project.version");
		this.rdfPrefixStructure = properties.getProperty("rdfPrefixSturcture");
		this.rdfPrefixCharacter = properties.getProperty("rdfPrefixCharacter");
		this.rdfPrefixProperty = properties.getProperty("rdfPrefixProperty");
		this.rdfPrefixModifier = properties.getProperty("rdfPrefixModifier");
		this.rdfPrefixConstraint = properties.getProperty("rdfPrefixConstraint");
		this.rdfPrefixDescription = properties.getProperty("rdfPrefixDescription");
		this.rdfSyntax = properties.getProperty("rdfSyntax");
		this.rdfBiol = properties.getProperty("rdfBiol");
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public String getRdfPrefixStructure() {
		return rdfPrefixStructure;
	}

	public String getRdfPrefixCharacter() {
		return rdfPrefixCharacter;
	}

	public String getRdfPrefixProperty() {
		return rdfPrefixProperty;
	}

	public String getRdfPrefixModifier() {
		return rdfPrefixModifier;
	}

	public String getRdfPrefixConstraint() {
		return rdfPrefixConstraint;
	}

	public String getRdfPrefixDescription() {
		return rdfPrefixDescription;
	}

	public String getRdfSyntax() {
		return rdfSyntax;
	}

	public String getRdfBiol() {
		return rdfBiol;
	}
		
}
