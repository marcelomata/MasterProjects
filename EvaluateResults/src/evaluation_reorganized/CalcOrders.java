package evaluation_reorganized;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import evaluation2.StatisticsComparation;

public class CalcOrders extends ComparisonAttributes {
	
//	public static void main(String[] args) throws IOException {
////		Map<String, double[][][]> diffs = processDiffs();
//		Map<String, int[]> orders = processOrders();
//		Set<String> keys = diffs.keySet();
//		for (String string : keys) {
//			if(diffs.get(string) != null) {
//				System.out.println(string);
//				for (int i = 0; i < diffs.get(string).length; i++) {
//					PrintUtils.printFieldInt(diffs.get(string)[i]);
//					System.out.println();
//				}
//			}
//			PrintUtils.printSeparator();
//		}
//	}

	private static Map<String, double[][][]> processOrders() throws IOException {
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
			boolean isDennis = patient.equalsIgnoreCase("Dennis");
			setUpDevicesFields(leftReportPrototypeData, rightReportPrototypeData, leftReportHumphreyData, rightReportHumphreyData, isDennis);
			setUpFieldsDevicesResult();
//			setUpFieldsPrototypeAndProtMeans();
//			setUpFieldsPrototypeMeansHumphreyMeans();
			result.put(patient, calculateOrders());
			
//			return result;
		}
		return result;
	}

	private static double[][][] calculateOrders() {
//		System.out.print(patient+",\n");
		double ratios[][][] = null;
		double diff_left_1[][] = new double[10][10];
		double diff_left_2[][] = new double[10][10];
		double diff_right_1[][] = new double[10][10];
		double diff_right_2[][] = new double[10][10];
		
		if(!checkFieldsNull()) {
			ratios = new double[4][10][10];
		
			diff_left_1 = StatisticsComparation.calc_order_pointwise(field1_left_1, field2_left_1, true);
			diff_left_2 = StatisticsComparation.calc_order_pointwise(field1_left_2, field2_left_2, true);
			
			diff_right_1 = StatisticsComparation.calc_order_pointwise(field1_right_1, field2_right_1, false);
			diff_right_2 = StatisticsComparation.calc_order_pointwise(field1_right_2, field2_right_2, false);

//		FileUtils.saveResultOnFile(diff_left_1, diff_left_2, diff_right_1, diff_right_2, evaluationFile);
		
			ratios[0] = diff_left_1;
			ratios[1] = diff_left_2;
			ratios[2] = diff_right_1;
			ratios[3] = diff_right_2;
		}
		
		return ratios;
	}
	
	private static int[] countingOrders() {
//		System.out.print(patient+",\n");
		int ordersCount[] = null;
		int count_left_1 = 0;
		int count_left_2 = 0;
		int count_right_1 = 0;
		int count_right_2 = 0;
		
		if(!checkFieldsNull()) {
			ordersCount = new int[4];
		
			count_left_1 = StatisticsComparation.calc_number_field1_bigger(field1_left_1, field2_left_1, true);
			count_left_2 = StatisticsComparation.calc_number_field1_bigger(field1_left_2, field2_left_2, true);
			
			count_right_1 = StatisticsComparation.calc_number_field1_bigger(field1_right_1, field2_right_1, false);
			count_right_2 = StatisticsComparation.calc_number_field1_bigger(field1_right_2, field2_right_2, false);

//		FileUtils.saveResultOnFile(diff_left_1, diff_left_2, diff_right_1, diff_right_2, evaluationFile);
		
			ordersCount[0] = count_left_1;
			ordersCount[1] = count_left_2;
			ordersCount[2] = count_right_1;
			ordersCount[3] = count_right_2;
		}
		
		return ordersCount;
	}

}