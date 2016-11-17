package main;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.PdfUtilities;

public class ConversorOCRHumphreyPdf {
	
	public static final String datapath = ".";
	
	public static double[][] getIntensitiesMapFromPdf(File imageFile) {
		double [][] intensities = new double[10][10]; 
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 570;
		int endX = 1220;
		int startY = 850;
		int endY = 1480;
		int verticalMiddleStep = 12;
		int row = 8;
		int column = 9;
		int verticalStep = (endY - startY - verticalMiddleStep) / row;
		int horizontalStep = (endX - startX) / column;
		int newX = 0;
		int newY = 0;
		int vStep = verticalStep;
		int hStep = horizontalStep;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
        String result = "";
        String temp = "";
		try {
			BufferedImage bi = ImageIO.read(files[0]);
			BufferedImage intensitiesMap = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			setPixelsWhite(intensitiesMap);
			ImageIO.write(intensitiesMap, "png", new File(dir+"/intensitiesMap.png"));
			for (int i = 0, k = 1; i < row+1; i++) {
				if(i == 3) {
					vStep = verticalMiddleStep;
				} else {
					vStep = verticalStep;
				}
				for (int j = 0; j < column; j++) {
					ImageIO.write(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep), "png", new File(dir+"/"+i+j+".png"));
					if(i != 3) {
						temp = instance.doOCR(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep)).trim().replaceAll("\n", "");
						temp = removeSimbols(temp);
						if(temp.length() > 0) {
							intensities[k][j] = Double.parseDouble(temp);
						}
						result += temp+" ";
					}
					newX += hStep;
				}
				if(i != 3) {
					result += "\n";
					k++;
				}
				newY += vStep;
				newX = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
		return intensities;
	}

	public static String removeSimbols(String temp) {
		String newString = "";
		char c = ' ';
		for (int i = 0; i < temp.length(); i++) {
			c = temp.charAt(i);
			if (c == 79) {
				c = 48;
			}
			if (c == 39) {
				c = 45;
				newString += c;
			}
			if(c >= 48 && c <= 57) {
				newString += c;
			}
		}
		return newString;
	}

