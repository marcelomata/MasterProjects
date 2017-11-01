package evaluation_reorganized;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import evaluation2.ReportData;
import evaluation2.StatisticsComparation;

public class CalcREMQ extends ComparisonAttributes {

	public static void main(String[] args) throws IOException {
		Map<String, double[][]> patientsEV = processEV();
		Set<String> keys = patientsEV.keySet();
		for (String key : keys) {
			System.out.println(key);
		}
		double[][] patient;
		for (String key : keys) {
			patient = patientsEV.get(key);
//			if(ReportData.isPatientsManually(key)) {
				System.out.println(key);
				System.out.println(patient[1][0]+", "+patient[1][1]);
				System.out.println(patient[0][0]+", "+patient[0][1]);
//			}
		}
	}
	
	
	private static Map<String, double[][]> processEV() throws IOException {
		setUpAttributes();
		
		Map<String,double[][]> result = new HashMap<String, double[][]>();
		
		String respectiveHumphewyKey = "";
		
		for (String patient : keysPrototype) {
			respectiveHumphewyKey = Utils.getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
			
			if(respectiveHumphewyKey.isEmpty()) {
				continue;
			}
			
			readDataDevices(respectiveHumphewyKey, patient);
			setUpDevicesFields(leftReportPrototypeData, rightReportPrototypeData, leftReportHumphreyData, rightReportHumphreyData, PrototypeUtils.isPatientsManually(patient));
			setUpFieldsDevicesResult();
			result.put(patient, calculateEV());
		}
		return result;
	}
	
	private static double [][] calculateEV() {
//			System.out.print(patient+",\n");
		double evs[][] = new double[2][2];
		double ev_left_prototype = 0.0;
		double ev_right_prototype = 0.0;
		double ev_left_humphrey = 0.0;
		double ev_right_humphrey = 0.0;
		
		
		if(!checkFieldsNull()) {
			evs = new double[2][2];
			
			ev_left_prototype = StatisticsComparation.rootMeanSquareEror(field1_left_1, field1_left_2, true);
			ev_right_prototype = StatisticsComparation.rootMeanSquareEror(field1_right_1, field1_right_2, false);
			
			ev_left_humphrey = StatisticsComparation.rootMeanSquareEror(field2_left_1, field2_left_2, true);
			ev_right_humphrey = StatisticsComparation.rootMeanSquareEror(field2_right_1, field2_right_2, false);
			
			evs[0][0] = ev_left_prototype; 
			evs[0][1] = ev_right_prototype;
			evs[1][0] = ev_left_humphrey;
			evs[1][1] = ev_right_humphrey;
		}
		
		return evs;
	}
}
