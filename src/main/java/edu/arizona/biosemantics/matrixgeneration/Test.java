package edu.arizona.biosemantics.matrixgeneration;

public class Test {

	public static void main(String[] args) throws Exception {
		Configuration.getInstance();
		String[] startArgs = { "C:/Users/rodenhausen/etcsite/users/1068/sample_1", 
				"C:/Users/rodenhausen/etcsite/cache/matrixGeneration/105/Matrix.mx",
				"true", "true" };
		Main.main(startArgs);
	}
	
}
