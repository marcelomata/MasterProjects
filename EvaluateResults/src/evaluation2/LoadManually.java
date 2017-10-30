package evaluation2;

public class LoadManually {
	
	public static double[][] intensitiesMathiasRight1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 28, 26, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 28, 31, 33, 30, 28, 31, 0.0, 0.0},
		{0.0, 30, 33, 30, 33, 31, 30, 32, 26, 0.0},
		{0.0, 28, 28, 34, 34, 34, 33, 19, 35, 0.0},
		{0.0, 29, 33, 34, 34, 34, 32, 30, 32, 0.0},
		{0.0, 29, 32, 30, 31, 31, 32, 30, 15, 0.0},
		{0.0, 0.0, 32, 33, 30, 32, 34, 31, 0.0, 0.0},
		{0.0, 0.0, 0.0, 32, 27, 28, 32, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesMathiasRight2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};

	
	public static void main(String[] args) {
		calcReports();
	}
	
	public static void calcReports() {
		double mse = StatisticsComparation.rootMeanSquareEror(intensitiesMathiasRight1, intensitiesMathiasRight2, true);
		System.out.println(mse+",");
	}
	
	public static double[][] intensitiesBernardoHumphreyLeft1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesBernardoHumphreyLeft2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesBernardoHumphreyRight1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesBernardoHumphreyRight2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesLizethHumphreyLeft1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesLizethHumphreyLeft2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesLizethHumphreyRight1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesLizethHumphreyRight2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesJoseHumphreyLeft1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesJoseHumphreyLeft2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesJoseHumphreyRight1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesJoseHumphreyRight2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesViniciusMHumphreyLeft1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesViniciusMHumphreyLeft2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesViniciusMHumphreyRight1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesViniciusMHumphreyRight2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesDennisHumphreyLeft1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesDennisHumphreyLeft2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesDennisHumphreyRight1 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
	
	public static double[][] intensitiesDennisHumphreyRight2 =
		{
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 22, 22, 23, 26, 0.0, 0.0, 0.0},
		{0.0, 0.0, 26, 24, 28, 26, 26, 25, 0.0, 0.0},
		{0.0, 23, 31, 26, 26, 25, 27, 25, 20, 0.0},
		{0.0, 24, 28, 29, 30, 29, 28, 30, 35, 0.0},
		{0.0, 23, 28, 29, 29, 29, 21, 30, 34, 0.0},
		{0.0, 21, 25, 24, 26, 23, 23, 20, 17, 0.0},
		{0.0, 0.0, 29, 31, 28, 25, 30, 22, 0.0, 0.0},
		{0.0, 0.0, 0.0, 21, 21, 19, 21, 0.0, 0.0, 0.0},
		{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
		};
}
