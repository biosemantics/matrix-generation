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
	
	public Configuration() throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		properties.load(loader.getResourceAsStream("config.properties"));
		this.projectVersion = properties.getProperty("project.version");
	}

	public String getProjectVersion() {
		return projectVersion;
	}
	
}
