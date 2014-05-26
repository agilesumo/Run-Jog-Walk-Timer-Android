package com.agilesumo.runjogwalk;

public class TimeDuration {

	private long hours = 0;
	private long minutes = 0;
	private long seconds = 0;
	
	private static final long SECONDS_IN_MINUTE = 60;
	private static final long MINUTES_IN_HOUR =60;
	
	public TimeDuration(){
		
	}
	
	// param: theSeconds - must have a value of 59 or less
	public void addSeconds(long theSeconds){
		long totalSecs = seconds + theSeconds;
		if (totalSecs > 59){
			seconds = totalSecs % SECONDS_IN_MINUTE;
			minutes++;
			if(minutes == MINUTES_IN_HOUR){
				hours++;
				minutes = 0;			
			}
		}
		else{
			seconds = totalSecs;
		}
	}
	
	// param theMinutes - can be any positive number with no upper limit
	public void addMinutes(long theMinutes){
		long totalMins = minutes + theMinutes;
		if (totalMins > 59){
			minutes = totalMins % MINUTES_IN_HOUR;
			hours += totalMins / MINUTES_IN_HOUR;
			
		}
		else {
			minutes += theMinutes;
		}
	}
	
	// param theHours - can be any positive number with no upper limit
    public void addHours(long theHours){
    	hours += theHours;
    }
    
    public void minusOneSecond(){
    	if(hours == 0 && minutes == 0 && seconds == 0){
    		return;
    	}
    	else if(minutes == 0 && seconds == 0){
    		hours--;
    		minutes = 59;
    		seconds = 59;
    	}
    	else if(hours == 0 && seconds == 0){
    		minutes--;
    		seconds = 59;
    	}
    	else if(seconds == 0){
    		minutes--;
    		seconds=59;
    	}
    	else{
    		seconds--;
    	}
    }
	
	public long getHours(){
		return hours;
	}
	
	public long getMinutes(){
		return minutes;
	}
	
	public long getSeconds(){
		return seconds;
	}
	
	public boolean isZero(){
		return (hours == 0 && minutes == 0 & seconds == 0);
	}
	
	public String toString(){
		String outputStr = "";
		if(hours==0){
			outputStr += "00";
		}
		else if (hours<10){
			outputStr += "0" + hours;
		}
		else{
			outputStr += hours;
		}
		outputStr += ":";
		
		if(minutes == 0){
			outputStr += "00";
		} 
		else if (minutes<10){
			outputStr += "0" + minutes;
		}
		else{
			outputStr += minutes;
		}
		outputStr += ":";
		
		if(seconds == 0){
			outputStr += "00";
		}
		else if (seconds < 10){
			outputStr += "0" + seconds;
		}
		else{
			outputStr += seconds;
		}
		
		return outputStr;
	}
	
	
	
	public String toStringLong(){
		String outputStr = "";
		if ( hours != 0 ){
			if ( hours == 1 ){
				outputStr += hours + "hr ";
			}
			else{
				outputStr += hours + "hrs ";
			}
		}
		if ( minutes != 0){
			if ( minutes == 0 ){
				outputStr += minutes + "min ";
			}
			else{
				outputStr += minutes + "mins ";
			}
		}
		if ( seconds != 0 ){
			if (seconds == 1){
				outputStr += seconds + "sec";
			}
			else{
				outputStr += seconds + "secs";
			}
		}
		
		return outputStr;
		
		
		
	}
}
