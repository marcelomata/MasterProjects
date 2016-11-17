
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import flanagan.analysis.Regression;


public class MainFitByIntensities {
	
	private static final double MAX_INTENSITY_HUMPHREY = 51;
	private static final double MAX_PERCENTAGE_HUMPHREY = 100;
	private static final double MEAN_PERCENTAGE_HUMPHREY = 50;
	private static int COUNT = 0;

	public static void main(String[] args) {
		File sampleDir = new File("./reportsTxt/");
		
		List<ReportData> reportsData = loadReports(sampleDir);

		System.out.println(reportsData.size());
		
		Map<Integer, List<ReportData>> reportsByAge = getReportsByAge(reportsData);
		
		Set<Integer> keys = reportsByAge.keySet();
		List<ReportData> leftReportData;
		List<ReportData> rightReportData;
		for (Integer age : keys) {
			leftReportData = getReportData(reportsByAge.get(age), 'E');
			rightReportData = getReportData(reportsByAge.get(age), 'D');
			fitCourves(leftReportData, age, "Esquedo");
			fitCourves(rightReportData, age, "Direito");
		}
		
	}

	private static void fitCourves(List<ReportData> reportData,	Integer age, String eyeSide) {
		File parametersFile = new File("./fit_parameters/"+age+"-"+eyeSide+".txt");
		if(!parametersFile.exists()) {
			try {
				parametersFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Age "+age);
		System.out.println("Eye side "+eyeSide);
		System.out.println("Number of reports "+reportData.size());
		
		if(reportData.size() >= 7) {
		
			FileWriter fw;
			try {
				fw = new FileWriter(parametersFile);
				
				int numPlots = 3;
				char[][] map  = getMap(eyeSide);
	//			GaussianCurveFitter fitter = GaussianCurveFitter.create();
				Sample2DPoints[][] xyData;
	//			double[] parametersTotalDeviation;
	//			double[] parametersModelDeviation;
				XyGui plot;
	//			Gaussian gaussian;
	//			Sigmoid sigmoid;
				double[] xDataTotal;
				double[] xDataModel;
				double[] xDataTotal2;
//				double[] xDataModel2;
				double[] yDataTotal;
				double[] yDataModel;
				double[] yDataTotal2;
//				double[] yDataModel2;
				double[] yDataTotal3;
				double[] yDataModel3;
				double errorTotal;
				double errorModel;
				Regression regTotal;
				Regression regModel;
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						if(map[i][j] == 'y') {
							xyData = getDataToFit(reportData);
	//						parametersTotalDeviation = fitter.fit(createDataset(xyData[i][j].getTotalDeviationAsArray()).toList());
							
							xDataTotal = getXData(xyData[i][j].getTotalDeviationAsArray());
							yDataTotal = getYData(xyData[i][j].getTotalDeviationAsArray());
							regTotal = new Regression(xDataTotal, yDataTotal);
							regTotal.setYscaleOption(true);
//							regTotal.supressYYplot();
//							regTotal.sigmoidThresholdPlot();
							regTotal.sigmoidThreshold();
							if(COUNT < numPlots) {
								plot = new XyGui("Test intensity 1 point "+COUNT, xDataTotal, yDataTotal);
								plot.plot();
							}
							xDataTotal2 = getNewX(xDataTotal);
							yDataTotal2 = getDataNewYSigmoid(xDataTotal2, regTotal.getBestEstimates());
							yDataTotal3 = getDataNewYSigmoid(xDataTotal, regTotal.getBestEstimates());
							if(COUNT < numPlots) {
								XyGui plot2 = new XyGui("Test intensity 2 point "+COUNT, xDataTotal2, yDataTotal2);
								plot2.plot();
								COUNT++;
							}

							errorTotal = getMeanError(yDataTotal, yDataTotal3);
							if(errorTotal < 3) {
								fw.write(i+","+j+"\n");
								fw.write(regTotal.getBestEstimates()[0]+" "+regTotal.getBestEstimates()[1]+" "+regTotal.getBestEstimates()[2]+"\n");
								System.out.println("Point "+i+","+j+" - Total ----> Slope "+regTotal.getBestEstimates()[0]+" - Theta "+regTotal.getBestEstimates()[1]+" - Scale "+regTotal.getBestEstimates()[2]);
								System.out.println("Error Total "+errorTotal);
							} else {
								fw.write("- \n");
								System.out.println("Error Total too big "+errorTotal);
							}
							
							if(xyData[i][j].isModelOk()) {
								xDataModel = getXData(xyData[i][j].getModelDeviationAsArray());
								yDataModel = getYData(xyData[i][j].getModelDeviationAsArray());
								regModel = new Regression(xDataModel, yDataModel);
								regModel.setYscaleOption(true);
								regModel.supressYYplot();
	//							regModel.sigmoidThresholdPlot();
								regModel.sigmoidThreshold();
	//							xDataModel2 = getNewX(xDataModel);
	//							yDataModel2 = getDataNewYSigmoid(xDataModel2, regModel.getBestEstimates());
								yDataModel3 = getDataNewYSigmoid(xDataModel, regModel.getBestEstimates());
								errorModel = getMeanError(yDataModel, yDataModel3);
								if(errorModel < 3) {
									fw.write(regModel.getBestEstimates()[0]+" "+regTotal.getBestEstimates()[1]+" "+regTotal.getBestEstimates()[2]+"\n");
									System.out.println("Point "+i+","+j+" - Model ----> Slope "+regModel.getBestEstimates()[0]+" - Theta "+regModel.getBestEstimates()[1]+" - Scale "+regModel.getBestEstimates()[2]);
									System.out.println("Error Model "+errorModel);
								} else {
									fw.write("- \n");
									System.out.println("Error Model too big "+errorModel);
								}
							} else {
								fw.write("- \n");
								System.out.println("Point "+i+","+j+" - Model ----> -");
								System.out.println("Error Model -");
							}
						}
					}
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			parametersFile.delete();
			System.out.println("Less than 3 reports with: Age "+age+" - Eye side "+eyeSide+" - Number of reports "+reportData.size());
		}
	}
	
	private static double getMeanError(double[] yData, double[] yData2) {
		double meanSqueareError = 0;
		
		for (int i = 0; i < yData2.length; i++) {
			meanSqueareError += (yData[i]*yData[i]) - (yData2[i]*yData2[i]); 
		}
		
		meanSqueareError /= yData.length;
		
		return Math.sqrt(meanSqueareError);
	}

	private static double[] getNewX(double[] xData) {
		Arrays.sort(xData);
		int max = (int)xData[xData.length - 1];
		double[] newXData = new double[max];
		for (int i = 0; i < max; i++) {
			newXData[i] = i;
		}
		return newXData;
	}

	
	private static double[] getDataNewYSigmoid(double[]xData, double[] param) {
		//paraName[0]="slope term";
        //paraName[1]="theta";
        //paraName[2]="y scale";
		double[] yData2 = new double[xData.length];
		for (int i = 0; i < yData2.length; i++) {
			//y = yscale/(1 + exp(-slopeTerm(x - theta)))
			yData2[i] = param[2] / (1 + Math.exp(-param[0]*(xData[i] - param[1])));
		}
		return yData2;
	}

	private static double[] getXData(double[][] ds) {
		return getData(0, ds);
	}
	
	private static double[] getYData(double[][] ds) {
		return getData(1, ds);
	}

	private static double[] getData(int j, double[][] dataset) {
		double[] data = new double[dataset.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = dataset[i][j];
		}
		return data;
	}

	private static List<ReportData> getReportData(List<ReportData> reports, char side) {
		List<ReportData> leftReportData = new ArrayList<ReportData>();
		for (ReportData reportData : reports) {
			if(reportData.getEyeSide().charAt(0) == side) {
				leftReportData.add(reportData);
			}
		}
		return leftReportData;
	}

	private static Map<Integer, List<ReportData>> getReportsByAge(List<ReportData> reportsData) {
		
		Map<Integer, List<ReportData>> reportsByAge = new HashMap<Integer, List<ReportData>>(); 
		
		int age;
		List<ReportData> reports;
		for(ReportData data : reportsData) {
			age = data.getAge();
			reports = reportsByAge.get(age);
			if(reports == null) {
				reports = new ArrayList<ReportData>();
				reportsByAge.put(age, reports);
			}
			reports.add(data);
		}
		
		return reportsByAge;
	}

	private static List<ReportData> loadReports(File sampleDir) {
		List<ReportData> reportsData = new ArrayList<ReportData>();
		
		File[] files = sampleDir.listFiles();
		ReportData data;
		for (int i = 0; i < files.length; i++) {
			if(getFileType(files[i]) == FileType.TXT) {
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
	
	private static FileType getFileType(File file) {
		String name = file.getName();
		String type = name.substring(name.length()-3);
		
		if(type.equalsIgnoreCase(FileType.PDF.toString())) {
			return FileType.PDF;
		} else if(type.equalsIgnoreCase(FileType.XML.toString())) {
			return FileType.XML;
		} else {
			return FileType.TXT;
		}
	}
	
	private static char[][] getMap(String eyeSide) {
		if(eyeSide.charAt(0) == 'E') {
			return MAP_LEFT;
		}
		return MAP_RIGHT;
	}

	private static Sample2DPoints[][] getDataToFit(List<ReportData> reportData) {
		
		char[][] map  = getMap(reportData.get(0).getEyeSide());
		
		double modelDeviationPercentage;
		double totalDeviationPercentage;
		double intensity;
		Sample2DPoints[][] samples = new Sample2DPoints[map.length][map[0].length];
		boolean isModelDeviationOk; 
		int numReports = 0;
		for (ReportData reportData2 : reportData) {
			isModelDeviationOk = reportData2.isModelDeviationOk();
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if(map[i][j] == 'y') {
						intensity = reportData2.getNumericIntensities(i, j);
						totalDeviationPercentage = reportData2.getTotalDeviationPercentage(i, j);
						if(samples[i][j] == null) {
							samples[i][j] = new Sample2DPoints();
						}
						if(numReports == 0) {
							samples[i][j].addSampleTotalDeviation(new Sample2D(intensity - reportData2.getTotalDeviation(i, j), MEAN_PERCENTAGE_HUMPHREY));
							samples[i][j].addSampleModelDeviation(new Sample2D(intensity - reportData2.getModelDeviation(i, j), MEAN_PERCENTAGE_HUMPHREY));
						}
						if(totalDeviationPercentage != 50) {
							samples[i][j].addSampleTotalDeviation(new Sample2D(intensity, totalDeviationPercentage));
						}
						if(isModelDeviationOk) {
							modelDeviationPercentage = reportData2.getTotalDeviationPercentage(i, j);
							if(modelDeviationPercentage != 50) {
								samples[i][j].addSampleModelDeviation(new Sample2D(intensity, modelDeviationPercentage));
							}
							samples[i][j].setIsModelOk(true);
						} else {
							samples[i][j].setIsModelOk(false);
						}
					}
				}
			}
			numReports++;
		}
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if(map[i][j] == 'y') {
					samples[i][j].addSampleTotalDeviation(new Sample2D(MAX_INTENSITY_HUMPHREY, MAX_PERCENTAGE_HUMPHREY));
//					samples[i][j].addSampleTotalDeviation(new Sample2D(MAX_INTENSITY_HUMPHREY-1, MAX_PERCENTAGE_HUMPHREY-1));
					samples[i][j].addSampleModelDeviation(new Sample2D(MAX_INTENSITY_HUMPHREY, MAX_PERCENTAGE_HUMPHREY));
//					samples[i][j].addSampleModelDeviation(new Sample2D(MAX_INTENSITY_HUMPHREY-1, MAX_PERCENTAGE_HUMPHREY-1));
				}
			}
		}
		
		return samples;
	}

//	private static WeightedObservedPoints createDataset(double[][] points) {
//	    final WeightedObservedPoints obs = new WeightedObservedPoints();
//	    for (int i = 0; i < points.length; i++) {
//	        obs.add(points[i][0], points[i][1]);
//	    }
//	    return obs;
//	}

	//8 rows/columns
	public static char MAP_RIGHT[][] = {{'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'},
										{'n', 'n', 'n', 'y', 'y', 'y', 'y', 'n', 'n', 'n'},
										{'n', 'n', 'y', 'y', 'y', 'y', 'y', 'y', 'n', 'n'},
										{'n', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'n'},
										{'n', 'y', 'y', 'y', 'y', 'y', 'y', 'b', 'y', 'n'},
										{'n', 'y', 'y', 'y', 'y', 'y', 'y', 'b', 'y', 'n'},
										{'n', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'n'},
										{'n', 'n', 'y', 'y', 'y', 'y', 'y', 'y', 'n', 'n'},
										{'n', 'n', 'n', 'y', 'y', 'y', 'y', 'n', 'n', 'n'},
										{'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'}};
		
	public static char MAP_LEFT[][] =  {{'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'},
										{'n', 'n', 'n', 'y', 'y', 'y', 'y', 'n', 'n', 'n'},
										{'n', 'n', 'y', 'y', 'y', 'y', 'y', 'y', 'n', 'n'},
										{'n', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'n'},
										{'n', 'y', 'b', 'y', 'y', 'y', 'y', 'y', 'y', 'n'},
										{'n', 'y', 'b', 'y', 'y', 'y', 'y', 'y', 'y', 'n'},
										{'n', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'y', 'n'},
										{'n', 'n', 'y', 'y', 'y', 'y', 'y', 'y', 'n', 'n'},
										{'n', 'n', 'n', 'y', 'y', 'y', 'y', 'n', 'n', 'n'},
										{'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'}};

}
