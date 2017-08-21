/**
 *  
 * Test Data Factory
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.testdatamanager;

public class TestDataFactory {
	
	public static TestData load(String file){
		if (file.endsWith("xls") || file.endsWith("xlsx"))
			return (new ExcelData(file));
		else if (file.endsWith("csv"))
			return (new CSVData(file));
		else
			return null;
	}
	
	// This function is applicable to both XLS/XLSX and CSV files
	// In case of a CSV file consider the second param i.i. sheet as the Output CSV file
	// That needs to be written and saved.
	public static TestData load(String file, String sheet){
		if (file.endsWith("xls") || file.endsWith("xlsx"))
			return (new ExcelData(file, sheet));
		else if (file.endsWith("csv"))
			return (new CSVData(file, sheet)); //sheet here means output CSV file
		else
			return null;
	}

	public static TestData load(String file, int sheetIndex){
		if (file.endsWith("xls") || file.endsWith("xlsx"))
			return (new ExcelData(file, sheetIndex));		
		else
			return null;
	}
	
}
