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

public class CalcDiff extends ComparisonAttributes {
	
	public static void main(String[] args) throws IOException {
		Map<String, double[][][]> diffs = processDiffs();
		Set<String> keys = diffs.keySet();
		for (String string : keys) {
			for (int i = 0; i < diffs.get(string).length; i++) {
				PrintUtils.printField(diffs.get(string)[i]);
				System.out.println();
			}
		}
	}

	private static Map<String, double[][][]> processDiffs() throws IOException {
		setUpAttributes();
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[][][]> result = new HashMap<String, double[][][]>();
		FileWriter fw;
		String evaluationName;
		double diff_left_1[][] = new double[10][10];
		double diff_left_2[][] = new double[10][10];
		double diff_right_1[][] = new double[10][10];
		double diff_right_2[][] = new double[10][10];
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
			
			double ratios[][][] = new double[4][10][10];
			leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
			leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
			rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
			rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			
			double[][] field_left_prototype_1 = leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
			double[][] field_left_prototype_2 = leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
			double[][] field_left_humphrey_1 = leftReportHumphreyData.get(0).getNumericIntensities();
			double[][] field_left_humphrey_2 = leftReportHumphreyData.get(1).getNumericIntensities();
			double[][] field_right_prototype_1 = rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble();
			double[][] field_right_prototype_2 = rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble();
			double[][] field_right_humphrey_1 = rightReportHumphreyData.get(0).getNumericIntensities();
			double[][] field_right_humphrey_2 = rightReportHumphreyData.get(1).getNumericIntensities();
			
			if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				System.out.print(patient+",\n");
				diff_left_1 = StatisticsComparation.calc_diff_pointwise(field_left_prototype_1, field_left_humphrey_1, true);
				diff_left_2 = StatisticsComparation.calc_diff_pointwise(field_left_prototype_2, field_left_humphrey_2, true);
//				fw.write("SQ_res Left = "+sqtotal_prototype[0]);
//				System.out.print(sqtotal_prototype[0]+",");
			}
			
			if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				diff_right_1 = StatisticsComparation.calc_diff_pointwise(field_right_prototype_1, field_right_humphrey_1, false);
				diff_right_2 = StatisticsComparation.calc_diff_pointwise(field_right_prototype_2, field_right_humphrey_2, false);
//				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			} else if(!patient.equalsIgnoreCase("Dennis")) {
				diff_right_1 = StatisticsComparation.calc_diff_pointwise(LoadMathias.intensitiesMathiasRight1, field_right_humphrey_1, true);
				diff_right_2 = StatisticsComparation.calc_diff_pointwise(LoadMathias.intensitiesMathiasRight2, field_right_humphrey_2, true);
//				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			}
			fw.flush();
			fw.close();
			ratios[0] = diff_left_1;
			ratios[1] = diff_left_2;
			ratios[2] = diff_right_1;
			ratios[2] = diff_right_2;
			result.put(patient, ratios);
			return result;
		}
		return result;
	}
	
}
