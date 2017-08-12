package edu.arizona.biosemantics.matrixgeneration.run;

public interface IRun {

	String getDescription();

	void run() throws Throwable;

}
