package com.agilesumo.runjogwalk;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ExcercisesDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_EXCERCISE, MySQLiteHelper.COLUMN_MINUTES, MySQLiteHelper.COLUMN_SECONDS };

  public ExcercisesDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Excercise createExcercise(String excercise, long minutes, long seconds) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_EXCERCISE, excercise);
    values.put(MySQLiteHelper.COLUMN_MINUTES, minutes);
    values.put(MySQLiteHelper.COLUMN_SECONDS, seconds);

    long insertId = database.insert(MySQLiteHelper.TABLE_EXCERCISES, null,
        values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXCERCISES,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Excercise newExcercise = cursorToExcercise(cursor);
    cursor.close();
    return newExcercise;
  }

  public void deleteExcercise(Excercise excercise) {
    long id = excercise.getId();
    System.out.println("Excercise deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_EXCERCISES, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public void deleteAllExcercises(){
	  database.delete(MySQLiteHelper.TABLE_EXCERCISES, null, null);
  }
  
  public List<Excercise> getAllExcercises() {
    List<Excercise> excercises = new ArrayList<Excercise>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXCERCISES,
        allColumns, null, null, null, null, null);

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
  
  public TimeDuration getTotalDuration(){
	  TimeDuration totalDuration = new TimeDuration();
	  List<Excercise> allExcercises = getAllExcercises();
	  for (Excercise excercise : allExcercises){
		  totalDuration.addSeconds(excercise.getSeconds());
		  totalDuration.addMinutes(excercise.getMinutes());
	  }
	  return totalDuration;
  }

  private Excercise cursorToExcercise(Cursor cursor) {
    Excercise excercise = new Excercise();
    excercise.setId(cursor.getLong(0));
    excercise.setExcercise(cursor.getString(1));
    excercise.setMinutes(cursor.getLong(2));
    excercise.setSeconds(cursor.getLong(3));
    return excercise;
  }
} 