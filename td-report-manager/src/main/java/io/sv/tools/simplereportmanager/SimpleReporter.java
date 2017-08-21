/**
 *  
 * SimpleReporter Interface definition
 * 
 * @author Sravan Vedala
 * @version 1.0
 * @since 20-Apr-2014
 * 
 */
package io.sv.tools.simplereportmanager;

import java.util.List;

public interface SimpleReporter {

	public void setFormatColumn1(String formatColumn1);
	public void setFormatColumn2(String formatColumn2);
	public void writeHeader(String[] header);
	public void report(String [] data);
	public void report(List <String []> data, boolean listContainsHeader);
	public void report(List <String []> data);
	public void close();
}
