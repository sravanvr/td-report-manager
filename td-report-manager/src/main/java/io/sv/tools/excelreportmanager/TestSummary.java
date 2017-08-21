/**
 * 
 * This class must be implemented by the client to supply to TestData so that
 * Test Data manager will be able to give a callback to display the Summary
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */
package io.sv.tools.excelreportmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class TestSummary implements ISummary{

	private Workbook workbook;
	private Sheet sheet;
	private Sheet sheetNavigator;
	private Row row;
	private Cell cell;
	private CreationHelper createHelper;	
	private Map <String, CellStyle> styleMap;
	int rowIndex = 0;
	boolean first = true; //To create a comment only for the first test link in Summary	
	int totalCells;
	int t1;
	int t2;
	private static int totSteps = 0, totPassed = 0, totFailed = 0;
	TestSummary(){
		
	}
	
	public Workbook createSummary(Workbook workbook, Map <String, CellStyle> styleMap){
		createHelper = workbook.getCreationHelper();
		this.styleMap = styleMap; 
		//Create specified sheet
				try{
					this.workbook = workbook;
					sheet = workbook.createSheet("Summary");			
					System.out.println(workbook.getNumberOfSheets());
					System.out.println(workbook.getSheetIndex("Third"));
				}
				catch (java.lang.IllegalArgumentException e){
					System.out.println("Sheet exists");
				}
				//Create the sheet at index 0, Test Summary implementation MUST follow this convention.
				workbook.setSheetOrder("Summary", 0);
				createSummary();
				return (workbook);
	}

	private void createHeader(){
		row = sheet.createRow(rowIndex++);
		
		for (int col = 0; col < totalCells; col++){
			cell = row.createCell(col);
			if (col == t1 || col == t2){
				cell.setCellStyle(styleMap.get("headerLink"));}
		}
		
		// Accommodate two extra cells to display a visible comment
		
		if (first){
			int commentCol = totalCells + 2;
			first = false;
		
			Drawing drawing = sheet.createDrawingPatriarch();

		    // When the comment box is visible, have it show in a 1x3 space
		    ClientAnchor anchor = createHelper.createClientAnchor();
		    anchor.setCol1(commentCol-1);
		    anchor.setCol2(commentCol+1);
		    anchor.setRow1(row.getRowNum());
		    anchor.setRow2(row.getRowNum()+ 1);

		    // Create the comment and set the text+author
		    Comment comment = drawing.createCellComment(anchor);
		    comment.setVisible(true);
		    
		    RichTextString str = createHelper.createRichTextString("Click on the title!");
		    comment.setString(str);
		    comment.setAuthor("Sravan Vedala");
		    
		 // Assign the comment to the cell
		    row.getCell(t1).setCellComment(comment);
		}
		 	    
		    
	}
	
	private void createSummHeader(){
		row = sheet.createRow(rowIndex++);
		row.setHeightInPoints(65);
		for (int col = 0; col < totalCells; col++){
			cell = row.createCell(col);
			if (col ==t1 || col == t2){
				cell.setCellStyle(styleMap.get("header"));}
		}
	}
	
	private void createRow(boolean blank){
		row = sheet.createRow(rowIndex++);
		for (int col = 0; col < totalCells; col++){
			cell = row.createCell(col);
			if ((col == t1 || col == t2) && !blank){
				cell.setCellStyle(styleMap.get("summCells"));}
		}
	}
	
	
	private void createSummary(){
		totalCells = 7;
		t1 = 5;	// Target cell 1
		t2 = 6;	// Target cell 2
		
		sheet.setColumnWidth(t1, 30 * 256);
		sheet.setColumnWidth(t2, 30 * 256);
		
		createRow(true);	//First row
		createSummHeader();	
		Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy");
	    
		row.getCell(t1).setCellValue("Test Results Summary Report"+"\n"+ ft.format(dNow));
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), t1, t2));
				
		// Start from sheet index-1 as index-0 is Summary sheet
		for (int i = 1; i < workbook.getNumberOfSheets(); i++){
			totPassed = 0;
			totFailed = 0;
			totSteps = 0;
			
			sheetNavigator = workbook.getSheetAt(i);
			getTestStats(sheetNavigator);
			
			createRow(true);
			createHeader();		
			cell = row.getCell(t1);
			cell.setCellValue(sheetNavigator.getSheetName() + " - Test Results");
			cell.setHyperlink(createHyperLink(sheetNavigator.getSheetName()));		
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), t1, t2));
			
			createRow(false);
			
			row.getCell(t1).setCellValue("Total Number of Test Cases");
			row.getCell(t2).setCellValue(totSteps);
			
			createRow(false);		
					
			row.getCell(t1).setCellValue("Total Number Passed");
			row.getCell(t2).setCellValue(totPassed);
			
			createRow(false);
			
			row.getCell(t1).setCellValue("Total Number Failed");
			if (totFailed > 0) 
				row.getCell(t2).setCellStyle(styleMap.get("summFailed"));
			row.getCell(t2).setCellValue(totFailed);
					
		}
	}
	
	private synchronized Hyperlink createHyperLink(String sheet){
		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		link.setAddress("'"+sheet+"'!A1");
		return link;
	}
	
	private void getTestStats(Sheet sheet){
		/* 
		 * note: sheet.getLastRowNum() returns the index of last row.
		 * Since first (zero'th) row is header row, start gathering stats from row 1 
		 * More than 1 physical number of rows should exist to gather the stats; one row i.e. header always there
		*/
		for (int row = 1; (row <= sheet.getLastRowNum()) && (sheet.getPhysicalNumberOfRows() > 1); row++){
			if (sheet.getRow(row).getCell(4).getStringCellValue().equals("PASS"))
				totPassed++;
			else if (sheet.getRow(row).getCell(4).getStringCellValue().equals("FAIL"))
				totFailed++;
			totSteps++;
		}
	}
}
