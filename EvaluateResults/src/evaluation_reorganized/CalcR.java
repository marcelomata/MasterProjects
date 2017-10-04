package evaluation_reorganized;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import evaluation2.LoadMathias;
import evaluation2.ReportData;
import evaluation2.StatisticsComparation;

public class CalcR extends ComparisonAttributes {
	
	public static void main(String[] args) {
		calcR();
	}
	
	public static void calcR() {
		setUpAttributes();
		double r_sqr_left = 0;
		double r_sqr_right = 0;
		Map<String,double[]> sq_total_humphrey = null;
		Map<String,double[]> sq_total_prototype = null;
		Map<String,double[]> sq_total_res = null;
		Map<String,double[]> pearson_correlation = null;
		
		try {
			sq_total_humphrey = processHumphreyR();
			sq_total_prototype = processPrototypeR();
			sq_total_res = processSumRes();
			pearson_correlation = pearsonCorrelation(evaluationDir,reportsPrototypeByPatient, reportsHumphreyByPatient, means, meansHumphrey, keysPrototype, keysHumphrey);
			PlotUtils.plotAll();
			for(String key : keysPrototype) {
				if(key.equalsIgnoreCase("Luiza") && sq_total_res.containsKey(key) && sq_total_prototype.containsKey(key) && sq_total_humphrey.containsKey(Utils.getHumphreyKeyByPrototypeKey(key, keysHumphrey))) {
					r_sqr_left = Math.sqrt(1 - (sq_total_res.get(key)[0] / sq_total_prototype.get(key)[0]));
					r_sqr_right = Math.sqrt(1 - (sq_total_res.get(key)[1] / sq_total_prototype.get(key)[1]));
	//				r_sqr_left = Math.sqrt(sq_total_humphrey.get(i)[0] / sq_total_prototype.get(i)[0]);
	//				r_sqr_right = Math.sqrt(sq_total_humphrey.get(i)[1] / sq_total_prototype.get(i)[1]);
					System.out.println(r_sqr_left+","+r_sqr_right);
					System.out.println(pearson_correlation.get(key)[0]+","+pearson_correlation.get(key)[1]);
				}
			}
			System.out.println();
//			Set<String> pearson_keys = pearson_correlation.keySet();
//			for (String string : pearson_keys) {
//				System.out.println("ho left = " + pearson_correlation.get(string)[0]);
//				System.out.println("ho right = " + pearson_correlation.get(string)[1]);
//			}
//			System.out.println(StatisticsComparation.deviation(means, meansHumphrey, true));
//			for(String key : keysPrototype) {
//				if(sq_total_res.containsKey(key) && sq_total_prototype.containsKey(key) && sq_total_humphrey.containsKey(Utils.getHumphreyKeyByPrototypeKey(key, keysHumphrey))) {
//					System.out.println();
//					System.out.println(key);
//					System.out.println("Res left "+sq_total_res.get(key)[0]);
//					System.out.println("Prot left "+sq_total_prototype.get(key)[0]);
//					System.out.println("Humphrey left "+sq_total_humphrey.get(Utils.getHumphreyKeyByPrototypeKey(key, keysHumphrey))[0]);
//					System.out.println("Res Right "+sq_total_res.get(key)[1]);
//					System.out.println("Prot Right "+sq_total_prototype.get(key)[1]);
//					System.out.println("Humphrey Right "+sq_total_humphrey.get(Utils.getHumphreyKeyByPrototypeKey(key, keysHumphrey))[1]);
//					r_sqr_left = Math.sqrt(1 - (sq_total_res.get(key)[0] / sq_total_prototype.get(key)[0]));
//					r_sqr_right = Math.sqrt(1 - (sq_total_res.get(key)[1] / sq_total_prototype.get(key)[1]));
//	//				r_sqr_left = Math.sqrt(sq_total_humphrey.get(i)[0] / sq_total_prototype.get(i)[0]);
//	//				r_sqr_right = Math.sqrt(sq_total_humphrey.get(i)[1] / sq_total_prototype.get(i)[1]);
//					System.out.println(r_sqr_left+","+r_sqr_right);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private static Map<String, double[]> processPrototypeR() throws IOException {
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
			leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), meansHumphrey, true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), meansHumphrey, true);
				sqtotal_prototype[0] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_total Prototype Left = "+sqtotal_prototype+"");
