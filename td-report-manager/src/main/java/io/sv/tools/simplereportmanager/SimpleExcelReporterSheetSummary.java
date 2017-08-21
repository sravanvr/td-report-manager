/**
 *  
 * This is a utility class and user can instantiate it and call this to create brief Sheet Summary on the excel workbook sheet.
 * This was used in SimpleExcelReporter.java class.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */
package io.sv.tools.simplereportmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import io.sv.tools.excelreportmanager.CellStyles;

public class SimpleExcelReporterSheetSummary {
	
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private Cell cell;	
	private Map <String, CellStyle> styleMap;
	private static int totSteps = 0, totPassed = 0, totFailed = 0;
	int rowIndex = 0; // Reset row index	
	int tc1, tc2, totalCells; // Two target cells which holds the cell numbers where Sheet summary should be printed	
	
	public SimpleExcelReporterSheetSummary(Workbook workbook){
		this.workbook = workbook;
		styleMap = (new CellStyles(this.workbook)).createStyles();		
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
				if (sheet.getRow(row).getCell(3).getStringCellValue().equals("PASS"))
					totPassed++;
				else if (sheet.getRow(row).getCell(3).getStringCellValue().equals("FAIL"))
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
		
		public Sheet createSheetSummary(int i){
			
			rowIndex = 0; // Reset row index
			totSteps = totPassed = totFailed = 0; // Reset stat counts.
			
			int tc1, tc2, totalCells; // Two target cells which holds the cell numbers where Sheet summary should be printed
			sheet = workbook.getSheetAt(i);
			tc1 = sheet.getRow(0).getLastCellNum() + 1;	// Target cell-1
			tc2 = tc1+1;	// Target cell 2
			totalCells = tc1 + 2; // Total number of cells in first row counting from the first cell including newly added two target cells.
			int actualRowsInSheet = sheet.getLastRowNum(); // Preserve the count (total number) of rows that the "test results" were entered so that we scan thru and capture 
			// test stats such as total passed, total failed; You need this in case the test result row count is smaller than the row count of (i.e. number of rows taken by) Sheet Summary after adding the Sheet Summary 
			// to the workbook sheet. Remember Sheet Summary takes just 4 rows and there could be less than 4 test result rows entered into the sheet.
			
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
			 
			
			return sheet;
		}
}
