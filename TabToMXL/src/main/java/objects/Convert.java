package objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Convert {
	
	public void convert() { 
		
		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					  "		<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.1 Partwise//EN" "http://www.musicxml.org/dtds/partwise.dtd\">";
		//trying to make code for conversion
		//idea1: create a loop and get the variable name for each beginning and ending
		//idea2: brute force like write it all out, but its inefficient and bad (write line by line)
		//idea3: use the textfile as a template
		//for (int i = 0; i <= )
		
		
		try {
		      FileWriter myWriter = new FileWriter("filename.txt");
		      myWriter.write(text);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
}
