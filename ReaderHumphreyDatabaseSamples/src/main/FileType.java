package main;

public enum FileType {
	
	PDF("pdf"), 
	XML("xml");
	
	private final String type;
	
	private FileType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	
}
