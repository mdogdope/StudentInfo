import java.util.Collections;
import java.util.Vector;

public class Result {
	private Vector<String> data = new Vector<>();
	private Vector<String> labels = new Vector<>();
	
	public void setData(String[] data) {
		Collections.addAll(this.data, data);
	}
	
	public void setData(Vector<String> data) {
		this.data = data;
	}
	
	public void setLabels(String[] labels) {
		Collections.addAll(this.labels, labels);
	}
	
	public void setLabels(Vector<String> labels) {
		this.labels = labels;
	}
	
	public Vector<String> getData() {
		return this.data;
	}
	
	public Vector<String> getLabels() {
		return this.labels;
	}
	
}
