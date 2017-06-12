package edu.arizona.biosemantics.matrixgeneration.run;

import java.io.IOException;
import java.util.Calendar;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;

/**
 * An AbstractRun implements some shared functionality of concrete Run implementations such as time string formating, config reporting,..
 * @author rodenhausen
 */
public abstract class AbstractRun implements IRun {

	protected String guiceModuleFile;

	/**
	 * @param guiceModuleFile
	 */
	@Inject
	public AbstractRun(@Named("GuiceModuleFile")String guiceModuleFile) {
		this.guiceModuleFile = guiceModuleFile;
	}
	
	public void run() throws Throwable {	
		StringBuilder config = new StringBuilder();
		appendConfigFile(config);
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		String startedAt = "started at " + startTime;
		config.append(startedAt + "\n\n");
		log(LogLevel.INFO, startedAt);
		
		doRun();
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		String wasDone = "was done at " + endTime;
		config.append(wasDone + "\n");
		log(LogLevel.INFO, wasDone);
		long milliseconds = endTime - startTime;
		String tookMe = "took me " + (endTime - startTime) + " milliseconds";
		config.append(tookMe + "\n");
		log(LogLevel.INFO, tookMe);
		
		String timeString = getTimeString(milliseconds);
		config.append(timeString + "\n");
		log(LogLevel.INFO, timeString);
	}

	protected abstract void doRun() throws Throwable;

	public String getDescription() {
		return this.getClass().toString();
	}

	
	protected void appendConfigFile(StringBuilder stringBuilder) throws IOException {
		stringBuilder.append("GuiceModule configuration of Run \n" +
		  "---------------------\n");
		stringBuilder.append(this.guiceModuleFile);
		stringBuilder.append("---------------------\n\n");
	}
	
	protected String getTimeString(long milliseconds) {
		int hours = (int)Math.floor(milliseconds/(1000 * 60.0 * 60.0));
		milliseconds = milliseconds - hours * (1000 * 60 * 60);
		int minutes = (int)Math.floor(milliseconds/(1000.0 * 60.0));
		milliseconds = milliseconds - minutes * (1000 * 60);
		int seconds = (int)(milliseconds/1000.0);
		
		String timeString = "that's " + hours + " hours, " + minutes + 
							" minutes, and " + seconds + " seconds";
		return timeString;
	}
}
