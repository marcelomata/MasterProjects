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
	
	public static void printFieldAsArrayVariable(double[][] intensitiesAsDouble, String name) {
		System.out.println("public static double[][] " + name + " = {");
		for (int i = 0; i < intensitiesAsDouble.length; i++) {
			System.out.print("{");
			for (int j = 0; j < intensitiesAsDouble[i].length; j++) {
				System.out.print(intensitiesAsDouble[i][j]);
				if(j < intensitiesAsDouble[i].length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println("},");
		}
		System.out.println("};");
	}

	public static void printSeparator() {
		System.out.println("##########################");
		System.out.println("##########################");
		System.out.println("##########################");
		System.out.println("##########################");
	}

	public static void printFieldInt(double[][] intensitiesAsDouble) {
		for (int i = 0; i < intensitiesAsDouble.length; i++) {
			for (int j = 0; j < intensitiesAsDouble[i].length; j++) {
				System.out.print((int)intensitiesAsDouble[i][j]+" ");
			}
			System.out.println();
		}
	}

}
