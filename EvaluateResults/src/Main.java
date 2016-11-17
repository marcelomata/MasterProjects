

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.ExaminationInformations;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV1_0;


public class Main {

	public static void main(String[] args) {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<VisualFieldFileLoaderV1_0> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		System.out.println(reportsHumphreyData.size());
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<VisualFieldFileLoaderV1_0>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		List<VisualFieldFileLoaderV1_0> leftReportPrototypeData;
		List<VisualFieldFileLoaderV1_0> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double mse;
		File evaluationFile;
		
		try {
			for (String patient : keysHumphrey) {
				evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
				evaluationFile = new File(evaluationName);
				if(!evaluationFile.exists()) {
					evaluationFile.createNewFile();
				}
				
				leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
				fw = new FileWriter(evaluationFile);
				if(leftReportHumphreyData.size() > 1) {
					mse = MeanSquareError.meanSquareEror(leftReportHumphreyData.get(0).getNumericIntensities(), leftReportHumphreyData.get(1).getNumericIntensities(), true);
					//mse humphrey left
					fw.write("MSE Humphrey Left = "+mse+"\n");
					System.out.println("MSE Humphrey Left = "+mse);
				}
				
				rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
				if(rightReportHumphreyData.size() > 1) {
					mse = MeanSquareError.meanSquareEror(rightReportHumphreyData.get(0).getNumericIntensities(), rightReportHumphreyData.get(1).getNumericIntensities(), true);
					fw.write("MSE Humphrey Right = "+mse+"\n");
					System.out.println("MSE Humphrey Right = "+mse);
				}
				fw.flush();
				fw.close();
			}
			
			for (String patient : keysPrototype) {
				evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
				evaluationFile = new File(evaluationName);
				if(!evaluationFile.exists()) {
					evaluationFile.createNewFile();
				}
				
				fw = new FileWriter(evaluationFile);
				
				leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
				if(leftReportPrototypeData.size() > 1) {
					mse = MeanSquareError.meanSquareEror(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), true);
					//mse prototype left
					fw.write("MSE Prototype Left = "+mse+"\n");
					System.out.println("MSE Prototype Left = "+mse);
				}
				
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1) {
					mse = MeanSquareError.meanSquareEror(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), true);
					fw.write("MSE Prototype Right = "+mse+"\n");
					System.out.println("MSE Prototype Right = "+mse);
				}
				fw.flush();
				fw.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private static List<VisualFieldFileLoaderV1_0> getReportPrototypeData(List<VisualFieldFileLoaderV1_0> list, EnumEye side) {
		List<VisualFieldFileLoaderV1_0> leftReportData = new ArrayList<VisualFieldFileLoaderV1_0>();
		for (VisualFieldFileLoaderV1_0 reportData : list) {
			if(reportData.getInformations().getCurrentEye() == side) {
				leftReportData.add(reportData);
			}
		}
		return leftReportData;
	}

	private static Map<String, List<VisualFieldFileLoaderV1_0>> getReportsPrototypeByPatient(List<VisualFieldFileLoaderV1_0> reportsPrototypeData) {
		Map<String, List<VisualFieldFileLoaderV1_0>> reportsByPatient = new HashMap<String, List<VisualFieldFileLoaderV1_0>>(); 
		
		String patient;
		List<VisualFieldFileLoaderV1_0> reports;
		for(VisualFieldFileLoaderV1_0 data : reportsPrototypeData) {
			patient = data.getInformations().getPatient().getName();
			reports = reportsByPatient.get(patient);
			if(reports == null) {
				reports = new ArrayList<VisualFieldFileLoaderV1_0>();
				reportsByPatient.put(patient, reports);
			}
			reports.add(data);
		}
		
		return reportsByPatient;
	}

	private static List<VisualFieldFileLoaderV1_0> loadReportsPrototype(File reportsPrototypeDir) {
		List<VisualFieldFileLoaderV1_0> reportsData = new ArrayList<VisualFieldFileLoaderV1_0>();
		
		File[] folder = reportsPrototypeDir.listFiles();
		File[] files;
		LoaderVisualField loader;
		
		ExaminationInformations informations;
		EnumEye eye;
		for (int i = 0; i < folder.length; i++) {
			files = folder[i].listFiles();
			for (int j = 0; j < files.length; j++) {
				if(getFileType(files[j]) == FileType.TXT) {
					informations = new ExaminationInformations();
					eye = files[j].getName().contains("Left") ? EnumEye.LEFT : EnumEye.RIGHT;
					informations.setCurrentEye(eye);
					loader = new VisualFieldFileLoaderV1_0(files[j].getName(), files[j].getParentFile().getAbsolutePath(), informations);
					loader.load();
					reportsData.add((VisualFieldFileLoaderV1_0) loader);
				}
			}
		}
		
		return reportsData;
	}

	private static List<ReportData> getReportHumphreyData(List<ReportData> reports, char side) {
		List<ReportData> leftReportData = new ArrayList<ReportData>();
		for (ReportData reportData : reports) {
			if(reportData.getEyeSide().charAt(0) == side) {
				leftReportData.add(reportData);
			}
		}
		return leftReportData;
	}

	private static Map<String, List<ReportData>> getReportsHumphreyByPatient(List<ReportData> reportsData) {
		
		Map<String, List<ReportData>> reportsByAge = new HashMap<String, List<ReportData>>(); 
		
		String patient;
		List<ReportData> reports;
		for(ReportData data : reportsData) {
			patient = data.getPatientName();
			reports = reportsByAge.get(patient);
			if(reports == null) {
				reports = new ArrayList<ReportData>();
				reportsByAge.put(patient, reports);
			}
			reports.add(data);
		}
		
		return reportsByAge;
	}

	private static List<ReportData> loadReportsHumphrey(File sampleDir) {
		List<ReportData> reportsData = new ArrayList<ReportData>();
		
		File[] files = sampleDir.listFiles();
		ReportData data;
		for (int i = 0; i < files.length; i++) {
			if(getFileType(files[i]) == FileType.TXT) {
				data = new ReportData(null, files[i]);
				data.loadDataFromTxt();
				if(data.isReportOk()) {
					reportsData.add(data);
				} else {
					System.out.println("Report "+files[i].getName()+" is not ok.");
				}
				if(!data.isModelDeviationOk()) {
					System.out.println("Model deviation of report "+files[i].getName()+" is not ok.");
				}
			}
		}
		
		return reportsData;
	}
	
	private static FileType getFileType(File file) {
		String name = file.getName();
		String type = name.substring(name.length()-3);
		
		if(type.equalsIgnoreCase(FileType.PDF.toString())) {
			return FileType.PDF;
		} else if(type.equalsIgnoreCase(FileType.XML.toString())) {
			return FileType.XML;
		} else {
			return FileType.TXT;
		}
	}

}
