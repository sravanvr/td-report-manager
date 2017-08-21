/**
 *  
 * SimpleReporterFactory.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.simplereportmanager;

public class SimpleReporterFactory {
	
	public static SimpleReporter create(String fileName){
		if (fileName.endsWith("csv"))
			return (new SimpleCSVReporter(fileName));
		else 
			return (new SimpleExcelReporter(fileName));
	}
	
	public static SimpleReporter create(String fileName, String [] header, boolean richReport){
		if (fileName.endsWith("csv"))
			return (new SimpleCSVReporter(fileName, header));
		else 
			return (new SimpleExcelReporter(fileName, header, richReport));
	}

	public static SimpleReporter create(String fileName, boolean richReport){
		if (fileName.endsWith("csv"))
			return (new SimpleCSVReporter(fileName));
		else 
			return (new SimpleExcelReporter(fileName, richReport));
	}
	
}
