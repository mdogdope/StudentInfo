import java.util.Vector;

public class Settings {
	private String outputMode = "block";
	private Vector<String> dataFiles = new Vector<>();
	
	public void setOutputMode(String mode) {
		if(mode.equals("block") || mode.equals("line")) {
			this.outputMode = mode;
		}
	}
	
	public String getOutputMode() {
		return this.outputMode;
	}
	
	public void addDataFile(String fileName) {
		this.dataFiles.add(fileName);
	}
	
	public void removeDataFile(String fileName) {
		Vector<String> temp = new Vector<>();
		for(String item : this.dataFiles) {
			if(item != fileName) {
				temp.add(item);
			}
		}
		this.dataFiles = temp;
	}
	
	public void clearDataFiles() {
		this.dataFiles = new Vector<>();
	}
	
	public Vector<String> getDataFileNames() {
		return this.dataFiles;
	}
	
}
