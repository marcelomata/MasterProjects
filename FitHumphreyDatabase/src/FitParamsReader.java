import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class FitParamsReader {

	private double[][][] paramsSigmoidTotal;
	private double[][][] paramsSigmoidModel;
	private File file;
	private int age;
	private String eyeSide;
	
	public FitParamsReader(File file) {
		this.paramsSigmoidTotal = new double[10][10][3];
		this.paramsSigmoidModel = new double[10][10][3];
		this.file = file;
		this.age = Integer.parseInt(file.getName().substring(0, 1));
		this.eyeSide = file.getName().substring(3, file.getName().length()-4);
	}
	
	public void loadParams() {
		BufferedReader in;
		String line;
		String words[];
		int i, j;
		try {
			in = new BufferedReader(new FileReader(file));
			line = in.readLine();
			if(line != null) {
				if(line.contains(",")) {
					words = line.split(",");
					i = Integer.parseInt(words[0]);
					j = Integer.parseInt(words[1]);
					line = in.readLine();
					words = line.split(" ");
					paramsSigmoidTotal[i][j][0] = Double.parseDouble(words[0]);
					paramsSigmoidTotal[i][j][1] = Double.parseDouble(words[1]);
					paramsSigmoidTotal[i][j][2] = Double.parseDouble(words[2]);
					words = line.split(" ");
					paramsSigmoidModel[i][j][0] = Double.parseDouble(words[0]);
					paramsSigmoidModel[i][j][1] = Double.parseDouble(words[1]);
					paramsSigmoidModel[i][j][2] = Double.parseDouble(words[2]);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println("Error reading file "+file.getName());
		}
	}
	
}
