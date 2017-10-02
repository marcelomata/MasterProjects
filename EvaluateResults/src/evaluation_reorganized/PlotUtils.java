package evaluation_reorganized;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.ui.RefineryUtilities;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import evaluation2.Constants;
import evaluation2.LoadMathias;
import evaluation2.ReportData;
import evaluation2.SampleXYDataset2;
import evaluation2.ScatterPlotDemo1;

public class PlotUtils {
	
	public static void plotAll(
			File evaluationDir,
			Map<String, List<LoaderVisualField>> reportsPrototypeByPatient,
			Map<String, List<ReportData>> reportsHumphreyByPatient,
			double [][] means1,
			double [][] means2,
			Set<String> keysPrototype,
			Set<String> keysHumphrey) throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		Map<String,double[]> result = new HashMap<String, double[]>();
		
		String respectiveHumphewyKey = "";
		for (String patient : keysPrototype) {
			if(patient.toLowerCase().contains("vinicius b")) {
				respectiveHumphewyKey = Utils.getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
				if(respectiveHumphewyKey.isEmpty()) {
					continue;
				}
				
				leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
				leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
				if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
					System.out.print(patient+",\n");
					plot(leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), means1, means2, true, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), means1, means2, true, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				}
				
				rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
				rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
					plot(rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				} else if(!patient.equalsIgnoreCase("Dennis")) {
					plot(LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), means1, means2, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				}
				return;
			}
		}
	}

	public static void plot(double field1[][], double field2[][], boolean left, String[] namesOfSeries) {
		char[][] map = left ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
//		XyGui plot;
		ScatterPlotDemo1 plot;
		Double [][]xDataTotal1 = new Double[2][50];
		Double [][]yDataTotal1 = new Double[2][50];
		int count = 0; 
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					xDataTotal1[0][count] = (double) count+1;
					yDataTotal1[0][count] = field1[i][j];
					xDataTotal1[1][count] = (double) count+1;
					yDataTotal1[1][count] = field2[i][j];
					count++;
				}
			}
		}
		
//		plot = new XyGui("Test samples ", xDataTotal1, yDataTotal1, true);
		SampleXYDataset2 s = new SampleXYDataset2(2, 50, xDataTotal1, yDataTotal1, namesOfSeries);
//		s.setxValues(new Double[][] {xDataTotal1, new Double[]{1.0}});
//		s.setyValues(new Double[][] {yDataTotal1, new Double[]{1.0}});
		plot = new ScatterPlotDemo1("Test samples ", s);
		plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
//		plot.plot();
	}
	
	public static void plot(double field1[][], double field2[][], double mean1[][], double mean2[][], boolean left, String[] seriesName) {
		char[][] map = left ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
//		XyGui plot;
		ScatterPlotDemo1 plot;
		Double [][]xDataTotal1 = new Double[4][50];
		Double [][]yDataTotal1 = new Double[4][50];
		int count = 0; 
		for (int i = 0; i < field1.length; i++) {
			for (int j = 0; j < field1[i].length; j++) {
				if(map[i][j] == 'y') {
					xDataTotal1[0][count] = (double) count+1;
					yDataTotal1[0][count] = field1[i][j];
					xDataTotal1[1][count] = (double) count+1;
					yDataTotal1[1][count] = field2[i][j];
					xDataTotal1[2][count] = (double) count+1;
					yDataTotal1[2][count] = mean1[i][j];
					xDataTotal1[3][count] = (double) count+1;
					yDataTotal1[3][count] = mean2[i][j];
					count++;
				}
			}
		}
		
//		plot = new XyGui("Test samples ", xDataTotal1, yDataTotal1, true);
		SampleXYDataset2 s = new SampleXYDataset2(4, 50, xDataTotal1, yDataTotal1, seriesName);
//		s.setxValues(new Double[][] {xDataTotal1, new Double[]{1.0}});
//		s.setyValues(new Double[][] {yDataTotal1, new Double[]{1.0}});
		plot = new ScatterPlotDemo1("Test samples ", s);
		plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
//		plot.plot();
	}
	
}
