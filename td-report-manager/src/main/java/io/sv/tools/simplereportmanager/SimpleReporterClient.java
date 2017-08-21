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

public class SimpleReporterClient {
	private static final Logger log = Logger.getLogger(SimpleReporterClient.class);
	SimpleReporterClient(){
		
			DOMConfigurator.configure("config/log4j-config.xml");	
	
	}

	public static void main(String[] args)  {
		SimpleReporter rep = SimpleReporterFactory.create( 
				"MyReport.xls", new String[]{"Action", "Expected Value", "Test Status"}, true);
		/*
		 * Sheet Summary will be displayed only when report is constructed as below.
		 * 		rep = SimpleReporterFactory.create( 
				"MyReport.xls", new String[]{"Switch Name", "Expected Value", "Actual Value", "Test Status", "Alert Setting"}, true);	
		 */
		
		//"formatColumn1" will be color coded (either green or red) based on the "PASS"/"FAIL" value of the "formatColumn2"
		rep.setFormatColumn1("A");
		rep.setFormatColumn2("C");
		
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "PASS"});
		rep.report(new String[]{"Step1", "Expected Val1", "PASS"});
				
		rep.close();
		
		rep = SimpleReporterFactory.create("Fun1.xls");
		rep.writeHeader(new String[]{"Action", "Expected Value", "Test Status"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});		
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "PASS"});
		rep.report(new String[]{"Step1", "Expected Val1", "PASS"});
		
		rep.close();
		
		rep = SimpleReporterFactory.create("Fun2.xls", true);		
		rep.writeHeader(new String[]{"Action", "Expected Value", "Test Status"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "PASS"});
		rep.report(new String[]{"Step1", "Expected Val1", "PASS"});
		
		rep.close();
		
		rep = SimpleReporterFactory.create("Fun_222.xls", true);		
		rep.setFormatColumn1("A");
		rep.setFormatColumn2("B");
		rep.writeHeader(new String[]{"Action", "Test Status"});
		rep.report(new String[]{"Step1", "FAIL"});
		rep.report(new String[]{"Step2", "FAIL"});
		rep.report(new String[]{"Step3", "FAIL"});
		rep.report(new String[]{"Step4", "FAIL"});
		rep.report(new String[]{"Step5", "PASS"});
		rep.report(new String[]{"Step6", "PASS"});
		
		rep.close();
		
		rep = SimpleReporterFactory.create("Fun3.xls", true);
		rep.writeHeader(new String[]{"Action", "Test Status"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.report(new String[]{"Step1", "Expected Val1", "FAIL"});
		rep.close();
		
		List<String[]> list = new ArrayList<String[]>();
		
		list.add(new String[] {"Number", "Rank", "Total", "Grade"});
		list.add(new String[] {"one", "two", "A", "PASS"});
		list.add(new String[] {"two", "Three", "B", "Four"});
		list.add(new String[] {"Three", "Four", "C", "FAIL"});
		list.add(new String[] {"Six", "Seven", "D", "FAIL"});
		
		rep = SimpleReporterFactory.create("List111.xlsx", true);
		rep.setFormatColumn1("A");
		rep.setFormatColumn2("D");
		rep.report(list, true);
		rep.close();
		
		rep = SimpleReporterFactory.create("MyCSVReport.csv");
		rep.writeHeader(new String[] {"Number", "Rank", "Total"});
		rep.report(new String[]{"Bill", "Gates", "Pass"});
		rep.report(new String[] {"one", "two", "PASS"});
		rep.report(new String[] {"two", "two", "PASS"});
		rep.report(new String[] {"three", "two", "PASS"});
		rep.report(new String[] {"Four", "two", "PASS"});
		rep.report(new String[] {"Five", "two", "PASS"});
		rep.close();
		
	}

}
