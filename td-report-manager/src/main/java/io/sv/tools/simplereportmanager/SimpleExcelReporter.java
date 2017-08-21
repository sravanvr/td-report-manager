/**
 * 
 * SimpleReporter shall be used to create simple one sheet reports. 
 * This class shall implement 'Simple Reporter' interface. This component should be simple and light weight, 
 * with the primary goal that user should be able to create a one sheet report quickly 
 * passing less parameters to this class. 
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */
package io.sv.tools.simplereportmanager;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell ;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row ;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Sheet ;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Workbook ;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference ;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.sv.tools.excelreportmanager.CellStyles;
import io.sv.tools.utils.Utils;

public class SimpleExcelReporter implements SimpleReporter {

	private int colCount;		
	private String excelFile;	
	private int rowIndex;
	private Map <String, CellStyle> styleMap;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private Cell cell;
	private CreationHelper createHelper;	
	private String [] header;
	private boolean richReport;
	private static final Logger log = Logger.getLogger(SimpleExcelReporter.class);	
	private String formatColumn1 = null, formatColumn2 = null; 
	private boolean writeSheetSummaryFlag = false;
	
	// "formatColumn1" will be color coded (either red or green) based on the "PASS"/"FAIL" value of the "formatColumn2"
	public void setFormatColumn1(String formatColumn1) {
		this.formatColumn1 = formatColumn1;
		writeSheetSummaryFlag = true; // Set this flag when user sets formatting for PASS/FAIL indicating it's a test report. Summary report feature is only for test reports.
	}

	public void setFormatColumn2(String formatColumn2) {
		this.formatColumn2 = formatColumn2;
	}
	
	public SimpleExcelReporter(String fileName) {	
		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("/") || fileName.contains("\\"))
			this.excelFile = fileName;
		else
			this.excelFile = "Reports//"+ fileName;
		
