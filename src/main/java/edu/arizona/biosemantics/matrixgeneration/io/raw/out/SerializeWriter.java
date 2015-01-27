package edu.arizona.biosemantics.matrixgeneration.io.raw.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;

public class SerializeWriter implements Writer {

	private String outputFile;

	@Inject
	public SerializeWriter(@Named("OutputFile") String outputFile) {
		this.outputFile = outputFile;
	}
	
	@Override
	public void write(Matrix rawMatrix) throws FileNotFoundException, IOException {
         try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(outputFile)))) {
        	 out.writeObject(rawMatrix);
        	 out.flush();
         }
	}
	
}
