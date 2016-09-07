import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GenerateNumberImages {
	
	private BufferedImage image;
	private BufferedImage[] numbers;
	
	public GenerateNumberImages(BufferedImage image) {
		this.image = image;
	}
	
	public void generateNumberImages() {
		int columns = 9;
		int rows = 8;
		numbers = new BufferedImage[columns*rows];
		
		int pixelColumn = 50;
		int pixelRow = 57;
		
		int startX = 0;
		int startY = 0;
		
		int imageCount = 0;
		
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				numbers[imageCount] = image.getSubimage(startX, startY, pixelColumn, pixelRow);
				try {
					ImageIO.write(numbers[imageCount], "png", new File("./humphreyReports/Marcelo/", "TestGen"+imageCount+".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				startY += pixelRow;
				imageCount++;
			}
			startY = 0;
			startX += pixelColumn;
		}
	}
	
	public static void main(String[] args) {
		File imageFile = new File("./humphreyReports/Marcelo/", "Test02.png");
		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GenerateNumberImages gen = new GenerateNumberImages(image);
		gen.generateNumberImages();
	}


}
