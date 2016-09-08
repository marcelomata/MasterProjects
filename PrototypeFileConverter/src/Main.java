
import java.io.File;

import converter.Converters;
import converter.ExaminationData;
import converter.FileVersion;
import converter.MobileCampimeterFileConverter;


public class Main {

	public static void main(String[] args) {
		
		File fileSource = null;
		
		ExaminationData data = null;
		
		MobileCampimeterFileConverter converter = Converters.getInstance(data, FileVersion.v2_0);
		File fileTarget = new File("./");
		converter.convert(fileTarget);

	}

}
