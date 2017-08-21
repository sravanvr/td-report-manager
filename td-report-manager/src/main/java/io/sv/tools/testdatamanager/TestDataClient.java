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

import java.util.Arrays;

public class TestDataClient extends DataTable{

	/*
	 UseCase 1:
	 Print all rows and columns in a table format
	 TestData td = new TestData("MyData.xlsx");
	 for (int i = 0; i < td.getRowCount(); i++){
			for (int j = 0; j < td.getColCount(); j++){
				System.out.print(td.get(i,j)+ "  " );
			}
			System.out.println();
		}
	 System.out.print(td.get(0,"Name")+ "  " );
	 System.out.print(td.get(1,"Address")+ "  " );
	 td.set(6, 0, "GuruDatta");
	 td.set(9, "Title", "President");
	 td.close();
	 
	 UseCase 2:
	 DataTable.load("MyData.xls");
	 for (int i = 0; i < DataTable.getRowCount(); i++){
			for (int j = 0; j < DataTable.getColCount(); j++){
				System.out.print(DataTable.get(i,j)+ "      " );
			}
			System.out.println();
		}
		System.out.print(DataTable.get(1,2));
		DataTable.set(9, 0, "Gandhi");
	  DataTable.close();
	  
	  UseCase 3:
	  String []header = {"Step Name", "Expected Value", "Actual Value", "Notes", "Test Status"};
		ReportManager reporter = new ReportManager("Report3.xlsx");
		
		reporter.createSheet("First", Arrays.asList( header));
		reporter.createSheet("Second", Arrays.asList( header));
		reporter.setActiveSheet("First");
		reporter.report(Arrays.asList(new String[]{"Step1", "Expected Val1", "Actual Val1", "Notes1", "PASS"}));
		reporter.report(Arrays.asList(new String[]{"Step2", "Expected Val2zzzzzz", "Actual Val2", "Notes2", "FAIL"}));
		reporter.close();
	 */
	
	public static void main(String[] args) {
		
		TestData c = TestDataFactory.load("TestData/UserIds.csv"); 
				
	
		for (int i = 1; i <= c.getRowCount(); i++){
			for (int j = 1; j <= c.getColCount(); j++)				
				System.out.print(c.get(i, j)+ "  ");
			System.out.println();
			
		}
		System.out.println ("4th col row count "+ c.getRowCount(3));
		System.out.println ("Val is: "+ c.get(11, 4));
		c.set(2, 2, "Barack Obama");
		c.set(2, 3, "Cheese Cake");
		c.set(2, 4, "Montessory School");
		c.setByID("800", 2, "Eisen Hover Updated");
		c.setByID("800", 3, "Supreme Commander");
		c.setByID("800", 4, "Allied Army");
		
		c.close();

//***************** below code is for Excel Data 
		TestData td = TestDataFactory.load("TestData/MyExcel.xls");
		 for (int i = 1; i <= td.getRowCount(); i++){
				for (int j = 1; j <= td.getColCount(); j++){
					System.out.print(td.get(i,j)+ "   " );
				}
				System.out.println();
			}
		 
//		 System.out.println("*********************");
		 
		 System.out.println ("From sheet2...............");
		 TestData td2 = TestDataFactory.load("TestData/MyExcel_111.xls", "Sheet2");
		 for (int i = 1; i <= td2.getRowCount(); i++){
				for (int j = 1; j <= td2.getColCount(); j++){
					System.out.print(td2.get(i,j)+ "  " );
				}
				System.out.println();
		
		 }
		 
		 for (int i = 1; i <= td2.getRowCount(); i++ )
			 td2.set(i,  10, "Aka");
		 
		 System.out.println("*********************");
		 
		 System.out.print(td2.get(3,"Name")+ "  " );
		 System.out.print(td2.get(5,"Field")+ "  " );
		 System.out.print(td2.get(2,"Country")+ "  " );
		 System.out.println();
		 
		 System.out.print(td2.get(7,"Name")+ "  " );
		 System.out.print(td2.get(3,5)+ "  " );
		 System.out.print(td2.get(5,-100)+ "  " );
		 
		 System.out.println();
		 
		 System.out.print(td2.getByID("5","Name")+ "  " );
		 System.out.print(td2.getByID("5","Field")+ "  " );
		 System.out.print(td2.getByID("5","Country")+ "  " );
		 
		 System.out.println ("From sheet1...............");
		 
		 td2.setActiveSheet("Sheet1");
		 td2.set(2,2,"@@@@@@@@@@@@@@@");
		 td2.set(4,3, "$$$$$$$$$$$$$$$$$$$$$r");
		 
		 td2.set(15,5, "Twin Cities");
		 td2.set(16,5, "Santa Clarita");
		 td2.set(17,10, "Los Angeles");
		 td2.set(18,10, "Los Angeles");
		 
		 td2.setByID("10", 2, "Awesome");
		 td2.setByID("10", 3, "Devine");
		 td2.setByID("10", 4, "Excellent");
		 
		 td2.setByID("15", 2, "Swamy");
		 td2.setByID("15", 3, "Vivekananda");
		 td2.setByID("15", 4, "Master");
		 
		 System.out.print(td2.getByID("6","Name")+ "  " );
		 System.out.print(td2.getByID("6","Field")+ "  " );
		 System.out.print(td2.getByID("6","Country")+ "  " );
		 		 
		 
		 System.out.println ("From sheet2...............");
		
		 td2.setActiveSheet("Sheet2");
		 
		 td2.set(6,"Name", "John Von Neumann");
		 td2.setByID("7","Country", "USA");
		 td2.setByID("7","Field", "Mathematcician");
		 td2.setByID("16","Name", "JP");
		 
		 
		 td2.close();
				
	
	}}

