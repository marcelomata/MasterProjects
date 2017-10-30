package evaluation_reorganized;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import evaluation2.StatisticsComparation;

public class CalcByIndex extends ComparisonAttributes {
	
	public static void main(String[] args) throws IOException {
//		Map<String, double[][][]> squareDiffsPrototype = new HashMap<String, double[][][]>();
//		Map<String, double[][][]> squareDiffsHumphrey = new HashMap<String, double[][][]>();
//		squareDiffsPrototype = processCov(2);
//		System.out.println("################");
//		squareDiffsHumphrey = processCov(3);
		boolean print = false;
		int typeOfPrint = 0;
		int side = 1;
		Map<String, double[][][][]> fieldsMeasurements = getFields(1, print, typeOfPrint);
//	    Object[] allDiffsArray = new Object[] {squareDiffsPrototype, squareDiffsHumphrey};
//	    Object[] allDiffsArray = new Object[] {squareDiffsPrototype};
//		Map<String, double[][][][]> squareDiffsAll = getAllSquareDiffs(allDiffsArray);
//		PlotUtils.plotByFieldIndex(squareDiffsAll, 1, true);
		PlotUtils.plotByFieldIndex(fieldsMeasurements, 2, side);
	}
	
	private static Map<String, double[][][]> processCov(int typeFields) throws IOException {
		Map<String,double[][][]> result = new HashMap<String, double[][][]>();
		processCovByType(result, null, typeFields);
		return result;
	}
	
	private static void processCovByType(Map<String, double[][][]> resultPointWise, Map<String, int[]> resultCount, 
				int typeFields) throws IOException {
		setUpAttributes();
		
		for (String patient : keysPrototype) {
			if(!setUpPatientDataToProcess(patient, typeFields)) {
				continue;
			}
			resultPointWise.put(patient, calculateDiffSquareByPoint());
		}
	}
	
	private static double[][][] calculateDiffSquareByPoint() {
		double diffs[][][] = null;
		double diff_left_1[][] = new double[10][10];
		double diff_left_2[][] = new double[10][10];
		double diff_right_1[][] = new double[10][10];
		double diff_right_2[][] = new double[10][10];
		
		if(!checkFieldsNull()) {
			diffs = new double[4][10][10];
		
			diff_left_1 = StatisticsComparation.calc_diff_square_by_point(field1_left_1, field2_left_1, true);
			diff_left_2 = StatisticsComparation.calc_diff_square_by_point(field1_left_2, field2_left_2, true);
			
			diff_right_1 = StatisticsComparation.calc_diff_square_by_point(field1_right_1, field2_right_1, false);
			diff_right_2 = StatisticsComparation.calc_diff_square_by_point(field1_right_2, field2_right_2, false);

			diffs[0] = diff_left_1;
			diffs[1] = diff_left_2;
			diffs[2] = diff_right_1;
			diffs[3] = diff_right_2;
		}
		
		return diffs;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, double[][][][]> getAllSquareDiffs(Object []allSquareDiffs) {
		Map<String, double[][][][]> squareDiffsAll = new HashMap<String, double[][][][]>();
		double[][][][] allDiffsValue = new double[allSquareDiffs.length][][][];
		double[][][][] currentValueAll = null;
		
		Map<String, double[][][]> currentObject = null;
		double[][][] currentValue = null;
		Set<String> keys = null;
		for (int i = 0; i < allSquareDiffs.length; i++) {
			currentObject = (Map<String, double[][][]>) allSquareDiffs[i];
			keys = currentObject.keySet();
			for (String key : keys) {
				currentValue = currentObject.get(key);
				allDiffsValue[i] = currentValue;
				currentValueAll = squareDiffsAll.get(key);
				if(currentValueAll != null) {
					squareDiffsAll.get(key)[i] = allDiffsValue[i];
				} else {
					squareDiffsAll.put(key, allDiffsValue);
				}
			}
		}
		return squareDiffsAll;
	}
	
}
