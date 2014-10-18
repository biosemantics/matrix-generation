package edu.arizona.biosemantics.matrixgeneration.io.raw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import edu.arizona.biosemantics.matrixgeneration.model.raw.RawMatrix;

public class SerializeWriter implements Writer {

	private File file;

	public SerializeWriter(File file) {
		this.file = file;
	}
	
	@Override
	public void write(RawMatrix rawMatrix) throws FileNotFoundException, IOException {
         try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
        	 out.writeObject(rawMatrix);
        	 out.flush();
         }
	}
	
}
