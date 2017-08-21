/**
 *  
 * Interface specification of Test Data that can deal with XLS, XLSX, and CSV files
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.testdatamanager;

public interface TestData {
	
	public int getRowCount();
	public int getRowCount(int col);
	public int getRowCount(String colName);
	public int egetRowCount(String colGridLetter);
	public int getColCount();
	
	public void setActiveSheet(int activeSheet);
	public void setActiveSheet(String activeSheet);
	
	public String get(int rowNum, int colNum);
	public String get(int rowNum, String colName);
	public String eget(int rowNum, String colGridLetter);
	public String getByID(String rowID, int colNum);
	public String getByID(String rowID, String colName);
	
	public void set(int rowNum, int colNum, String value);
	public void set(int rowNum, String colName, String value);	
	public void eset(int rowNum, String colGridLetter, String value);
	//public void eset(int rowNum, String colGridLetter, String value);	
	public void setByID(String rowID, int colNum, String value);
	public void setByID(String rowID, String colName, String value);
	public void close();	
}
