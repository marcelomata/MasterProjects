import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.PdfUtilities;

public class TestOCRPdfHumphreyOk {
	
	private static final String datapath = ".";
	
	public static void main(String[] args) {
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
        
		File imageFile = new File("./humphreyReports/Marcelo/", "MAR_20151208_164138_OD_74040671_SFA.pdf");
		
		//Read line 12 -> 4 first numbers
		//Read line 13 -> 6 first numbers
		//Read line 14 -> 8 first numbers
		//Read line 15 -> 8 first numbers
		double intensities[][] = getIntensitiesMapFromPdf(imageFile);
		String patientName = getPatientNameFromPdf(imageFile);
		int age = getPatientAgeFromPdf(imageFile);
		String eyeSide = getEyeSideFromPdf(imageFile);
		String date = getDateFromPdf(imageFile);
		String hour = getHourFromPdf(imageFile);
		String duration = getDurationFromPdf(imageFile);
		String faslseNegative = getFalseNegativeFromPdf(imageFile);
		String faslsePositive = getFalsePositiveFromPdf(imageFile);
		String fixationLosing = getFixationLosingFromPdf(imageFile);	
		String birthday = getBirthdayFromPdf(imageFile);
		
	}

	private static double[][] getIntensitiesMapFromPdf(File imageFile) {
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
//			ImageIO.write(intensitiesMap, "png", new File(dir+"/intensitiesMap.png"));
			for (int i = 0, k = 1; i < row+1; i++) {
				if(i == 3) {
					vStep = verticalMiddleStep;
				} else {
					vStep = verticalStep;
				}
				for (int j = 0; j < column; j++) {
//					ImageIO.write(intensitiesMap.getSubimage(newX, newY, hStep, verticalStep), "png", new File(dir+"/"+i+j+".png"));
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

	private static String removeSimbols(String temp) {
		String newString = "";
		char c = ' ';
		for (int i = 0; i < temp.length(); i++) {
			c = temp.charAt(i);
			if(c >= 48 && c <= 57) {
				newString += c;
			}
		}
		return newString;
	}

	private static void setPixelsWhite(BufferedImage intensitiesMap) {
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
	
	private static String getPatientNameFromPdf(File imageFile) {
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
		
		System.out.println(name);
		return name;
	}
	
	private static int getPatientAgeFromPdf(File imageFile) {
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
	
	private static String getEyeSideFromPdf(File imageFile) {
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
	
	private static String getDateFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	private static String getHourFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	private static String getDurationFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	private static String getFalseNegativeFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	private static String getFalsePositiveFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	private static String getFixationLosingFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}
	
	private static String getBirthdayFromPdf(File imageFile) {
		String eyeSide = "";
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
			eyeSide = instance.doOCR(nameBI).trim().replaceAll("\n", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(eyeSide);
		return eyeSide;
	}

}
