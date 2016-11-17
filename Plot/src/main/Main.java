package main;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class Main {
	
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
    /** Poor data: right of peak not symmetric with left of peak. */
    protected static final double[][] DATASET2 = new double[][] {
        {-20.15,   1523.0},
        {-19.65,   1566.0},
        {-19.15,   1592.0},
        {-18.65,   1927.0},
        {-18.15,   3089.0},
        {-17.65,   6068.0},
        {-17.15,  14239.0},
        {-16.65,  34124.0},
        {-16.15,  64097.0},
        {-15.65, 110352.0},
        {-15.15, 164742.0},
        {-14.65, 209499.0},
        {-14.15, 267274.0},
        {-13.65, 283290.0},
        {-13.15, 275363.0},
        {-12.65, 258014.0},
        {-12.15, 225000.0},
        {-11.65, 200000.0},
        {-11.15, 190000.0},
        {-10.65, 185000.0},
        {-10.15, 180000.0},
        { -9.65, 179000.0},
        { -9.15, 178000.0},
        { -8.65, 177000.0},
        { -8.15, 176000.0},
        { -7.65, 175000.0},
        { -7.15, 174000.0},
        { -6.65, 173000.0},
        { -6.15, 172000.0},
        { -5.65, 171000.0},
        { -5.15, 170000.0}
    };
    /** Poor data: long tails. */
    protected static final double[][] DATASET3 = new double[][] {
        {-90.15,   1513.0},
        {-80.15,   1514.0},
        {-70.15,   1513.0},
        {-60.15,   1514.0},
        {-50.15,   1513.0},
        {-40.15,   1514.0},
        {-30.15,   1513.0},
        {-20.15,   1523.0},
        {-19.65,   1566.0},
        {-19.15,   1592.0},
        {-18.65,   1927.0},
        {-18.15,   3089.0},
        {-17.65,   6068.0},
        {-17.15,  14239.0},
        {-16.65,  34124.0},
        {-16.15,  64097.0},
        {-15.65, 110352.0},
        {-15.15, 164742.0},
        {-14.65, 209499.0},
        {-14.15, 267274.0},
        {-13.65, 283290.0},
        {-13.15, 275363.0},
        {-12.65, 258014.0},
        {-12.15, 214073.0},
        {-11.65, 182244.0},
        {-11.15, 136419.0},
        {-10.65,  97823.0},
        {-10.15,  58930.0},
        { -9.65,  35404.0},
        { -9.15,  16120.0},
        { -8.65,   9823.0},
        { -8.15,   5064.0},
        { -7.65,   2575.0},
        { -7.15,   1642.0},
        { -6.65,   1101.0},
        { -6.15,    812.0},
        { -5.65,    690.0},
        { -5.15,    565.0},
        {  5.15,    564.0},
        { 15.15,    565.0},
        { 25.15,    564.0},
        { 35.15,    565.0},
        { 45.15,    564.0},
        { 55.15,    565.0},
        { 65.15,    564.0},
        { 75.15,    565.0}
    };
    /** Poor data: right of peak is missing. */
    protected static final double[][] DATASET4 = new double[][] {
        {-20.15,   1523.0},
        {-19.65,   1566.0},
        {-19.15,   1592.0},
        {-18.65,   1927.0},
        {-18.15,   3089.0},
        {-17.65,   6068.0},
        {-17.15,  14239.0},
        {-16.65,  34124.0},
        {-16.15,  64097.0},
        {-15.65, 110352.0},
        {-15.15, 164742.0},
        {-14.65, 209499.0},
        {-14.15, 267274.0},
        {-13.65, 283290.0}
    };
    /** Good data, but few points. */
    protected static final double[][] DATASET5 = new double[][] {
        {4.0254623,  531026.0},
        {4.03128248, 984167.0},
        {4.03839603, 1887233.0},
        {4.04421621, 2687152.0},
        {4.05132976, 3461228.0},
        {4.05326982, 3580526.0},
        {4.05779662, 3439750.0},
        {4.0636168,  2877648.0},
        {4.06943698, 2175960.0},
        {4.07525716, 1447024.0},
        {4.08237071, 717104.0},
        {4.08366408, 620014.0}
    };
	
	public static void main(String[] args) {
		GaussianCurveFitter fitter = GaussianCurveFitter.create();
        double[] parameters = fitter.fit(createDataset(DATASET1).toList());
        
	}
	
	private static WeightedObservedPoints createDataset(double[][] points) {
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        for (int i = 0; i < points.length; i++) {
            obs.add(points[i][0], points[i][1]);
        }
        return obs;
    }

}
