

public class Sample2D implements Comparable<Sample2D> {
	
	private double x;
	private double y;
	
	public Sample2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double[] getSampleAsVector() {
		return new double[]{x, y};
	}

	@Override
	public int compareTo(Sample2D o) {
		if(x > o.getX()) {
			return 1;
		} else if(x < o.getX()) {
			return -1;
		}
		return 0;
	}

}
