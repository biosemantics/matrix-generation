import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;

import edu.arizona.biosemantics.matrixgeneration.model.complete.Statement;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Taxon;
import edu.arizona.biosemantics.matrixgeneration.model.complete.Value;
import edu.arizona.biosemantics.matrixgeneration.model.raw.Matrix;


public class Test {

	public static void main(String[] args) {
		Matrix model = null;
		try(ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(
				new File("C:/etcsitebase/etcsite/data/matrixGeneration/336/Matrix.ser"))))) {
			//C:/etcsitebase/etcsite/data/matrixGeneration/336/TaxonMatrix.ser
			//C:/micropie/outputser/MicroPIETest.ser
			model = (Matrix)input.readObject();
			List<Taxon> taxa = model.getSource().getTaxa();
			for(Value v : model.getSource().getValues())
				System.out.println(v.getValue()+"===>"+v.getStatement().getText());
//			for(Taxon t:taxa){
//				for(Statement s:t.getStatement())
//					System.out.println(s.getText());
//				t.get
//			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
