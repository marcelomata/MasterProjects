package evaluation_reorganized;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evaluation2.FileType;
import evaluation2.ReportData;

public class HumphreyUtils {

	public static Map<String, List<ReportData>> getReportsHumphreyByPatient(List<ReportData> reportsData) {
		
		Map<String, List<ReportData>> reportsByAge = new HashMap<String, List<ReportData>>(); 
		
		String patient;
		List<ReportData> reports;
		for(ReportData data : reportsData) {
			patient = data.getPatientName();
			reports = reportsByAge.get(patient);
			if(reports == null) {
				reports = new ArrayList<ReportData>();
				reportsByAge.put(patient, reports);
			}
			reports.add(data);
		}
		
		return reportsByAge;
	}

	public static List<ReportData> loadReportsHumphrey(File sampleDir) {
		List<ReportData> reportsData = new ArrayList<ReportData>();
		
		File[] files = sampleDir.listFiles();
		ReportData data;
		for (int i = 0; i < files.length; i++) {
			if(Utils.getFileType(files[i]) == FileType.TXT) {
				data = new ReportData(null, files[i]);
				data.loadDataFromTxt();
				if(data.isReportOk()) {
					reportsData.add(data);
				} else {
					System.out.println("Report "+files[i].getName()+" is not ok.");
				}
				if(!data.isModelDeviationOk()) {
					System.out.println("Model deviation of report "+files[i].getName()+" is not ok.");
				}
			}
		}
		
		return reportsData;
	}
	
	public static List<ReportData> getReportHumphreyData(List<ReportData> reports, char side) {
		List<ReportData> leftReportData = new ArrayList<ReportData>();
		for (ReportData reportData : reports) {
			if(reportData.getEyeSide().charAt(0) == side) {
				leftReportData.add(reportData);
			}
		}
		return leftReportData;
	}
	
}
