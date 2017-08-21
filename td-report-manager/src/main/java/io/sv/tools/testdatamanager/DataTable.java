/**
 *  
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.testdatamanager;

public class DataTable {

	public static String getClassName(){
		StackTraceElement[] stack = Thread.currentThread ().getStackTrace ();
	    StackTraceElement main = stack[stack.length - 1];
	    String [] arr = main.getClassName ().split("\\.");
	    String mainClass = arr[arr.length - 1];
	    System.out.println("Main class name is....:"+mainClass);
	    return mainClass;
	}
	
	public void getName(){
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null) {
		    System.out.println(enclosingClass.getName());}
		else {
		    System.out.println(getClass().getName());
		}
	}
	public final static ExcelData DataTable = new ExcelData(); 
}
