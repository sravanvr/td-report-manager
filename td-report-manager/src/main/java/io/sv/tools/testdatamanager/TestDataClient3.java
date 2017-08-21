/**
 *  
 * A sample Test Data client. User can see lots of sample calls to TestData Manager component
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.testdatamanager;

public class TestDataClient3 {

	public static void main(String[] args) {
		TestData td;
		td = TestDataFactory.load("TestData/MyData100.xlsx");
		System.out.println("Total rows: " + td.egetRowCount("D"));
		
		for (int i = 1; i <= td.getRowCount(); i++){
			System.out.printf("%-20s", td.get(i,1));
			System.out.printf("%-20s", td.get(i,2));
			System.out.printf("%-20s", td.get(i,3));
			System.out.printf("%-20s", td.get(i,4));
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");		
		for (int i = 1; i <= td.getRowCount(2); i++){
			System.out.printf("%-20s", td.get(i,"Id"));
			System.out.printf("%-20s", td.get(i,"Name"));
			System.out.printf("%-20s", td.get(i,"Title"));
			System.out.printf("%-20s", td.get(i,"Location"));
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");
		for (int i = 1; i <= td.egetRowCount("D"); i++){
			System.out.printf("%-20s", td.eget(i,"A"));
			System.out.printf("%-20s", td.eget(i,"B"));
			System.out.printf("%-20s", td.eget(i,"C"));
			System.out.printf("%-20s", td.eget(i,"D"));
			System.out.println();
		}
		
		td.set(9, 4, "Great Britain");		
		td.set(10, 8, "Freeway design");
		td.set(10, 6, "United States Of America");
		td.set(15, 15, "This is test");
		td.eset(16, "P", "Excel letter This is test");
		td.close();
		
		System.out.println("******************************************************************************************");
		
		td = TestDataFactory.load("TestData/MyData100.xls");
		System.out.println("Total rows: " + td.egetRowCount("D"));
		
		for (int i = 1; i <= td.getRowCount(); i++){
			System.out.printf("%-20s", td.get(i,1));
			System.out.printf("%-20s", td.get(i,2));
			System.out.printf("%-20s", td.get(i,3));
			System.out.printf("%-20s", td.get(i,4));
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");		
		for (int i = 1; i <= td.getRowCount(2); i++){
			System.out.printf("%-20s", td.get(i,"Id"));
			System.out.printf("%-20s", td.get(i,"Name"));
			System.out.printf("%-20s", td.get(i,"Title"));
			System.out.printf("%-20s", td.get(i,"Location"));
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");
		for (int i = 1; i <= td.egetRowCount("D"); i++){
			System.out.printf("%-20s", td.eget(i,"A"));
			System.out.printf("%-20s", td.eget(i,"B"));
			System.out.printf("%-20s", td.eget(i,"C"));
			System.out.printf("%-20s", td.eget(i,"D"));
			System.out.println();
		}
		
		td.set(9, 4, "Great Britain");		
		td.set(10, 8, "Freeway design");
		td.set(10, 6, "United States Of America");
		td.set(15, 15, "This is test");
		td.eset(16, "P", "Excel letter This is test");
		td.close();
		
		System.out.println("******************************************************************************************");
		
		td = TestDataFactory.load("TestData/MyData100.csv");
		System.out.println("Total rows: " + td.egetRowCount("D"));
		
		for (int i = 1; i <= td.getRowCount(); i++){
			System.out.printf("%-20s", td.get(i,1));
			System.out.printf("%-20s", td.get(i,2));
			System.out.printf("%-20s", td.get(i,3));
			System.out.printf("%-20s", td.get(i,4));
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");		
		for (int i = 1; i <= td.getRowCount(2); i++){
			System.out.printf("%-20s", td.get(i,"Id"));
			System.out.printf("%-20s", td.get(i,"Name"));
			System.out.printf("%-20s", td.get(i,"Title"));
			System.out.printf("%-20s", td.get(i,"Location"));
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------");
		for (int i = 1; i <= td.egetRowCount("D"); i++){
			System.out.printf("%-20s", td.eget(i,"A"));
			System.out.printf("%-20s", td.eget(i,"B"));
			System.out.printf("%-20s", td.eget(i,"C"));
			System.out.printf("%-20s", td.eget(i,"D"));
			System.out.println();
		}
		
		td.set(9, 4, "Great Britain");		
		td.set(10, 8, "Freeway design");
		td.set(10, 6, "United States Of America");
		td.set(10, 7, "Bhale bhaleeeeeee");
		td.set(15, 15, "This is test");
		td.eset(16, "P", "16P-Test");
		td.eset(20, "M", "20M-Test");
		td.eset(18, "K", "18K-Test");
		td.set(28, 12, "28/12-Test");
		td.set(24, 12, "24/12-Test");
		td.set(28, 10, "28/10-Test");
		
		for (int i = 1; i <= td.getRowCount(); i++){
			td.set(i,5, "Rama");
		}
		td.close();
		
		System.out.println("----------------------------------------------------------------------------------");
		
		
	}

}
