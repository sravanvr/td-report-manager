package io.sv.tools.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

//import framework.DataBase.DBManagerClient;

import java.util.concurrent.TimeUnit;

public class Utils {
	
	private static Logger log = Logger.getLogger(Utils.class);
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public Utils(){	
		DOMConfigurator.configure("config/log4j-config.xml");
	}
	
	/**
	 * Convert given excel column name to column Index, ex 'A=1', 'Z=26'
	 * @param colName
	 * @return 1 based index of the column
	 */
	public static int getExcelColNumberFromLetter (String colName) {

	    //remove any whitespace
	    colName = colName.trim();

	    StringBuffer buff = new StringBuffer(colName);

	    //string to lower case, reverse then place in char array
	    char chars[] = buff.reverse().toString().toLowerCase().toCharArray();

	    int retVal=0, multiplier=0;

	    for(int i = 0; i < chars.length;i++){
	        //retrieve ascii value of character, subtract 96 so number corresponds to place in alphabet. ascii 'a' = 97 
	        multiplier = (int)chars[i]-96;
	        //mult the number by 26^(position in array)
	        retVal += multiplier * Math.pow(26, i);
	    }
	    return retVal;
	}


	public static String getExcelColumnName(int columnNumber)
	{
	    int dividend = columnNumber;
	    String columnName = "";
	    int modulo;

	    while (dividend > 0)
	    {
	        modulo = (dividend - 1) % 26;
	        columnName = (char)(65 + modulo) + columnName;
	        dividend = (int)((dividend - modulo) / 26);
	    } 

	    return columnName;
	}
	
	
	// Returns names of all directories inside the given directory
	public static String[] getAllFolders(String dirPath){
		
		File file = new File(dirPath);
		
		String[] directories = file.list(new FilenameFilter() {
		  public boolean accept(File dir, String name) {
		    return new File(dir, name).isDirectory();
		  }
		});	
		return directories;
	}
	
