package main;

import java.io.FileWriter;

public class FitSigmoidParametersHumphreyFile {
	
	private double alphaTotal;
	private double alphaModel;
	private double thetaTotal;
	private double thetaModel;
	private double normalizationTotal;
	private double normalizationModel;
	
	private boolean isModelOk;
	
	public FitSigmoidParametersHumphreyFile(double paramsTotal[], double paramsModel[]) {
		this.alphaTotal = paramsTotal[0];
		this.thetaTotal = paramsTotal[1];
		this.normalizationTotal = paramsTotal[2];
		if(paramsModel == null) {
			isModelOk = false;
		} else {
			isModelOk = true;
			alphaModel = paramsModel[0];
			thetaModel = paramsModel[1];
		}
	}
	
	public boolean isModelOk() {
		return isModelOk;
	}
	
	public void saveParameters(FileWriter in) {
		
	}

}
