
package evaluation2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
		//calcREMQ();
		//calcDM();
//		calcMDP(); //Check about the error in Prototype calculus
//		calcEV();
		calcMDPC();
	}
	
	public static void calcREMQ() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		
		try {
			processHumphreyRMSE(evaluationDir, reportsHumphreyByPatient, keysHumphrey);
			processPrototypeRSME(evaluationDir, reportsPrototypeByPatient, keysPrototype);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private static void processHumphreyRMSE(File evaluationDir,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		FileWriter fw;
		String evaluationName;
		double mse;
		File evaluationFile;
		for (String patient : keysHumphrey) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",");
			if(leftReportHumphreyData.size() > 1) {
				mse = StatisticsComparation.rootMeanSquareEror(leftReportHumphreyData.get(0).getNumericIntensities(), leftReportHumphreyData.get(1).getNumericIntensities(), true);
				fw.write("RMSE Humphrey Left = "+mse+"\n");
				System.out.print(mse+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				mse = StatisticsComparation.rootMeanSquareEror(rightReportHumphreyData.get(0).getNumericIntensities(), rightReportHumphreyData.get(1).getNumericIntensities(), true);
				fw.write("RMSE Humphrey Right = "+mse+"\n");
				System.out.println(mse+",");
			}
			fw.flush();
			fw.close();
		}
	}

	private static void processPrototypeRSME(
			File evaluationDir,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double mse;
		File evaluationFile;
		for (String patient : keysPrototype) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				System.out.print(patient+",");
				mse = StatisticsComparation.rootMeanSquareEror(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), true);
				fw.write("RMSE Prototype Left = "+mse+"\n");
				System.out.print(mse+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(patient.equalsIgnoreCase("Mathias")) {
				LoadMathias.calcReports();
			} else {
				if(rightReportPrototypeData.size() > 1) {
					mse = StatisticsComparation.rootMeanSquareEror(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), true);
					fw.write("RMSE Prototype Right = "+mse+"\n");
					System.out.println(mse+",");
				}
			}
			fw.flush();
			fw.close();
		}
	}
	
	public static void calcDM() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		
		try {
			processHumphreyDM(evaluationDir, reportsHumphreyByPatient, keysHumphrey);
			processPrototypeDM(evaluationDir, means, variances, reportsPrototypeByPatient, keysPrototype);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private static void processHumphreyDM(File evaluationDir,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		FileWriter fw;
		String evaluationName;
		double dm;
		File evaluationFile;
		for (String patient : keysHumphrey) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",");
			if(leftReportHumphreyData.size() > 1) {
				dm = leftReportHumphreyData.get(0).getMD();
				fw.write("DM Humphrey Left = "+dm+"\n");
				System.out.print(dm+",");
				dm = leftReportHumphreyData.get(1).getMD();
				fw.write("DM Humphrey Left = "+dm+"\n");
				System.out.print(dm+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				dm = rightReportHumphreyData.get(0).getMD();
				fw.write("DM Humphrey Right = "+dm+"\n");
				System.out.print(dm+",");
				dm = rightReportHumphreyData.get(1).getMD();
				fw.write("DM Humphrey Right = "+dm+"\n");
				System.out.println(dm+",");
			}
			fw.flush();
			fw.close();
		}
	}

	private static void processPrototypeDM(
			File evaluationDir,
			double[][] means,
			double[][] variances,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double dm;
		File evaluationFile;
		for (String patient : keysPrototype) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				System.out.print(patient+",");
				dm = StatisticsComparation.DM(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), variances, means, true);
				fw.write("DM Prototype Left = "+dm+"\n");
				System.out.print(dm+",");
				dm = StatisticsComparation.DM(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances, means, true);
				fw.write("DM Prototype Left = "+dm+"\n");
				System.out.print(dm+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				dm = StatisticsComparation.DM(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), variances, means, false);
				fw.write("DM Prototype Right = "+dm+"\n");
				System.out.print(dm+",");
				dm = StatisticsComparation.DM(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances, means, false);
				fw.write("DM Prototype Right = "+dm+"\n");
				System.out.println(dm+",");
			} else {
				dm = StatisticsComparation.DM(LoadMathias.intensitiesMathiasRight1, variances, means, false);
				fw.write("DM Prototype Right = "+dm+"\n");
				System.out.print(dm+",");
				dm = StatisticsComparation.DM(LoadMathias.intensitiesMathiasRight2, variances, means, false);
				fw.write("DM Prototype Right = "+dm+"\n");
				System.out.println(dm+",");
			}
			
			fw.flush();
			fw.close();
		}
	}

	public static void calcMDP() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
