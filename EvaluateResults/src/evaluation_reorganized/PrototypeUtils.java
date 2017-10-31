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
import evaluation2.LoadManually;
import evaluation2.ReportData;

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
	
	public static List<String> getPatientsManually() {
		List<String> patientsManual = new ArrayList<String>();
		patientsManual.add("Lizeth");
		patientsManual.add("Bernardo");
		patientsManual.add("Jose");
		patientsManual.add("Dennis");
		patientsManual.add("Vinicius M");
		return patientsManual;
	}
	
	public static List<ReportData> getManualPatients() {
		List<ReportData> reports = new ArrayList<ReportData>();
		List<String> patients = getPatientsManually();
		for (int i = 0; i < patients.size(); i++) {
			for (int j = 0; j < 4; j++) {
				reports.add(new ReportData(patients.get(i), getIntensitiesByPatient(patients.get(i), j), j < 2 ? "Esquerdo" : "Direito"));
			}
		}
		return reports;
	}
	
	public static double[][] getIntensitiesByPatient(String patient, int field) {
		if(patient.toLowerCase().contains("lizeth" )) {
			switch (field) {
				case 0:
					return LoadManually.intensitiesLizethPrototypeLeft1;
				case 1:
					return LoadManually.intensitiesLizethPrototypeLeft2;
				case 2:
					return LoadManually.intensitiesLizethPrototypeRight1;
				case 3:
					return LoadManually.intensitiesLizethPrototypeRight2;
				case 4:
					return LoadManually.intensitiesLizethPrototypeLeft1;
				case 5:
					return LoadManually.intensitiesLizethPrototypeLeft2;
				case 6:
					return LoadManually.intensitiesLizethPrototypeRight1;
				case 7:
					return LoadManually.intensitiesLizethPrototypeRight2;
			}
		} else if(patient.toLowerCase().contains("bernardo" )) {
			switch (field) {
				case 0:
					return LoadManually.intensitiesBernardoPrototypeLeft1;
				case 1:
					return LoadManually.intensitiesBernardoPrototypeLeft2;
				case 2:
					return LoadManually.intensitiesBernardoPrototypeRight1;
				case 3:
					return LoadManually.intensitiesBernardoPrototypeRight2;
				case 4:
					return LoadManually.intensitiesBernardoPrototypeLeft1;
				case 5:
					return LoadManually.intensitiesBernardoPrototypeLeft2;
				case 6:
					return LoadManually.intensitiesBernardoPrototypeRight1;
				case 7:
					return LoadManually.intensitiesBernardoPrototypeRight2;
			}
		} else if(patient.toLowerCase().contains("dennis")) {
			switch (field) {
				case 0:
					return LoadManually.intensitiesDennisPrototypeLeft1;
				case 1:
					return LoadManually.intensitiesDennisPrototypeLeft2;
				case 2:
					return LoadManually.intensitiesDennisPrototypeRight1;
				case 3:
					return LoadManually.intensitiesDennisPrototypeRight2;
				case 4:
					return LoadManually.intensitiesDennisPrototypeLeft1;
				case 5:
					return LoadManually.intensitiesDennisPrototypeLeft2;
				case 6:
					return LoadManually.intensitiesDennisPrototypeRight1;
				case 7:
					return LoadManually.intensitiesDennisPrototypeRight2;
			}
		} else if(patient.toLowerCase().contains("jose")) {
			switch (field) {
				case 0:
					return LoadManually.intensitiesJosePrototypeLeft1;
				case 1:
					return LoadManually.intensitiesJosePrototypeLeft2;
				case 2:
					return LoadManually.intensitiesJosePrototypeRight1;
				case 3:
					return LoadManually.intensitiesJosePrototypeRight2;
				case 4:
					return LoadManually.intensitiesJosePrototypeLeft1;
				case 5:
					return LoadManually.intensitiesJosePrototypeLeft2;
				case 6:
					return LoadManually.intensitiesJosePrototypeRight1;
				case 7:
					return LoadManually.intensitiesJosePrototypeRight2;
				case 8:
					return LoadManually.intensitiesJosePrototypeLeft1;
				case 9:
					return LoadManually.intensitiesJosePrototypeLeft2;
				case 10:
					return LoadManually.intensitiesJosePrototypeRight1;
				case 11:
					return LoadManually.intensitiesJosePrototypeRight2;
			}
		} else if(patient.toLowerCase().contains("vinicius m")) {
			switch (field) {
			case 0:
				return LoadManually.intensitiesViniciusMPrototypeLeft1;
			case 1:
				return LoadManually.intensitiesViniciusMPrototypeLeft2;
			case 2:
				return LoadManually.intensitiesViniciusMPrototypeRight1;
			case 3:
				return LoadManually.intensitiesViniciusMPrototypeRight2;
			case 4:
				return LoadManually.intensitiesViniciusMPrototypeLeft1;
			case 5:
				return LoadManually.intensitiesViniciusMPrototypeLeft2;
			case 6:
				return LoadManually.intensitiesViniciusMPrototypeRight1;
			case 7:
				return LoadManually.intensitiesViniciusMPrototypeRight2;
			}
		}
		
		return null;
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
				setManualyByPatient(data, patient, 0);
			} else {
				setManualyByPatient(data, patient, reports.size());
			}
			reports.add(data);
		}
		
		return reportsByPatient;
	}
	
	public static boolean isPatientsManually(String patient) {
		List<String> patientsManual = getPatientsManually();
		for (String string : patientsManual) {
			if(patient.toLowerCase().contains(string.toLowerCase())) return true;
		}
		return false;
	}
	
	public static void setManualyByPatient(LoaderVisualField data, String patient, int field) {
		if(isPatientsManually(patient)) {
			data.getParameters().setIntensities(getIntensitiesByPatient(patient, field));
		}
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
