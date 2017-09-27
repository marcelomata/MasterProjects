
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
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderMap;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV1_0;


public class MainOrganizing {

	
	public static void main(String[] args) {
//		calcREMQ();
		//calcDM();
//		calcMDP(); //Check about the error
//		calcEV();
//		calcMDPC();
		calcR();
	}
	
	private static void calcR() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		
		double r_sqr_left = 0;
		double r_sqr_right = 0;
		double sq_total_humphrey[];
		double sq_total_prototype[];
		double sq_total_res[] = new double[] {0, 0};
		
		try {
			sq_total_humphrey = processHumphreyR(evaluationDir, meansHumphrey, reportsHumphreyByPatient, keysHumphrey);
			sq_total_prototype = processPrototypeR(evaluationDir, means, reportsPrototypeByPatient, keysPrototype);
			sq_total_res = processSumRes(evaluationDir,reportsPrototypeByPatient, reportsHumphreyByPatient, keysPrototype, keysHumphrey);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void calcREMQ() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
//		System.out.println(reportsHumphreyData.size());
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
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
				System.out.print(patient+",");
				if(leftReportHumphreyData.size() > 1) {
					mse = StatisticsComparation.rootMeanSquareEror(leftReportHumphreyData.get(0).getNumericIntensities(), leftReportHumphreyData.get(1).getNumericIntensities(), true);
					//mse humphrey left
					fw.write("RMSE Humphrey Left = "+mse+"\n");
					//System.out.println("RMSE Humphrey Left = "+mse);
					System.out.print(mse+",");
				}
				
				rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
				if(rightReportHumphreyData.size() > 1) {
					mse = StatisticsComparation.rootMeanSquareEror(rightReportHumphreyData.get(0).getNumericIntensities(), rightReportHumphreyData.get(1).getNumericIntensities(), true);
					fw.write("RMSE Humphrey Right = "+mse+"\n");
					//System.out.println("RMSE Humphrey Right = "+mse);
					System.out.println(mse+",");
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
					System.out.print(patient+",");
					double intensities1[][];
					if(leftReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(leftReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					mse = StatisticsComparation.rootMeanSquareEror(intensities1, intensities2, true);
					//mse prototype left
					fw.write("RMSE Prototype Left = "+mse+"\n");
					//System.out.println("RMSE Prototype Left = "+mse);
					System.out.print(mse+",");
				}
				
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(patient.equalsIgnoreCase("Mathias")) {
					LoadMathias.calcReports();
				} else {
					if(rightReportPrototypeData.size() > 1) {
						double intensities1[][];
						if(rightReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
							intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
						} else {
							intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
						}
						double intensities2[][];
						if(rightReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
							intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
						} else {
							intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
						}
						mse = StatisticsComparation.rootMeanSquareEror(intensities1, intensities2, true);
						fw.write("RMSE Prototype Right = "+mse+"\n");
						//System.out.println("RMSE Prototype Right = "+mse);
						System.out.println(mse+",");
					}
				}
				fw.flush();
				fw.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public static void calcDM() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
//		System.out.println(reportsHumphreyData.size());
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double dm;
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
				System.out.print(patient+",");
				if(leftReportHumphreyData.size() > 1) {
					dm = leftReportHumphreyData.get(0).getMD();
					//mse humphrey left
					fw.write("DM Humphrey Left = "+dm+"\n");
					//System.out.println("RMSE Humphrey Left = "+mse);
					System.out.print(dm+",");
					dm = leftReportHumphreyData.get(1).getMD();
					fw.write("DM Humphrey Left = "+dm+"\n");
					System.out.print(dm+",");
				}
				
				rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
				if(rightReportHumphreyData.size() > 1) {
					dm = rightReportHumphreyData.get(0).getMD();
					fw.write("DM Humphrey Right = "+dm+"\n");
					//System.out.println("RMSE Humphrey Right = "+mse);
					System.out.print(dm+",");
					dm = rightReportHumphreyData.get(1).getMD();
					fw.write("DM Humphrey Right = "+dm+"\n");
					System.out.println(dm+",");
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
					System.out.print(patient+",");
					double intensities1[][];
					if(leftReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(leftReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					dm = StatisticsComparation.DM(intensities1, variances, means, true);
					//mse prototype left
					fw.write("DM Prototype Left = "+dm+"\n");
					//System.out.println("RMSE Prototype Left = "+mse);
					System.out.print(dm+",");
					dm = StatisticsComparation.DM(intensities2, variances, means, true);
					fw.write("DM Prototype Left = "+dm+"\n");
					System.out.print(dm+",");
				}
				
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1) {
					double intensities1[][];
					if(rightReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(rightReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					dm = StatisticsComparation.DM(intensities1, variances, means, false);
					fw.write("DM Prototype Right = "+dm+"\n");
					//System.out.println("RMSE Prototype Right = "+mse);
					System.out.print(dm+",");
					dm = StatisticsComparation.DM(intensities2, variances, means, false);
					fw.write("DM Prototype Right = "+dm+"\n");
					System.out.println(dm+",");
				} else {
					dm = StatisticsComparation.DM(LoadMathias.intensitiesMathiasRight1, variances, means, false);
					fw.write("DM Prototype Right = "+dm+"\n");
					System.out.print(dm+",");
					dm = StatisticsComparation.DM(LoadMathias.intensitiesMathiasRight2, variances, means, false);
					fw.write("DM Prototype Right = "+dm+"\n");
					System.out.print(dm+",");
				}
				fw.flush();
				fw.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void calcMDP() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
//		System.out.println(reportsHumphreyData.size());
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double mdp;
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
					double intensities1[][];
					if(leftReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(leftReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					mdp = StatisticsComparation.MDP(intensities1, variances, means, true);
					mdp = Math.sqrt(mdp);
					//mse prototype left
					fw.write("MDP Prototype Left = "+mdp+"\n");
					//System.out.println("RMSE Prototype Left = "+mse);
					System.out.print(mdp+",");
					mdp = StatisticsComparation.MDP(intensities2, variances, means, true);
					mdp = Math.sqrt(mdp);
					fw.write("MDP Prototype Left = "+mdp+"\n");
					System.out.print(mdp+",");
				}
				
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1) {
					double intensities1[][];
					if(rightReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(rightReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					mdp = StatisticsComparation.MDP(intensities1, variances, means, true);
					mdp = Math.sqrt(mdp);
					fw.write("MDP Prototype Right = "+mdp+"\n");
					//System.out.println("RMSE Prototype Right = "+mse);
					System.out.print(mdp+",");
					mdp = StatisticsComparation.MDP(intensities2, variances, means, true);
					mdp = Math.sqrt(mdp);
					fw.write("DM Prototype Right = "+mdp+"\n");
					System.out.println(mdp+",");
				} else {
					mdp = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight1, variances, means, false);
					mdp = Math.sqrt(mdp);
					fw.write("DM Prototype Right = "+mdp+"\n");
					System.out.print(mdp+",");
					mdp = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight2, variances, means, false);
					mdp = Math.sqrt(mdp);
					fw.write("DM Prototype Right = "+mdp+"\n");
					System.out.print(mdp+",");
				}
				fw.flush();
				fw.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public static void calcEV() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
//		System.out.println(reportsHumphreyData.size());
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances2 = StatisticsComparation.getVariances2(reportsPrototypeData, means);
		double[][] meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		double[][] variances2Humphrey = StatisticsComparation.getVariances2Humphrey(reportsHumphreyData, meansHumphrey);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double ev;
		File evaluationFile;
		
		try {
			
			String patientName = "Mathias";
			for (String patient : keysHumphrey) {
				evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
				evaluationFile = new File(evaluationName);
				if(!evaluationFile.exists()) {
					evaluationFile.createNewFile();
				}
				
				leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
				fw = new FileWriter(evaluationFile);
				
//				if(patient.toUpperCase().contains(patientName.toUpperCase())) {
				
					System.out.print(patient+",");
					if(leftReportHumphreyData.size() > 1) {
						double intensities1[][];
						intensities1 = leftReportHumphreyData.get(0).getNumericIntensities();
						double intensities2[][];
						intensities2 = leftReportHumphreyData.get(1).getNumericIntensities();
//						if(patient.toUpperCase().contains(patientName.toUpperCase())) {
//							System.out.println();
//							printField(variances2Humphrey);
//							System.out.println();
//							printField(intensities1);
//							System.out.println();
//							printField(intensities2);
//							System.out.println();
//							printFieldDifferences(intensities1, intensities2);
//							System.out.println();
//						}
						ev = StatisticsComparation.EV(intensities1, intensities2, variances2Humphrey, true);
						ev = Math.sqrt(ev);
						//mse humphrey left
						fw.write("EV Humphrey Left = "+ev+"\n");
						//System.out.println("RMSE Humphrey Left = "+mse);
						System.out.print(ev+",");
					}
					
					rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
					if(rightReportHumphreyData.size() > 1) {
						double intensities1[][];
						intensities1 = rightReportHumphreyData.get(0).getNumericIntensities();
						double intensities2[][];
						intensities2 = rightReportHumphreyData.get(1).getNumericIntensities();
//						if(patient.toUpperCase().contains(patientName.toUpperCase())) {
//							System.out.println();
//							printField(variances2Humphrey);
//							System.out.println();
//							printField(intensities1);
//							System.out.println();
//							printField(intensities2);
//							System.out.println();
//							printFieldDifferences(intensities1, intensities2);
//							System.out.println();
//						}
						ev = StatisticsComparation.EV(intensities1, intensities2, variances2Humphrey, true);
						ev = Math.sqrt(ev);
						fw.write("EV Humphrey Right = "+ev+"\n");
						//System.out.println("RMSE Humphrey Right = "+mse);
						System.out.println(ev+",");
					}
//				}
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
				
//				if(patient.equalsIgnoreCase(patientName)) {
				
					leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
					
					if(leftReportPrototypeData.size() > 1) {
//						System.out.println();
						System.out.print(patient+",");
						
						double intensities1[][];
						if(leftReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
							intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
						} else {
							intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
						}
						double intensities2[][];
						if(leftReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
							intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
						} else {
							intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
						}
//						if(patient.equalsIgnoreCase(patientName)) {
//							System.out.println();
//							printField(variances2);
//							System.out.println();
//							printField(intensities1);
//							System.out.println();
//							printField(intensities2);
//							System.out.println();
//							printFieldDifferences(intensities1, intensities2);
//							System.out.println();
//						}
						ev = StatisticsComparation.EV(intensities1, intensities2, variances2, true);
//						System.out.println(ev);
						ev = Math.sqrt(ev);
						//mse prototype left
						fw.write("EV Prototype Left = "+ev+"\n");
						//System.out.println("RMSE Prototype Left = "+mse);
						System.out.print(ev+",");
					}
					
					rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
					if(rightReportPrototypeData.size() > 1) {
						double intensities1[][];
						if(rightReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
							intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
						} else {
							intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
						}
						double intensities2[][];
						if(rightReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
							intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
						} else {
							intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
						}
//						if(patient.equalsIgnoreCase(patientName)) {
//							System.out.println();
//							printField(variances2);
//							System.out.println();
//							printField(intensities1);
//							System.out.println();
//							printField(intensities2);
//							System.out.println();
//							printFieldDifferences(intensities1, intensities2);
//							System.out.println();
//						}
						ev = StatisticsComparation.EV(intensities1, intensities2, variances2, false);
//						System.out.println(ev);
						ev = Math.sqrt(ev);
						fw.write("EV Prototype Right = "+ev+"\n");
						//System.out.println("RMSE Prototype Right = "+mse);
						System.out.println(ev+",");					
					} else {
						double intensities1[][];
						intensities1 = LoadMathias.intensitiesMathiasRight1;
						double intensities2[][];
						intensities2 = LoadMathias.intensitiesMathiasRight2;
						
//						if(patient.equalsIgnoreCase(patientName)) {
//							System.out.println();
//							printField(variances2);
//							System.out.println();
//							printField(intensities1);
//							System.out.println();
//							printField(intensities2);
//							System.out.println();
//							printFieldDifferences(intensities1, intensities2);
//							System.out.println();
//						}
						ev = StatisticsComparation.EV(intensities1, intensities2, variances2, false);
						ev = Math.sqrt(ev);
						System.out.println(ev+",");
					}
//				}
				fw.flush();
				fw.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private static void printFieldDifferences(double[][] intensities1, double[][] intensities2) {
		int sum = 0;
		int value = 0;
		for (int i = 0; i < intensities1.length; i++) {
			for (int j = 0; j < intensities1[i].length; j++) {
				value = (int) (intensities1[i][j] - intensities2[i][j]);
				sum += (value * value);
				System.out.print(Math.abs(value) + " ");
			}
			System.out.println();
		}
		System.out.println(sum);
		
	}

	private static void printField(double[][] intensitiesAsDouble) {
		for (int i = 0; i < intensitiesAsDouble.length; i++) {
			for (int j = 0; j < intensitiesAsDouble[i].length; j++) {
				System.out.print(intensitiesAsDouble[i][j]+" ");
			}
			System.out.println();
		}
	}

	public static void calcMDPC() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = loadReportsPrototype(reportsPrototypeDir);
//		System.out.println(reportsHumphreyData.size());
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		double[][] variances2 = StatisticsComparation.getVariances2(reportsPrototypeData, means);
		double[][] meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		double[][] variances2Humphrey = StatisticsComparation.getVariances2Humphrey(reportsHumphreyData, meansHumphrey);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = getReportsPrototypeByPatient(reportsPrototypeData);
		
		Set<String> keysHumphrey = reportsHumphreyByPatient.keySet();
		Set<String> keysPrototype = reportsPrototypeByPatient.keySet();
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double ev;
		double mdp1;
		double mdp2;
		double mdpc;
		double k = 2;
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
				System.out.print(patient+",");
				if(leftReportHumphreyData.size() > 1) {
					ev = StatisticsComparation.EV(leftReportHumphreyData.get(0).getNumericIntensities(), leftReportHumphreyData.get(1).getNumericIntensities(), variances2Humphrey, true);
					//ev = Math.sqrt(ev);
					mdp1 = leftReportHumphreyData.get(0).getPSD();
					mdp2 = leftReportHumphreyData.get(1).getPSD();
					mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
//					mdpc = StatisticsComparation.MDPC(Math.sqrt(mdp1), Math.sqrt(ev), k);
					mdpc = Math.sqrt(mdpc);
					//mse humphrey left
					fw.write("MDPC Humphrey Left = "+mdpc+"\n");
					//System.out.println("RMSE Humphrey Left = "+mse);
					System.out.print(mdpc+",");
					
					mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
//					mdpc = StatisticsComparation.MDPC(Math.sqrt(mdp2), Math.sqrt(ev), k);
					mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Humphrey Left = "+mdpc+"\n");
					System.out.print(mdpc+",");
				}
				
				rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
				if(rightReportHumphreyData.size() > 1) {
					ev = StatisticsComparation.EV(rightReportHumphreyData.get(0).getNumericIntensities(), rightReportHumphreyData.get(1).getNumericIntensities(), variances2Humphrey, true);
//					ev = Math.sqrt(ev);
					mdp1 = rightReportHumphreyData.get(0).getPSD();
					mdp2 = rightReportHumphreyData.get(1).getPSD();
					mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
//					mdpc = StatisticsComparation.MDPC(Math.sqrt(mdp1), Math.sqrt(ev), k);
					mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Humphrey Right = "+mdpc+"\n");
					//System.out.println("RMSE Humphrey Right = "+mse);
					System.out.print(mdpc+",");
					
					mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
//					mdpc = StatisticsComparation.MDPC(Math.sqrt(mdp2), Math.sqrt(ev), k);
					mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Humphrey Right = "+mdpc+"\n");
					System.out.println(mdpc+",");
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
					System.out.print(patient+",");
					double intensities1[][];
					if(leftReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(leftReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					ev = StatisticsComparation.EV(intensities1, intensities2, variances2, true);
//					ev = Math.sqrt(ev);
					mdp1 = StatisticsComparation.MDP(intensities1, variances, means, true);
//					mdp1 = Math.sqrt(mdp1);
					mdp2 = StatisticsComparation.MDP(intensities2, variances, means, true);
//					mdp2 = Math.sqrt(mdp2);
					mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
					mdpc = Math.sqrt(mdpc);
					//mse prototype left
					fw.write("MDPC Prototype Left = "+mdpc+"\n");
					//System.out.println("RMSE Prototype Left = "+mse);
					System.out.print(mdpc+",");
					
					mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
					mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Prototype Left = "+mdpc+"\n");
					System.out.print(mdpc+",");
				}
				
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1) {
					double intensities1[][];
					if(rightReportPrototypeData.get(0) instanceof VisualFieldFileLoaderV1_0) {
						intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					} else {
						intensities1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
					}
					double intensities2[][];
					if(rightReportPrototypeData.get(1) instanceof VisualFieldFileLoaderV1_0) {
						intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					} else {
						intensities2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
					}
					ev = StatisticsComparation.EV(intensities1, intensities2, variances2, true);
//					ev = Math.sqrt(ev);
					mdp1 = StatisticsComparation.MDP(intensities1, variances, means, false);
//					mdp1 = Math.sqrt(mdp1);
					mdp2 = StatisticsComparation.MDP(intensities2, variances, means, false);
//					mdp2 = Math.sqrt(mdp2);
					mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
					mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Prototype Right = "+ev+"\n");
					//System.out.println("RMSE Prototype Right = "+mse);
					System.out.print(mdpc+",");
					
					mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
					//mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Prototype Right = "+mdpc+"\n");
					System.out.println(mdpc+",");
				} else {
					ev = StatisticsComparation.EV(LoadMathias.intensitiesMathiasRight1, LoadMathias.intensitiesMathiasRight2, variances2, false);
					mdp1 = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight1, variances, means, false);
//					mdp1 = Math.sqrt(mdp1);
					mdp2 = StatisticsComparation.MDP(LoadMathias.intensitiesMathiasRight2, variances, means, false);
//					mdp2 = Math.sqrt(mdp2);
					mdpc = StatisticsComparation.MDPC(mdp1, ev, k);
					//mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Prototype Right = "+mdpc+"\n");
					System.out.println(mdpc+",");
					
					mdpc = StatisticsComparation.MDPC(mdp2, ev, k);
					//mdpc = Math.sqrt(mdpc);
					fw.write("MDPC Prototype Right = "+mdpc+"\n");
					System.out.println(mdpc+",");
				}
				fw.flush();
				fw.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
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
		//List<VisualFieldFileLoaderV1_0> reportsData = new ArrayList<VisualFieldFileLoaderV1_0>();
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
					informations.getPatient().setName(folder[i].getName());
					if(files[j].getName().contains("xls")) {
						loader = new VisualFieldFileLoaderMap(files[j].getName(), files[j].getParentFile().getAbsolutePath(), informations);
						loader.load();
					} else {
						loader = new VisualFieldFileLoaderV1_0(files[j].getName(), files[j].getParentFile().getAbsolutePath(), informations);
						loader.load();
						if(loader.getInformations().getPatient().getName().trim().equalsIgnoreCase("Default")) {
							loader.getInformations().getPatient().setName(folder[i].getName());
						}
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
	
	//TODO
	private static double[] processHumphreyR(File evaluationDir,
			double[][] means,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		FileWriter fw;
		String evaluationName;
		double sqtotal_humphrey_test1;
		double sqtotal_humphrey_test2;
		double []sqtotal_humphrey = {0, 0};
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
				System.out.print(patient+",");
				sqtotal_humphrey_test1 = StatisticsComparation.SQ_Total(leftReportHumphreyData.get(0).getNumericIntensities(), means, true);
				sqtotal_humphrey_test2 = StatisticsComparation.SQ_Total(leftReportHumphreyData.get(1).getNumericIntensities(), means, true);
				sqtotal_humphrey[0] = sqtotal_humphrey_test1 + sqtotal_humphrey_test2;
				fw.write("SQ_total Humphrey Left = "+sqtotal_humphrey+"\n");
				System.out.print(sqtotal_humphrey+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				System.out.print(patient+",");
				sqtotal_humphrey_test1 = StatisticsComparation.SQ_Total(rightReportHumphreyData.get(0).getNumericIntensities(), means, false);
				sqtotal_humphrey_test2 = StatisticsComparation.SQ_Total(rightReportHumphreyData.get(1).getNumericIntensities(), means, false);
				sqtotal_humphrey[1] = sqtotal_humphrey_test1 + sqtotal_humphrey_test2;
				fw.write("SQ_total Humphrey Right = "+sqtotal_humphrey+"\n");
				System.out.print(sqtotal_humphrey+",");
			}
			fw.flush();
			fw.close();
			return sqtotal_humphrey;
		}
		return new double[] {0, 0};
	}

	//TODO
	private static double[] processPrototypeR(
			File evaluationDir,
			double[][] means,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		FileWriter fw;
		String evaluationName;
		double sqtotal_prototype_test1;
		double sqtotal_prototype_test2;
		double sqtotal_prototype[] = new double[] {0, 0};
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
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype[0] = sqtotal_prototype_test1 + sqtotal_prototype_test2;
				fw.write("SQ_total Prototype Left = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				System.out.print(patient+",");
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype[1] = sqtotal_prototype_test1 + sqtotal_prototype_test2;
				fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype+",");
			} else {
				System.out.print(patient+",");
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, means, false);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, means, false);
				sqtotal_prototype[1] += sqtotal_prototype_test1 + sqtotal_prototype_test2;
				fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype+",");
			}
			fw.flush();
			fw.close();
			return sqtotal_prototype;
		}
		return new double[] {0, 0};
	}
	
	//TODO
		private static double[] processSumRes(
				File evaluationDir,
				Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
				Map<String, List<ReportData>> reportsHumphreyByPatient,
				Set<String> keysPrototype,
				Set<String> keysHumphrey) throws IOException {
			List<LoaderVisualField> leftReportPrototypeData;
			List<LoaderVisualField> rightReportPrototypeData;
			FileWriter fw;
			String evaluationName;
			double sqtotal_prototype_test1;
			double sqtotal_prototype_test2;
			double sqtotal_prototype[] = new double[] {0, 0};
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
					sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), , true);
					sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), means, true);
					sqtotal_prototype[0] = sqtotal_prototype_test1 + sqtotal_prototype_test2;
					fw.write("SQ_total Prototype Left = "+sqtotal_prototype+"\n");
					System.out.print(sqtotal_prototype+",");
				}
				
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1) {
					System.out.print(patient+",");
					sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), means, true);
					sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), means, true);
					sqtotal_prototype[1] = sqtotal_prototype_test1 + sqtotal_prototype_test2;
					fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
					System.out.print(sqtotal_prototype+",");
				} else {
					System.out.print(patient+",");
					sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, means, false);
					sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, means, false);
					sqtotal_prototype[1] += sqtotal_prototype_test1 + sqtotal_prototype_test2;
					fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
					System.out.print(sqtotal_prototype+",");
				}
				fw.flush();
				fw.close();
				return sqtotal_prototype;
			}
			return new double[] {0, 0};
		}

}
