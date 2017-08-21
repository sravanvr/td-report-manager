/**
 * 
 * 12/27/2013 update:
 * To achieve the simplicity and enable the end-user to easily use this component the following design decisions were made with regards to 
 * "row" and "column" indexing of the excel (i.e. test data) sheet.
 * 1) Internal to this class both "row" and "column" indexing are zero based just as Apache POI implementation. This means internal to this class, in a given excel sheet the indexing starts with 0 th row.  * 		
 * 3) However to avoid any confusion to the end-user, the end-user is instructed to provide "1" based indexing.
 * 4) That means end-user facing methods MUST be called by supplying "1" based indexes of rows and columns.
 * 5) All methods such as "get", "getByID", "set", "setByID" are end-user facing methods and must be called using "1" based indexing of rows and columns.
 * 6) All methods such as "iget", "igetByID", "iset", "isetByID" are internal methods and must be called by using "0" based indexing similar to Apache POI, the core engine underneath this component.  
 * 
 * "rowCount" and "colCount" gets the total number of rows and columns filled in the sheet.
 * If 10 rows are filled in the Excel then "rowCount" would be 10. 
 * Internal to this class, "rowCount" can also be thought of as equivalent to "index of the last row" PLUS 1.
 * 
 * Usage: Above is to say that...row 4 represents the 5th row on a sheet.
 * 		TestData td = new TestData("MyExcel2.xls");
 * 		td.get(4,"Address") will get you data from the 4th row from "Address" column  
 *  
 * 08/20/2017:
 * This library assumes there are no blank columns in the header row (i.e. first row) of the table.
 * Example: Below you have blank coulmns between "Aka" and following "Aka".This library does not support this case.
 * ID	Name	Field	Country	Aka					Aka 
 * However, you could have blank columns in any other row other than 1st row.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 */

package io.sv.tools.testdatamanager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell ;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row ;
import org.apache.poi.ss.usermodel.Sheet ;
import org.apache.poi.ss.usermodel.Workbook ;
import org.apache.poi.ss.usermodel.WorkbookFactory ;
import org.apache.poi.ss.util.CellReference;


public class ExcelData implements TestData{
	
	int rowCount;
	int colCount;
	int lastColNum;
	String excelFile;
	public Map <String, Integer> headerMap;		
	int activeSheet;
	
	FileInputStream file;
	
	Workbook workbook;
	Sheet sheet;
	
	public int getRowCount() {
		return rowCount + 1; // for public use
	}

	// A dummy function to be consistent with the interface definition. Implement it when required.
	// 12/14/2013 - Implemented the function that returns the rowcount of a given column
	public int getRowCount(int col){
		int i =1;
		for (; i <= getRowCount(); i++){
			if (get(i, col) == "NO_VAL_AVAILABLE")
				break;
		}
		return (i-1);	
	}
	
	// 12/14/2013 - Implemented this function to return the row-count of a given column
	public int getRowCount(String colName){
		int col = headerMap.get(colName).intValue() + 1;
		int i =1;
		for (; i <= getRowCount(); i++){
			if (get(i, col) == "NO_VAL_AVAILABLE")
				break;
		}
		return (i-1);
	}
	
	/* 
	 * This method gets the row count of the specific column that is represented by column grid letter (i.e. the alphabetic letters in Excel on the top of each column)
	 *  @param colGridLetter The String of characters that represents the alphabet on the top of the required column in your excel
	 */
	public int egetRowCount(String colGridLetter){
		int col = CellReference.convertColStringToIndex(colGridLetter) + 1; //add 1 since we are passing it to "get()" and not "iget()"
		int i =1;
		for (; i <= getRowCount(); i++){
			if (get(i, col) == "NO_VAL_AVAILABLE")
				break;
		}
		return (i-1);
	}
	
	private void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	/*
	 * Gets the index (zero based in Apache POI) of the last cell contained in the very first row of the excel sheet PLUS ONE.
	 * In other words returns the total number of first 'n' consecutive non-empty columns in the 'first row' of the given excel-sheet.
	 * @see framework.TestData.TestData#getColCount()
	 */
	public int getColCount() {
		return colCount;
	}
	
	private void setColCount(int colCount) {
		this.colCount = colCount;
	}
	
	ExcelData(){
		
	}
	
	ExcelData(String fileName){
		//excelFile = "TestData//"+fileName;  //give more control to user on the location
		excelFile = fileName;
		headerMap = new HashMap<String, Integer>  ();					
		activeSheet = 0;
		importWorkbook();
		setActiveSheet(activeSheet);
		
	}
	
