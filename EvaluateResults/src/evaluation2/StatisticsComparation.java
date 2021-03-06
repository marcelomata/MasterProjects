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
				if(map[i][j] == 'y' && Math.abs(field1[i][j] - means[i][j] - dm) < 10) {
//				if(map[i][j] == 'y') {
					value = (field1[i][j] - means[i][j] - dm);
					error1 += variances[i][j];
					error2 += ((value*value) / variances[i][j]);
					//error2 += (value*value);
					count++;
				}
			}
		}
		System.out.println(count);
		error1 /= count;
		error2 /= (count - 1);
//		error2 /= count+5;
		
//		System.out.println(error2);
		
		return error1*error2;
	}
	
	public static double SQ_Total(double[][] field1, double[][] means, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
//		printArray(field1, "intensities");
//		System.out.println();
//		printArray(means, "means");
//		System.out.println();
		
		double div = 0;
		double sqtotal = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y' && (field1[i][j] -  means[i][j]) < 5) {
					sqtotal += Math.pow(field1[i][j] - means[i][j], 2);
					System.out.println("("+field1[i][j] +" - "+ means[i][j] + ") ^ 2 = " + sqtotal);
//					System.out.println(sqtotal);
					div++;
					if(div == 1) 
						break;

				}
				if(div == 1) 
					break;
			}
		}
		
		return sqtotal / div;
	}
	
	public static double[][] calc_ratios_pointwise(double[][] field1, double[][] field2, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
		double ratio[][] = new double[field1.length][field1[0].length];
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					ratio[i][j] = (int)((field2[i][j] / field1[i][j]) * 100);
				}
			}
		}
		
		return ratio;
	}
	
	public static double[][] calc_diff_pointwise(double[][] field1, double[][] field2, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
		double ratio[][] = new double[field1.length][field1[0].length];
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					ratio[i][j] = (int) (field2[i][j] - field1[i][j]);
				}
			}
		}
		
		return ratio;
	}
	
    /*
     * 1 the field 1 is bigger than the field 2
     * 0 the field 1 is equal than the field 2
     * -1 the field 1 is smaller than the field 2
     */
	public static double[][] calc_order_pointwise(double[][] field1, double[][] field2, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
		double ratio[][] = new double[field1.length][field1[0].length];
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					ratio[i][j] = field2[i][j] > field1[i][j] ? 1 : field2[i][j] < field1[i][j] ? -1 : 0;
				}
			}
		}
		
		return ratio;
	}
	
	public static double[][] calc_diff_square_by_point(double[][] field1, double[][] field2, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
		double diffSquare[][] = new double[field1.length][field1[0].length];
		int count = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					count++;
					if(count == 1) {
						System.out.println(Math.pow(field2[i][j] - field1[i][j], 2) + " - " + field1[i][j]);
					}
					diffSquare[i][j] = Math.pow(field2[i][j] - field1[i][j], 2);
				}
			}
		}
		
		return diffSquare;
	}
	
	public static int calc_number_field1_bigger(double[][] field1, double[][] field2, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
		int count = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y' && field2[i][j] > field1[i][j]) {
					count += 1;
				}
			}
		}
		
		return count;
	}
	
	public static double pearson_correlation(double[][] field1, double[][] field2, double[][] means1, double[][] means2, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
//		printArray(field1, "intensities");
//		System.out.println();
//		printArray(means, "means");
//		System.out.println();
		
		double humphrey = 0;
		double humphreySqr = 0;
		double protpotype = 0;
		double protpotypeSqr = 0;
		double mult = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					protpotype = field1[i][j] - means1[i][j];
					humphrey = field2[i][j] - means2[i][j];
					mult += protpotype * humphrey;
					protpotypeSqr = Math.pow(field1[i][j] - means1[i][j], 2);
					humphreySqr = Math.pow(field2[i][j] - means2[i][j], 2);
				}
			}
		}
		
		return mult / (Math.sqrt(protpotypeSqr)*Math.sqrt(humphreySqr));
	}
	
	public static double deviation(double[][] field1, double[][] means, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		
//		printArray(field1, "means1");
//		System.out.println();
//		printArray(means, "means2");
//		System.out.println();
		
		double div = 0;
		double sqtotal = 0;
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
//					System.out.println((field1[i][j] - means[i][j]));
					sqtotal += (field1[i][j] - means[i][j]);
					div++;
					if(div == 1) 
						break;
				}
			}
			if(div == 1) 
				break;
		}
		
		return sqtotal / div;
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
		
		int counts[][] = new int[10][10];
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
					counts[i][j]++;
				}
			}
		}
		
		means = normalize(means, counts, false);
		
		return means;
	}
	
	public static double[][] getMeansHumphrey(List<ReportData> reportsHumphreyData) {
		double[][] means = new double[10][10];
		
		int count = reportsHumphreyData.size();
		double [][]intensities = null;
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
		int counts[][] = new int[10][10];
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
					counts[i][j]++;
				}
			}
		}
		
		variances = normalize(variances, counts, true);
		
		return variances;
	}
	
	private static double[][] normalize(double[][] values, int[][] counts, boolean variance) {
		double[][] normalized = new double[values.length][values[0].length];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if(values[i][j] > 0) {
					normalized[i][j] = values[i][j] / (variance ? (counts[i][j]) : counts[i][j]);
				}
			}
		}
		
		return normalized;
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
	
}
