/**
 * 
 * This interface must be implemented if you want to create a summary sheet
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */
package io.sv.tools.excelreportmanager;

import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface ISummary {

	public Workbook createSummary(Workbook workbook, Map <String, CellStyle> styleMap);
		
	
}
