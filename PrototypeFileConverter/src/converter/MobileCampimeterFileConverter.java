package converter;
import java.io.File;

public abstract class MobileCampimeterFileConverter {

	protected ExaminationData data;
	
	public MobileCampimeterFileConverter(ExaminationData data) {
		this.data = data;
	}
	
	public abstract void convert(File versionTarget);

}