	public static void setPixelsWhite(BufferedImage intensitiesMap) {
		int startX = 360;
		int endX = 380;
		int startY = 295;
		int endY = 325;
		int width = intensitiesMap.getWidth();
		int height = intensitiesMap.getHeight();
		
		//remove horizontal line 
		for (int i = 0; i < width; i++) {
			for (int j = startY; j < endY; j++) {
				intensitiesMap.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
		
		//remove vertical line 
		for (int i = startX; i < endX; i++) {
			for (int j = 0; j < height; j++) {
				intensitiesMap.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
	}
	
	public static void setPixelsWhite(BufferedImage intensitiesMap, int startX, int startY, int endX, int endY) {
		int width = intensitiesMap.getWidth();
		int height = intensitiesMap.getHeight();
		
		//remove horizontal line 
		for (int i = 0; i < width; i++) {
			for (int j = startY; j < endY; j++) {
				intensitiesMap.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
		
		//remove vertical line 
		for (int i = startX; i < endX; i++) {
			for (int j = 0; j < height; j++) {
				intensitiesMap.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
	}
	
	public static String getPatientNameFromPdf(File imageFile) {
		String name = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 365;
		int endX = 1745;
		int startY = 300;
		int endY = 340;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			name = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(name);
		return name;
	}
	
	public static int getPatientAgeFromPdf(File imageFile) {
		int age = 0;
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1960;
		int endX = 2050;
		int startY = 600;
		int endY = 630;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			age = Integer.parseInt(instance.doOCR(nameBI).trim().replaceAll("\n", ""));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(age);
		return age;
	}
	
	public static String getEyeSideFromPdf(File imageFile) {
		String eyeSide = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1840;
		int endX = 2020;
		int startY = 240;
		int endY = 285;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	public static String getDateFromPdf(File imageFile) {
		String date = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1950;
		int endX = 2165;
		int startY = 490;
		int endY = 530;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			date = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(date);
		return date;
	}
	
	public static String getHourFromPdf(File imageFile) {
		String hour = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1970;
		int endX = 2070;
		int startY = 545;
		int endY = 580;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			hour = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(hour);
		return hour;
	}
	
	public static String getDurationFromPdf(File imageFile) {
		String duration = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 520;
		int endX = 625;
		int startY = 760;
		int endY = 800;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			duration = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(duration);
		return duration;
	}
	
	public static String getFalseNegativeFromPdf(File imageFile) {
		String falseNegative = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 525;
		int endX = 640;
		int startY = 710;
		int endY = 745;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			falseNegative = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(falseNegative);
		return falseNegative;
	}
	
	public static String getFalsePositiveFromPdf(File imageFile) {
		String falsePositive = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 525;
		int endX = 680;
		int startY = 655;
		int endY = 695;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			falsePositive = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(falsePositive);
		return falsePositive;
	}
	
	public static String getFixationLosingFromPdf(File imageFile) {
		String fixationLossing = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 535;
		int endX = 625;
		int startY = 600;
		int endY = 640;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			fixationLossing = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(fixationLossing);
		return fixationLossing;
	}
	
	public static String getBirthdayFromPdf(File imageFile) {
		String birthday = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1835;
		int endX = 2060;
		int startY = 300;
		int endY = 340;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			birthday = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(birthday);
		return birthday;
	}
	
	public static String getGHTFromPdf(File imageFile) {
		String ght = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1670;
		int endX = 2200;
		int startY = 1785;
		int endY = 1835;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			ght = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(ght);
		return ght;
	}
	
	public static double getVFIFromPdf(File imageFile) {
		String vhi = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1750;
		int endX = 2020;
		int startY = 1890;
		int endY = 1940;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			vhi = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(ght);
		return Double.parseDouble(vhi.replaceAll("%", "").replaceAll("/+", "").replaceAll("O", "0").trim());
	}
	
	public static double getMDFromPdf(File imageFile) {
		String md = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1750;
		int endX = 2020;
		int startY = 1970;
		int endY = 2020;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			md = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(md);
		md = md.split("dB")[0].replaceAll("/+", "");
		return Double.parseDouble(md.replaceAll("O", "0").trim());
	}
	
	public static double getPSDFromPdf(File imageFile) {
		String psd = "";
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1750;
		int endX = 2020;
		int startY = 2025;
		int endY = 2080;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(files[0]);
			BufferedImage nameBI = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			psd = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
//		System.out.println(ght);
		psd = psd.split("dB")[0];
		return Double.parseDouble(psd.replaceAll("/+", "").replaceAll("O", "0").trim());
	}
	
	public static double[][] getTotalDeviationMapFromPdf(File imageFile) {
		double [][] deviations = new double[10][10]; 
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 335;
		int endX = 780;
		int startY = 2235;
		int endY = 2665;
		int row = 8;
		int column = 9;
		int verticalStep = (endY - startY) / row;
		int horizontalStep = (endX - startX) / column;
		int newX = 0;
		int newY = 0;
		int vStep = verticalStep;
		int hStep = horizontalStep;
		String result = "";
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		try {
			BufferedImage bi = ImageIO.read(files[0]);
			BufferedImage intensitiesMap = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			int startX2 = 240;
			int endX2 = 260;
			int startY2 = 210;
			int endY2 = 225;
			setPixelsWhite(intensitiesMap, startX2, startY2, endX2, endY2);
			ImageIO.write(intensitiesMap, "png", new File(dir+"/totalDeviationPercentMap.png"));
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					deviations[i+1][j] = getDeviationByBlackPixels(intensitiesMap.getSubimage(newX, newY, hStep, vStep));
					result += deviations[i+1][j] + " ";
					ImageIO.write(intensitiesMap.getSubimage(newX, newY, hStep, vStep), "png", new File(dir+"/"+i+j+".png"));
					newX += hStep;
				}
				result += "\n";
				newY += vStep;
				newX = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
		return deviations;
	}

	public static double getDeviationByBlackPixels(BufferedImage subimage) {
		int width = subimage.getWidth();
		int height = subimage.getHeight();
		int totalPixels = width * height;
		int numBlackPixels = 0;
		
		Color c;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				c = new Color(subimage.getRGB(i, j));
				if(c.getRed() == 0) {
					numBlackPixels++;
				}
			}
		}
		
		double percentage = (double)numBlackPixels / (double)totalPixels;
//		System.out.println(percentage);
		
		if(percentage == 0) {
			return 0;
		} else if(percentage < 0.01) {
			return 50;
		} else if(percentage < 0.05) {
			return 5;
		} else if(percentage < 0.15) {
			return 2;
		} else if(percentage < 0.2) {
			return 1;
		} else if(percentage < 0.3) {
			return 0.5;
		}
		
		return 0;
	}
	
	public static double[][] getNumericTotalDeviationMapFromPdf(File imageFile) {
		double [][] intensities = new double[10][10]; 
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 365;
		int endX = 785;
		int startY = 1595;
		int endY = 2020;
		int row = 8;
		int column = 9;
		int verticalStep = (endY - startY) / row;
		int horizontalStep = (endX - startX) / column;
		int newX = 0;
		int newY = 0;
		int vStep = verticalStep;
		int hStep = horizontalStep;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
        String result = "";
        String temp = "";
		try {
			BufferedImage bi = ImageIO.read(files[0]);
			BufferedImage intensitiesMap = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			int startX2 = 222;
			int endX2 = 230;
			int startY2 = 205;
			int endY2 = 215;
			setPixelsWhite(intensitiesMap, startX2, startY2, endX2, endY2);
			ImageIO.write(intensitiesMap, "png", new File(dir+"/totalDeviationNumericMap.png"));
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					ImageIO.write(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep), "png", new File(dir+"/"+i+j+".png"));
					temp = instance.doOCR(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep)).trim().replaceAll("\n", "");
					temp = removeSimbols(temp);
					if(temp.length() > 0) {
						intensities[i+1][j] = Double.parseDouble(temp);
					}
					result += temp+" ";
					newX += hStep;
				}
				result += "\n";
				newY += vStep;
				newX = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
		return intensities;
	}
	
	public static double[][] getNumericModelDeviationMapFromPdf(File imageFile, char[][] map) {
		double [][] intensities = new double[10][10]; 
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1130;
		int endX = 1555;
		int startY = 1595;
		int endY = 2020;
		int row = 8;
		int column = 9;
		int verticalStep = (endY - startY) / row;
		int horizontalStep = (endX - startX) / column;
		int newX = 0;
		int newY = 0;
		int vStep = verticalStep;
		int hStep = horizontalStep;
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
        String result = "";
        String temp = "";
		try {
			BufferedImage bi = ImageIO.read(files[0]);
			BufferedImage intensitiesMap = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			int startX2 = 190;
			int endX2 = 195;
			int startY2 = 205;
			int endY2 = 215;
			setPixelsWhite(intensitiesMap, startX2, startY2, endX2, endY2);
			ImageIO.write(intensitiesMap, "png", new File(dir+"/modelDeviationNumericMap.png"));
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					ImageIO.write(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep), "png", new File(dir+"/"+i+j+".png"));
					temp = instance.doOCR(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep)).trim().replaceAll("\n", "");
					temp = removeSimbols(temp);
					if(temp.length() > 0) {
						try {
							intensities[i+1][j+1] = Double.parseDouble(temp);
						} catch (Exception e) {
							System.out.println("Out of pattern -> " + temp);
							System.out.println(imageFile.getParentFile().getName());
							return null;
						}
					} else if(map[i+1][j+1] == 'y') {
						System.out.println("Out of pattern -> " + temp);
						System.out.println(imageFile.getParentFile().getName());
						return null;
					}
					result += temp+" ";
					newX += hStep;
				}
				result += "\n";
				newY += vStep;
				newX = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
		return intensities;
	}
	
	public static double[][] getModelDeviationMapFromPdf(File imageFile) {
		double [][] deviations = new double[10][10]; 
		File[] files = PdfUtilities.convertPdf2Png(imageFile);
		File dir = new File(imageFile.getParent()+"/"+imageFile.getName().substring(0, imageFile.getName().length()-4));
		dir.mkdirs();
		int startX = 1070;
		int endX = 1515;
		int startY = 2235;
		int endY = 2665;
		int row = 8;
		int column = 9;
		int verticalStep = (endY - startY) / row;
		int horizontalStep = (endX - startX) / column;
		int newX = 0;
		int newY = 0;
		int vStep = verticalStep;
		int hStep = horizontalStep;
		String result = "";
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		try {
			BufferedImage bi = ImageIO.read(files[0]);
			BufferedImage intensitiesMap = bi.getSubimage(startX, startY, endX - startX, endY - startY);
			int startX2 = 240;
			int endX2 = 260;
			int startY2 = 210;
			int endY2 = 225;
			setPixelsWhite(intensitiesMap, startX2, startY2, endX2, endY2);
			ImageIO.write(intensitiesMap, "png", new File(dir+"/modelDeviationPercentMap.png"));
			for (int i = 0, k = 1; i < row; i++) {
				for (int j = 0; j < column; j++) {
					deviations[k][j] = getDeviationByBlackPixels(intensitiesMap.getSubimage(newX, newY, hStep, vStep));
					result += deviations[k][j] + " ";
					ImageIO.write(intensitiesMap.getSubimage(newX, newY, hStep, vStep), "png", new File(dir+"/"+i+j+".png"));
					newX += hStep;
				}
				result += "\n";
				k++;
				newY += vStep;
				newX = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
		return deviations;
	}

}
