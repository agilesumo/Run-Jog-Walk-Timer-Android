package com.agilesumo.runjogwalk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_EXCERCISES = "excercises";
  public static final String COLUMN_EXCERCISE_ID = "excercise_id";
  public static final String COLUMN_EXCERCISE = "excercise";
  public static final String COLUMN_HOURS = "hours";
  public static final String COLUMN_MINUTES = "minutes";
  public static final String COLUMN_SECONDS = "seconds";
  public static final String COLUMN_WORKOUT_NAME = "workout";
  
  public static final String TABLE_WORKOUTS = "workouts";
  public static final String COLUMN_WORKOUT_ID = "workout_id";


		  

  private static final String DATABASE_NAME = "excercises.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE_EXCERCISE = "create table "
      + TABLE_EXCERCISES + "(" + COLUMN_EXCERCISE_ID
      + " integer primary key autoincrement,"  + COLUMN_EXCERCISE + " text not null, "
      + COLUMN_HOURS + " integer default 0 ," + COLUMN_MINUTES + " integer default 0 ," 
      + COLUMN_SECONDS + " integer default 0 , " + COLUMN_WORKOUT_ID + " integer ,"+ "foreign key (" + COLUMN_WORKOUT_ID
      + ") references " + TABLE_WORKOUTS + "(" + COLUMN_WORKOUT_ID + ") on delete cascade );";
  
  private static final String DATABASE_CREATE_WORKOUT ="create table "
		  + TABLE_WORKOUTS +"(" + COLUMN_WORKOUT_ID + " integer primary key autoincrement,"
		  + COLUMN_WORKOUT_NAME + " text not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
	  database.execSQL(DATABASE_CREATE_WORKOUT);
	  database.execSQL(DATABASE_CREATE_EXCERCISE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCERCISES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);

    onCreate(db);
  }

} 