	ExcelData(String fileName, int activeSheet){		
		//excelFile = "TestData//"+fileName;
		excelFile = fileName;
		headerMap = new HashMap<String, Integer> ();		
		this.activeSheet = activeSheet;
		importWorkbook();
		setActiveSheet(activeSheet);
				
	}
	
	ExcelData(String fileName, String activeSheet){		
		//excelFile = "TestData//"+fileName;
		excelFile = fileName;
		headerMap = new HashMap<String, Integer> ();					
		importWorkbook();		
		setActiveSheet(activeSheet);
				
	}
	
	public void setActiveSheet(int activeSheet) {
		this.activeSheet = activeSheet;
		// Get first sheet from the workbook
	    sheet = workbook.getSheetAt(activeSheet);
	    setWorkSheetParams();
	}
	
	public void setActiveSheet(String activeSheet) {
		this.activeSheet = workbook.getSheetIndex(activeSheet);		
		//sheet = workbook.getSheetAt(this.activeSheet);
		setActiveSheet(this.activeSheet);
	}
	
	// This function is to provide a way for DataTable to access TestData
	public void load(String fileName){
		//excelFile = "TestData//"+fileName;
		excelFile = fileName;
		importWorkbook();
		setActiveSheet(0);
		setWorkSheetParams();
	}
	
