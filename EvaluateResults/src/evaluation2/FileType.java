package evaluation2;

public enum FileType {
	
	PDF("pdf"), 
	XML("xml"),
	TXT("txt");
	
	private final String type;
	
	private FileType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	
}
