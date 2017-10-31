package evaluation_reorganized;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import evaluation2.StatisticsComparation;

public class CalcRatio extends ComparisonAttributes {
	
	public static void main(String[] args) throws IOException {
		Map<String, double[][][]> ratios = processRatios();
		Set<String> keys = ratios.keySet();
		for (String string : keys) {
			if(ratios.get(string) != null) {
				System.out.println(string);
				for (int i = 0; i < ratios.get(string).length; i++) {
					PrintUtils.printFieldInt(ratios.get(string)[i]);
					System.out.println();
				}
			}
			PrintUtils.printSeparator();
		}
	}

	private static Map<String, double[][][]> processRatios() throws IOException {
		setUpAttributes();
		
		Map<String,double[][][]> result = new HashMap<String, double[][][]>();
		
		String evaluationName;
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
			
			readDataDevices(respectiveHumphewyKey, patient);
			setUpDevicesFields(leftReportPrototypeData, rightReportPrototypeData, leftReportHumphreyData, rightReportHumphreyData,PrototypeUtils.isPatientsManually(patient));
//			setUpFieldsDevicesResult();
			setUpFieldsPrototypeMeansHumphreyMeans();
			result.put(patient, calculateRatios());
			
//			return result;
		}
		return result;
	}

	private static double [][][] calculateRatios() {
//		System.out.print(patient+",\n");
		double ratios[][][] = null;
		double ratio_left_1[][] = new double[10][10];
		double ratio_left_2[][] = new double[10][10];
		double ratio_right_1[][] = new double[10][10];
		double ratio_right_2[][] = new double[10][10];
		
		if(!checkFieldsNull()) {
			ratios = new double[4][10][10];
			
			ratio_left_1 = StatisticsComparation.calc_ratios_pointwise(field1_left_1, field2_left_1, true);
			ratio_left_2 = StatisticsComparation.calc_ratios_pointwise(field1_left_2, field2_left_2, true);
			
			ratio_right_1 = StatisticsComparation.calc_ratios_pointwise(field1_right_1, field2_right_1, false);
			ratio_right_2 = StatisticsComparation.calc_ratios_pointwise(field1_right_2, field2_right_2, false);
			
	//		FileUtils.saveResultOnFile(ratio_left_1, ratio_left_2, ratio_right_1, ratio_right_2, evaluationFile);
	
			ratios[0] = ratio_left_1;
			ratios[1] = ratio_left_2;
			ratios[2] = ratio_right_1;
			ratios[3] = ratio_right_2;
		}
		
		return ratios;
	}
	
}
