
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ReportData {

	private final static int NUM_ROWS = 10;
	private final static int NUM_COLUMNS = 10;
	
	private double[][] intensities;
	private double[][] totalDeviation;
	private double[][] totalDeviationPercent;
	private double[][] modelDeviation;
	private double[][] modelDeviationPercent;
	private int age;
	private String eyeSide;
	
	private boolean modelDeviationOk;
	private boolean reportOk;
	
	private File reportTxt;
//	private File pdfFile;
	
	public ReportData(File pdfFile, File txtFile) {
//		this.pdfFile = pdfFile;
		if(pdfFile != null) {
			reportTxt = new File("./reportsTxt/"+pdfFile.getName().substring(0, pdfFile.getName().length()-3)+"txt");
			if(!reportTxt.getParentFile().exists()) {
				reportTxt.getParentFile().mkdirs();
			}
			if(!reportTxt.exists()) {
				try {
					reportTxt.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			reportTxt = txtFile;
		}
	}

	public int getAge() {
		return age;
	}
	
	public String getEyeSide() {
		return eyeSide;
	}
	
	public double getModelDeviation(int i, int j) {
		return modelDeviation[i][j];
	}
	
	public double getTotalDeviation(int i, int j) {
		return totalDeviation[i][j];
	}
	
	public double getModelDeviationPercentage(int i, int j) {
		return modelDeviationPercent[i][j];
	}
	
	public double getTotalDeviationPercentage(int i, int j) {
		return totalDeviationPercent[i][j];
	}
	
	public double getNumericIntensities(int i, int j) {
		return intensities[i][j];
	}
	
	public void saveFileAsTxt() {
		FileWriter fw;
		try {
			fw = new FileWriter(reportTxt);
			fw.write(""+(reportOk ? 'y':'n')); fw.write("\n");
			fw.write(""+(modelDeviationOk ? 'y':'n')); fw.write("\n");
			fw.write(""+age); fw.write("\n");
			fw.write(eyeSide); fw.write("\n");
			writeArray(fw, intensities);
			writeArray(fw, totalDeviation);
			writeArray(fw, totalDeviationPercent);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeArray(FileWriter fw, double[][] numeric) {
		try {
			for (int i = 0; i < numeric.length; i++) {
				for (int j = 0; j < numeric[i].length; j++) {
					fw.write(numeric[i][j]+" ");
				}
				fw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDataFromTxt() {
		BufferedReader in;
		String line;
		try {
			in = new BufferedReader(new FileReader(reportTxt));
			reportOk = in.readLine().charAt(0) == 'y' ? true : false;
			if(reportOk) {
				modelDeviationOk = in.readLine().charAt(0) == 'y' ? true : false;
				line = in.readLine();
				age = Integer.parseInt(line);
				eyeSide = in.readLine();
				intensities = readArray(in);
				totalDeviation = readArray(in);
				totalDeviationPercent = readArray(in);
				modelDeviation = readArray(in);
				modelDeviationPercent = readArray(in);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double[][] readArray(BufferedReader in) {
		double[][] numeric = new double[NUM_ROWS][NUM_COLUMNS];
		String line[];
		try {
			for (int i = 0; i < NUM_ROWS; i++) {
				line = in.readLine().split(" ");
				for (int j = 0; j < NUM_COLUMNS; j++) {
					numeric[i][j] = Double.parseDouble(line[j]);
				}
			}
		} catch (Exception e) {
			
		}
		return numeric;
	}
	
	public boolean isModelDeviationOk() {
		return modelDeviationOk;
	}
	
	public boolean isReportOk() {
		return reportOk;
	}
	
}
