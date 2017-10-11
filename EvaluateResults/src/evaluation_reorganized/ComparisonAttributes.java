package evaluation_reorganized;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrgs.campimeter.examination.enums.EnumEye;
import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import evaluation2.LoadMathias;
import evaluation2.ReportData;
import evaluation2.StatisticsComparation;

public class ComparisonAttributes {
	
	protected static File reportsHumphreyDir;
	protected static File reportsPrototypeDir;
	protected static File evaluationDir;
	protected static List<ReportData> reportsHumphreyData;
	protected static List<LoaderVisualField> reportsPrototypeData;
	protected static double[][] means;
	protected static double[][] meansHumphrey;
	protected static Map<String, List<ReportData>> reportsHumphreyByPatient;
	protected static Map<String, List<LoaderVisualField>> reportsPrototypeByPatient;
	protected static Set<String> keysHumphrey;
	protected static Set<String> keysPrototype;
	
	protected static double[][] field_left_prototype_1 = null;
	protected static double[][] field_left_prototype_2 = null;
	protected static double[][] field_left_humphrey_1 = null;
	protected static double[][] field_left_humphrey_2 = null;
	
	protected static double[][] field_right_prototype_1 = null;
	protected static double[][] field_right_prototype_2 = null;
	protected static double[][] field_right_humphrey_1 = null;
	protected static double[][] field_right_humphrey_2 = null;
	
	protected static List<LoaderVisualField> leftReportPrototypeData;
	protected static List<LoaderVisualField> rightReportPrototypeData;
	protected static List<ReportData> leftReportHumphreyData;
	protected static List<ReportData> rightReportHumphreyData;
	
	protected static double[][] field1_left_1 = null;
	protected static double[][] field1_left_2 = null;
	protected static double[][] field2_left_1 = null;
	protected static double[][] field2_left_2 = null;
	
	protected static double[][] field1_right_1 = null;
	protected static double[][] field1_right_2 = null;
	protected static double[][] field2_right_1 = null;
	protected static double[][] field2_right_2 = null;
	
	
	protected static void setUpAttributes() {
		reportsHumphreyDir = new File("./reports_patients_humphrey_txt/");
		//reportsHumphreyDir = new File("./reports_patients_humphrey_txt_2/");
		reportsPrototypeDir = new File("./reports_patients_prototype_txt/");
		evaluationDir = new File("./evaluation_patients/");
		
		reportsHumphreyData = HumphreyUtils.loadReportsHumphrey(reportsHumphreyDir);
		reportsPrototypeData = PrototypeUtils.loadReportsPrototype(reportsPrototypeDir);
		
		means = StatisticsComparation.getMeans(reportsPrototypeData);
		meansHumphrey = StatisticsComparation.getMeansHumphrey(reportsHumphreyData);
		
		reportsHumphreyByPatient = HumphreyUtils.getReportsHumphreyByPatient(reportsHumphreyData);
		reportsPrototypeByPatient = PrototypeUtils.getReportsPrototypeByPatient(reportsPrototypeData);
		
		keysHumphrey = reportsHumphreyByPatient.keySet();
		keysPrototype = reportsPrototypeByPatient.keySet();
	}
	
	protected static void setUpDevicesFields(List<LoaderVisualField> leftReportPrototypeData,
			List<LoaderVisualField> rightReportPrototypeData, List<ReportData> leftReportHumphreyData,
			List<ReportData> rightReportHumphreyData, boolean isDennis) {
		
		field_left_prototype_1 = leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !isDennis ? 
				leftReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble() : null;
		field_left_prototype_2 = leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !isDennis ? 
				leftReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble() : null;
		field_left_humphrey_1 = leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !isDennis ? 
				leftReportHumphreyData.get(0).getNumericIntensities() : null;
		field_left_humphrey_2 = leftReportPrototypeData.size() > 1 && leftReportHumphreyData.size() > 1 && !isDennis ? 
				leftReportHumphreyData.get(1).getNumericIntensities() : null;
		
		field_right_prototype_1 = rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 ? 
						rightReportPrototypeData.get(0).getParameters().getIntensitiesAsDouble() : LoadMathias.intensitiesMathiasRight1;
		field_right_prototype_2 = rightReportPrototypeData.size() > 1 && rightReportHumphreyData.size() > 1 ? 
						rightReportPrototypeData.get(1).getParameters().getIntensitiesAsDouble() : LoadMathias.intensitiesMathiasRight2;
		field_right_humphrey_1 = !isDennis ? rightReportHumphreyData.get(0).getNumericIntensities() : null;
		field_right_humphrey_2 = !isDennis ? rightReportHumphreyData.get(1).getNumericIntensities() : null;
	}
	
