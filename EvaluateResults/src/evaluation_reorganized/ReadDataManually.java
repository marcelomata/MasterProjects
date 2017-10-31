package evaluation_reorganized;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadDataManually {
	
	public static void main(String[] args) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/home/cin_moliveira/Downloads/gtemp/master/reports/humphrey_measurements_bernardo.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/humphrey_measurements_bernardo.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/humphrey_measurements_lizeth.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/humphrey_measurements_dennis.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/humphrey_measurements_jose.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/humphrey_measurements_vinicius_m.txt"))));
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/prototype_measurements_bernardo.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/prototype_measurements_lizeth.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/prototype_measurements_dennis.txt"))));
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/prototype_measurements_jose.txt"))));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/cygwin64/home/marce/git/master/reports/prototype_measurements_vinicius_m.txt"))));
		
	    double [][]field = new double[10][10];
	    boolean left = false;
	    int countNumbers = 0;
	    int startPosSplit = 0;
	    int startPos = 0;
	    int lineIndex = 1;
	    int countPrint = 0;
		while(br.ready()) {
			countNumbers = 0;
			String line = br.readLine();
			String lineSplit[] = line.split("\t");
			if(lineSplit.length > 1) {
				countNumbers = countNumbers(lineSplit);
				startPosSplit = lineSplit.length - countNumbers;
				startPos = (10 - countNumbers) / 2;
				System.out.print("{");
				for (int i = 0; i < startPos; i++) {
					System.out.print("0.0, ");
				}
				for (int i = startPosSplit; i < lineSplit.length; i++) {
					field[lineIndex][startPos] = Float.parseFloat(lineSplit[i]);
					System.out.print(field[lineIndex][startPos]+", ");
				}
				for (int i = 0; i < startPos; i++) {
					if(i == startPos - 1) {
						System.out.print("0.0");
					} else {
						System.out.print("0.0, ");
					}
				}
				System.out.println("},");
				lineIndex++;
			} else if(lineSplit.length == 1) {
				lineIndex = 1;
				left = !left;
				if(countPrint != 0) {
					System.out.println("{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},");
					System.out.println("};\n");
				}
				countPrint++;
				if(countPrint <= 4) {
//					System.out.println(left ? "Esquedo" : "Direito");
					System.out.println(left ? "public static double[][] intensitiesViniciusMPrototypeLeft = {" : "public static double[][] intensitiesViniciusMPrototypeRight = {");
					System.out.println("{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},");
				}
			} else {
				lineIndex = 1;
				continue;
			}
		}
	}
	
	public static int countNumbers(String[] lineSplit) {
		int count = 0;
		for (int i = 0; i < lineSplit.length; i++) {
			try {
				Float.parseFloat(lineSplit[i]);
				count++;
			} catch (Exception e) {

			}
		}
		return count;
	}

}
