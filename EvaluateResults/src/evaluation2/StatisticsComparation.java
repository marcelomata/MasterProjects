package evaluation2;

import java.util.List;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV1_0;


public class StatisticsComparation {
	
	public static double meanSquareEror(double[][] field1, double[][] field2, boolean leftSide) {
		double mse = 0;
		
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT; 
		
		int count = 0;
		for (int i = 0; i < field2.length; i++) {
			for (int j = 0; j < field2[i].length; j++) {
				if(map[i][j] == 'y') {
					mse += (field1[i][j]*field1[i][j]) - (field2[i][j]*field2[i][j]);
					count++;
				}
			}
		}
		mse /= count;
		if(mse < 0) 
			return -Math.sqrt(-mse);
		else 
			return Math.sqrt(mse);
	}
	
	
	public static double rootMeanSquareEror(double[][] field1, double[][] field2, boolean leftSide) {
		double mse = 0;
		
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT; 
		
		int count = 0;
		for (int i = 0; i < field2.length; i++) {
			for (int j = 0; j < field2[i].length; j++) {
				if(map[i][j] == 'y') {
					mse += (field1[i][j]*field1[i][j]) - (field2[i][j]*field2[i][j]);
					count++;
				}
			}
		}
		mse /= count;
		
		if(mse < 0) 
			mse = mse*(-1);
		
		return Math.sqrt(Math.sqrt(mse));
	}
	
	public static double DM(double[][] field1, double[][] variances, double[][] means, boolean leftSide) {
		double error1 = 0;
		double error2 = 0;
		
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT; 
		
		int count = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					error1 += ((field1[i][j] - means[i][j]) / variances[i][j]);
					error2 += (1 / variances[i][j]);
					count++;
				}
			}
		}
		error1 /= count;
		error2 /= count;
		
		return error1 / error2;
	}
	
	public static double MDP(double[][] field1, double[][] variances, double[][] means, boolean leftSide) {
		double error1 = 0;
		double error2 = 0;
		
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT; 
		
		int count = 0;
		double dm = DM(field1, variances, means, leftSide);
		double value;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y' && Math.abs(field1[i][j] - means[i][j]) < 10) {
					value = (field1[i][j] - means[i][j] - dm);
					error1 += variances[i][j];
					error2 += ((value*value) / variances[i][j]);
					//error2 += (value*value);
					count++;
				}
			}
		}
		error1 /= count;
		error2 /= (count - 1);
		//error2 /= count;
		
//		System.out.println(error2);
		
		return error1*error2;
	}
	
	public static double SQ_Total(double[][] field1, double[][] means, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
		double sqtotal = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					sqtotal = Math.pow(field1[i][j] - means[i][j], 2);
				}
			}
		}
		
		return sqtotal;
	}
	
	public static double EV(double[][] field1, double[][] field2, double[][] variances2, boolean leftSide) {
		double error1 = 0;
		double error2 = 0;
		double error3 = 0;
		
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT; 
		
		int count = 1;
		double value;
		double value2 = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
				//if(map[i][j] == 'y' && Math.abs(field1[i][j] - field2[i][j]) < 20) {
					value = (field1[i][j] - field2[i][j]);
					error1 += variances2[i][j];
					value2 += (value*value);
					error3 += (2*variances2[i][j]);
					error2 += (value2 / error3);
					count++;
				}
				if(count == 10) {
					break;
				}
			}
			if(count == 10) {
				break;
			}
		}
		
