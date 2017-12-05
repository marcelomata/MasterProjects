package evaluation_reorganized;

public class PrintMeanDeviationAndVariance extends ComparisonAttributes {
	
	public static void main(String[] args) {
		setUpAttributes();
		PrintUtils.printFieldAsArrayVariable(variancePrototype, "variance");
		PrintUtils.printFieldAsArrayVariable(means, "mean");
	}

}
