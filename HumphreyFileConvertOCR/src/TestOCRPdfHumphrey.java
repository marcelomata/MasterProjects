import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Config;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import ocr.OCR;
import ocr.OCRBuilder;
import ocr.feature_extraction.FEMethod;
import ocr.feature_extraction.FeatureExtractionBuilder;
import ocr.feature_extraction.HorizontalCelledProjection;
import ocr.feature_extraction.HorizontalProjectionHistogram;
import ocr.feature_extraction.LocalLineFitting;
import ocr.feature_extraction.VerticalCelledProjection;
import ocr.feature_extraction.VerticalProjectionHistogram;
import ocr.neural_network.NeuralNetworkBuilder;

public class TestOCRPdfHumphrey {
	
	private static final String datapath = ".";
	
	public static void main(String[] args) {
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
//		File imageFile = new File("./humphreyReports/Marcelo/", "MAR_20151208_164138_OD_74040671_SFA.pdf");
//		File imageFile = new File("./humphreyReports/Marcelo/", "Test02.png");
		File imageFile = new File("./humphreyReports/Marcelo/", "TestGen33.png");
		
		//Read line 12 -> 4 first numbers
		//Read line 13 -> 6 first numbers
		//Read line 14 -> 8 first numbers
		//Read line 15 -> 8 first numbers
//        List<IIOImage> imageList;
		BufferedImage bi;
        String result = "";
//        OCR ocr = OCRBuilder
//                .create()
//                .featureExtraction(
//                    FeatureExtractionBuilder
//                        .create()
//                        .children(new FEMethod[] {
//                                  new HorizontalCelledProjection(5),
//                                  new VerticalCelledProjection(5),
//                                  new HorizontalProjectionHistogram(),
//                                  new VerticalProjectionHistogram(),
//                                  new LocalLineFitting(49)})
//                        .build()
//                    )
//                .neuralNetwork(
//                    NeuralNetworkBuilder
//                        .create()
//                        .fromFile(new File("./resources/"+Config.NN_FILENAME).getAbsolutePath())
//                        .build()
//                    )
//                .build();
		try {
//			imageList = ImageIOHelper.getIIOImageList(imageFile);
//			result = instance.doOCR(imageList, null);
			bi = ImageIO.read(imageFile);
			result = instance.doOCR(bi);
//			result = ocr.identify(bi.getSubimage(0, 0, 50, 50));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

}
