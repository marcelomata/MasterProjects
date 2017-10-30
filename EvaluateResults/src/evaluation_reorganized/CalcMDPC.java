package evaluation_reorganized;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV1_0;
import evaluation2.LoadManually;
import evaluation2.ReportData;
import evaluation2.StatisticsComparation;

public class CalcMDPC {

	public static void calcMDPC() {
		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
//		File reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		File reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		File evaluationDir = new File("./evaluation_patients/");
		
		List<ReportData> reportsHumphreyData = HumphreyUtils.loadReportsHumphrey(reportsHumphreyDir);
		List<LoaderVisualField> reportsPrototypeData = PrototypeUtils.loadReportsPrototype(reportsPrototypeDir);
//		System.out.println(reportsHumphreyData.size());
		
		double[][] means = StatisticsComparation.getMeans(reportsPrototypeData);
		double[][] variances = StatisticsComparation.getVariances(reportsPrototypeData, means);
		double[][] variances2 = StatisticsComparation.getVariances2(reportsPrototypeData, means);
		double[][] meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		double[][] variances2Humphrey = StatisticsComparation.getVariances2Humphrey(reportsHumphreyData, meansHumphrey);
		
		Map<String, List<ReportData>> reportsHumphreyByPatient = HumphreyUtils.getReportsHumphreyByPatient(reportsHumphreyData);
		Map<String, List<LoaderVisualField>> reportsPrototypeByPatient = PrototypeUtils.getReportsPrototypeByPatient(reportsPrototypeData);
		
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
				
				leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'E');
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
				
				rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(patient), 'D');
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
				
				leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
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
				
				rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
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
					ev = StatisticsComparation.EV(LoadManually.intensitiesMathiasRight1, LoadManually.intensitiesMathiasRight2, variances2, false);
					mdp1 = StatisticsComparation.MDP(LoadManually.intensitiesMathiasRight1, variances, means, false);
//					mdp1 = Math.sqrt(mdp1);
					mdp2 = StatisticsComparation.MDP(LoadManually.intensitiesMathiasRight2, variances, means, false);
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
	
}
