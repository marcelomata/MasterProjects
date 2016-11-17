package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
//		File sampleDir = new File("./samplesA5Left/");
		File sampleDir = new File("./samples/");
		List<ReportData> reportsData = loadReports(sampleDir);
		
		System.out.println(reportsData.size());
		
		for(ReportData data : reportsData) {
			data.saveFileAsTxt();
		}  
	}
		
	private static List<ReportData> loadReports(File sampleDir) {
		List<ReportData> reportsData = new ArrayList<ReportData>();
		File[] folders = sampleDir.listFiles();
		
		ReportData data;
		File[] files;
		for (int i = 0; i < folders.length; i++) {
			files = folders[i].listFiles();
			
			for (int j = 0; j < files.length; j++) {
				if(getFileType(files[j]) == FileType.PDF) {
					data = new ReportData(files[j]);
					data.loadDataFromPdf();
					reportsData.add(data);
				}
			}
		}
		
		return reportsData;
	}

	private static FileType getFileType(File file) {
		String name = file.getName();
		String type = name.substring(name.length()-3);
		
		if(type.equalsIgnoreCase(FileType.PDF.toString())) {
			return FileType.PDF;
		} else {
			return FileType.XML;
		}
	}

}
