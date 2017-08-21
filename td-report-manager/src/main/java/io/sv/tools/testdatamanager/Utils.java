/**
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */

package io.sv.tools.testdatamanager;

public class Utils {

	public void getClassName(){
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null) {
		    System.out.println(enclosingClass.getName());}
		else {
		    System.out.println(getClass().getName());
		}
	}
	
}
