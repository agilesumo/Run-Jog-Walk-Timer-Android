package com.agilesumo.runjogwalk;




import java.util.List;


import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends ListActivity {

	//=======Constants========
	public final static String EXTRA_WORKOUT_NAME = "com.agilesumo.runjogwalk.WORKOUT_NAME";
	public final static String EXTRA_WORKOUT_ID = "com.agilesumo.runjogwalk.ID";


	
	// =======================
	
	// =====Instance variables=====
	private ExcercisesDataSource datasource;

		
	
	ArrayAdapter<Workout> adapter;
	
	private TextView addNewPrompt;
	
	private TextView totalDurationText;
	
	private Button startBtn;
	
	private Button clearBtn;

	
	// ===========================



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		TextView savedWorkoutsHeading = (TextView)findViewById(R.id.savedWorkoutsHeading);
		
		datasource = new ExcercisesDataSource(this);
	    datasource.open();
        
	    List<Workout> values = datasource.getAllWorkouts();
	    
	    adapter = new ArrayAdapter<Workout>(this, R.layout.workout_list_item, R.id.listTextView, values);
		setListAdapter(adapter);
		
		ListView workoutsList = getListView();
		    
		
		if(values.isEmpty()){
			savedWorkoutsHeading.setVisibility(View.INVISIBLE);
	    }
		else{
			savedWorkoutsHeading.setVisibility(View.VISIBLE);
		}
		
	    workoutsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	        	try {
					Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
				    List<Workout> values = datasource.getAllWorkouts();

					Workout workout = values.get(position);
					String workoutName = workout.getWorkoutName();
					long workoutId = workout.getId();
					
					intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
	                intent.putExtra(EXTRA_WORKOUT_NAME, workoutName);
					
					
		            startActivity(intent);
		            					
				}
				catch (Exception e) {
				    // handle any errors
				    Log.e("ErrorMainActivity", "Error in activity", e);  // log the error
				    // Also let the user know something went wrong
				    Toast.makeText(
				        getApplicationContext(),
				        e.getClass().getName() + " " + e.getMessage(),
				        Toast.LENGTH_LONG).show();
				}
	          
	        }	
	      });
	    

	    
	      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   
		
	    switch (item.getItemId()) {

	        case R.id.action_settings:
		        // Launch settings activity
		        Intent intent = new Intent(this, SettingsActivity.class);
		        startActivity(intent);
		        break;
	    }
		return true;
	}
	
	  @Override
	  public void onStart() {
	    super.onStart();
	  }

	  
	 
	
	
	protected void onStop() {
		super.onStop();
		
	}

	/** Called when the user clicks the add create workout button */
	
	  public void createClicked(View view){
		  
		  AlertDialog.Builder alert = new AlertDialog.Builder(this);

		  alert.setTitle("CREATE NEW WORKOUT");
		  alert.setMessage("Workout Name");

		  // Set an EditText view to get user input 
		  final EditText input = new EditText(this);
		  alert.setView(input);

		  alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
	              String workoutName = input.getText().toString();
	              datasource = new ExcercisesDataSource(MainActivity.this);
				  datasource.open();
				  // save the new comment to the database
				  Workout newWorkout = datasource.createWorkout(workoutName);
				  datasource.close();
				  
				  Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
				  long id = newWorkout.getId();
                  intent.putExtra(EXTRA_WORKOUT_ID, id);
                  intent.putExtra(EXTRA_WORKOUT_NAME, workoutName);
                  startActivity(intent);

		      }
		  });

		  alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
		        // Canceled.
		      }
		  });

		  alert.show();
		  
	  }
	
	
	
	 @SuppressWarnings("unchecked")
	@Override
	  protected void onResume() {

	    datasource.open();
	    super.onResume();

		if(!datasource.getAllWorkouts().isEmpty()) {
			
            adapter.clear();
		    addAllWorkouts(datasource.getAllWorkouts(),adapter);
		    adapter.notifyDataSetChanged();
		}
		else {
			adapter.clear();
		    adapter.notifyDataSetChanged();
		}
		
      }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	  

	  
	 // method added because ArrayAdapter.addAll() method is not supported in older versions of android api
	@SuppressLint("NewApi")
	private void addAllWorkouts(List<Workout> workouts, ArrayAdapter<Workout> theAdapter) {
		    if (workouts != null) {
		        //If the platform supports it, use addAll, otherwise add in loop
		        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		            theAdapter.addAll(workouts);
		        } else {
		            for(Workout workout: workouts) {
		                theAdapter.add(workout);
		            }
		        }
		    }
	}
	
	
}
