package evaluation_reorganized;

import java.io.File;
import java.io.IOException;
import java.util.Set;

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

}
