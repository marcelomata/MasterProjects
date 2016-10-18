
import java.io.File;

import converter.ConvertersManager;
import converter.FileVersion;


public class Main {

	public static void main(String[] args) {
		
		
		ConvertersManager manager = new ConvertersManager();
		manager.convertAllFiles(new File("./prototype-files/"), new File("./convertedFiles/"), FileVersion.v0_1, FileVersion.v1_0);

	}

}
