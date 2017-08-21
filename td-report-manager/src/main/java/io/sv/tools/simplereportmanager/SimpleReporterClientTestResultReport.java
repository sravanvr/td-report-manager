/**
 *  
 * Run this program as a "Java Application" to see Simple Reporter in action.
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.simplereportmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class SimpleReporterClientTestResultReport {
	private static final Logger log = Logger.getLogger(SimpleReporterClientTestResultReport.class);
	SimpleReporterClientTestResultReport(){
			DOMConfigurator.configure("config/log4j-config.xml");
	}

	public static void main(String[] args)  {
		SimpleReporter rep = SimpleReporterFactory.create( 
				"MyReport.xls", new String[]{"Action", "Expected Value", "Test Status"}, true);
		
		rep = SimpleReporterFactory.create( 
				"MyReport.xls", new String[]{"Switch Name", "Expected Value", "Actual Value", "Test Status", "Alert Setting"}, true);	
		
		//"formatColumn1" will be color coded (either green or red) based on the "PASS"/"FAIL" value of the "formatColumn2"
		rep.setFormatColumn1("A");
		rep.setFormatColumn2("D");
		
		rep.report(new String[]{"Step1", "Expected Val1", "ACTUAl VAL", "FAIL", "Some addditional notes here"});
		rep.report(new String[]{"Step2", "Expected Val1", "ACTUAl VAL", "PASS", "Some addditional notes here"});
		rep.report(new String[]{"Step3", "Expected Val1", "ACTUAl VAL", "FAIL", "Some addditional notes here"});
		rep.report(new String[]{"Step4", "Expected Val1", "ACTUAl VAL", "PASS", "Some addditional notes here"});
		rep.report(new String[]{"Step5", "Expected Val1", "ACTUAl VAL", "PASS", "Some addditional notes here"});
		rep.report(new String[]{"Step6", "Expected Val1", "ACTUAl VAL", "PASS", "Some addditional notes here"});
				
		rep.close();
		
		
	}

}
