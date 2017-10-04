package evaluation_reorganized;

public class PrintUtils {
	
	public static void printField(double[][] intensitiesAsDouble) {
		for (int i = 0; i < intensitiesAsDouble.length; i++) {
			for (int j = 0; j < intensitiesAsDouble[i].length; j++) {
				System.out.print(intensitiesAsDouble[i][j]+" ");
			}
			System.out.println();
		}
	}

}
