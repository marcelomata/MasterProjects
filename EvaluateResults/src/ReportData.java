
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ReportData {

	private final static int NUM_ROWS = 10;
	private final static int NUM_COLUMNS = 10;
	
	private String patientName;
	private String birthday;
	private int age;
	private String eyeSide;
	
	private double VFI;
	private double MD;
	private double PSD;
	private String GHT;
	private double[][] intensities;
	private double[][] totalDeviation;
	private double[][] totalDeviationPercent;
	private double[][] modelDeviation;
	private double[][] modelDeviationPercent;
	private String faslseNegative;
	private String faslsePositive;
	private String fixationLosing;
	
	private String hour;
	private String duration;
	private String date;
	
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
	
	public double[][] getNumericIntensities() {
		return intensities;
	}
	
	public void saveFileAsTxt() {
		FileWriter fw;
		try {
			fw = new FileWriter(reportTxt);
			fw.write(""+(reportOk ? 'y':'n')); fw.write("\n");
			if(reportOk) {
				fw.write(""+(modelDeviationOk ? 'y':'n')); fw.write("\n");
				fw.write(""+patientName); fw.write("\n");
				fw.write(birthday); fw.write("\n");
				fw.write(""+age); fw.write("\n");
				fw.write(eyeSide); fw.write("\n");
				
				fw.write(""+VFI); fw.write("\n");
				fw.write(""+MD); fw.write("\n");
				fw.write(""+PSD); fw.write("\n");
				fw.write(GHT); fw.write("\n");
				writeArray(fw, intensities);
				writeArray(fw, totalDeviation);
				writeArray(fw, totalDeviationPercent);
				writeArray(fw, modelDeviation);
				writeArray(fw, modelDeviationPercent);
				fw.write(faslseNegative); fw.write("\n");
				fw.write(faslsePositive); fw.write("\n");
				fw.write(fixationLosing); fw.write("\n");
				
				fw.write(hour); fw.write("\n");
				fw.write(duration); fw.write("\n");
				fw.write(date); fw.write("\n");
			} else {
				fw.close();
				reportTxt.delete();
			}
			
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
				patientName = in.readLine();
				birthday = in.readLine();
				age = Integer.parseInt(in.readLine());
				eyeSide = in.readLine();
				
				VFI = Double.parseDouble(in.readLine());
				MD = Double.parseDouble(in.readLine());
				PSD = Double.parseDouble(in.readLine());
				GHT = in.readLine();
				intensities = readArray(in);
				totalDeviation = readArray(in);
				totalDeviationPercent = readArray(in);
				modelDeviation = readArray(in);
				modelDeviationPercent = readArray(in);
				faslseNegative = in.readLine();
				faslsePositive = in.readLine();
				fixationLosing = in.readLine();
				
				hour = in.readLine();
				duration = in.readLine();
				date = in.readLine();
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
	
	public String getPatientName() {
		return patientName;
	}
	
}
