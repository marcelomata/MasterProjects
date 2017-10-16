package evaluation_reorganized;

import java.io.IOException;
import java.util.Map;

import evaluation2.StatisticalMatrix;

public class CalcCovariance extends ComparisonAttributes {

	public static void main(String[] args) throws IOException {
		int numberOfIndex = Utils.getNumberOfIndex();
		for (int index = 1; index <= numberOfIndex; index++) {
			Map<String, double[][][][]> fieldsMeasurements = getFields(index);
			double correlation = getCorrelationByIndex(fieldsMeasurements, index);
			System.out.println(index + " - " + correlation);
		}
		int []indexToPlot = {49};
		for (int i = 0; i < indexToPlot.length; i++) {
			PlotUtils.plotByFieldIndex(getFields(indexToPlot[i]), indexToPlot[i]);
		}
	}
	
	public static double getCorrelationByIndex(Map<String, double[][][][]> squareDiffs, int index) {
		Double [][][]valuesByIndex = Utils.getValuesByIndex(squareDiffs, index);
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
	
}
