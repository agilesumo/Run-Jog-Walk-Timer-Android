package com.agilesumo.runjogwalk;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;

public class ExcercisesDataSource {

  private static final String DATABASE_SELECT_WORKOUTS = MySQLiteHelper.COLUMN_WORKOUT_ID + "= ?"; 		
  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] excerciseTableColumns = { MySQLiteHelper.COLUMN_EXCERCISE_ID, MySQLiteHelper.COLUMN_EXCERCISE, MySQLiteHelper.COLUMN_HOURS, 
		  MySQLiteHelper.COLUMN_MINUTES, MySQLiteHelper.COLUMN_SECONDS, MySQLiteHelper.COLUMN_WORKOUT_ID };
  private String[] workoutTableColumns = { MySQLiteHelper.COLUMN_WORKOUT_ID, MySQLiteHelper.COLUMN_WORKOUT_NAME };

  public ExcercisesDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }
  
  public Workout createWorkout(String workoutName){
	  ContentValues values = new ContentValues();
	  values.put(MySQLiteHelper.COLUMN_WORKOUT_NAME, workoutName);
	  long insertId = database.insert(MySQLiteHelper.TABLE_WORKOUTS, null, values);
	  
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
	      workoutTableColumns, MySQLiteHelper.COLUMN_WORKOUT_ID + " = " + insertId, null, null, null, null);
      cursor.moveToFirst();
      Workout newWorkout = cursorToWorkout(cursor);
      cursor.close();
      return newWorkout;
	      

  }

  public Excercise createExcercise(String excercise, long hours, long minutes, long seconds, long workoutId) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_EXCERCISE, excercise);
    values.put(MySQLiteHelper.COLUMN_HOURS, hours);
    values.put(MySQLiteHelper.COLUMN_MINUTES, minutes);
    values.put(MySQLiteHelper.COLUMN_SECONDS, seconds);
    values.put(MySQLiteHelper.COLUMN_WORKOUT_ID, workoutId);


    long insertId = database.insert(MySQLiteHelper.TABLE_EXCERCISES, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXCERCISES,
        excerciseTableColumns, MySQLiteHelper.COLUMN_EXCERCISE_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Excercise newExcercise = cursorToExcercise(cursor);
    cursor.close();
    return newExcercise;
  }
  
  public void deleteWorkout(long workoutId){
      database.delete(MySQLiteHelper.TABLE_WORKOUTS, MySQLiteHelper.COLUMN_WORKOUT_ID
				+ " = " + workoutId, null);
  }

  public void deleteExcercise(Excercise excercise) {
    long id = excercise.getId();
    System.out.println("Excercise deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_EXCERCISES, MySQLiteHelper.COLUMN_EXCERCISE_ID
        + " = " + id, null);
  }
  
  public void deleteWorkoutExcercises(long workoutId) {
	database.delete(MySQLiteHelper.TABLE_EXCERCISES, MySQLiteHelper.COLUMN_WORKOUT_ID
	+ " = " + workoutId, null);
  }

  public void deleteAllExcercises(){
	  database.delete(MySQLiteHelper.TABLE_EXCERCISES, null, null);
  }
  
  public List<Excercise> getAllExcercises() {
    List<Excercise> excercises = new ArrayList<Excercise>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXCERCISES,
        excerciseTableColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Excercise excercise = cursorToExcercise(cursor);
      excercises.add(excercise);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return excercises;
  }
  
  public ArrayList<Excercise> getExcercisesByWorkoutId(long workoutId) {
	    ArrayList<Excercise> excercises = new ArrayList<Excercise>();
        String[] workoutIdStr = { "" + workoutId };
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXCERCISES,
	        excerciseTableColumns, DATABASE_SELECT_WORKOUTS, workoutIdStr, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Excercise excercise = cursorToExcercise(cursor);
	      excercises.add(excercise);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return excercises;
	  }
  
  public List<Workout> getAllWorkouts() {
	    List<Workout> workouts = new ArrayList<Workout>();

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
	        workoutTableColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Workout workout = cursorToWorkout(cursor);
	      workouts.add(workout);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return workouts;
	  }
  
  public TimeDuration getTotalDuration(){
	  TimeDuration totalDuration = new TimeDuration();
	  List<Excercise> allExcercises = getAllExcercises();
	  for (Excercise excercise : allExcercises){
		  totalDuration.addSeconds(excercise.getSeconds());
		  totalDuration.addMinutes(excercise.getMinutes());
		  totalDuration.addHours(excercise.getHours());
	  }
	  return totalDuration;
  }
  
  public TimeDuration getWorkoutDuration(long workoutId){
	  TimeDuration totalDuration = new TimeDuration();
	  List<Excercise> allExcercises = getExcercisesByWorkoutId(workoutId);
	  for (Excercise excercise : allExcercises){
		  totalDuration.addSeconds(excercise.getSeconds());
		  totalDuration.addMinutes(excercise.getMinutes());
		  totalDuration.addHours(excercise.getHours());
	  }
	  return totalDuration;
  }

  private Excercise cursorToExcercise(Cursor cursor) {
    Excercise excercise = new Excercise();
    excercise.setId(cursor.getLong(0));
    excercise.setExcercise(cursor.getString(1));
    excercise.setHours(cursor.getLong(2));
    excercise.setMinutes(cursor.getLong(3));
    excercise.setSeconds(cursor.getLong(4));
    excercise.setWorkoutId(cursor.getLong(5));
    return excercise;
  }
  
  private Workout cursorToWorkout(Cursor cursor) {
      Workout workout = new Workout();
	  workout.setId(cursor.getLong(0));
	  workout.setWorkoutName(cursor.getString(1));
      return workout;
  }
  
  
  
} 