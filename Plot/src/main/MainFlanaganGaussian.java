package main;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import flanagan.analysis.Regression;

public class MainFlanaganGaussian {
	
	/** Good data. */
    protected static final double[][] DATASET1 = new double[][] {
        {4.0254623,  531026.0},
        {4.02804905, 664002.0},
        {4.02934242, 787079.0},
        {4.03128248, 984167.0},
        {4.03386923, 1294546.0},
        {4.03580929, 1560230.0},
        {4.03839603, 1887233.0},
        {4.0396894,  2113240.0},
        {4.04162946, 2375211.0},
        {4.04421621, 2687152.0},
        {4.04550958, 2862644.0},
        {4.04744964, 3078898.0},
        {4.05003639, 3327238.0},
        {4.05132976, 3461228.0},
        {4.05326982, 3580526.0},
        {4.05585657, 3576946.0},
        {4.05779662, 3439750.0},
        {4.06038337, 3220296.0},
        {4.06167674, 3070073.0},
        {4.0636168,  2877648.0},
        {4.06620355, 2595848.0},
        {4.06749692, 2390157.0},
        {4.06943698, 2175960.0},
        {4.07202373, 1895104.0},
        {4.0733171,  1687576.0},
        {4.07525716, 1447024.0},
        {4.0778439,  1130879.0},
        {4.07978396, 904900.0},
        {4.08237071, 717104.0},
        {4.08366408, 620014.0}
    };

	public static void main(String[] args) {
		
		
		
		double[] xdata = new double[DATASET1.length];
		double[] ydata = new double[DATASET1.length];
		for (int i = 0; i < xdata.length; i++) {
			xdata[i] = DATASET1[i][0];
			ydata[i] = DATASET1[i][1];
		}
		
//		Regression reg = new Regression(xdata, ydata);
//		reg.setYscaleOption(false);
//		reg.supressYYplot();
//		reg.gaussianPlot();
//		System.out.println("a = "+reg.getBestEstimates()[0]+"; b = "+reg.getBestEstimates()[1]+"; c = "+reg.getBestEstimates()[2]);

		GaussianCurveFitter fitter = GaussianCurveFitter.create();
        double[] parameters = fitter.fit(createDataset(DATASET1).toList());
        
		System.out.println("a = "+parameters[0]+"; b = "+parameters[1]+"; c = "+parameters[2]);
		
		XyGui plot = new XyGui("Test intensity ", xdata, ydata);
		plot.plot();
		
		double[] ydata2 = new double[DATASET1.length];
		Gaussian gaussian = new Gaussian(parameters[0], parameters[1], parameters[2]);
		for (int i = 0; i < xdata.length; i++) {
//			ydata2[i] = (1/Math.sqrt(2*parameters[2]*parameters[2]*Math.PI)) * Math.exp(-(xdata[i] - parameters[1]) / (2*parameters[2]*parameters[2]));
			ydata2[i] = gaussian.value(xdata[i]);
		}
		XyGui plot2 = new XyGui("Test intensity 2", xdata, ydata2);
		plot2.plot();
		
		print(ydata);
		print(ydata2);
	}
	
	private static WeightedObservedPoints createDataset(double[][] points) {
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        for (int i = 0; i < points.length; i++) {
            obs.add(points[i][0], points[i][1]);
        }
        return obs;
    }
	
	public static void print(double[] data) {
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + " ");
		}
		System.out.println();
	}

}