		this.header = null;
		richReport = false;
		rowIndex = 0;		
		styleMap = new HashMap <String, CellStyle>();
		createWorkBook();
		styleMap = (new CellStyles(workbook)).createStyles();
		createSheet();	
	}
	
	public SimpleExcelReporter(String fileName, String [] header, boolean richReport){
		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("/") || fileName.contains("\\"))
			this.excelFile = fileName;
		else
			this.excelFile = "Reports//"+ fileName;
		
		this.header = header;
		this.richReport = richReport;
		
		rowIndex = 0;	
		styleMap = new HashMap <String, CellStyle>();
		createWorkBook();
		styleMap = (new CellStyles(workbook)).createStyles();
		createSheet();
	}
	
	public SimpleExcelReporter(String fileName, boolean richReport){
		DOMConfigurator.configure("config/log4j-config.xml");	
		
		if (fileName.contains("/") || fileName.contains("\\"))
			this.excelFile = fileName;
		else
			this.excelFile = "Reports//"+ fileName;
		
		this.header = null;
		this.richReport = richReport;
		
		rowIndex = 0;	
		styleMap = new HashMap <String, CellStyle>();
		createWorkBook();
		styleMap = (new CellStyles(workbook)).createStyles();
		createSheet();
	}
	
	private void createWorkBook(){
		if (excelFile.endsWith("xlsx"))
			workbook = new XSSFWorkbook();
		
		else
			workbook = new HSSFWorkbook();
		createHelper = workbook.getCreationHelper();
		log.info("Workbook by name \""+ excelFile+"\" has been created");			
	}
	
	private void createSheet(){	
		// Create the sheet
		try{
			sheet = workbook.createSheet("Sheet1");		

		}
		catch (java.lang.IllegalArgumentException e){
			log.debug(e);
		}
		
		rowIndex = 0; // This seems to be a limitation in POI. Can not tell row count correctly when there are zero rows.
		
		if ( header != null ){
			// Setup row count and column counts; then write header
			colCount = header.length;
			writeHeader();	
		}
		else 
			colCount = -1;
			
	}

	private void writeHeader(){
		
		if (! richReport){
			report(header);	
		}
		
		else{
			row = sheet.createRow(rowIndex++);			
			for (int i = 0; i < header.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(createHelper.createRichTextString(header[i]));
				cell.setCellStyle(styleMap.get("header"));
			}	
			
		}
			
	}
	
	public void writeHeader(String[] header){
		this.header = header;
		colCount = header.length;
		writeHeader();
			
	}
	
	public void report(String [] data){
		
		if (colCount > 0){	// i.e. User has defined the header and must pass data for all columns in the header			
		
			if (data.length != colCount ){
				log.error("Invalid number of items in the report data");				
			}
			
			else{	// User is free to pass data for any number of columns 
				writeReport(data);
			}
		}
		
		else{ //No header is provided; user is free to report any number of columns
			writeReport(data);
		}
	}
	
	// Writes report from 'List <String []>' data structure.
	// Writes header if the list contains the header row.
	public void report(List <String []> data, boolean listContainsHeader){
			
			if (listContainsHeader && header == null){ //List has header row. Remove this and write this header 
				header = data.remove(0);
				colCount = header.length;
				writeHeader(header);				
				report(data);
			}
			else
				report(data);
		}
		
	// Writes report from 'List <String []>' data structure.
	public void report(List <String []> data){
		
		if (colCount > 0){			
			
			for (String [] arr: data){
				if (arr.length!= colCount ){
					log.error("Invalid number of items in the report data");
				}
			
				else{
					writeReport(arr);
				}				
			}			
		}
		
		else{ //No header is provided; user is free to report any number of columns
			
			for (String [] arr: data){
				writeReport(arr);	
			}
			
		}
	}

	public void writeReport(String [] data){
		// No restriction on number of columns of data passed.
		row = sheet.createRow(rowIndex++);			
		for (int i = 0; i < data.length; i++){
			cell = row.createCell(i);
			cell.setCellValue(data[i]);		
			
			// Below is example code to retrieve complete cell address Ex: A1, B22, etc..
			//CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
	        //System.out.println("XXX "+cellRef.convertNumToColString(i));
					        
			if (richReport && formatColumn1 != null && formatColumn2 != null && (CellReference.convertNumToColString(i).equals(formatColumn1)) && data[data.length - 1] == "FAIL")					
				cell.setCellStyle(styleMap.get("failed"));
			else
				cell.setCellStyle(styleMap.get("allCells"));
		}	
	
	}	
	
	
	public void close(){
		// auto sizing a column is heavy weight process. Run it at the end of the processing.
		// Loop through each sheet of the workbook and auto size each column in the selected sheet
		// Ignore zero'th index which is Summary sheet
		
		SimpleExcelReporterSheetSummary summary = new SimpleExcelReporterSheetSummary(workbook);
		
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++){
			 sheet.autoSizeColumn(i);
		}		
		
		// Perform sheet level display settings and Conditional formatting
		
		for (int i = 0; i < workbook.getNumberOfSheets(); i++){
			sheet = workbook.getSheetAt(i);
			 //turn off grid lines
	        sheet.setDisplayGridlines(false);
	        sheet.setPrintGridlines(false);        
	        sheet.setFitToPage(true);
	        sheet.setHorizontallyCenter(true);
	        
	        // Create conditional formatting for PASS and FAIL for each sheet
	        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();	     
	      
	        // Define Pass Rule
	        ConditionalFormattingRule passRule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL,  "\"PASS\"");
	        FontFormatting passFormat = passRule.createFontFormatting();
	        passFormat.setFontColorIndex(IndexedColors.GREEN.index);	        
	        	        	     
	        // Define Fail Rule
	        ConditionalFormattingRule failRule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL,  "\"FAIL\"");
	        FontFormatting failFont = failRule.createFontFormatting();
	        failFont.setFontColorIndex(IndexedColors.RED.index);
	        failFont.setFontStyle(false, true);
	     
	       // Add conditional formatting only when format column names are supplied by the user.
	        int lastRow = sheet.getLastRowNum() + 1;
	        if (richReport && formatColumn1 != null && formatColumn2 !=null) {
	        	CellRangeAddress[] regions = {
		                CellRangeAddress.valueOf(formatColumn2+"1:"+formatColumn2+lastRow)
		        };
		        
		        // Add region to the rule
		        sheetCF.addConditionalFormatting(regions, passRule, failRule);	
	        }	       

	        // Create Auto Filter for the region test results were entered. 
	        String region = "A1:"+Utils.getExcelColumnName(colCount)+rowIndex;
	        sheet.setAutoFilter(CellRangeAddress.valueOf(region)); //"A1:C5"
	        
	        // Create sheet summary only when user is creating a "Test Report" which we are determining by looking at number of columns the user has requested to create 
	        // while instantiating the Simple Reporter as well as by looking at "setFormatColumn1()" method call. 
	        if (writeSheetSummaryFlag && header.length ==5)
	        summary.createSheetSummary(i);
	        
	       // sheet.setZoom(4,3); // You can send 3,6 as well :)    
		}
        
		try{			 
			 FileOutputStream out = 
			        new FileOutputStream(new File(excelFile));
			 workbook.write(out);
			 out.close();
			 log.info("Report "+excelFile+" creation complete"); 
			 
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	
		try {	// Give 1 Sec. time just to let the above created file get saved.
			Thread.sleep(1000);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		
		openExcelWhenDone(excelFile);
		
	} //close
	
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
