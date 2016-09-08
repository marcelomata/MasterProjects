package converter;



public class Converters {
	
	private static MobileCampimeterFileConverter converter;
	
	public static MobileCampimeterFileConverter getInstance(ExaminationData data, FileVersion targetVersion) {
		
		if(converter == null) {
			switch (targetVersion) {
			case v0_1:
				converter = new ConverterCampimeterFileV0_1(data);
				break;
			case v1_0:
				converter = new ConverterCampimeterFileV1_0(data);
				break;
			case v2_0:
				converter = new ConverterCampimeterFileV2_0(data);
				break;
		}
		}
		
		return converter;
	}

}
