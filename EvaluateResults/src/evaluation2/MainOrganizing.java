
package evaluation2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.LayoutManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.RefineryUtilities;

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
		Map<String,double[]> sq_total_humphrey = null;
		Map<String,double[]> sq_total_prototype = null;
		Map<String,double[]> sq_total_res = null;
		Map<String,double[]> pearson_correlation = null;
		
		try {
			sq_total_humphrey = processHumphreyR(evaluationDir, meansHumphrey, reportsHumphreyByPatient, keysHumphrey);
//			sq_total_prototype = processPrototypeR(evaluationDir, means, reportsPrototypeByPatient, keysPrototype);
			sq_total_prototype = processPrototypeR(evaluationDir, meansHumphrey, reportsPrototypeByPatient, keysPrototype);
			sq_total_res = processSumRes(evaluationDir,reportsPrototypeByPatient, reportsHumphreyByPatient, keysPrototype, keysHumphrey);
			pearson_correlation = pearsonCorrelation(evaluationDir,reportsPrototypeByPatient, reportsHumphreyByPatient, means, meansHumphrey, keysPrototype, keysHumphrey);
//			plotAll(evaluationDir,reportsPrototypeByPatient, reportsHumphreyByPatient, means, meansHumphrey, keysPrototype, keysHumphrey);
			Set<String> pearson_keys = pearson_correlation.keySet();
			for (String string : pearson_keys) {
				System.out.println("ho left = " + pearson_correlation.get(string)[0]);
				System.out.println("ho right = " + pearson_correlation.get(string)[1]);
			}
//			System.out.println(StatisticsComparation.deviation(means, meansHumphrey, true));
			for(String key : keysPrototype) {
				if(sq_total_res.containsKey(key) && sq_total_prototype.containsKey(key) && sq_total_humphrey.containsKey(getHumphreyKeyByPrototypeKey(key, keysHumphrey))) {
					System.out.println();
					System.out.println(key);
					System.out.println("Res left "+sq_total_res.get(key)[0]);
					System.out.println("Prot left "+sq_total_prototype.get(key)[0]);
					System.out.println("Humphrey left "+sq_total_humphrey.get(getHumphreyKeyByPrototypeKey(key, keysHumphrey))[0]);
					System.out.println("Res Right "+sq_total_res.get(key)[1]);
					System.out.println("Prot Right "+sq_total_prototype.get(key)[1]);
					System.out.println("Humphrey Right "+sq_total_humphrey.get(getHumphreyKeyByPrototypeKey(key, keysHumphrey))[1]);
					r_sqr_left = Math.sqrt(1 - (sq_total_res.get(key)[0] / sq_total_prototype.get(key)[0]));
					r_sqr_right = Math.sqrt(1 - (sq_total_res.get(key)[1] / sq_total_prototype.get(key)[1]));
	//				r_sqr_left = Math.sqrt(sq_total_humphrey.get(i)[0] / sq_total_prototype.get(i)[0]);
	//				r_sqr_right = Math.sqrt(sq_total_humphrey.get(i)[1] / sq_total_prototype.get(i)[1]);
					System.out.println(r_sqr_left+","+r_sqr_right);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void calcREMQ() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt_2/");
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
	private static Map<String, double[]> processHumphreyR(File evaluationDir,
			double[][] means,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysHumphrey) throws IOException {
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[]> result = new HashMap<String, double[]>();
		FileWriter fw;
		String evaluationName;
		double sqtotal_humphrey_test1;
		double sqtotal_humphrey_test2;
		File evaluationFile;
		for (String patient : keysHumphrey) {
			
			if(!patient.toLowerCase().contains("luiza"))
				continue;
			
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Humphrey.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			double []sqtotal_humphrey = {0, 0};
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",\n");
			if(leftReportHumphreyData.size() > 1) {
				sqtotal_humphrey_test1 = StatisticsComparation.SQ_Total(leftReportHumphreyData.get(0).getNumericIntensities(), means, true);
				sqtotal_humphrey_test2 = StatisticsComparation.SQ_Total(leftReportHumphreyData.get(1).getNumericIntensities(), means, true);
				sqtotal_humphrey[0] = (sqtotal_humphrey_test1 + sqtotal_humphrey_test2) / 2;
				fw.write("SQ_total Humphrey Left = "+sqtotal_humphrey+"\n");
				System.out.print(sqtotal_humphrey[0]+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				sqtotal_humphrey_test1 = StatisticsComparation.SQ_Total(rightReportHumphreyData.get(0).getNumericIntensities(), means, false);
				sqtotal_humphrey_test2 = StatisticsComparation.SQ_Total(rightReportHumphreyData.get(1).getNumericIntensities(), means, false);
				sqtotal_humphrey[1] = (sqtotal_humphrey_test1 + sqtotal_humphrey_test2) / 2;
				fw.write("SQ_total Humphrey Right = "+sqtotal_humphrey+"\n");
				System.out.print(sqtotal_humphrey[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, sqtotal_humphrey);
			return result;
		}
		return result;
	}

	//TODO
	private static Map<String, double[]> processPrototypeR(
			File evaluationDir,
			double[][] means,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Set<String> keysPrototype) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		Map<String,double[]> result = new HashMap<String, double[]>();
		FileWriter fw;
		String evaluationName;
		double sqtotal_prototype_test1;
		double sqtotal_prototype_test2;
		File evaluationFile;
		for (String patient : keysPrototype) {
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			double sqtotal_prototype[] = new double[] {0, 0};
			System.out.print(patient+",\n");
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype[0] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_total Prototype Left = "+sqtotal_prototype+"");
				System.out.print(sqtotal_prototype[0]+",");
			}
			
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), means, true);
				sqtotal_prototype[1] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype[1]+",\n");
			} else {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, means, false);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, means, false);
				sqtotal_prototype[1] += (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, sqtotal_prototype);
			return result;
		}
		return result;
	}
	
	//TODO
	private static Map<String, double[]> processSumRes(
			File evaluationDir,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			Set<String> keysPrototype,
			Set<String> keysHumphrey) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[]> result = new HashMap<String, double[]>();
		FileWriter fw;
		String evaluationName;
		double sqtotal_prototype_test1 = 0;
		double sqtotal_prototype_test2 = 0;
		File evaluationFile;
		String respectiveHumphewyKey = "";
		for (String patient : keysPrototype) {
			respectiveHumphewyKey = getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
			if(respectiveHumphewyKey.isEmpty()) {
				continue;
			}
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			double sqtotal_prototype[] = new double[] {0, 0};
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				System.out.print(patient+",\n");
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), true);
				sqtotal_prototype[0] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_res Left = "+sqtotal_prototype[0]);
				System.out.print(sqtotal_prototype[0]+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false);
