/**
 *  
 * A virtual table to view and retrieve 2-dimensional table-like data.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.testdatamanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualTable {
	private List<String []> table;
	private Map <String, Integer> headerMap;
	
	private int rowCount;
	
	private int colCount;
	
	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}
	
	public VirtualTable(List <String[] > table){
		this.table = table;
		headerMap = new HashMap<String, Integer>  ();
		
		 setRowCount(table.size());
		
		 String [] header = (String[])table.get(0);
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
			return (table.get(rowNum)[colNum]);
		else 
			return "No_VAL_EXIST";
	}
	
	public String get(int rowNum, String colName){
		// Subtract 1 from rowNum and colNum that comes from outside i.e. client.
		rowNum = rowNum - 1;
		int colNum = headerMap.get(colName);		
		
		if (rowNum <rowCount && colNum < colCount)
			return (table.get(rowNum)[colNum]);
		else 
			return "No_VAL_EXIST";
	}
	
	public void set(int rowNum, int colNum, String val){
		rowNum = rowNum - 1;
		colNum = colNum - 1;
		String [] temp = table.get(rowNum);
		temp[colNum] = val;		
		table.set(rowNum, temp);
	}
	
	public void set(int rowNum, String colName, String val){
		rowNum = rowNum - 1;		
		int colNum = headerMap.get(colName);
		if (rowNum < rowCount && colNum < colCount){
			String [] temp = table.get(rowNum);
			temp[colNum] = val;		
			table.set(rowNum, temp);	
		}
		else{
			if (rowNum < table.size() ){
				String [] temp = table.get(rowNum);
				temp[colNum] = val;		
				table.set(rowNum, temp);
			}
			else{
				String [] temp = new String[colCount];
				temp[colNum] = val;		
				table.add(rowNum, temp);	
				// Since a new is added, reset the rowCount again
				 setRowCount(table.size());
			}			
		}
		
	}
	
}
