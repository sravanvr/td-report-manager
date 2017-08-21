/**
 *  
 * Purpose: Use this from TestData interface for all CSV based test data needs and test data management.
 * Always remember, the end-user must use "1" based indexing. i.e. indexing begins with "1"
 * Where as internally always use "0" based indexing.
 * 2/2/2014:
 * All issues in this library have been resolved and fully tested. Now both Excel and CSV Test Data components works in the same way consistently.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */
package io.sv.tools.testdatamanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.util.CellReference;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSVData implements TestData{
	
	private String csvFile = null;
	private String csvOutFile = null;
	private boolean outFileExists = false;
	private int rowCount;
	private	int colCount;
	private List <String[]> csvList;
	private List <String> idList;
	private Map <String, Integer> headerMap;
	
	CSVData(){		
		csvFile = "MyData.csv";
		headerMap = new HashMap<String, Integer>  ();
		readCSV();
	}
	
	CSVData(String csvFile){	
		
		this.csvFile = csvFile;
		headerMap = new HashMap<String, Integer>  ();
		readCSV();
	}
	
	// Use this two write output in a different file
	CSVData(String csvInFile, String csvOutFile){	
		
		this.csvFile = csvInFile;
		this.csvOutFile = csvOutFile;
		outFileExists = true;
		headerMap = new HashMap<String, Integer>  ();
		readCSV();
	}
	
	// Use this constructor to load a List<String[]> of CSV data
	CSVData(String csvFile, List <String[]> csvList){
		this.csvFile = csvFile;
		this.csvList = csvList;
		headerMap = new HashMap<String, Integer>  ();
		loadCSV();
	}
	
	public int getRowCount() {
		// Restrict user's starting index to be "1". 
		// In the loop use "<=" condition to check the the upper limit. Ex: the condition for looping thru 20 rows shall be written as for(i=1; i<=20; i++)
		// Internally maintain zero based indexing.    
		return rowCount;
	}

	/*
	 * Return the Excel indexed row number before the occurance of a non-blank cell in a particular column 
	 * User must always use "1" based indexing. 
	 * @ param colName Name of the column for which the row count is required.
	 * @ return row count
	 */
	
	public int getRowCount(String colName){
		int colNum = headerMap.get(colName);
		return getRowCount(colNum);
	}
	
	// Return the Excel indexed row number before the occurance of a non-blank cell in a particular column 
	// User must always use "1" based indexing.
	public int getRowCount(int col) {
		String [] nextLine = null;
		int rowNumOfCol = -1;
		if (col-1 < getColCount()){
			// Restrict user index from "1". 
			// In the loop use "<=" condition.
			// Internally maintain zero based indexing.    
			 for (int i = 0; i < csvList.size(); i++){
			   nextLine = (String[]) csvList.get(i);
			   //System.out.println("Value is: "  + nextLine[col - 1] );
				   if (nextLine[col - 1].equals("")){
					   rowNumOfCol = i;
					   break;
				   }		   	   
			  }
			
			 if (rowNumOfCol > 0)
				 return rowNumOfCol;
			 else
				 return rowCount;
		}
		else
			return rowNumOfCol;
		
	}
	
	// Gets row count based on the column grid letter (the alphabet on the top of the excel column)
	public int egetRowCount(String colGridLetter){
		int col = CellReference.convertColStringToIndex(colGridLetter) + 1;		
		return (getRowCount(col));
	}
	
	private void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	// colCount is set to '1'more than the number of columns in CSV sheet.
	// If number of columns filled in CSV is '5' then this method returns '6'.
	public int getColCount() {
		// Add 1 to colCount to allow "1" based indexing for the client. 
		// Internally maintain zero based indexing.    
		return colCount;
	}

	private void setColCount(int colCount) {
		this.colCount = colCount;
	}
	
	private void readCSV(){
		try {			 
			 
			 String [] nextLine;			
			// Read CSV file using Opeb CSV
			 CSVReader reader = new CSVReader((new BufferedReader(new FileReader(csvFile))));
			 // Read all contents into a List
			 csvList = reader.readAll(); 
			 // Set row count and column count to be consumed by the clients
			 setRowCount(csvList.size());
			 String [] header = (String[])csvList.get(0);
			 // colCount will be set to array length which means '1' more than the number of filled in columns of CSV  
			 setColCount(header.length);
			 
			 for (int i = 0; i < header.length; i++){
			   headerMap.put(header[i], i);
			 }
			
			 //Load explicit IDs into a list for later use.
			 loadExplicitIDList();
			    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Use this function to load CSV data that already exists in a List
	// The goal is to let user manipulate data as and when needed and finally save the dats
	// to the file passed by the user.
	
	private void loadCSV(){
		setRowCount(csvList.size());
		 String [] header = (String[])csvList.get(0);
		 setColCount(header.length);
		 
		 for (int i = 0; i < header.length; i++){
		   headerMap.put(header[i], i);
		 }
	}
	
	public String get(int rowNum, int colNum){
		// Subtract 1 from rowNum and colNum that comes from outside i.e. client.
		rowNum = rowNum - 1;
		colNum = colNum - 1;
		if (rowNum <rowCount && colNum < colCount)
			return (csvList.get(rowNum)[colNum]);
		else 
			return "No_VAL_EXIST";
	}
	
	public String get(int rowNum, String colName){
		// Subtract 1 from only rowNum. No Need to subtract from column as it was calculated internally.
		rowNum = rowNum - 1;		
		int colNum = headerMap.get(colName);		
		return (csvList.get(rowNum)[colNum]);
	}

	// Remember internally everything across the board is "zero based indexing".
	public void set(int rowNum, int colNum, String val){
		// Start by converting rowNum and colNum to internal convention - zero based indexing. Subtract 1 from both.
		rowNum = rowNum - 1;
		colNum = colNum - 1;

		if(rowNum < rowCount && colNum < colCount){ // that means within the boundaries of the matrix/table of data entered in the CSV.
			String [] temp = csvList.get(rowNum);
			temp[colNum] = val;		
			csvList.set(rowNum, temp);	
		}
		else{ //if either rowNum or colNum happens to be outside of the boundaries

			// At this time just add a new row at the end even though the rowNum specified by the user is far off.
			if (rowNum >= rowCount && colNum < colCount){  // >= operator bcoz of zero based indexing
				
				// add empty rows
				for (int i = rowCount; i < rowNum-1; i++ ){
					csvList.add(i, new String[colCount]); 
				}
				
				String [] temp = new String[colCount];
				temp[colNum] = val;		
				csvList.add(rowNum, temp);	
				// Since a new is added, reset the rowCount again
				setRowCount(csvList.size());
			}
			
			else if (rowNum >= rowCount && colNum >= colCount){
				// add empty rows
				for (int i = rowCount; i < rowNum; i++ ){
					csvList.add(i, new String[colCount]); 
				}
				setRowCount(rowNum + 1);
				
				String [] temp = new String[colNum+1];				
				temp[colNum] = val;		
				csvList.add(rowNum, temp);
				// Since a new is added, reset the rowCount again
				setRowCount(csvList.size());

			}
			
			else if (rowNum < rowCount && colNum >= colCount){
				String []holder = csvList.get(rowNum);

				if (holder.length > colNum){	// Always check this condition bcoz every row could have different number of columns. A new column might have been added by the user just in the previous step.
					holder[colNum] = val;		
					csvList.set(rowNum, holder);
				}
				else{	// i.e. user specified column is truely bigger than total (perhaps added) columns in that row
					String [] temp = new String[colNum+1];
					System.arraycopy(holder, 0, temp, 0, holder.length);
					temp[colNum] = val;		
					csvList.set(rowNum, temp);					 
				}
			}
		}

	}
	
	public void set(int rowNum, String colName, String val){
		rowNum = rowNum - 1;		
		int colNum = headerMap.get(colName); //It's already zero based indexing no need to subtract 1 from colNum
		set(rowNum, colNum, val);
		
	}

	// Sets based on the column grid letter (the alphabet on the top of the excel column)
	public void eset(int rowNum, String colGridLetter, String value){		
		int colNum = CellReference.convertColStringToIndex(colGridLetter) + 1;
		set(rowNum, colNum, value);
	}
	
	// Gets based on the column grid letter (the alphabet on the top of the excel column)
	public String eget(int rowNum, String colGridLetter){
		int colNum = CellReference.convertColStringToIndex(colGridLetter) + 1;		
		return get(rowNum, colNum);
	}
	
	public String getByID(String rowID, int colNum){		
		colNum = colNum - 1;
		if (isDuplicate(rowID)){
			System.out.println("Duplicate ID. Ensure explicit ID is unique.");
			return "VALUE_NOT_AVAILABLE";
		}			
		else{
			return csvList.get(getRowIndexofID(rowID))[colNum];
		}	
	}
	
	public String getByID(String rowID, String colName){
		int colNum = headerMap.get(colName);
		if (isDuplicate(rowID)){
			System.out.println("Duplicate ID. Ensure explicit ID is unique.");
			return "VALUE_NOT_AVAILABLE";
		}			
		else{
			return csvList.get(getRowIndexofID(rowID))[colNum];
		}	
	}
	
	public void setByID(String rowID, int colNum, String val){
		colNum = colNum - 1;
		// Check if the ID supplied does exist
		if (idList.contains(rowID)){
			int rowNum = getRowIndexofID(rowID);
			String [] temp = csvList.get(rowNum);
			temp[colNum] = val;		
			csvList.set(rowNum, temp);	
		}
		else{
			System.out.println("Explicit ID does not exist. record can not be inserted.");			
		}
			
	}
	
	public void setByID(String rowID, String colName, String val){
		int colNum = headerMap.get(colName);
		colNum = colNum - 1;
		// Check if the ID supplied does exist
		if (idList.contains(rowID)){
			int rowNum = getRowIndexofID(rowID);
			String [] temp = csvList.get(rowNum);
			temp[colNum] = val;		
			csvList.set(rowNum, temp);	
		}
		else{
			System.out.println("Explicit ID does not exist. record can not be inserted.");			
		}
	}
	
	private int getRowIndexofID(String id){
		int rowIndex = -1;
		for (int i = 0; i < idList.size(); i++){
			if (idList.get(i).equals(id)){
				rowIndex = i;
				break;
			}			
		}		
		return rowIndex;
	}
	
	private void loadExplicitIDList(){
		idList = new ArrayList<String>();
		  
		   for (int i = 0; i < csvList.size(); i++){
			   String []nextLine = csvList.get(i);
			   idList.add(nextLine[0]);
		   }
	}
	
	private boolean isDuplicate(String idVal)
	{ 	   
	   final Set<String> duplicateSet = new HashSet(); 
	   final Set<String> set = new HashSet();

	   for (String x : idList)
		  {
		   if (!set.add(x))
		   {
			   duplicateSet.add(x);
		   }
		 }
		 
	   if (duplicateSet.contains(idList))
		   return true;
	   else
		   return false;	  
	}
	
	public void close(){
		CSVWriter writer;
		try {
			if (outFileExists)
				writer = new CSVWriter(new FileWriter(csvOutFile), ',');
			else
				writer = new CSVWriter(new FileWriter(csvFile), ',');
			writer.writeAll(csvList);
			writer.close();	
		} catch (IOException e) {			
			e.printStackTrace();
		}     
	    
	   
	}

	// The following two dummy methods are created to satisfy the TestData interface
	// There is no real use of these methods
	// Consider looking for a better design than this - future task
	
	public void setActiveSheet(int activeSheet){
		// DUMMY METHOD
	}
	public void setActiveSheet(String activeSheet){
		//DUMMY METHOD
	}

}
