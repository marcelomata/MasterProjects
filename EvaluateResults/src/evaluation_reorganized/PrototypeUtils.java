package evaluation_reorganized;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.ExaminationInformations;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderMap;
import br.ufrgs.campimeter.examination.visualfield.file.VisualFieldFileLoaderV1_0;
import evaluation2.FileType;

public class PrototypeUtils {
	
	public static List<LoaderVisualField> getReportPrototypeData(List<LoaderVisualField> list, EnumEye side) {
		List<LoaderVisualField> leftReportData = new ArrayList<LoaderVisualField>();
		for (LoaderVisualField reportData : list) {
			if(reportData.getInformations().getCurrentEye() == side) {
				leftReportData.add(reportData);
			}
		}
		return leftReportData;
	}

	public static Map<String, List<LoaderVisualField>> getReportsPrototypeByPatient(List<LoaderVisualField> reportsPrototypeData) {
		Map<String, List<LoaderVisualField>> reportsByPatient = new HashMap<String, List<LoaderVisualField>>(); 
		
		String patient;
		List<LoaderVisualField> reports;
		for(LoaderVisualField data : reportsPrototypeData) {
			patient = data.getInformations().getPatient().getName();
			reports = reportsByPatient.get(patient);
			if(reports == null) {
				reports = new ArrayList<LoaderVisualField>();
				reportsByPatient.put(patient, reports);
			}
			reports.add(data);
		}
		
		return reportsByPatient;
	}

	public static List<LoaderVisualField> loadReportsPrototype(File reportsPrototypeDir) {
		//List<VisualFieldFileLoaderV1_0> reportsData = new ArrayList<VisualFieldFileLoaderV1_0>();
		List<LoaderVisualField> reportsData = new ArrayList<LoaderVisualField>();
		
		File[] folder = reportsPrototypeDir.listFiles();
		File[] files;
		LoaderVisualField loader;
		
		ExaminationInformations informations;
		EnumEye eye;
		for (int i = 0; i < folder.length; i++) {
			files = folder[i].listFiles();
			for (int j = 0; j < files.length; j++) {
				if(Utils.getFileType(files[j]) == FileType.TXT) {
					informations = new ExaminationInformations();
					eye = files[j].getName().contains("Left") ? EnumEye.LEFT : EnumEye.RIGHT;
					informations.setCurrentEye(eye);
					informations.getPatient().setName(folder[i].getName());
					if(files[j].getName().contains("xls")) {
						loader = new VisualFieldFileLoaderMap(files[j].getName(), files[j].getParentFile().getAbsolutePath(), informations);
						loader.load();
					} else {
						loader = new VisualFieldFileLoaderV1_0(files[j].getName(), files[j].getParentFile().getAbsolutePath(), informations);
						loader.load();
						if(loader.getInformations().getPatient().getName().trim().equalsIgnoreCase("Default")) {
							loader.getInformations().getPatient().setName(folder[i].getName());
						}
					}
					reportsData.add((LoaderVisualField) loader);
				}
			}
		}
		
		return reportsData;
	}

}