//				plot(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false, new String[] {"Protótipo", "Humphrey"});
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), false);
				sqtotal_prototype[1] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype[1]+",\n");
			} else if(!patient.equalsIgnoreCase("Dennis")) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), false);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), false);
				sqtotal_prototype[1] += (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
				System.out.print(sqtotal_prototype[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, sqtotal_prototype);
			return result;
		}
		return result;
	}
	
	//TODO
	private static Map<String, double[]> pearsonCorrelation(
			File evaluationDir,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			double[][] means1,
			double[][] means2,
			Set<String> keysPrototype,
			Set<String> keysHumphrey) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[]> result = new HashMap<String, double[]>();
		FileWriter fw;
		String evaluationName;
		double pearson_correlation_test1 = 0;
		double pearson_correlation_test2 = 0;
		File evaluationFile;
		String respectiveHumphewyKey = "";
		for (String patient : keysPrototype) {
			respectiveHumphewyKey = getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
			if(respectiveHumphewyKey.isEmpty()) {
				continue;
			}
			evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
			evaluationFile = new File(evaluationName);
			if(!evaluationFile.exists()) {
				evaluationFile.createNewFile();
			}
			
			fw = new FileWriter(evaluationFile);
			
			double pearson_correlation[] = new double[] {0, 0};
			leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
			leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				System.out.print(patient+",\n");
				pearson_correlation_test1 = StatisticsComparation.pearson_correlation(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), means1, means2, true);
				pearson_correlation_test2 = StatisticsComparation.pearson_correlation(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), means1, means2, true);
				pearson_correlation[0] = (pearson_correlation_test1 + pearson_correlation_test2);
				fw.write("pearson correlation Left = "+pearson_correlation[0]);
				System.out.print(pearson_correlation[0]+",");
			}
			
			rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
			rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				pearson_correlation_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false);
				pearson_correlation_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), false);
				pearson_correlation[1] = (pearson_correlation_test1 + pearson_correlation_test2);
				fw.write("pearson correlation Right = "+pearson_correlation+"\n");
				System.out.print(pearson_correlation[1]+",\n");
			} else if(!patient.equalsIgnoreCase("Dennis")) {
				pearson_correlation_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), false);
				pearson_correlation_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), false);
				pearson_correlation[1] += (pearson_correlation_test1 + pearson_correlation_test2);
				fw.write("pearson correlation Right = "+pearson_correlation+"\n");
				System.out.print(pearson_correlation[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, pearson_correlation);
			return result;
		}
		return result;
	}
	
	private static String getHumphreyKeyByPrototypeKey(String key, Set<String> keysHumphrey) {
		String namesP[] = key.split(" ");
		String namesH[] = null;
		for (String string : keysHumphrey) {
			namesH = string.split(" ");
			if(namesH[0].toLowerCase().contains(key.split(" ")[0].toLowerCase())) {
				if(namesH.length > 1 && namesP.length > 1) {
					if(namesH[1].toLowerCase().equalsIgnoreCase("ufrgs") || namesH[1].toLowerCase().charAt(0) == namesP[1].toLowerCase().charAt(0)) {
//						System.out.println("\n" + string + " contains " + key);
						return string;
					}
				} else {
//					System.out.println("\n" + string + " contains " + key);
					return string;
				}
			}
			
		}
		return "";
	}
	
	//TODO
	private static void plotAll(
			File evaluationDir,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			double [][] means1,
			double [][] means2,
			Set<String> keysPrototype,
			Set<String> keysHumphrey) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[]> result = new HashMap<String, double[]>();
		
		String respectiveHumphewyKey = "";
		for (String patient : keysPrototype) {
			if(patient.toLowerCase().contains("vinicius b")) {
				respectiveHumphewyKey = getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
				if(respectiveHumphewyKey.isEmpty()) {
					continue;
				}
				
				leftReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
				leftReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
				if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
					System.out.print(patient+",\n");
					plot(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), means1, means2, true, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), means1, means2, true, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				}
				
				rightReportHumphreyData = getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
				rightReportPrototypeData = getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
					plot(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				} else if(!patient.equalsIgnoreCase("Dennis")) {
					plot(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				}
				return;
			}
		}
	}
	
	private static void plot(double field1[][], double field2[][], boolean left, String[] namesOfSeries) {
		char[][] map = left ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
//		XyGui plot;
		ScatterPlotDemo1 plot;
		Double [][]xDataTotal1 = new Double[2][50];
		Double [][]yDataTotal1 = new Double[2][50];
		int count = 0; 
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					xDataTotal1[0][count] = (double) count+1;
					yDataTotal1[0][count] = field1[i][j];
					xDataTotal1[1][count] = (double) count+1;
					yDataTotal1[1][count] = field2[i][j];
					count++;
				}
			}
		}
		
