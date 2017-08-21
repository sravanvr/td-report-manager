/**
 *  
 * Use this SimpleTextReporter to report simple key/value pairs of data in the text format.
 * Do Not forget to call close() method at the end of your reporting in order to save the file.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */
package io.sv.tools.simplereportmanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SimpleTextReporter {

	private String fileName;
	private BufferedWriter writer = null;
	private String reportFormat = "%-20s = %-20s %n";
	
	// If a report file path is not passed, a report text file will be created in 'Reports' directory.
	public SimpleTextReporter(){
		fileName = "Reports/" + new SimpleDateFormat("MMM_dd_yyyy_HHmmss_sss").format(Calendar.getInstance().getTime()) + ".txt";
		createFile();
	}

	// User can pass the location where the report file needs to be created.
	// Ex: c:/mytemp
	// Report file will be created in the specified location by naming the file with the current date and time as seen in the code below.
	public SimpleTextReporter(String fileName){
		if( ! fileName.endsWith(".txt"))
			fileName = fileName +"/" + new SimpleDateFormat("MMM_dd_yyyy_HHmmss_sss").format(Calendar.getInstance().getTime()) + ".txt";
		this.fileName = fileName;
		createFile();
	}
	
	private void createFile(){
		 File reportFile = new File(fileName);
		 final File parent_directory = reportFile.getParentFile();

		 if (null != parent_directory){
		     parent_directory.mkdirs();
		 }
		 
		 try {
		
			 writer = new BufferedWriter(new FileWriter(reportFile));
		
		 } catch (IOException e) {			
			e.printStackTrace();
		}	 
		 
	}
	
	public void report(String key, String val){
		try {
			writer.write(String.format(reportFormat, key, val));
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
       SimpleTextReporter rep = new SimpleTextReporter("c:/mytemp");
       rep.report("Start Time", "Nov 3 2013 4:15PM");
       rep.report("End Time", "Nov 3 2013 4:15PM");
       rep.report("Total Duration", "2 hrs 30 mins");
       rep.report("Build number", "ANANS SHJHDSUU6787889");
       		       		
       rep.close();
    }
    
}