	// This function gets all files from a given directory that starts with a given string
	public static File [] getFilesFromDir(String directory, final String startsWith){
		File dir = new File(directory);
		File [] foundFiles = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.startsWith(startsWith);
		    }
		});
	
		return foundFiles;
	}
	
	// Gets all 'file paths' from a given directory
	public static List<File> getAllFiles(String directoryName) throws IOException {
	        File directory = new File(directoryName);

	        List<File> resultList = new ArrayList<File>();

	        // get all the files from a directory
	        File[] fList = directory.listFiles();
	        resultList.addAll(Arrays.asList(fList));
	        for (File file : fList) {
	            if (file.isFile()) {
	                //System.out.println(file.getAbsolutePath());
	            } else if (file.isDirectory()) {
	                resultList.addAll(getAllFiles(file.getPath()));
	            }
	        }
	        //System.out.println(fList);
	        return resultList;
	  } 
		
		// Returns the last modified file that starts with a specific string.
	public static File lastFileModified(String directory, final String startsWith) {		
			final File dir = new File(directory);
			final File [] foundFiles = dir.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.startsWith(startsWith);
			    }
			});				
			long lastMod = Long.MIN_VALUE;
			File choise = null;
			for (File file : foundFiles) {
				if (file.lastModified() > lastMod) {
					choise = file;
					lastMod = file.lastModified();
				}
			}
			return choise;
		}
		
		public static double  convertToMins(String time) throws ParseException{
			 
			 SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
			 Date dateTime = timeFormat.parse(time);
			 long millis = dateTime.getTime() - 28800000;		 
			 double hours = (millis * 1.0)/(1000*60);
			 //System.out.format("%.3f\n", hours);		 
			 DecimalFormat decim = new DecimalFormat("0.00");
			 hours = Double.parseDouble(decim.format(hours));
			 return hours;
			    
		}
		
		public static double  convertToMinsSS(String time) throws ParseException{
			 
			 SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			 Date dateTime = timeFormat.parse(time);
			 long millis = dateTime.getTime() - 28800000;		 
			 double hours = (millis * 1.0)/(1000*60);
			 System.out.format("%.3f\n", hours);		 
			 DecimalFormat decim = new DecimalFormat("0.00");
			 hours = Double.parseDouble(decim.format(hours));
			 return hours;
			    
		}
		
		public static String PSTtoUTC1(String dateTime) throws ParseException{
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			log.debug("PST Time: "+ dateTime);
			dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
			final Date date = dateFormat.parse(dateTime);
			//final Date date = new Date();
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			log.debug("Converted UTC time: " + dateFormat.format(date));		
			return (dateFormat.format(date));
			
		}	
		
		public String UTCtoPST(String dateTime) throws ParseException{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			log.debug("UTC Time: "+ dateTime);
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			final Date date = dateFormat.parse(dateTime);
			//final Date date = new Date();
			dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
			log.debug("Converted PST time: " + dateFormat.format(date));
			return (dateFormat.format(date));
		}
		
		/*
		 * Purpose: Pass two dates and get the difference in hours and minutes
		 * Example: dateOne = 11-02-2013 08:33:59 pm
		 * 			dateTwo = 11-02-2013 10:43:59 pm 
		 * 			The method returns "0 hrs 48 mins"
		 * Refer the client code for more clarity.
		 */
		public static String getTimeDiffOfDates(Date dateOne, Date dateTwo) throws ParseException {
			String diff = "";
			long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
			//	        diff = String.format("%d hour(s) %d min(s)", TimeUnit.MILLISECONDS.toHours(timeDiff),
			//	                TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
			diff = String.format("%d hrs %d mins", TimeUnit.MILLISECONDS.toHours(timeDiff),
					TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
			
			/*
			 * Client code: <copy this code and run from any where>
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
			String startTime = dateFormat.format(new Date());
	        Date d1 = dateFormat.parse(startTime);  	        	    
		    Date d2 = dateFormat.parse("11-02-2013 08:33:59 pm");
		    String endTime = dateFormat.format(d2);
		    System.out.println("Start : "+ startTime);
		    System.out.println("End : "+ endTime);
		    System.out.println("Time diff: " + getTimeDiffOfDates(d2, d1));
		    
			 */
					    
			return diff;
		}
		
		/* Gets the time difference between two given dates in the format HH:MM:SS.
		 	Example: dateOne = 11-02-2013 08:33:59 PM
		 * 	  		 dateTwo = 11-02-2013 10:43:59 PM 
		 * 			 The method returns "0 hrs 48 mins 0 secs"
		*/
		public static String timeDiffOfDates(Date start, Date end){
			 long msecs  = end.getTime()- start.getTime();
		        if (msecs > 0){ 
		        	
		        	String diff = String.format("%d hour(s): %d mins: %02d secs", msecs / (1000* 3600), (msecs % (1000*3600)) /( 1000 * 60), ((msecs % (1000*60*60)) % (1000*60)) / 1000);
		        	return diff;
		        }
		        else 
		        	return null;
		}
		
		/* Gets the time difference between two supplied dates in days, hrs, minutes and secs
		 * Example:  Date1 = "01/14/2012 09:29:58"
					 Date2 = "01/15/2012 11:36:48"
					 The method returns 1 day(s) 2 hr(s) 06 mins 50 secs	 
		 */
		public static String timeDiffOfDatesInDaysHrsMinsSecs(Date start, Date end){
			
			//in milliseconds
			long diff = Math.abs(start.getTime() - end.getTime()); 
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			String difference = String.format("%d day(s) %d hr(s) %02d mins %02d secs", diffDays, diffHours, diffMinutes, diffSeconds);
			return difference;
		}
		
		/*
		 * Accepts two dates in strings and gets the time difference in the format 'HH:MM:SS.SSS'
		 * Example: 
		 * start = 2013-01-10 22:45:09.330
		 * end = 2013-01-10 22:45:13.681
		 * Then difference = 00:00:04.351  
		 */
		private static String diffBetweenDates(String d1, String d2) throws ParseException{
			 /* 'Milliseconds' to 'HH:MM:SS:SSS' conversion formulae
	         * Hours = Milliseconds / (1000*60*60)
			   Minutes = (Milliseconds % (1000*60*60)) / (1000*60)
			   Seconds = ((Milliseconds % (1000*60*60)) % (1000*60)) / 1000
			   MilliSceconds = ((Milliseconds % (1000*60*60)) % (1000*60)) % 1000
	         */
			
			 DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
			 
			if (d1 != null && d2 != null){
				Date start = format.parse(d1);
		        Date end = format.parse(d2);        
		        long msecs  = end.getTime()- start.getTime();
		        if (msecs > 0){ 
		        	//String diff = String.format("%02d:%02d:%02d.%03d", msecs / (1000* 3600), (msecs % (1000*3600)) /( 1000 * 60), ((msecs % (1000*60*60)) % (1000*60)) / 1000, ((msecs % (1000*60*60)) % (1000*60)) % 1000);
		        	String diff = String.format("%02d hours: %02d mins: %02d secs .%03d msecs", msecs / (1000* 3600), (msecs % (1000*3600)) /( 1000 * 60), ((msecs % (1000*60*60)) % (1000*60)) / 1000, ((msecs % (1000*60*60)) % (1000*60)) % 1000);
		        	return diff;
		        }
		        else
		        	return "NA";
		        
	        }
			else return "NA";
		}
		
		
		private String millisToHH_MM_SS_SSS(long msecs ) throws ParseException{		
			 /* 'Milliseconds' to 'HH:MM:SS:SSS' conversion formulae
	        * Hours = Milliseconds / (1000*60*60)
			   Minutes = (Milliseconds % (1000*60*60)) / (1000*60)
			   Seconds = ((Milliseconds % (1000*60*60)) % (1000*60)) / 1000
			   MilliSceconds = ((Milliseconds % (1000*60*60)) % (1000*60)) % 1000
	        */
			
		      String time = String.format("%02d:%02d:%02d.%03d", msecs / (1000* 3600), (msecs % (1000*3600)) /( 1000 * 60), ((msecs % (1000*60*60)) % (1000*60)) / 1000, ((msecs % (1000*60*60)) % (1000*60)) % 1000);
		      return time;      
		}
		
		private long convertToMillis(String time) throws ParseException{
			 if (!time.equals("NA")){
				 SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
				 Date dateTime = timeFormat.parse(time);
				 return ((long)dateTime.getTime()- 28800000);
			 }
			 else return 0;
		}
		
		public void moveFiles(File [] files, String destination){
			boolean success = true;
			String bkpSubFolder = dateFormat.format(new Date());
			//File subDir = new File("logBackUpDir/bkpSubFolder");
			File subDir = new File(destination);
			subDir.mkdir();
			for (File fileName : files) {
				//if (!fileName.renameTo(new File(configInfo.logBackUpDir+ bkpSubFolder+fileName.getName())))
				if (!fileName.renameTo(new File(subDir+ "/"+fileName.getName())))
					success = false;			
			}
		
			if (success )log.debug(" Clean up successful. All log files have been moved to the backup folder");
			
		}
		
		
		public static String convertMillis(long milliseconds){
			long seconds, minutes, hours;
			   seconds = milliseconds / 1000;
			   minutes = seconds / 60;
			   seconds = seconds % 60;
			   hours = minutes / 60;
			   minutes = minutes % 60;
		       return(hours + ":" + minutes + ":" + seconds+"(HH:MM:SS)");
		}
		
		public static void main(String[] args) throws Exception {
			// Unit test code here
			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
			String startTime = dateFormat.format(new Date());
	        Date d1 = dateFormat.parse(startTime);  	        	    
		    Date d2 = dateFormat.parse("11-02-2013 08:33:59 pm");
		    String endTime = dateFormat.format(d2);
		    System.out.println("Start : "+ startTime);
		    System.out.println("End : "+ endTime);
		    System.out.println("Time diff: " + getTimeDiffOfDates(d2, d1));
		    
		    //Unit test code for 
		    String dateStart = "01/14/2012 09:29:58";
			String dateStop = "01/15/2012 11:36:48";	 
			//HH converts hour in 24 hours format (0-23), day calculation
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date d3 = format.parse(dateStart);
			Date d4 = format.parse(dateStop);
			
			log.info(new Utils().timeDiffOfDatesInDaysHrsMinsSecs(d3, d4));
			
			System.out.println(Utils.getExcelColNumberFromLetter("L"));
			System.out.println(Utils.getExcelColNumberFromLetter("K"));
			System.out.println(Utils.getExcelColNumberFromLetter("A"));
			System.out.println(Utils.getExcelColNumberFromLetter("Z"));
			System.out.println(Utils.getExcelColNumberFromLetter("AZ"));
			System.out.println(Utils.getExcelColumnName(12));
			System.out.println(Utils.getExcelColumnName(11));
			System.out.println(Utils.getExcelColumnName(1));
			System.out.println(Utils.getExcelColumnName(26));
			System.out.println(Utils.getExcelColumnName(52));
			
			
		}

		
}
