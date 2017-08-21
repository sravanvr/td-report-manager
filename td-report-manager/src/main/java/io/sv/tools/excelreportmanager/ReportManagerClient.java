/**
 * 
 * Run this program as a "Java Application" see ReportManager in action.
 * @author Sravan Vedala
 * @version 1.0
 * @since 2017-08-01
 * 
 */

package io.sv.tools.excelreportmanager;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ReportManagerClient {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	
		
		String []header = {"Step Name", "Expected Value", "Actual Value", "Notes", "Test Status"};
		ReportManager reporter = new ReportManager("Reports/TestReport1111.xlsx");
		
		reporter.createSheet("First", header);
		reporter.createSheet("Second", header);
		reporter.createSheet("Second1", header);
		reporter.createSheet("Second2", header);
		reporter.createSheet("Third", header);
		
		reporter.setActiveSheet("Second");				
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1", "Notes1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2zzzzzz", "Actual Val2", "Notes2", "FAIL"});
				
		reporter.setActiveSheet("First");
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1", "Notes1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2xxxxxxxxx", "Actual Val2", "Notes2", "PASS"});
		
		reporter.setActiveSheet("Third");
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1", "Notes1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2", "Actual Val2", "Notes2", "FAIL"});
		reporter.report(new String[]{"Step3", "Expected Val3", "Actual Val3", "Notes3", "PASS"});
		reporter.report(new String[]{"Step4", "Expected Val4", "Actual Val4", "Notes4", "FAIL"});
		reporter.report(new String[]{"Step5", "Expected Val5", "Actual Val5", "Notes5", "FAIL"});
		reporter.report(new String[]{"Step6", "Expected Val6", "Actual Val6", "Notes6", "FAIL"});
		
		reporter.setActiveSheet("Second");
		reporter.report(new String[]{"Step3", "Expected Val3", "Actual Val3", "Notes3", "PASS"});
		reporter.report(new String[]{"Step4", "Expected Val4zzzzzzzzzzzzzzzzzz", "Actual Val4", "Notes4", "PASS"});
		reporter.report(new String[]{"Step5", "Expected Val5", "Actual Val5", "Notes5", "FAIL"});
		
		String []header1 = {"Step Name", "Expected Value", "Actual Value", "Test Status"};
		reporter.createSheet("Fourth", header1);
		
		reporter.setActiveSheet("Fourth");
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		
		reporter.createSheet("Hahahaha",  header);
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		reporter.report(new String[]{"Step9", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		reporter.report(new String[]{"Step10", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		reporter.report(new String[]{"Step11", "Expected Val2", "Actual Val2", "Actual Val1", "PASS"});
		
		reporter.createSheet("Rama", new String[] {"Step Name", "Expected Value", "Actual Value", "Notes", "Test Status"});		
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step3", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step4", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		reporter.report(new String[]{"Step2", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		reporter.report(new String[]{"Step3", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		reporter.report(new String[]{"Step4", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		
		reporter.createSheet("Krishna", new String[] {"Step Name", "Expected Value", "Actual Value", "Notes", "Test Status"});		
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step2", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step3", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step4", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step1", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		reporter.report(new String[]{"Step2", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		reporter.report(new String[]{"Step3", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		reporter.report(new String[]{"Step4", "Expected Val1", "Actual Val1","Actual Val1", "FAIL"});
		
		reporter.setActiveSheet("Rama");
		reporter.report(new String[]{"Step9", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		reporter.report(new String[]{"Step10", "Expected Val1", "Actual Val1","Actual Val1", "PASS"});
		
		
		
		// To createSummary sheet at the beginning
		reporter.createSummary(new TestSummary());
		
		// This method MUST be called to save the workbook.
		reporter.close();

		Desktop dt = Desktop.getDesktop();
		dt.open(new File("Reports/TestReport111.xls"));

	}

}
