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
		Vector<Result> results = new Vector<>(); // Holds the results of the search.
		
		for(String fileName : this.settings.getDataFileNames()) { // Loops through all files in settings.
			try {
				BufferedReader fin = new BufferedReader(new FileReader(new File(fileName))); // Initialize file reader.
				
				Vector<String> headers = new Vector<>(); // Holds the column titles.
				headers.add("File"); // Manually add the File title to the headers.
				headers.addAll(parseLine(fin.readLine())); // Reads the first line and parses it.
				
				while(fin.ready()) { // While file reader has a next line keep looping.
					String line = fin.readLine(); // Read line from file.
					if(line.toLowerCase().contains(query.toLowerCase())) { // Check if query is in the line.
						Vector<String> data = new Vector<>(); // Holds the data in the same order as headers.
						data.add(fileName); // Manually add file name to data.
						
						Result result = new Result(); // Creates a new Result object to hold the headers and data.
						result.setLabels(headers);  // Sets the result object's labels with the headers.
						data.addAll(parseLine(line)); // Parse the line and adds it to data.
						result.setData(data); // Sets the result object's data to data.
						results.add(result); // Adds result to the results vector.
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
		Vector<String> ret = new Vector<>(); // Create a vector to hold the parsed data.
		
		Boolean insideQuote = false; // Tracks if the parser is inside of quotes.
		String section = ""; // Temporary string that holds a parsed section.
		
		for(Character c : line.toCharArray()) { // Loops through passed string's characters.
			if(c == '\"') {
				// If character is a quote switch insideQuote.
				insideQuote = !insideQuote;
			}else if(c == ',' && !insideQuote) {
				// If character is a comma and it is not inside a quote add section string to ret.
				ret.add(section);
				section = ""; // Clears the section string.
			}else {
				// For all other characters add them to section.
				section += c;
			}
		}
		ret.add(section); // Adds the last section to ret.
		
		return ret;
	}
	
}