//		plot = new XyGui("Test samples ", xDataTotal1, yDataTotal1, true);
		SampleXYDataset2 s = new SampleXYDataset2(2, 50, xDataTotal1, yDataTotal1, namesOfSeries);
//		s.setxValues(new Double[][] {xDataTotal1, new Double[]{1.0}});
//		s.setyValues(new Double[][] {yDataTotal1, new Double[]{1.0}});
		plot = new ScatterPlotDemo1("Test samples ", s);
		plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
//		plot.plot();
	}
	
	private static void plot(double field1[][], double field2[][], double mean1[][], double mean2[][], boolean left, String[] seriesName) {
		char[][] map = left ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
//		XyGui plot;
		ScatterPlotDemo1 plot;
		Double [][]xDataTotal1 = new Double[4][50];
		Double [][]yDataTotal1 = new Double[4][50];
		int count = 0; 
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					xDataTotal1[0][count] = (double) count+1;
					yDataTotal1[0][count] = field1[i][j];
					xDataTotal1[1][count] = (double) count+1;
					yDataTotal1[1][count] = field2[i][j];
					xDataTotal1[2][count] = (double) count+1;
					yDataTotal1[2][count] = mean1[i][j];
					xDataTotal1[3][count] = (double) count+1;
					yDataTotal1[3][count] = mean2[i][j];
					count++;
				}
			}
		}
		
//		plot = new XyGui("Test samples ", xDataTotal1, yDataTotal1, true);
		SampleXYDataset2 s = new SampleXYDataset2(4, 50, xDataTotal1, yDataTotal1, seriesName);
//		s.setxValues(new Double[][] {xDataTotal1, new Double[]{1.0}});
//		s.setyValues(new Double[][] {yDataTotal1, new Double[]{1.0}});
		plot = new ScatterPlotDemo1("Test samples ", s);
		plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
//		plot.plot();
	}

}
