import java.util.Scanner;
import java.util.Vector;

public class CLI {
	public CLI() {
		
		boolean debug = false;// Turn to true for debug mode.
		
		if(debug) {
			// Stuff to test while in debug mode.
			StudentInfo si = new StudentInfo();
			
			displayResults(si.search("Earl"), "block");
			
		}else {
			// Start of CLI.
			StudentInfo studentInfo = new StudentInfo();
			
			Scanner input = new Scanner(System.in);
			
			while(true) {
				//##################################################################
				// Print out menu and settings.
				System.out.println("Student Info");
				System.out.println("===================");
				System.out.println("Output Mode = " + studentInfo.getOutputMode());
				System.out.println("Loaded Files = ");
				for(String dataFileName : studentInfo.getDataFileNames()) {
					//Print out each file that is loaded.
					System.out.println("	" + dataFileName);
				}
				System.out.println("===================");
				System.out.println("1: Search");
				System.out.println("2: Add data file");
				System.out.println("3: Clear data files");
				System.out.println("4: Change output mode");
				System.out.println("5: Quit");
				//##################################################################
				
				// Prompt for user to input a choice.
				System.out.print(">> ");
				Integer choice = input.nextInt();
				
				if(choice == 1) {
					// Choice one starts the search prompt.
					System.out.print("Search: ");
					input.nextLine(); // Clears input for query.
					String query = input.nextLine();
					Vector<Result> results = studentInfo.search(query); // Pass search query to the search method in studentInfo.
					
					displayResults(results, studentInfo.getOutputMode()); // Pass results to the display method.
					
					System.out.println("Press ENTER to continue.");
					input.nextLine(); // Pause output to allow user to read results.
					
					
				}else if(choice == 2) {
					// Choice two lets the user add a file to be searched.
					System.out.print("File name: ");
					input.nextLine(); // Clears input for fileName.
					String fileName = input.nextLine();
					
					studentInfo.addDataFileName(fileName); // Adds fileName to studentInfo.
					studentInfo.saveSettings(); // Saves the new file name to the settings file.
				}else if(choice == 3) {
					// Choice three lets the user clear all the loaded files.
					studentInfo.clearDataFileNames();
					studentInfo.saveSettings(); // Saves the new file name to the settings file.
				}else if(choice == 4) {
					// Choice four let the user change the way the information is formated.
					studentInfo.changeOutputMode();
					studentInfo.saveSettings(); // Saves the new file name to the settings file.
				}else if(choice == 5) {
					// Choice five quits the program.
					break;
				}
			}
			input.close();
		}	
	}
	
	private void displayResults(Vector<Result> results, String mode) {
		System.out.println(""); // Adds an empty line to the top of the results output.
		if(mode.equals("block")) { // Checks if output should be in block format
			for(Result result : results) {
				for(int i = 0; i < result.getLabels().size(); i++) {
					System.out.println(result.getLabels().elementAt(i) + ": " + result.getData().elementAt(i));
				}
				System.out.println("");
			}
		}else { // If output is not in block more output in line mode.
			for(Result result : results) {
				String line = result.getData().elementAt(0);
				for(int i = 1; i < result.getData().size(); i++) {
					line += " | " + result.getData().elementAt(i);
				}
				System.out.println(line + "\n");
			}
		}
	}
}
