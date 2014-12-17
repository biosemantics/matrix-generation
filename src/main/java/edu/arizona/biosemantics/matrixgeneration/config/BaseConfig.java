package edu.arizona.biosemantics.matrixgeneration.config;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;


public class BaseConfig extends AbstractModule {

	  private String version = "N/A";
	  
	  public BaseConfig() throws IOException {			
		  ClassLoader loader = Thread.currentThread().getContextClassLoader();
		  Properties properties = new Properties();
		  properties.load(loader.getResourceAsStream("edu/arizona/biosemantics/matrixgeneration/static.properties"));
		  this.version = properties.getProperty("project.version");
	  }
	
	@Override
	protected void configure() {
		//try {
			  bind(String.class).annotatedWith(Names.named("Version")).toInstance(version);
			  bind(String.class).annotatedWith(Names.named("GuiceModuleFile")).toInstance(this.toString());
		//} catch(IOException e) {
		//	log(LogLevel.ERROR, "Exception loading configuration", e);
		//	throw new IllegalArgumentException();
		//}
	}

}
