import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.IIOImage;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageIOHelper;

public class TestOCRPdfHumphrey {
	
	private static final String datapath = ".";
	
	public static void main(String[] args) {
		ITesseract instance = new Tesseract();
        instance.setDatapath(new File(datapath).getPath());
		File imageFile = new File("./humphreyReports/Marcelo/", "MAR_20151208_164138_OD_74040671_SFA.pdf");
        List<IIOImage> imageList;
        String result = "";
		try {
			imageList = ImageIOHelper.getIIOImageList(imageFile);
			result = instance.doOCR(imageList, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}

}
