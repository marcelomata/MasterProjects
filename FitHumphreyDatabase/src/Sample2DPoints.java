
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Sample2DPoints {
	
	private List<Sample2D> xyDataTotalDeviation;
	private List<Sample2D> xyDataModelDeviation;
	private boolean isModelOk;
	
	public Sample2DPoints() {
		this.xyDataTotalDeviation = new ArrayList<Sample2D>();
		this.xyDataModelDeviation = new ArrayList<Sample2D>();
	}
	
	public void addSampleTotalDeviation(Sample2D sample) {
		this.xyDataTotalDeviation.add(sample);
	}
	
	public void addSampleModelDeviation(Sample2D sample) {
		this.xyDataModelDeviation.add(sample);
	}

	public double[][] getTotalDeviationAsArray() {
		Collections.sort(xyDataTotalDeviation);
		return getListAsArray(xyDataTotalDeviation);
	}
	
	public double[][] getModelDeviationAsArray() {
		Collections.sort(xyDataModelDeviation);
		return getListAsArray(xyDataModelDeviation);
	}
	
	private double[][] getListAsArray(List<Sample2D> xyDataTotalDeviation2) {
		double[][] result = new double[xyDataTotalDeviation2.size()][2];
		Sample2D sample;
		for (int i = 0; i < result.length; i++) {
			sample = xyDataTotalDeviation2.get(i);
			result[i][0] = sample.getX();
			result[i][1] = sample.getY();
		}
		return result;
	}

	public void setIsModelOk(boolean b) {
		this.isModelOk = b;
	}
	
	public boolean isModelOk() {
		return isModelOk;
	}

}
