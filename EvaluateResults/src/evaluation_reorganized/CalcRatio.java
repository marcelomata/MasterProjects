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

public class CalcRatio extends ComparisonAttributes {
	
	public static void main(String[] args) throws IOException {
		Map<String, double[][][]> ratios = processRatios();
		Set<String> keys = ratios.keySet();
		for (String string : keys) {
			for (int i = 0; i < ratios.get(string).length; i++) {
				PrintUtils.printField(ratios.get(string)[i]);
			}
		}
	}

	private static Map<String, double[][][]> processRatios() throws IOException {
		setUpAttributes();
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[][][]> result = new HashMap<String, double[][][]>();
		FileWriter fw;
		String evaluationName;
		double ratio_left_1[][] = new double[10][10];
		double ratio_left_2[][] = new double[10][10];
		double ratio_right_1[][] = new double[10][10];
		double ratio_right_2[][] = new double[10][10];
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
			if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				System.out.print(patient+",\n");
				ratio_left_1 = StatisticsComparation.calc_ratios_pointwise(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), true);
				ratio_left_2 = StatisticsComparation.calc_ratios_pointwise(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), true);
//				fw.write("SQ_res Left = "+sqtotal_prototype[0]);
//				System.out.print(sqtotal_prototype[0]+",");
			}
			
			rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
			rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
			if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
				ratio_right_1 = StatisticsComparation.calc_ratios_pointwise(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), false);
				ratio_right_2 = StatisticsComparation.calc_ratios_pointwise(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), false);
//				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			} else if(!patient.equalsIgnoreCase("Dennis")) {
				ratio_right_1 = StatisticsComparation.calc_ratios_pointwise(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), true);
				ratio_right_2 = StatisticsComparation.calc_ratios_pointwise(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), true);
//				fw.write("SQ_res Right = "+sqtotal_prototype+"\n");
//				System.out.print(sqtotal_prototype[1]+",\n");
			}
			fw.flush();
			fw.close();
			ratios[0] = ratio_left_1;
			ratios[1] = ratio_left_2;
			ratios[2] = ratio_right_1;
			ratios[2] = ratio_right_2;
			result.put(patient, ratios);
			return result;
		}
		return result;
	}
	
}
