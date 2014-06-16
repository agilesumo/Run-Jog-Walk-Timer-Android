package com.agilesumo.runjogwalk;

public class Workout {
	
	private long id;
	private String workoutName;
	
	public long getId() {
	    return id;
	}

	public void setId(long id) {
	    this.id = id;
	}
	
	public String getWorkoutName(){
	    return workoutName;
	}

	public void setWorkoutName(String workoutName){
	    this.workoutName = workoutName;
	}
	
	public String toString(){
		return workoutName;
	}


}
