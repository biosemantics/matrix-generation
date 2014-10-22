package edu.arizona.biosemantics.matrixgeneration;

public class Test {

	public static void main(String[] args) throws Exception {
		Configuration.getInstance();
		String[] startArgs = { "C:/Users/rodenhausen/etcsite/users/1068/shrooms_23", 
				"C:/Users/rodenhausen/etcsite/cache/matrixGeneration/105/Matrix.mx",
				"true", "true" };
		Main.main(startArgs);
	}
	
}
