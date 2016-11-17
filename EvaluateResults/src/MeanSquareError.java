
public class MeanSquareError {
	
	public static double meanSquareEror(double[][] field1, double[][] field2, boolean leftSide) {
		double mse = 0;
		
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT; 
		
		int count = 0;
		for (int i = 0; i < field2.length; i++) {
			for (int j = 0; j < field2[i].length; j++) {
				if(map[i][j] == 'y') {
					mse += (field1[i][j]*field1[i][j]) - (field2[i][j]*field2[i][j]);
					count++;
				}
			}
		}
		mse /= count;
		if(mse < 0) 
			return -Math.sqrt(-mse);
		else 
			return Math.sqrt(mse);
	}
	
	
	
	

}