	protected static boolean checkFieldsNull() {
		return field1_left_1 == null || field2_left_1 == null || field1_left_2 == null || field2_left_2 == null ||
				field1_right_1 == null || field2_right_1 == null || field1_right_2 == null || field2_right_2 == null;
	}
	
	protected static void readDataDevices(String respectiveHumphewyKey, String patient) {
		leftReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'E');
		leftReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.LEFT);
		rightReportHumphreyData = HumphreyUtils.getReportHumphreyData(reportsHumphreyByPatient.get(respectiveHumphewyKey), 'D');
		rightReportPrototypeData = PrototypeUtils.getReportPrototypeData(reportsPrototypeByPatient.get(patient), EnumEye.RIGHT);
	}
	
	protected static void setUpFieldsDevicesResult() {
		setUpField1PrototypeResult();
		setUpField2HumphreyResult();
	}
	
	private static void setUpField2HumphreyResult() {
		field2_left_1 = field_left_humphrey_1;
		field2_left_2 = field_left_humphrey_2;
		
		field2_right_1 = field_right_humphrey_1;
		field2_right_2 = field_right_humphrey_2;
	}

	private static void setUpField1PrototypeResult() {
		field1_left_1 = field_left_prototype_1;
		field1_left_2 = field_left_prototype_2;
		
		field1_right_1 = field_right_prototype_1;
		field1_right_2 = field_right_prototype_2;
	}
	
	private static void setUpField1HumphreyResult() {
		field1_left_1 = field_left_humphrey_1;
		field1_left_2 = field_left_humphrey_2;
		
		field1_right_1 = field_right_humphrey_1;
		field1_right_2 = field_right_humphrey_2;
	}
	
	private static void setUpField1PrototypeMeans() {
		field1_left_1 = means;
		field1_left_2 = means;
		
		field1_right_1 = means;
		field1_right_2 = means;
	}
	
	private static void setUpField1HumphreyMeans() {
		field1_left_1 = meansHumphrey;
		field1_left_2 = meansHumphrey;
		
		field1_right_1 = meansHumphrey;
		field1_right_2 = meansHumphrey;
	}
	
	private static void setUpField2PrototypeMeans() {
		field2_left_1 = means;
		field2_left_2 = means;
		
		field2_right_1 = means;
		field2_right_2 = means;
	}
	
	private static void setUpField2HumphreyMeans() {
		field2_left_1 = meansHumphrey;
		field2_left_2 = meansHumphrey;
		
		field2_right_1 = meansHumphrey;
		field2_right_2 = meansHumphrey;
	}
	
	protected static void setUpFieldsPrototypeAndProtMeans() {
		setUpField1PrototypeResult();
		setUpField2PrototypeMeans();
	}
	
	protected static void setUpFieldsPrototypeAndHumphreyMeans() {
		setUpField1PrototypeResult();
		setUpField2HumphreyMeans();
	}
	
	protected static void setUpFieldsHumphreyAndHumphreyMeans() {
		setUpField1HumphreyResult();
		setUpField2HumphreyMeans();
	}
	
	protected static void setUpFieldsHumphreyAndPrototypeMeans() {
		setUpField1HumphreyResult();
		setUpField2HumphreyMeans();
	}
	
	protected static void setUpFieldsPrototypeMeansHumphreyMeans() {
		setUpField1PrototypeMeans();
		setUpField2HumphreyMeans();
	}
	
	protected static void setUpFieldsHumphreyMeansPrototypeMeans() {
		setUpField1HumphreyMeans();
		setUpField2HumphreyResult();
	}

}
