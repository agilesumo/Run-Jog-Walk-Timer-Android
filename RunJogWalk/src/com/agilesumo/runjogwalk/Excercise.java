package com.agilesumo.runjogwalk;

public class Excercise {
  private long id;
  private String excercise;
  private long hours;
  private long minutes;
  private long seconds;
  
  public long getId() {
      return id;
  }

  public void setId(long id) {
      this.id = id;
  }

  public long getHours() {
	  return hours;
  }
  
  public void setHours(long hours) {
	  this.hours = hours;
  }
  public String getExcercise() {
      return excercise;
  }

  public void setExcercise(String excercise) {
      this.excercise = excercise;
  }

  public long getMinutes() {
      return minutes;
  }

  public void setMinutes(long minutes) {
      this.minutes = minutes;
  }
	  
  public long getSeconds() {
	  return seconds;
  }

  public void setSeconds(long seconds) {
	  this.seconds = seconds;
  }
  
  public TimeDuration getTimeDuration(){
	  TimeDuration duration = new TimeDuration();
	  duration.addHours(hours);
	  duration.addMinutes(minutes);
	  duration.addSeconds(seconds);
	  return duration;
	  
  }
  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
	  String durationStr = excercise + ": ";
	  if (minutes == 0 && seconds == 0){
		  return  durationStr + hours + " hr";
	  }
	  if (hours > 0){
		  durationStr += hours + " hr, ";
	  }
	  
	  if (minutes == 0){
		  return durationStr + seconds + " secs";
	  }
	  if (seconds == 0){
		  return durationStr + minutes + " mins";
	  }
      return durationStr + minutes+" mins, "+seconds+" secs";
  }
} 