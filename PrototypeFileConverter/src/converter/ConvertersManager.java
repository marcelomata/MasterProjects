package converter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.Parameters;
import br.ufrgs.campimeter.examination.visualfield.VisualField;
import br.ufrgs.campimeter.examination.visualfield.file.ExaminationInformations;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import br.ufrgs.campimeter.examination.visualfield.file.Parser;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFile;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV0_1;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV1_0;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileParserV0_1;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileParserV1_0;



public class ConvertersManager {
	
	public void convertAllFiles(File source, File target, FileVersion sourceVersion, FileVersion targetVersion) {
		
		List<File> files = loadFiles(source);
		
		LoaderVisualField loader = null;
		Parser parser = null;
		VisualField vfl = null;
		VisualField vfr = null;
		Parameters pl = null;
		Parameters pr = null;
		String dirConverted = target.getAbsolutePath();
		
		ExaminationInformations informations;
		EnumEye eye;
		for (File file : files) {
			informations = new ExaminationInformations();
			eye = file.getName().contains("Left") ? EnumEye.LEFT : EnumEye.RIGHT;
			informations.setCurrentEye(eye);
			switch (sourceVersion) {
			case v0_1:
				loader = new VisualFieldFileLoaderV0_1(file.getName(), file.getParent(), informations);
				break;
			case v1_0:
				loader = new VisualFieldFileLoaderV1_0(file.getName(), file.getParent(), informations);
				break;
			}
			loader.load();
			vfl = loader.getVisualField();
			vfr = loader.getVisualField();
			pl = loader.getParameters();
			pr = loader.getParameters();
			
			switch (targetVersion) {
			case v0_1:
				dirConverted += "/Version/0_1/";
				parser = new VisualFieldFileParserV0_1(vfl, vfr, pl, pr, informations);
				break;
			case v1_0:
				dirConverted += "/Version/1_0/";
				if(eye == EnumEye.LEFT) {
					parser = new VisualFieldFileParserV1_0(vfl, pl, informations);
				} else {
					parser = new VisualFieldFileParserV1_0(vfr, pr, informations);
				}
				break;
			}
			
			File dirConvertedCreate = new File(dirConverted);
			if(!dirConvertedCreate.exists()) {
				dirConvertedCreate.mkdirs();
			}
			
			((VisualFieldFile)parser).setDataFilesDir(dirConverted);
			parser.parse();
			dirConverted = target.getAbsolutePath();
		}
		
	}

	private static List<File> loadFiles(File source) {
		File[] files = source.listFiles();
		List<File> resultFiles = new ArrayList<File>();
		
		for (File file : files) {
			if(file.isDirectory()) {
				resultFiles.addAll(loadFiles(file));
			} else {
				resultFiles.add(file);
			}
		}
		
		return resultFiles;
	}
	

}
