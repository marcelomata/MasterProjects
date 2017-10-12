package evaluation_reorganized;

import java.io.IOException;
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

public class PlotUtils extends ComparisonAttributes {
	
//	public static void main(String[] args) throws IOException {
//		setUpAttributes();
//		plotAll();
//	}
	
	public static void plotAll() throws IOException {
		List<LoaderVisualField> leftReportPrototypeData;
		List<LoaderVisualField> rightReportPrototypeData;
		List<ReportData> leftReportHumphreyData;
		List<ReportData> rightReportHumphreyData;
		
		String respectiveHumphewyKey = "";
		for (String patient : keysPrototype) {
			if(patient.toLowerCase().contains("luiza")) {
				respectiveHumphewyKey = Utils.getHumphreyKeyByPrototypeKey(patient, keysHumphrey);
				if(respectiveHumphewyKey.isEmpty()) {
					continue;
				}
				
				leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
				leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
				if(leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
					System.out.print(patient+",\n");
					plot(true, leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(0).getNumericIntensities(), means, meansHumphrey, true, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(true, leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), leftReportHumphreyData.get(1).getNumericIntensities(), means, meansHumphrey, true, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				}
				
				rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
				rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
				if(rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 && !patient.equalsIgnoreCase("Dennis")) {
					plot(false, rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(0).getNumericIntensities(), means, meansHumphrey, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(false, rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble(), rightReportHumphreyData.get(1).getNumericIntensities(), means, meansHumphrey, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
				} else if(!patient.equalsIgnoreCase("Dennis")) {
					plot(false, LoadMathias.intensitiesMathiasRight1, rightReportHumphreyData.get(0).getNumericIntensities(), means, meansHumphrey, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
					plot(false, LoadMathias.intensitiesMathiasRight2, rightReportHumphreyData.get(1).getNumericIntensities(), means, meansHumphrey, false, new String[] {"Protótipo", "Humphrey", "Média no protótipo", "Média no Humphrey"});
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
	
	public static void plot(boolean leftSide, double field1[][], double field2[][], double mean1[][], double mean2[][], boolean left, String[] seriesName) {
		String title = leftSide ? "Olho esquerdo" : "Olho direito";
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
		plot = new ScatterPlotDemo1("Test samples - "+title, s);
		plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
//		plot.plot();
	}

	public static void plotByFieldIndex(Map<String, double[][][][]> squareDiffs, int index, boolean leftSide) {
		char[][] map = leftSide ? Constants.MAP_LEFT : Constants.MAP_RIGHT;
		int countIndex = 0;
		int count = 0;
		int countIntern = 0;
		Set<String> keys = squareDiffs.keySet();
		double[][][] field = null;
		double[][][][] fieldsAll = null;
//		int numberPoints = Utils.getNumberValidPoints(map);
		int numberOfPointsToPlot = keys.size()*4;
		Double [][]xDataTotal1 = new Double[2][numberOfPointsToPlot];
		Double [][]yDataTotal1 = new Double[2][numberOfPointsToPlot];
		int indexInsert = 0;
		int lessPatient = 0;
		for (String string : keys) {
			if(squareDiffs.get(string) != null) {
				fieldsAll = squareDiffs.get(string);
				for(int k = 0; k < fieldsAll.length; k++) {
					System.out.println("################");
					field = fieldsAll[k];
					countIntern = count*4;
					for(int m = 0; m < field.length; m++) {
						countIndex = 0;
						for (int i = 0; i < field[m].length; i++) {
							for (int j = 0; j < field[m][i].length; j++) {
								if(map[i][j] == 'y') {
									countIndex++;
									if(countIndex == index) {
										indexInsert = countIntern;
										xDataTotal1[k][indexInsert] = (double)indexInsert;
										yDataTotal1[k][indexInsert] = field[m][i][j];
										if(k ==1) {
											System.out.println(field[m][i][j]);
										}
										countIntern++;
									}
								}
							}
						}
					}
				}
				count++;
			} else {
				lessPatient++;
				System.out.println("Less one patient");
			}
		}
		
		setNullAsZero(xDataTotal1, yDataTotal1, lessPatient);
		
//		SampleXYDataset2 s = new SampleXYDataset2(2, numberOfPointsToPlot, xDataTotal1, yDataTotal1, 
//				new String[]{"Prototype", "Humphrey"}) ;
		SampleXYDataset2 s = new SampleXYDataset2(2, numberOfPointsToPlot, xDataTotal1, yDataTotal1, 
				new String[]{"1", "2"}) ;
//		ScatterPlotDemo1 plot = new ScatterPlotDemo1("Test samples - Covariance", s);
		ScatterPlotDemo1 plot = new ScatterPlotDemo1("", s);
		plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
		
//		Double [][]xtemp1 = new Double[1][numberOfPointsToPlot];
//		xtemp1[0] = xDataTotal1[0]; 
//		Double [][]ytemp1 = new Double[1][numberOfPointsToPlot];
//		ytemp1[0] = yDataTotal1[0]; 
//		SampleXYDataset2 s = new SampleXYDataset2(1, numberOfPointsToPlot, xtemp1, ytemp1, 
//						new String[]{"1"}) ;
//		ScatterPlotDemo1 plot = new ScatterPlotDemo1("", s);
//		plot.pack();
//		RefineryUtilities.centerFrameOnScreen(plot);
//		plot.setVisible(true);
//        
//		Double [][]xtemp2 = new Double[1][numberOfPointsToPlot];
//		xtemp2[0] = xDataTotal1[1]; 
//		Double [][]ytemp2 = new Double[1][numberOfPointsToPlot];
//		ytemp2[0] = yDataTotal1[1]; 
//        s = new SampleXYDataset2(1, numberOfPointsToPlot, xtemp2, ytemp2, 
//				new String[]{"2"}) ;
//		plot = new ScatterPlotDemo1("", s);
//		plot.pack();
//        RefineryUtilities.centerFrameOnScreen(plot);
//        plot.setVisible(true);
	}

	private static void setNullAsZero(Double[][] xDataTotal1, Double[][] yDataTotal1, int lessPatient) {
		for (int i = 0; i < yDataTotal1.length; i++) {
			for (int j = 0; j < yDataTotal1[i].length; j++) {
				if(xDataTotal1[i][j] == null) {
					xDataTotal1[i][j] = 0.0;
				}
				
				if(yDataTotal1[i][j] == null) {
					yDataTotal1[i][j] = 0.0;
				}
			}
		}
	}
	
}