	private void importWorkbook(){
		try {					
				file = new FileInputStream(new File(excelFile));
				    
			    //Get the workbook instance for XLS file 
			    //workbook = new HSSFWorkbook (file);
				workbook = WorkbookFactory.create(file);
			  				     
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/*
	 * Goals of this function are following
	 * 1) Find the index of the last row in the currently selected sheet
	 * 2) If the last row index is greater than zero (i.e. header exists) 
	 *    capture column headers for later use by the client 
	 */
	private void setWorkSheetParams(){
		
		Row row;
		// sheet.getLastRowNum() returns the index of the last row. 
		// With POI this number is indeterminate as to whether there are zero rows or only one row 
		// that exists in the Excel WHEN the number returned by this function is "0".
		
		setRowCount(sheet.getLastRowNum());		
		// "rowCount > 0" means there is at least one row. Now capture all the column names from the 0th row.
		// i.e. header row.
		if (rowCount > 0){
			row = sheet.getRow(0);		
			setColCount(row.getLastCellNum());
			lastColNum = getColCount();
			
			for (int i = 0; i < colCount; i++){
	        	headerMap.put(row.getCell(i).getStringCellValue(), Integer.valueOf(i));
			}

		}
		
	}
	
	public String get(int rowNum, int colNum){
		return (iget(rowNum -1, colNum - 1 ));
	}
	
	/*
	 * Use this method to get values from excel cells by representing the target cell by giving row number and the column grid letter labels.
	 * Ex: Get a value from 10th row and the column represented by "D" in the excel sheet.
	 */
	public String eget(int rowNum, String colGridLetter){
		int colNum = CellReference.convertColStringToIndex(colGridLetter);
		return iget(rowNum - 1, colNum);		
	}
	
	// Internal get method
	private String iget(int rowNum, int colNum){
		
		Row row;
		//if (rowNum <= rowCount && (colNum >=0 && colNum <= colCount)){
		if (rowNum <= rowCount && (colNum >=0)){
			row = sheet.getRow(rowNum);		// To deal with Excel created blank rows in place of deleted rows.
			if ((row != null) && (row.getCell(colNum, Row.CREATE_NULL_AS_BLANK).toString() !=  "")){
				Cell cell = sheet.getRow(rowNum).getCell(colNum, Row.RETURN_NULL_AND_BLANK);
				return (readCell(cell));				
			}				
			
			else return "NO_VAL_AVAILABLE";
		}
		else return "NO_VAL_AVAILABLE";
	}
	
	public String get(int rowNum, String colName){
		return (iget(rowNum -1, colName));
	}
	
	// Internal get method
	private String iget(int rowNum, String colName){
		
		int colNum = headerMap.get(colName).intValue();

		return (iget(rowNum, colNum));
	}
	
	public String getByID(String rowID, int colNum){
		return (igetByID(rowID, colNum - 1));
	}
	
	private String igetByID(String rowID, int colNum){
		
		if (isDuplicate(rowID)){
			System.out.println("ID supplied duplicate. Use a unique row ID reference.");
			return "DUPLICATE ID";
		}
		else{
			int rowNum = getRowIndexofID(rowID);
			//if (colNum >= 0 && colNum <= colCount){	// Design decision made to make "iget" method return cells beyond the "header row" based column count  
			if (colNum >= 0 ){
				if (rowNum >= 0){
					if (sheet.getRow(rowNum) != null && sheet.getRow(rowNum).getCell(colNum, Row.CREATE_NULL_AS_BLANK).toString() !=  ""){
						Cell cell = sheet.getRow(rowNum).getCell(colNum, Row.RETURN_NULL_AND_BLANK);
						return (readCell(cell));				
					}				
					
					else return "NO_VAL_AVAILABLE";
				}			
				else{
					System.out.println("ID supplied does not exist. Use a unique & existing row ID reference.");
					return "NO_VAL_AVAILABLE";
				}					
			}			
			else 
				return "NO_VAL_AVAILABLE";
		}
	}
	
	public String getByID(String rowID, String colName){
		
		int colNum = headerMap.get(colName).intValue();
		if (isDuplicate(rowID)){
			System.out.println("ID supplied duplicate. Use a unique row ID reference.");
			return "DUPLICATE ID";
		}
		else{
			int rowNum = getRowIndexofID(rowID);
			if (colNum >= 0 && colNum <= colCount){			
				if (rowNum >= 0){
					if (sheet.getRow(rowNum) != null && sheet.getRow(rowNum).getCell(colNum, Row.CREATE_NULL_AS_BLANK).toString() !=  ""){
						Cell cell = sheet.getRow(rowNum).getCell(colNum, Row.RETURN_NULL_AND_BLANK);
						return (readCell(cell));				
					}				
					
					else return "NO_VAL_AVAILABLE";
				}			
				else{
					System.out.println("ID supplied does not exist. Use a unique & existing row ID reference.");
					return "NO_VAL_AVAILABLE";
				}					
			}			
			else 
				return "NO_VAL_AVAILABLE";
		}
				
	}
	
	private String readCell(Cell cell){
		
		String retVal = "";
		
		switch (cell.getCellType()) {
	        
			 case Cell.CELL_TYPE_STRING:
	             retVal = cell.getRichStringCellValue().getString();
	             break;
	             
	         case Cell.CELL_TYPE_NUMERIC:
	             if (DateUtil.isCellDateFormatted(cell)) {
	                 retVal = cell.getDateCellValue().toString();
	             } else {
	                 retVal = formatDouble(cell.getNumericCellValue());
	                 //System.out.println("Double converted to String: "+ retVal);
	             }	             
	             break;
	             
	         case Cell.CELL_TYPE_BOOLEAN:
	             retVal = String.valueOf(cell.getBooleanCellValue());
	             break;
	             
	         case Cell.CELL_TYPE_FORMULA:
	             retVal = cell.getCellFormula();	             
	             break;
	             
	         default:
	             retVal = "Unknown Cell Type";
		}	
		return retVal;
	}
	
	public void set(int rowNum, int colNum, String value){
		iset(rowNum - 1, colNum - 1, value);
	}
	
	/*
	 * Use this method to set values in excel cells by giving row number and the column grid letter labels.
	 * Ex: Insert a value in 10th row and the column represented by "D" in the excel sheet.
	 */
	public void eset(int rowNum, String colGridLetter, String value){
		int colNum = CellReference.convertColStringToIndex(colGridLetter);
		iset(rowNum - 1, colNum, value);	
		
		if (colNum > lastColNum)
			lastColNum = colNum;
		
	}
	
	private void iset(int rowNum, int colNum, String value){
		if (rowNum <= sheet.getLastRowNum() + 1){
		//If no row exists..create a fresh row
		if (sheet.getRow(rowNum) == null){
			rowNum = sheet.getLastRowNum() + 1;
			sheet.createRow(rowNum);
		}
		// If a row exists but no column exists...then create a fresh column
		if (sheet.getRow(rowNum).getCell(colNum, Row.CREATE_NULL_AS_BLANK).toString() == ""){
			 sheet.getRow(rowNum).createCell(colNum);
		}		
		// By now you should have row and column
		sheet.getRow(rowNum).getCell(colNum).setCellValue(value);
		}
		else			
			isetWideRange(rowNum, colNum, value);
	}

	/*
	 * 12/28/2013: This newly added method is useful to insert (or set) data into the cells that are out side of the expected rectangular table.
	 * Ex: Generally a test data sheet is expected to be in the format of "m X n" matrix (or table) with "m" rows and "n" columns. However a user may
	 * deviate from the standard and popular format and may choose to insert a value outside of this tale. For example a user may want to insert 
	 * a value into an excel into "10th row and 15th column" where the test data table has 4 rows and 5 columns.  
	 */
	private void isetWideRange(int rowNum, int colNum, String value){
		int lastRow = sheet.getLastRowNum();
		int rowPointer  = lastRow + 1; 
		
		for (; rowPointer <= rowNum; rowPointer++ )
			sheet.createRow(rowPointer);
		
		int col = 0;
		for (; col < colNum + 1; col++)
			sheet.getRow(rowPointer -1).createCell(col);
		
		sheet.getRow(rowPointer -1).getCell(col -1).setCellValue(value);
		
	}
	
	public void set(int rowNum, String colName, String value){
		iset(rowNum - 1, colName, value);
	}
	
	private void iset(int rowNum, String colName, String value){
		
		int colNum = headerMap.get(colName).intValue();
		//If no row exists..create a fresh row
		if (sheet.getRow(rowNum) == null){
			rowNum = sheet.getLastRowNum() + 1;
			sheet.createRow(rowNum);
		}		
		iset(rowNum, colNum, value);
		
	}
	
	public void setByID(String rowID, int colNum, String value){
		isetByID(rowID, colNum - 1, value);
	}
	
	private void isetByID(String rowID, int colNum, String value){
				
		if (isDuplicate(rowID)){
			System.out.println("ID supplied duplicate. Use a unique row ID reference.");
		}
		else{
			int rowNum = getRowIndexofID(rowID);
			
			//If no row exists..create a fresh row
			if (sheet.getRow(rowNum) == null){
				rowNum = sheet.getLastRowNum() + 1;
				//rowNum = sheet.getPhysicalNumberOfRows();
				Row row = sheet.createRow(rowNum);
				System.out.println("max rows: "+ sheet.getLastRowNum());
				row.createCell(0).setCellValue(rowID);
				row.createCell(colNum).setCellValue(value);
			}
			// If a row exists but no column exists...then create a fresh column
			else{ 
				if (sheet.getRow(rowNum).getCell(colNum, Row.CREATE_NULL_AS_BLANK).toString() == "")
					 sheet.getRow(rowNum).createCell(colNum);
				// By now you should have row and column
				sheet.getRow(rowNum).getCell(colNum).setCellValue(value);
			}
		}
	}

	public void setByID(String rowID, String colName, String value){
		
		if (isDuplicate(rowID)){
			System.out.println("ID supplied duplicate. Use a unique row ID reference.");
		}
		else{
			int colNum = headerMap.get(colName).intValue();			
			isetByID(rowID, colNum, value);
		}
	}
	
	private int getRowIndexofID(String id){
		Row row;
		int foundIndex = -1;
		for (int i = 0; i <= sheet.getLastRowNum(); i++){
			//To deal with Excel created blank rows in place of deleted rows.
			row = sheet.getRow(i);
			  if ( row != null && (row.getCell(0) != null) && (readCell(row.getCell(0)).equals(id))){
				  foundIndex = i;
				  break;
			  }
		   }
		return foundIndex;
	}
	
	private boolean isDuplicate(String idVal)
	{ 
	   
	   List<String> idList = new ArrayList<String> ();
	   // 0th row is table header
	   for (int i = 1; i <= sheet.getLastRowNum(); i++){
		   if (sheet.getRow(i) != null && sheet.getRow(i).getCell(0) != null) //To deal with Excel created blank rows in place of deleted rows.
			   idList.add(readCell(sheet.getRow(i).getCell(0)));
	   }
	   
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
	
	private static String formatDouble(double d)
	{
	    if(d == (int) d)
	        return String.format("%d",(int)d);
	    else
	        return String.format("%s",d);
	}
	
	public void close(){
		// Define simple cell formatting rule		
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		
		// Set the style to each cell that exists. 
		for (int i = 0; i < workbook.getNumberOfSheets(); i++){
			sheet = workbook.getSheetAt(i);
			for (Row row: sheet){
				if (row != null){
					for (Cell cell: row){
						if (cell != null)
							cell.setCellStyle(style);
					}
				}
				
			}
		}
		
		// Loop through each sheet in the workbook and adjust column size.
		for (int i = 0; i < workbook.getNumberOfSheets(); i++){
			sheet = workbook.getSheetAt(i);
			for (int j=0; j < lastColNum; j++)
				sheet.autoSizeColumn(j);
			
			/* Since the design decision is made to enable user to freely "set" data anywhere in the excel (user is not required to confine to just 
			 * inside the matrix) the below code is not necessary.
			Row row = sheet.getRow(0);
			for (int j = 0; (row != null) && j < row.getLastCellNum(); j++){				
				if (row.getCell(j) != null) 
					sheet.autoSizeColumn(j);
			}
			*/
		}
		
		try{
			file.close();
			 FileOutputStream out = 
			        new FileOutputStream(new File(excelFile));
			 workbook.write(out);
			 out.close();
			 
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
	}


}
