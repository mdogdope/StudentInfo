import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import com.google.gson.Gson;

public class StudentInfo {
	private final File settingsFile = new File("settings.json");
	private Settings settings = new Settings();
	
	public StudentInfo() {
		
		//Check if settings file exists and creates template if it does not.
		if(!settingsFile.exists()) {
			Settings settings = new Settings(); // Get a default settings object to save to file.
			Gson gson = new Gson(); // This is a JSON file library.
			try {
				FileWriter fout = new FileWriter(settingsFile); 
				fout.write(gson.toJson(settings)); // Writes the JSON file will all the default settings.
				fout.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		// Read and load all settings.
		try {
			BufferedReader fin = new BufferedReader(new FileReader(settingsFile));
			
			String json = fin.readLine(); // Reads the JSON file.
			
			Gson gson = new Gson();
			this.settings = gson.fromJson(json, Settings.class); // Turns the read content into a settings object.
			
			fin.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void saveSettings() {
		try {
			BufferedWriter fout = new BufferedWriter(new FileWriter(settingsFile));
			
			Gson gson = new Gson();
			String json = gson.toJson(this.settings); // Takes the settings object and converts it to a JSON string.
			fout.write(json); // Write JSON string to file.
			
			fout.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void changeOutputMode() {
		// Changes the output mode.
		if(this.settings.getOutputMode().equals("line")) {
			this.settings.setOutputMode("block");
		}else {
			this.settings.setOutputMode("line");
		}
	}
	
	// Clears the data files from the setting object.
	public void clearDataFileNames() {
		this.settings.clearDataFiles();
	}
	
	// Adds a file to the settings object.
	public void addDataFileName(String fileName) {
		this.settings.addDataFile(fileName);
	}
	
	// Returns the current output mode of the settings object.
	public String getOutputMode() {
		return this.settings.getOutputMode();
	}
	
	// Returns the loaded file names.
	public Vector<String> getDataFileNames() {
		return this.settings.getDataFileNames();
	}
	
	// Performs a search with a given query.
	public Vector<Result> search(String query) {
		Vector<Result> results = new Vector<>();
		
		for(String fileName : this.settings.getDataFileNames()) {
			try {
				BufferedReader fin = new BufferedReader(new FileReader(new File(fileName)));
				
				Vector<String> headers = new Vector<>();
				headers.add("File");
				headers.addAll(parseLine(fin.readLine()));
				
				while(fin.ready()) {
					String line = fin.readLine();
					if(line.toLowerCase().contains(query.toLowerCase())) {
						Vector<String> data = new Vector<>();
						data.add(fileName);
						
						Result result = new Result();
						result.setLabels(headers);
						data.addAll(parseLine(line));
						result.setData(data);
						results.add(result);
					}
				}
				
				fin.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return results;
	}
	
	private Vector<String> parseLine(String line) {
		Vector<String> ret = new Vector<>();
		
		Boolean insideQuote = false;
		String section = "";
		
		for(Character c : line.toCharArray()) {
			if(c == '\"') {
				insideQuote = !insideQuote;
			}else if(c == ',' && !insideQuote) {
				ret.add(section);
				section = "";
			}else {
				section += c;
			}
			
		}
		
		ret.add(section);
		
		return ret;
	}
	
}
