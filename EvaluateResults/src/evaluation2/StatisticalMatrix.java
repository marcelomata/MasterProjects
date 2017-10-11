package evaluation2;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import evaluation_reorganized.PrintUtils;

public class StatisticalMatrix {

	public static void main(String[] args) {
		
	}
	
	public static void calculateCovarianceMatrix() {
		double[][] data = new double[][]{
			  {1, 2, 3},
			  {2, 4, 6}
			};
		RealMatrix mx = MatrixUtils.createRealMatrix(data);
		RealMatrix cov = new Covariance(mx).getCovarianceMatrix();
		PrintUtils.printField(cov.getData());
		
	}
	
}
