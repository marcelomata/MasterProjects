package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ReportData {

	private final static int NUM_ROWS = 10;
	private final static int NUM_COLUMNS = 10;
	private static int count = 0;
	
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
	private File pdfFile;
	
	public ReportData(File pdfFile) {
		this.pdfFile = pdfFile;
		String nameReportTxt = "./reportsTxt/"+count+"_"+pdfFile.getName().substring(0, pdfFile.getName().length()-3)+"txt";
		count++;
		reportTxt = new File(nameReportTxt);
		this.modelDeviationOk = true; 
		this.reportOk = true; 
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
	}

	public void loadDataFromPdf() {
//		String patientName = getPatientNameFromPdf(imageFile);
//		String date = getDateFromPdf(imageFile);
//		String hour = getHourFromPdf(imageFile);
//		String duration = getDurationFromPdf(imageFile);
//		String faslseNegative = getFalseNegativeFromPdf(imageFile);
//		String faslsePositive = getFalsePositiveFromPdf(imageFile);
//		String fixationLosing = getFixationLosingFromPdf(imageFile);	
//		String birthday = getBirthdayFromPdf(imageFile);
//		String GHT = getGHTFromPdf(imageFile);
//		double VFI = getVFIFromPdf(imageFile);
//		double MD = getMDFromPdf(imageFile);
//		double PSD = getPSDFromPdf(imageFile);
		try {
			eyeSide = ConversorOCRHumphreyPdf.getEyeSideFromPdf(pdfFile);
			char[][] map = (eyeSide.charAt(0)=='E') ? Constants.MAP_LEFT : Constants.MAP_RIGHT;  
			age = ConversorOCRHumphreyPdf.getPatientAgeFromPdf(pdfFile);
			intensities = ConversorOCRHumphreyPdf.getIntensitiesMapFromPdf(pdfFile);
			totalDeviation = ConversorOCRHumphreyPdf.getNumericTotalDeviationMapFromPdf(pdfFile);
			totalDeviationPercent = ConversorOCRHumphreyPdf.getTotalDeviationMapFromPdf(pdfFile);
			modelDeviation = ConversorOCRHumphreyPdf.getNumericModelDeviationMapFromPdf(pdfFile, map);
			if(modelDeviation == null) {
				modelDeviationPercent = null;
				initializeDeviation();
			} else {
				modelDeviationPercent = ConversorOCRHumphreyPdf.getModelDeviationMapFromPdf(pdfFile);
			}
		} catch (Exception e) {
			System.out.println("Error loading " + pdfFile.getParentFile().getName());
			reportOk = false;
		}
	}
	
	private void initializeDeviation() {
		modelDeviation = new double[NUM_ROWS][NUM_COLUMNS];
		modelDeviationPercent = new double[NUM_ROWS][NUM_COLUMNS];
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				modelDeviation[i][j] = -1;
				modelDeviationPercent[i][j] = -1;
			}
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
			if(reportOk) {
				fw.write(""+(modelDeviationOk ? 'y':'n')); fw.write("\n");
				fw.write(""+age); fw.write("\n");
				fw.write(eyeSide); fw.write("\n");
				writeArray(fw, intensities);
				writeArray(fw, totalDeviation);
				writeArray(fw, totalDeviationPercent);
				writeArray(fw, modelDeviation);
				writeArray(fw, modelDeviationPercent);
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
		try {
			in = new BufferedReader(new FileReader(reportTxt));
			reportOk = in.readLine().charAt(0) == 'y' ? true : false;
			if(reportOk) {
				modelDeviationOk = in.readLine().charAt(0) == 'y' ? true : false;
				age = Integer.parseInt(in.readLine());
				eyeSide = in.readLine();
				readArray(in, intensities);
				readArray(in, totalDeviation);
				readArray(in, totalDeviationPercent);
				readArray(in, modelDeviation);
				readArray(in, totalDeviationPercent);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readArray(BufferedReader in, double[][] numeric) {
		String line[];
		numeric = new double[NUM_ROWS][NUM_COLUMNS];
		try {
			for (int i = 0; i < NUM_ROWS; i++) {
				line = in.readLine().split(" ");
				for (int j = 0; j < NUM_COLUMNS; j++) {
					numeric[i][j] = Double.parseDouble(line[j]);
				}
			}
		} catch (Exception e) {
			
		}
		
	}
	
	public boolean isModelDeviationOk() {
		return modelDeviationOk;
	}
	
	public boolean isReportOk() {
		return reportOk;
	}
	
}