//		System.out.println("Count = " + count + " - Error 1 = " + error1 + " - Value 2 = " + value2 + " - Error 2 = " + error3);
//		System.out.println();
		
		error1 /= count;
		error2 /= count;
		
		return error1 * error2;
	}
	
	public static double MDPC(double mdp, double ev, double k) {
		double mdpc = mdp - (k*ev);
		return mdpc < 0 ? 0 : mdpc;
	}
	
	public static double DMS() {
		double dms = 0;
		return dms;
	}
	
	public static double[][] getMeans(List<LoaderVisualField> reportsPrototypeData) {
		double[][] means = new double[10][10];
		
		int count = reportsPrototypeData.size();
		double [][]intensities;
		for (LoaderVisualField report : reportsPrototypeData) {
			if(report instanceof VisualFieldFileLoaderV1_0) {
				intensities = report.getParameters().getIntensitiesAsDouble();
			} else {
				intensities = report.getParameters().getIntensities();
			}
			
			//printArray(intensities, "Intensities "+report.getInformations().getPatient().getName());
			for (int i = 0; i < means.length; i++) {
				for (int j = 0; j < means[i].length; j++) {
					means[i][j] += intensities[i][j];
				}
			}
		}
		
		means = normalize(means, count);
		
		return means;
	}
	
	public static double[][] getMeansHumphrey(List<ReportData> reportsHumphreyData) {
		double[][] means = new double[10][10];
		
		int count = reportsHumphreyData.size();
		double [][]intensities;
		for (ReportData report : reportsHumphreyData) {
			intensities = report.getNumericIntensities();
			for (int i = 0; i < means.length; i++) {
				for (int j = 0; j < means[i].length; j++) {
					means[i][j] += intensities[i][j];
				}
			}
		}

		means = normalize(means, count);
		
		return means;
	}
	
	public static double[][] getVariances2(List<LoaderVisualField> reportsPrototypeData, double[][] means) {
		double[][] variances = new double[10][10];

		char[][] map = Constants.MAP_RIGHT;
		
		double difference;
		double [][]intensities;
		int count = 1;
		boolean ok = false;
		for (LoaderVisualField report : reportsPrototypeData) {
			map = report.getEnumEye() == EnumEye.RIGHT ? Constants.MAP_RIGHT : Constants.MAP_LEFT;
			if(report instanceof VisualFieldFileLoaderV1_0) {
				intensities = report.getParameters().getIntensitiesAsDouble();
			} else {
				intensities = report.getParameters().getIntensities();
			}
			for (int i = 0; i < variances.length; i++) {
				for (int j = 0; j < variances[i].length; j++) {
					if(map[i][j] == 'y') {
						difference = intensities[i][j] - means[i][j];
						variances[i][j] += (difference * difference);
						count++;
					}
					if(count == 10) {
						ok = true;
						break;
					}
				}
				if(ok == true) {
					break;
				}
			}
		}
		
		means = normalize(variances, reportsPrototypeData.size()-1);
		
		return variances;
	}
	
	public static double[][] getVariances2Humphrey(List<ReportData> reportsHumphreyData, double[][] means) {
		double[][] variances = new double[10][10];
		
		char[][] map = Constants.MAP_RIGHT;
		
		double difference;
		double [][]intensities;
		int count = 0;
		boolean ok = false;
		for (ReportData report : reportsHumphreyData) {
			map = report.getEyeSide().equalsIgnoreCase("Direito") ? Constants.MAP_RIGHT : Constants.MAP_LEFT;
			intensities = report.getNumericIntensities();
			for (int i = 0; i < variances.length; i++) {
				for (int j = 0; j < variances[i].length; j++) {
					if(map[i][j] == 'y') {
						difference = intensities[i][j] - means[i][j];
						variances[i][j] += (difference * difference);
						count++;
					}
					if(count == 10) {
						ok = true;
						break;
					}
				}
				if(ok == true) {
					break;
				}
			}
		}
		
		means = normalize(variances, reportsHumphreyData.size()-1);
		
		return variances;
	}
	
	public static double[][] getVariances(List<LoaderVisualField> reportsPrototypeData, double[][] means) {
		double[][] variances = new double[10][10];
		
		double difference;
		double [][]intensities;
		int count = 0;
		for (LoaderVisualField report : reportsPrototypeData) {
			if(report instanceof VisualFieldFileLoaderV1_0) {
				intensities = report.getParameters().getIntensitiesAsDouble();
			} else {
				intensities = report.getParameters().getIntensities();
			}
			for (int i = 0; i < variances.length; i++) {
				for (int j = 0; j < variances[i].length; j++) {
					difference = intensities[i][j] - means[i][j];
					variances[i][j] += (difference * difference);
				}
			}
			count++;
		}
		
		means = normalize(variances, reportsPrototypeData.size()-1);
		
		return variances;
	}
	
	public static double[][] normalize(double[][]values, int total) {
		double[][] normalized = new double[values.length][values[0].length];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if(values[i][j] > 0) {
					normalized[i][j] = values[i][j] / total;
				}
			}
		}
		
		return normalized;
	}
	
	private static void printArray(double[][] array, String string) {
		System.out.println(string);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j]+",");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}
	

}
