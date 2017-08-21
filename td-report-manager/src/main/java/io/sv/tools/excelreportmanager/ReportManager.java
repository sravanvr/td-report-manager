/**
 * 
 * User must instantiate ReportManager by passing report file name. ReportManager object maintains two  key properties "rowIndex and colCount" to keep track of row index 
 * and column count of the active work-sheet (It could be a sheet that is set by the the user as active or a sheet that is newly created and became active by default ) 
 * at the the given moment. Constructor is responsible to initialize these two important properties along with other properties and create POI Workbook. At any given point of 
 * time there is one active-sheet in the workbook that's being used by the user to send reporting data. Whenever a sheet becomes active-sheet either newly created or set 
 * by user as active, the first thing we do is compute rowIndex and colCount and make them available to Report Manager.  
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */

package io.sv.tools.excelreportmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell ;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row ;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportManager {
	
	private int activeSheet;	
	private String excelFile;
	private String sheetName;
	private FileOutputStream file;
	//private List header;
	private String[] header;
	private Map <String, Integer> sheetMap;
	private Map <String, CellStyle> styleMap;
	boolean summaryExist;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private Cell cell;
	private int colCount;
	private int rowIndex;
	private int sheetIndex;	
	private CreationHelper createHelper;
	private Footer footer;
	private static int totSteps = 0, totPassed = 0, totFailed = 0;
	private double startTime, stopTime, elapsedTime;
	
	/* 
	 * User must instantiate ReportManager by passing report file name. ReportManager object maintains two  key properties "rowIndex and colCount" to keep track of row index 
	 * and column count of the active work-sheet (It could be a sheet that is set by the the user as active or a sheet that is newly created and became active by default ) 
	 * at the the given moment. Constructor is responsible to initialize these two important properties along with other properties and create POI Workbook. At any given point of 
	 * time there is one active-sheet in the workbook that's being used by the user to send reporting data. Whenever a sheet becomes active-sheet either newly created or set 
	 * by user as active, the first thing we do is compute rowIndex and colCount and make them available to Report Manager.  
	 * 
	 */
	public ReportManager(String fileName){
		startTime = System.currentTimeMillis();
		System.out.println("start " + startTime);
		this.excelFile = fileName;
		rowIndex = 0;	
		sheetIndex = 0;
		sheetMap = new HashMap <String, Integer>();		
		styleMap = new HashMap <String, CellStyle>();
		createWorkBook();
		styleMap = (new CellStyles(workbook)).createStyles();
		summaryExist = false; //Assume there is no summary sheet
		
	}
	
	// Can create both XLS as as well XLSX type workbooks depending on the file extension user passed.
	private void createWorkBook(){
		if (excelFile.endsWith("xlsx"))
			workbook = new XSSFWorkbook();
		
		else
			workbook = new HSSFWorkbook();
		createHelper = workbook.getCreationHelper();
	}
	
	/*
	 * User must create a sheet by calling this method before he can begin reporting. This method creates a new sheet in the workbook and makes it ACTIVE-SHEET by default. 
	 * However user can change this default behavior and make another sheet as active-sheet by making a call to "setActiveSheet(String sheetName)" method explicitly. 
	 * Note that reporting data will always be sent to the active-sheet.
	 * Importantly sets active sheet, writes header, and initializes "rowIndex AND colCount" to track rows and columns of active work-sheet. 
	 */
	public void createSheet(String sheetName, String[] header ){		
		//Create specified sheet
		try{
			sheet = workbook.createSheet(sheetName);			
			//sheetMap.put(sheetName, Integer.valueOf(sheetIndex++));  //there is a better way as below			
			sheetMap.put(sheetName, Integer.valueOf(workbook.getSheetIndex(sheetName)));
		}
		catch (java.lang.IllegalArgumentException e){
			System.out.println("Sheet exists");
		}
		this.header = header;
		colCount = header.length;
		rowIndex = 0; // This seems to be a limitation in POI. Can not tell row count correctly when there are zero rows.
		writeHeader(header);	
		
	}
	
	/*
	 * Overloaded method to above method. Creates a new sheet in the workbook and makes it ACTIVE-SHEET by default. User can also pass the location of the work-sheet where he wants to 
	 * create the new sheet. The new sheet will be placed in the order specified by the "loc" variable. If there are three sheets
	 * at a given time and "loc = 1" then the the new sheet will be placed at index 1 in the order.  
	 */
	public void createSheet(String sheetName, String [] header, int loc){
		int numOfSheets = workbook.getNumberOfSheets();
					
		//Create specified sheet
		try{
			sheet = workbook.createSheet(sheetName);			
			//sheetMap.put(sheetName, Integer.valueOf(sheetIndex++));	//there is a better way as below
			sheetMap.put(sheetName, Integer.valueOf(workbook.getSheetIndex(sheetName)));
		}
		catch (java.lang.IllegalArgumentException e){
			System.out.println("Sheet exists");
		}
		
		if  (numOfSheets > loc){			
			workbook.setSheetOrder(sheetName, loc);
		}
		else{
			System.out.println("Invalid location");
		}
		
		this.header = header;
		colCount = header.length;
		rowIndex = 0; // This seems to be a limitation in POI. Can not tell row count correctly when there are zero rows.
		writeHeader(header);	

	}
	
	private void writeHeader(){
		report(header);
	}
	
	// User can explicitly call this method and make any specific sheet as active-sheet. By default the newly created sheet becomes the active sheet.
	// This method will re-initialize rowIndex and colCount variables.
	
	public void setActiveSheet(String sheetName){		
		sheet = workbook.getSheet(sheetName);
		setRowIndex(sheetName);
		colCount = sheet.getRow(0).getLastCellNum();
	}
	
	private void setRowIndex(String sheetName){
		rowIndex = workbook.getSheet(sheetName).getLastRowNum() + 1;
	}

	private void writeHeader(String[] data){
		
		if (data.length != colCount ){
			System.out.println("Invalid number of items in the report data");				
		}
		else{
			row = sheet.createRow(rowIndex++);			
			for (int i = 0; i < data.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(createHelper.createRichTextString(data[i]));
				cell.setCellStyle(styleMap.get("header"));
			}	
		}
	}
	
	private Hyperlink createHyperLink(String sheet){
		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		link.setAddress("'"+sheet+"'!A1");
		return link;
	}
	
	// This is the key method user calls to send reporting data to Report Manager. 
	// Accepts reporting parameters (or data) in the form of a list.
	public void report(List <String> data){
		
		if (data.size() != colCount ){
			System.out.println("Invalid number of items in the report data");				
		}
		else{
			row = sheet.createRow(rowIndex++);			
			for (int i = 0; i < data.size(); i++){
				cell = row.createCell(i);
				cell.setCellValue(data.get(i));				
				if ((i==0) && data.get(data.size() - 1) == "FAIL")
					cell.setCellStyle(styleMap.get("failed"));
				else
					cell.setCellStyle(styleMap.get("allCells"));
			}	
		}
	
	}
	
	// This is the key method user calls to send reporting data to Report Manager. 
	// Accepts reporting parameters (or data) in the form of an array.
	public void report(String[] data){
		
		if (data.length != colCount ){
			System.out.println("Invalid number of items in the report data");				
		}
		else{
			row = sheet.createRow(rowIndex++);			
			for (int i = 0; i < data.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(data[i]);				
				if ((i==0) && data[data.length - 1] == "FAIL")
					cell.setCellStyle(styleMap.get("failed"));
				else
					cell.setCellStyle(styleMap.get("allCells"));
			}	
		}
	
	}

	public void createSummary(ISummary summary){
		summaryExist = true;
		workbook = summary.createSummary(workbook, styleMap);		
		
	}
	// Utility method to gather stats to display Sheet Summary	
	private void getTestStats(Sheet sheet, int totResultRowsInSheet){
		/* 
		 * note: sheet.getLastRowNum() returns the index of last row.
		 * Since first (zero'th) row is header row, start gathering stats from row 1 
		 * More than 1 physical number of rows should exist to gather the stats; one row i.e. header always there
		*/
		
		//for (int row = 1; (row <= sheet.getLastRowNum()) && (sheet.getPhysicalNumberOfRows() > 1); row++){
		for (int row = 1; (row <= totResultRowsInSheet) && (sheet.getPhysicalNumberOfRows() > 1); row++){
			if (sheet.getRow(row).getCell(4).getStringCellValue().equals("PASS"))
				totPassed++;
			else if (sheet.getRow(row).getCell(4).getStringCellValue().equals("FAIL"))
				totFailed++;
			totSteps++;
		}
	}
	
	// Creates a new row and formats specific target cells t1 and t2 with required border and font
	// if blank = true, format the cell like a general data cell not like a header cell 
	private void createRow(boolean blank, int t1, int t2 , int totalCells){
		row = sheet.createRow(rowIndex++);		
		for (int col = 0; col < totalCells; col++){
			cell = row.createCell(col);
			if ((col == t1 || col == t2) && blank){
				cell.setCellStyle(styleMap.get("summCells"));}
		}
	}
	
	// Creates cells (which are currently unavailable because they were not already created) in an already existing row.
	// Note: With Apache POI, every excel row and cell needs to be specifically created. In a given row only those no of cells which were created
	// programmatically are available to access.
	// if blank = true, format the cell like a general data cell not like a header cell 
	private void createCellsInRow(boolean blank, int t1, int t2 , int totalCells){
		// row = sheet.createRow(rowIndex++);
		if (rowIndex <= sheet.getLastRowNum()){
			row = sheet.getRow(rowIndex++);
			for (int col = t1; col <= totalCells ; col++){
				cell = row.createCell(col);
				if ((col == t1 || col == t2) && blank){
					cell.setCellStyle(styleMap.get("summCells"));}
			}
		}
		else{
			
			createRow(blank, t1, t2, totalCells);
		}
	}
	
	private void createSheetSummary(Sheet sheet){
		
		rowIndex = 0; // Reset row index
		int tc1, tc2, totalCells; // Two target cells which holds the cell numbers where Sheet summary should be printed
		
		tc1 = sheet.getRow(0).getLastCellNum();	// Target cell-1
		tc2 = tc1+1;	// Target cell 2
		totalCells = tc1 + 2; // Total number of cells in first row counting from the first cell including newly added two target cells.
		int actualRowsInSheet = sheet.getLastRowNum(); // Preserve total result rows to scan thru and capture stats as asfter adding Sheet Summary 
		// result rows could be smaller number less than "4". Remember Sheet Summary takes 4 rows.
		
		sheet.setColumnWidth(tc1, 30 * 256);
		sheet.setColumnWidth(tc2, 30 * 256);
		
		createCellsInRow(true, tc1, tc2, totalCells);	//First row
		Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy");
	    
	    cell = row.getCell(tc1);
	    cell.setCellStyle(styleMap.get("header"));	    
		cell.setCellValue("Test Summary"+"- "+ ft.format(dNow));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), tc1, tc2));
			
		getTestStats(sheet, actualRowsInSheet);
		
		createCellsInRow(true, tc1, tc2, totalCells);	//Second row
		
		row.getCell(tc1).setCellValue("Total Test Cases");
		row.getCell(tc2).setCellValue(totSteps);
		
		createCellsInRow(true, tc1, tc2, totalCells);	//Third row	
				
		row.getCell(tc1).setCellValue("Total Passed");
		row.getCell(tc2).setCellValue(totPassed);
		
		createCellsInRow(true, tc1, tc2, totalCells);	//Fourth row
		
		row.getCell(tc1).setCellValue("Total Failed");
		if (totFailed > 0) 
			row.getCell(tc2).setCellStyle(styleMap.get("summFailed"));
		row.getCell(tc2).setCellValue(totFailed);
		
		totSteps = totPassed = totFailed = 0; // Sheet's summary preparation done. Now reset stat counts. 
	}
	
	public void close(){
		// auto sizing a column is heavy weight process. Run it at the end of the processing.
		// Loop through each sheet of the workbook and auto size each column in the selected sheet
		// Ignore zero'th index which is Summary sheet

		int start;
		if (summaryExist) {
			start = 1;		
		}
		else start = 0;

		for (int i = start; i < workbook.getNumberOfSheets(); i++){
			sheet = workbook.getSheetAt(i);
			for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++){
				sheet.autoSizeColumn(j);
			}

			if (summaryExist){
				Row row = sheet.getRow(0);
				// Create two target cells in each sheet after the last column of the very first row to store the "back" button  
				int tc1 = row.getLastCellNum(); //Target Cell1
				int tc2 = row.getLastCellNum() + 1; //Target Cell2 
				row.createCell(tc1);
				row.createCell(tc2);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, tc1, tc2));
				Cell cell = sheet.getRow(0).getCell(tc1);
				cell.setCellValue("[Back To Summary]");
				cell.setHyperlink(createHyperLink(workbook.getSheetName(0)));
				cell.setCellStyle(styleMap.get("hyperlink"));
			}
			// Print Sheet Summary on the right handside on the sheet.
			createSheetSummary(sheet);
			//createSummary(new SheetSummary());
		}

		// Perform sheet level display settings and Conditional formatting

		for (int i = 0; i < workbook.getNumberOfSheets(); i++){
			sheet = workbook.getSheetAt(i);
			//turn off grid lines
			sheet.setDisplayGridlines(false);
			sheet.setPrintGridlines(false);        
			sheet.setFitToPage(true);
			sheet.setHorizontallyCenter(true);
			sheet.setAutoFilter(CellRangeAddress.valueOf("A1:C5"));

			// Create conditional formatting for PASS and FAIL for each sheet
			SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();	     

			// Define Pass Rule
			ConditionalFormattingRule passRule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL,  "\"PASS\"");
			FontFormatting passFormat = passRule.createFontFormatting();
			passFormat.setFontColorIndex(IndexedColors.GREEN.index);	        

			ConditionalFormattingRule failRule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL,  "\"FAIL\"");
			FontFormatting failFont = failRule.createFontFormatting();
			failFont.setFontColorIndex(IndexedColors.RED.index);
			failFont.setFontStyle(false, true);

			// Define region
			CellRangeAddress[] regions = {
					CellRangeAddress.valueOf("E1:E100")
			};

			// Add region to the rule
			sheetCF.addConditionalFormatting(regions, passRule, failRule);

		}

		try{			 
			FileOutputStream out = 
					new FileOutputStream(new File(excelFile));
			workbook.write(out);
			out.close();

			stopTime = System.currentTimeMillis();
			System.out.println("stop " + stopTime);
			elapsedTime = (stopTime - startTime) / 1000;
			DecimalFormat df = new DecimalFormat("#.00");
			System.out.println("Report generated in: "+df.format(elapsedTime) + " seconds");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	} //close
	
	
	 
}
