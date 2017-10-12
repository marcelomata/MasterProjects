package evaluation2;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class StatisticalMatrix {
	
	public static double[][] getVariance(Double[][] data, double meanByIndex[], int takeOutValues) {
		double[][] variance = new double[data.length][data[0].length - takeOutValues];
		for (int i = 0; i < variance.length; i++) {
			for (int j = 0; j < variance[i].length - takeOutValues; j++) {
				variance[i][j] = data[i][j] - meanByIndex[i];
			}
		}
		return variance;
	}
	
	public static double[][] getCovarianceMatrix(double[][] data) {
		RealMatrix realData = MatrixUtils.createRealMatrix(data);
		RealMatrix realDataTransposed = realData.transpose();
		RealMatrix covarianceMatrix = realData.multiply(realDataTransposed);
		double [][]covarianceMatrixResult = covarianceMatrix.getData();
		for (int i = 0; i < covarianceMatrixResult.length; i++) {
			for (int j = 0; j < covarianceMatrixResult[i].length; j++) {
				covarianceMatrixResult[i][j] = covarianceMatrixResult[i][j] / (double)(data[0].length - 1);
			}
		}
		return covarianceMatrixResult;
	}

	public static double getCorrelation2D(double[][] covarianceMatrix) {
		return covarianceMatrix[0][1] / Math.sqrt(covarianceMatrix[0][0] * covarianceMatrix[1][1]);
	}
	
}