//		File reportsPrototypeDir = new File("./reports_patients_prototype_txt_2/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		
		try {
			processHumphreyMDP(evaluationDir, reportsHumphreyByPatient, keysHumphrey);
			processPrototypeMDP(evaluationDir, means, variances, reportsPrototypeByPatient, keysPrototype);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private static void processHumphreyMDP(File evaluationDir,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		FileWriter fw;
		String evaluationName;
		double mdp;
		File evaluationFile;
		for (String patient : keysHumphrey) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",");
			if(leftReportHumphreyData.size() > 1) {
				mdp = leftReportHumphreyData.get(0).getPSD();
				//mse humphrey left
				fw.write("MDP Humphrey Left = "+mdp+"\n");
				//System.out.println("RMSE Humphrey Left = "+mse);
				System.out.print(mdp+",");
				mdp = leftReportHumphreyData.get(1).getPSD();
				fw.write("MDP Humphrey Left = "+mdp+"\n");
				System.out.print(mdp+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				mdp = rightReportHumphreyData.get(0).getPSD();
				fw.write("MDP Humphrey Right = "+mdp+"\n");
				//System.out.println("RMSE Humphrey Right = "+mse);
				System.out.print(mdp+",");
				mdp = rightReportHumphreyData.get(1).getPSD();
				fw.write("MDP Humphrey Right = "+mdp+"\n");
				System.out.println(mdp+",");
			}
			fw.flush();
			fw.close();
		}
	}

	private static void processPrototypeMDP(
			File evaluationDir,
			double[][] means,
			double[][] variances,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double mdp;
		File evaluationFile;
		for (String patient : keysPrototype) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				System.out.print(patient+",");
				mdp = StatisticsComparation.MDP(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), variances, means, true);
				Math.sqrt(mdp);
				fw.write("MDP Prototype Left = "+mdp+"\n");
				System.out.print(mdp+",");
				mdp = StatisticsComparation.MDP(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances, means, true);
				fw.write("MDP Prototype Left = "+mdp+"\n");
				System.out.print(mdp+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				mdp = StatisticsComparation.MDP(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), variances, means, true);
				mdp = Math.sqrt(mdp);
				fw.write("MDP Prototype Right = "+mdp+"\n");
				System.out.print(mdp+",");
				mdp = StatisticsComparation.MDP(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances, means, true);
				mdp = Math.sqrt(mdp);
				fw.write("MDP Prototype Right = "+mdp+"\n");
				System.out.println(mdp+",");
			} else {
				mdp = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight1, variances, means, false);
				mdp = Math.sqrt(mdp);
				fw.write("MDP Prototype Right = "+mdp+"\n");
				System.out.print(mdp+",");
				mdp = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight2, variances, means, false);
				mdp = Math.sqrt(mdp);
				fw.write("MDP Prototype Right = "+mdp+"\n");
				System.out.println(mdp+",");
			}
			fw.flush();
			fw.close();
		}
	}
	
	public static void calcEV() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances2 = StatisticsComparation.getVariances2(reportsPrototypeData, means);
		double[][] meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		double[][] variances2Humphrey = StatisticsComparation.getVariances2Humphrey(reportsHumphreyData, meansHumphrey);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		
		//printArray(means, "Means Prototype");
		//printArray(meansHumphrey, "Means Humphrey");
		//printArray(variances2, "Variances Prototype");
		//printArray(variances2Humphrey, "Variances Humphrey");
		
		try {
			processHumphreyEV(evaluationDir, variances2Humphrey, reportsHumphreyByPatient, keysHumphrey);
			processPrototypeEV(evaluationDir, variances2, reportsPrototypeByPatient, keysPrototype);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private static void printArray(double[][] variances2, String string) {
		System.out.println(string);
		for (int i = 0; i < variances2.length; i++) {
			for (int j = 0; j < variances2[i].length; j++) {
				System.out.print(variances2[i][j]+",");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}

	private static void processHumphreyEV(File evaluationDir,
			double[][] variances2Humphrey,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		FileWriter fw;
		String evaluationName;
		double ev;
		File evaluationFile;
		for (String patient : keysHumphrey) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",");
			if(leftReportHumphreyData.size() > 1) {
				ev = StatisticsComparation.EV(leftReportHumphreyData.get(0).getNumericIntensities(), leftReportHumphreyData.get(1).getNumericIntensities(), variances2Humphrey, true);
				ev = Math.sqrt(ev);
				fw.write("EV Humphrey Left = "+ev+"\n");
				System.out.print(ev+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				ev = StatisticsComparation.EV(rightReportHumphreyData.get(0).getNumericIntensities(), rightReportHumphreyData.get(1).getNumericIntensities(), variances2Humphrey, true);
				ev = Math.sqrt(ev);
				fw.write("EV Humphrey Right = "+ev+"\n");
				System.out.println(ev+",");
			}
			fw.flush();
			fw.close();
		}
	}

	private static void processPrototypeEV(
			File evaluationDir,
			double[][] variances2,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double ev;
		File evaluationFile;
		for (String patient : keysPrototype) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				System.out.print(patient+",");
				ev = StatisticsComparation.EV(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances2, true);
				ev = Math.sqrt(ev);
				fw.write("EV Prototype Left = "+ev+"\n");
				System.out.print(ev+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				ev = StatisticsComparation.EV(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances2, false);
				ev = Math.sqrt(ev);
				fw.write("EV Prototype Right = "+ev+"\n");
				System.out.println(ev+",");					
			} else {
				ev = StatisticsComparation.EV(LoadMathias.intensitiesMathiasRight1, LoadMathias.intensitiesMathiasRight2, variances2, false);
				ev = Math.sqrt(ev);
				System.out.println(ev+",");
			}
			fw.flush();
			fw.close();
		}
	}
	
	public static void calcMDPC() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		double[][] variances2 = StatisticsComparation.getVariances2(reportsPrototypeData, means);
		double[][] meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		double[][] variances2Humphrey = StatisticsComparation.getVariances2Humphrey(reportsHumphreyData, meansHumphrey);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		double k = 1.1;
		
		try {
			processHumphreyMDPC(evaluationDir, variances2Humphrey, reportsHumphreyByPatient, keysHumphrey, k);
			processPrototypeMDPC(evaluationDir, means, variances, variances2, reportsPrototypeByPatient, keysPrototype, k);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private static void processHumphreyMDPC(File evaluationDir,
			double[][] variances2Humphrey,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey, double k) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		FileWriter fw;
		String evaluationName;
		double ev;
		double mdp1;
		double mdp2;
		double mdpc;
		File evaluationFile;
		for (String patient : keysHumphrey) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",");
			if(leftReportHumphreyData.size() > 1) {
				ev = StatisticsComparation.EV(leftReportHumphreyData.get(0).getNumericIntensities(), leftReportHumphreyData.get(1).getNumericIntensities(), variances2Humphrey, true);
				mdp1 = leftReportHumphreyData.get(0).getPSD();
				mdp2 = leftReportHumphreyData.get(1).getPSD();
				mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Humphrey Left = "+mdpc+"\n");
				System.out.print(mdpc+",");
				
				mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Humphrey Left = "+mdpc+"\n");
				System.out.print(mdpc+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				ev = StatisticsComparation.EV(rightReportHumphreyData.get(0).getNumericIntensities(), rightReportHumphreyData.get(1).getNumericIntensities(), variances2Humphrey, true);
				mdp1 = rightReportHumphreyData.get(0).getPSD();
				mdp2 = rightReportHumphreyData.get(1).getPSD();
				mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Humphrey Right = "+mdpc+"\n");
				System.out.print(mdpc+",");
				
				mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Humphrey Right = "+mdpc+"\n");
				System.out.println(mdpc+",");
			}
			fw.flush();
			fw.close();
		}
	}

	private static void processPrototypeMDPC(
			File evaluationDir,
			double[][] means,
			double[][] variances,
			double[][] variances2,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype, double k) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double ev;
		double mdp1;
		double mdp2;
		double mdpc;
		File evaluationFile;
		for (String patient : keysPrototype) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				System.out.print(patient+",");
				ev = StatisticsComparation.EV(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances2, true);
				ev = Math.sqrt(ev);
				mdp1 = StatisticsComparation.MDP(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), variances, means, true);
				mdp1 = Math.sqrt(mdp1);
				mdp2 = StatisticsComparation.MDP(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances, means, true);
				mdp2 = Math.sqrt(mdp2);
				mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Prototype Left = "+mdpc+"\n");
				System.out.print(mdpc+",");
				
				mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Prototype Left = "+mdpc+"\n");
				System.out.print(mdpc+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				ev = StatisticsComparation.EV(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances2, true);
				ev = Math.sqrt(ev);
				mdp1 = StatisticsComparation.MDP(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), variances, means, false);
				mdp1 = Math.sqrt(mdp1);
				mdp2 = StatisticsComparation.MDP(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), variances, means, false);
				mdp2 = Math.sqrt(mdp2);
				mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Prototype Right = "+ev+"\n");
				System.out.print(mdpc+",");
				
				mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Prototype Right = "+mdpc+"\n");
				System.out.println(mdpc+",");
			} else {
				ev = StatisticsComparation.EV(LoadMathias.intensitiesMathiasRight1, LoadMathias.intensitiesMathiasRight2, variances2, false);
				ev = Math.sqrt(ev);
				mdp1 = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight1, variances, means, false);
				mdp1 = Math.sqrt(mdp1);
				mdp2 = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight2, variances, means, false);
				mdp2 = Math.sqrt(mdp2);
				mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Prototype Right = "+ev+"\n");
				System.out.println(mdpc+",");
				
				mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
				if(mdpc < 0) {
					mdpc = 0;
				}
				fw.write("MDPC Prototype Right = "+mdpc+"\n");
				System.out.println(mdpc+",");
			}
			fw.flush();
			fw.close();
		}
	}
	
	private static List<LoaderVisualField> getReportPrototypeData(List<LoaderVisualField> list, EnumEye side) {
		List<LoaderVisualField> leftReportData = new ArrayList<LoaderVisualField>();
		for (LoaderVisualField reportData : list) {
			if(reportData.getInformations().getCurrentEye() == side) {
				leftReportData.add(reportData);
			}
		}
		return leftReportData;
	}

	private static Map<String, List<LoaderVisualField>> getReportsPrototypeByPatient(List<LoaderVisualField> reportsPrototypeData) {
		Map<String, List<LoaderVisualField>> reportsByPatient = new HashMap<String, List<LoaderVisualField>>(); 
		
		String patient;
		List<LoaderVisualField> reports;
		for(LoaderVisualField data : reportsPrototypeData) {
			patient = data.getInformations().getPatient().getName();
			reports = reportsByPatient.get(patient);
			if(reports == null) {
				reports = new ArrayList<LoaderVisualField>();
				reportsByPatient.put(patient, reports);
			}
			reports.add(data);
		}
		
		return reportsByPatient;
	}

	private static List<LoaderVisualField> loadReportsPrototype(File reportsPrototypeDir) {
		List<LoaderVisualField> reportsData = new ArrayList<LoaderVisualField>();
		
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
					if(loader.getInformations().getPatient().getName().trim().equalsIgnoreCase("Default")) {
						loader.getInformations().getPatient().setName(folder[i].getName());
					}
					reportsData.add((LoaderVisualField) loader);
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
