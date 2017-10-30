package evaluation_reorganized;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import evaluation2.StatisticalMatrix;

public class CalcCovariance extends ComparisonAttributes {

	/*
	public static void main(String[] args) throws IOException {
		int numberOfIndex = Utils.getNumberOfIndex();
		boolean print = true;
		int typeOfPrint = 1;
		int typeOfField = 1;
		int side = 1;
		getFields(49, print,typeOfPrint);
		print = false;
		for (int index = 1; index <= numberOfIndex; index++) {
			Map<String, double[][][][]> fieldsMeasurements = getFields(typeOfField, print,typeOfPrint);
			double correlation = getCorrelationByIndex(fieldsMeasurements, index, side);
			//System.out.println(index + " - " + correlation);
			System.out.println(correlation);
		}
//		int []indexToPlot = {49};
//		for (int i = 0; i < indexToPlot.length; i++) {
//			PlotUtils.plotByFieldIndex(getFields(indexToPlot[i], print, typeOfPrint), indexToPlot[i], side);
//		}
	}
	*/
	
	public static double getCorrelationByIndex(Map<String, double[][][][]> squareDiffs, int index, int side) {
		Double [][][]valuesByIndex = Utils.getValuesByIndex(squareDiffs, index, side);
		Double [][]xDataTotal1 = valuesByIndex[0];
		int type = 1;
		double meanHumphreyByIndex = Math.max(getMeanByIndex(type, index, true), getMeanByIndex(type, index, false)) ;
		type = 2;
		double meanPrototypeByIndex = Math.max(getMeanByIndex(type, index, true), getMeanByIndex(type, index, false)) ;
		int takeOut = Utils.setNullAsZero(xDataTotal1);
		double [][]xVariance = StatisticalMatrix.getVariance(xDataTotal1, new double[] {meanPrototypeByIndex, meanHumphreyByIndex}, takeOut);
		double [][]covarianceMatrix = StatisticalMatrix.getCovarianceMatrix(xVariance);
		double correlation = StatisticalMatrix.getCorrelation2D(covarianceMatrix);
		return correlation;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/home/cin_moliveira/Downloads/gtemp/master/reports/humphrey_measurements_bernardo.txt"))));
	    double [][]field = new double[10][10];
	    boolean left = false;
	    int countNumbers = 0;
	    int startPosSplit = 0;
	    int startPos = 0;
	    int lineIndex = 1;
		while(br.ready()) {
//			System.out.println(left ? "Esquedo" : "Direito");
			countNumbers = 0;
			String line = br.readLine();
			String lineSplit[] = line.split("\t");
			if(lineSplit.length > 1) {
				countNumbers = countNumbers(lineSplit);
				startPosSplit = lineSplit.length - countNumbers;
				startPos = 10 - countNumbers;
				for (int i = startPosSplit; i < lineSplit.length; i++) {
					field[lineIndex][startPos] = Float.parseFloat(lineSplit[i]);
				}
				lineIndex++;
			} else {
				lineIndex = 1;
				left = !left;
			}
			System.out.println();
		}
	}
	
	public static int countNumbers(String[] lineSplit) {
		int count = 0;
		for (int i = 0; i < lineSplit.length; i++) {
			try {
				Float.parseFloat(lineSplit[i]);
				count++;
			} catch (Exception e) {

			}
		}
		return count;
	}
	
}
