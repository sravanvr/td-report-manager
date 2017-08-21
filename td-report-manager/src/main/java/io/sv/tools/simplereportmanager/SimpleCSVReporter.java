/**
 * 
 * Implements CSV reporting functions.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */

package io.sv.tools.simplereportmanager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class SimpleCSVReporter implements SimpleReporter{

	private int colCount;		
	private String csvFile;	
	private int rowIndex;
	private String [] header;
	private CSVWriter writer;
	private boolean append;
	
	public SimpleCSVReporter(String fileName) {

		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("//") || fileName.contains("\\"))
			this.csvFile = fileName;
		else
			this.csvFile = "Reports//"+ fileName;
		
		this.header = null;
		this.append = false;
		
		createCSVFile();
	}
	
	public SimpleCSVReporter(String fileName, boolean append) {

		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("//") || fileName.contains("\\"))
			this.csvFile = fileName;
		else
			this.csvFile = "Reports//"+ fileName;
		
		this.header = null;
		this.append = append;
		
		createCSVFile();		
	}
	
	public SimpleCSVReporter(String fileName, String [] header) {

		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("//") || fileName.contains("\\"))
			this.csvFile = fileName;
		else
			this.csvFile = "Reports//"+ fileName;
		
		this.header = header;	
		this.append = false;
		
		createCSVFile();
		
	}
	
	public SimpleCSVReporter(String fileName, String [] header, boolean append) {
		
		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("//") || fileName.contains("\\"))
			this.csvFile = fileName;
		else
			this.csvFile = "Reports//"+ fileName;
		
		this.header = header;	
		this.append = append;
		
		createCSVFile();
	}
	
	private void createCSVFile(){
		try {
			if (this.append)
				writer = new CSVWriter(new FileWriter(csvFile, this.append), ',');
			else
				writer = new CSVWriter(new FileWriter(csvFile), ',');
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		if (header != null)
			writeHeader();
	}
	
	// Write the header passed through the constructor
	private void writeHeader(){
		writer.writeNext(header);
	}
	
	// Allow user write header
	public void writeHeader(String [] header){
		writer.writeNext(header);
	}
	
	public void report(String [] data){
		writer.writeNext(data);
	}
	
	// This is just a repetitive method as the one below.
	// Serves nothing special except to comply with the SimpleReporter interface. 
	
	public void report(List <String []> data, boolean listContainsHeader){
		writer.writeAll(data);
	}
	
	public void report(List <String []> data){
		writer.writeAll(data);
	}
	
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		try {	// Give 1 Sec. time just to let the above created file get saved.
			Thread.sleep(1000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		
		//openExcelWhenDone(csvFile); Enable this when required.
	}
	
	public void setFormatColumn1(String formatColumn1){
		// This is a dummy method to comply with SimpleReporter interface
	}
	
	public void setFormatColumn2(String formatColumn2){
		// This is a dummy method to comply with SimpleReporter interface	
	}
	
	private void openExcelWhenDone(String file){
		Desktop dt = Desktop.getDesktop();
		try {
			dt.open(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
