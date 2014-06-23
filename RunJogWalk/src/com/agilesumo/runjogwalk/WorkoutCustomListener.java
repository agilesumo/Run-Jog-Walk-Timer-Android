package com.agilesumo.runjogwalk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;


public class WorkoutCustomListener implements View.OnClickListener {
	    private final Dialog dialog;
	    private final EditText editText;
	    private final ExcercisesDataSource datasource;
	    private final Context context;
	    public WorkoutCustomListener(Dialog dialog, EditText editText, ExcercisesDataSource datasource, Context context ) {
	        this.dialog = dialog;
	        this.editText = editText;
	        this.datasource = datasource;
	        this.context = context;
	    }
	    @Override
	    public void onClick(View v) {
	        // put your code here
			datasource.open();
	        
			if(Validation.hasText(editText) && Validation.isValidName(editText)){

	            String workoutName = editText.getText().toString().trim();
				// save the new comment to the database
				Workout newWorkout = datasource.createWorkout(workoutName);
				  
				Intent intent = new Intent(context, WorkoutActivity.class);
				long id = newWorkout.getId();
                intent.putExtra(MainActivity.EXTRA_WORKOUT_ID, id);
                intent.putExtra(MainActivity.EXTRA_WORKOUT_NAME, workoutName);
                context.startActivity(intent);
	            dialog.dismiss();
	        }
			else{
	            //Toast.makeText(YourActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
	        }
			datasource.close();

	    }
	    

	}

