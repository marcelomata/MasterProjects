package evaluation_reorganized;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrgs.campimeter.examination.visualfield.file.LoaderVisualField;
import evaluation2.ReportData;
import evaluation2.StatisticsComparation;

public class ComparisonAtributes {
	
	public static File reportsHumphreyDir;
	public static File reportsPrototypeDir;
	public static File evaluationDir;
	public static List<ReportData> reportsHumphreyData;
	public static List<LoaderVisualField> reportsPrototypeData;
	public static double[][] means;
	public static double[][] meansHumphrey;
	public static Map<String, List<ReportData>> reportsHumphreyByPatient;
	public static Map<String, List<LoaderVisualField>> reportsPrototypeByPatient;
	public static Set<String> keysHumphrey;
	public static Set<String> keysPrototype;
	
	public static void setUpAttributes() {
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

}
