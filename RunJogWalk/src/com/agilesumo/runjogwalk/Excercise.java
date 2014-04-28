package com.agilesumo.runjogwalk;

public class Excercise {
  private long id;
  private String excercise;
  private long minutes;
  private long seconds;
  
  public long getId() {
      return id;
  }

  public void setId(long id) {
      this.id = id;
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
	  
  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
	  if (minutes == 0){
		  return excercise +": " + seconds + " secs";
	  }
	  if (seconds == 0){
		  return excercise + ": " + minutes + " mins";
	  }
      return excercise+": "+minutes+" mins, "+seconds+" secs";
  }
} 