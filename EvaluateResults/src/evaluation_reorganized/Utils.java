package evaluation_reorganized;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import evaluation2.Constants;
import evaluation2.FileType;

public class Utils {
	
	public static FileType getFileType(File file) {
		String name = file.getName();
		String type = name.substring(name.length()-3);
		
		if(type.equalsIgnoreCase(FileType.PDF.toString())) {
			return FileType.PDF;
		} else if(type.equalsIgnoreCase(FileType.XML.toString())) {
			return FileType.XML;
		} else {
			return FileType.TXT;
		}
	}
	
	public static String getHumphreyKeyByPrototypeKey(String key, Set<String> keysHumphrey) {
		String namesP[] = key.split(" ");
		String namesH[] = null;
		for (String string : keysHumphrey) {
			namesH = string.split(" ");
			if(namesH[0].toLowerCase().contains(key.split(" ")[0].toLowerCase())) {
				if(namesH.length > 1 && namesP.length > 1) {
					if(namesH[1].toLowerCase().equalsIgnoreCase("ufrgs") || namesH[1].toLowerCase().charAt(0) == namesP[1].toLowerCase().charAt(0)) {
//						System.out.println("\n" + string + " contains " + key);
						return string;
					}
				} else {
//					System.out.println("\n" + string + " contains " + key);
					return string;
				}
			}
			
		}
		return "";
	}

	public static int getNumberValidPoints(char[][] map) {
		int count = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if(map[i][j] == 'y') {
					count++;
				}
			}
		}
		return count;
	}
	
	public static File setEvaluationFile(String patient, File evaluationDir) throws IOException {
		String evaluationName = evaluationDir.getAbsolutePath()+"/"+patient+"_Prototype.txt";
		File evaluationFile = new File(evaluationName);
		if(!evaluationFile.exists()) {
			evaluationFile.createNewFile();
		}
		return evaluationFile;
	}
	
	public static Double[][][] getValuesByIndex(Map<String, double[][][][]> allFields, int index) {
		char[][] map = Constants.MAP_LEFT;
		int countIndex = 0;
		int count = 0;
		int countIntern = 0;
		Set<String> keys = allFields.keySet();
		double[][][] field = null;
		double[][][][] fieldsAll = null;
//		int numberPoints = Utils.getNumberValidPoints(map);
		int numberOfPointsToPlot = keys.size()*4;
		Double [][]xDataTotal1 = new Double[2][numberOfPointsToPlot];
		Double [][]yDataTotal1 = new Double[2][numberOfPointsToPlot];
		int indexInsert = 0;
		for (String string : keys) {
			if(allFields.get(string) != null) {
				fieldsAll = allFields.get(string);
				for(int k = 0; k < fieldsAll.length; k++) {
					System.out.println("################");
					field = fieldsAll[k];
					countIntern = count*4;
					for(int m = 0; m < field.length; m++) {
						map = getMap(m);
						countIndex = 0;
						for (int i = 0; i < field[m].length; i++) {
							for (int j = 0; j < field[m][i].length; j++) {
								if(map[i][j] == 'y') {
									countIndex++;
									if(countIndex == index) {
										indexInsert = countIntern;
										xDataTotal1[k][indexInsert] = (double)indexInsert;
										yDataTotal1[k][indexInsert] = field[m][i][j];
										if(k ==1) {
											System.out.println(field[m][i][j]);
										}
										countIntern++;
									}
								}
							}
						}
					}
				}
				count++;
			} else {
				System.out.println("Less one patient");
			}
		}
		
		return new Double[][][] {xDataTotal1, yDataTotal1};
	}
	
	private static char[][] getMap(int m) {
		if(m < 2) {
			return Constants.MAP_LEFT;
		} else {
			return Constants.MAP_RIGHT;
		}
	}

	public static double getValueByIndex(double[][] field, int index, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		double result = 0.0;
		int currentIndex = 0;
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if(map[i][j] == 'y') {
					currentIndex++;
					if(currentIndex == index) {
						return field[i][j];
					}
				}
			}
		}
		return result;
	}
	
	protected static int setNullAsZero(Double[][] dataTotal) {
		int count = 0; 
		for (int i = 0; i < dataTotal.length; i++) {
			for (int j = 0; j < dataTotal[i].length; j++) {
				if(dataTotal[i][j] == null) {
					dataTotal[i][j] = 0.0;
					count++;
				}
			}
		}
		return count;
	}
}
