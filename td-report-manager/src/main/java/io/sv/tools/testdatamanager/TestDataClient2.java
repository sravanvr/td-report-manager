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

public class TestDataClient2 {

	public static void main(String[] args) {
		TestData td = TestDataFactory.load("TestData/TestDataClient.xlsx");
		System.out.println("Total rows: " + td.egetRowCount("D"));
		
		
		int i = 1;
		for (; i<= td.egetRowCount("C"); i++){
			System.out.println(td.eget(i,"C"));
						
		}
		td.eset(14, "D", "HAHAHAHAHAHAHA");

		for (; i<= td.egetRowCount("B"); i++){
			System.out.print(td.get(i,1) + "\t");
			System.out.print(td.get(i,2) + "\t");
			System.out.print(td.get(i,3) + "\t");			
			System.out.println();
			td.eset(i, "D", "SACHIN TENDULKAR");
		}
		
		td.set(11,3, "MAHATMA");
		td.set(11,5, "Gandhi Nagar");
		
		td.set(18,6, "Nalanda");
		td.set(18,8, "Takshasila");
		td.set(18,10, "Kaasi");
		
		td.set(19,6, "Nalanda");
		td.set(19,7, "Takshasila");
		td.set(19,8, "Kaasi");
		
		td.set(20,6, "Nalanda");
		td.set(20,7, "Takshasila");
		td.set(20,8, "Kaasi");
		td.eset(21, "L", "GREAT DEVINE CREATION");
		td.eset(26, "EC", "TEST");
		
		td.eset(30, "I", "Sir Isac Newton");
		
		td.eset(35, "I", "Nobel");
		td.eset(40, "V", "Queen Elizabeth");
		td.eset(45, "J", "BAHUBALI");
		
		System.out.println(td.eget(21, "L"));
		System.out.println(td.eget(26, "CB"));
		
		System.out.println("****************************");
		System.out.println(td.eget(14, "E"));
		System.out.println(td.eget(15, "F"));
		System.out.println(td.eget(16, "G"));
		System.out.println(td.eget(17, "H"));
		
		System.out.println(td.eget(40, "V"));
		
		td.close();
		System.out.println("****************************");
		
		TestData td1 = TestDataFactory.load("TestData/MyData.csv");
		System.out.println("Total rows: " + td1.egetRowCount("C"));
		
		
		
		for (i = 1; i<= td1.egetRowCount("C"); i++){
			System.out.println(td1.eget(i,"C"));
						
		}
		td1.eset(7, "D", "MyLargeMessageText");
		td1.set(8, 6, "India");
		td1.eset(8, "J", "India");
		td1.set(8, 8, "India");
		td1.set(11, 15, "Gret Devine");
		td1.set(11, 10, "Great Excellent");
		td1.eset(11, "I", "Gootafied Libnetz");
		
		System.out.println("from in between....."+ td1.get(8, 5));
		
		//td1.set(13, "Dr", "Kalam");
		td1.close();
		TestData td2 = TestDataFactory.load("TestData/102400_Session_Clinic.xlsx");
		
		for (i = 1; i <= td2.getRowCount(); i++)
			System.out.println(td2.get(i, 1));
		
	}

}