//				System.out.print(sqtotal_prototype[0]+",");
			}
			
			rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), meansHumphrey, true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), meansHumphrey, true);
				sqtotal_prototype[1] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			} else {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, meansHumphrey, false);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, meansHumphrey, false);
				sqtotal_prototype[1] += (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_total Prototype Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, sqtotal_prototype);
			return result;
		}
		return result;
	}
		
	private static Map<String, double[]> processSumRes() throws IOException {
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
			respectiveHumphewyKey = Utils.getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
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
			leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
			leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				System.out.print(patient+",\n");
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), true);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), true);
				sqtotal_prototype[0] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_res Left = "+sqtotal_prototype[0]);
//				System.out.print(sqtotal_prototype[0]+",");
			}
			
			rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
			rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false);
//					plot(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false, new String[] {"Protótipo", "Humphrey"});
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), false);
				sqtotal_prototype[1] = (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			} else if(!patient.equalsIgnoreCase("Dennis")) {
				sqtotal_prototype_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), false);
				sqtotal_prototype_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), false);
				sqtotal_prototype[1] += (sqtotal_prototype_test1 + sqtotal_prototype_test2) / 2;
				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, sqtotal_prototype);
			return result;
		}
		return result;
	}
	
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
			respectiveHumphewyKey = Utils.getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
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
			leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
			leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				System.out.print(patient+",\n");
				pearson_correlation_test1 = StatisticsComparation.pearson_correlation(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), means1, means2, true);
				pearson_correlation_test2 = StatisticsComparation.pearson_correlation(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), means1, means2, true);
				pearson_correlation[0] = (pearson_correlation_test1 + pearson_correlation_test2);
				fw.write("pearson correlation Left = "+pearson_correlation[0]);
//				System.out.print(pearson_correlation[0]+",");
			}
			
			rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
			rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				pearson_correlation_test1 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false);
				pearson_correlation_test2 = StatisticsComparation.SQ_Total(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), false);
				pearson_correlation[1] = (pearson_correlation_test1 + pearson_correlation_test2);
				fw.write("pearson correlation Right = "+pearson_correlation+"\n");
//				System.out.print(pearson_correlation[1]+",\n");
			} else if(!patient.equalsIgnoreCase("Dennis")) {
				pearson_correlation_test1 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), false);
				pearson_correlation_test2 = StatisticsComparation.SQ_Total(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), false);
				pearson_correlation[1] += (pearson_correlation_test1 + pearson_correlation_test2);
				fw.write("pearson correlation Right = "+pearson_correlation+"\n");
//				System.out.print(pearson_correlation[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, pearson_correlation);
			return result;
		}
		return result;
	}
		
	private static Map<String, double[]> processHumphreyR() throws IOException {
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
			leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
			fw = new FileWriter(evaluationFile);
			System.out.print(patient+",\n");
			if(leftReportHumphreyData.size() > 1) {
				sqtotal_humphrey_test1 = StatisticsComparation.SQ_Total(leftReportHumphreyData.get(0).getNumericIntensities(), meansHumphrey, true);
				sqtotal_humphrey_test2 = StatisticsComparation.SQ_Total(leftReportHumphreyData.get(1).getNumericIntensities(), meansHumphrey, true);
				sqtotal_humphrey[0] = (sqtotal_humphrey_test1 + sqtotal_humphrey_test2) / 2;
				fw.write("SQ_total Humphrey Left = "+sqtotal_humphrey+"\n");
//				System.out.print(sqtotal_humphrey[0]+",");
			}
			
			rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
			if(rightReportHumphreyData.size() > 1) {
				sqtotal_humphrey_test1 = StatisticsComparation.SQ_Total(rightReportHumphreyData.get(0).getNumericIntensities(), meansHumphrey, false);
				sqtotal_humphrey_test2 = StatisticsComparation.SQ_Total(rightReportHumphreyData.get(1).getNumericIntensities(), meansHumphrey, false);
				sqtotal_humphrey[1] = (sqtotal_humphrey_test1 + sqtotal_humphrey_test2) / 2;
				fw.write("SQ_total Humphrey Right = "+sqtotal_humphrey+"\n");
//				System.out.print(sqtotal_humphrey[1]+",\n");
			}
			fw.flush();
			fw.close();
			result.put(patient, sqtotal_humphrey);
			return result;
		}
		return result;
	}

}
