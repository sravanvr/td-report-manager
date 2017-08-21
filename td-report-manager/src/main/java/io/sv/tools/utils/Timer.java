package io.sv.tools.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Timer {

	private Date startDate, endDate;
	private String startTime, endTime, diff;
	private static Logger log = Logger.getLogger(Timer.class);
		
	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

//	public String getDiff() {
//		return diff;
//	}

	private DateFormat dateFormat;
	
	public Timer(){
		DOMConfigurator.configure("config/log4j-config.xml");
		dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
	}
	
	public String start(){
		this.startTime = dateFormat.format(new Date());	
		
		try {
			this.startDate = dateFormat.parse(startTime);
		} catch (ParseException e) {			
			e.printStackTrace();
		}  
		
		return this.startTime;
	}
	
	public String stop(){
		this.endTime = dateFormat.format(new Date());
		try {
			this.endDate = dateFormat.parse(endTime);
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		return this.endTime;
	}
	
	public String getDiff(){
		this.diff = Utils.timeDiffOfDates(this.startDate, this.endDate);
		return this.diff;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer();
		timer.start();
		Object mon = new Object();
		synchronized (mon){
			Thread.sleep(3000);
		}		
		timer.stop();
		
		log.info(timer.startTime);
		log.info(timer.endTime);
		log.info(timer.getDiff());
	}

}
