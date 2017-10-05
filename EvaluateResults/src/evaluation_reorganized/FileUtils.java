package evaluation_reorganized;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static void saveResultOnFile(double[][] diff_left_1, double[][] diff_left_2, double[][] diff_right_1,
			double[][] diff_right_2, File evaluationFile) throws IOException {
		FileWriter fw = new FileWriter(evaluationFile);
		fw.write("SQ_res Left = "+diff_left_1[0]);
		System.out.print(diff_left_2[0]+",");
		fw.write("SQ_res Right = "+diff_right_1+"\n");
		System.out.print(diff_right_2[1]+",\n");
		fw.flush();
		fw.close();
	}
	
}
