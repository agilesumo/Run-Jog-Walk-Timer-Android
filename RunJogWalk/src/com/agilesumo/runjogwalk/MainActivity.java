package com.agilesumo.runjogwalk;




import java.util.Collections;
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
import android.text.InputFilter;
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
	
	private TextView savedWorkoutsHeading;
	
	private Button createWorkoutBtn;
	ArrayAdapter<Workout> adapter;
	
	

	
		
	// ===========================



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		savedWorkoutsHeading = (TextView)findViewById(R.id.savedWorkoutsHeading);
		
		datasource = new ExcercisesDataSource(this);
	    datasource.open();
        
	    List<Workout> allWorkouts = datasource.getAllWorkouts();
	    
	    adapter = new ArrayAdapter<Workout>(this, R.layout.workout_list_item, R.id.listTextView, allWorkouts);
		setListAdapter(adapter);
		
		ListView workoutsList = getListView();
		
		    
		
		if(allWorkouts.isEmpty()){
			savedWorkoutsHeading.setVisibility(View.INVISIBLE);
	    }
		else{
			savedWorkoutsHeading.setVisibility(View.VISIBLE);
		}
		
	    workoutsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	        	try {
	        		datasource.open();
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
	    
	    datasource.close();
	    
	      
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
	        	if (Build.VERSION.SDK_INT < 11) {
			        Intent intent = new Intent(this, SettingsActivity.class);
			        startActivity(intent);
			        break;
	        	}
	        	else{
	        		try{
	        			Intent intent = new Intent(this, SettingsAPI11PlusActivity.class);
				        startActivity(intent);
				        break;
	        		}
	        		catch(Exception e){
	        			Log.d("Andy",e.getMessage());
	        		}
			        
	        	}
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
		  // reference for implementation see -> 
		  // http://stackoverflow.com/questions/11363209/alertdialog-with-positive-button-and-validating-custom-edittext
		  
		  AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

		  alertBuilder.setTitle("CREATE NEW WORKOUT");
		  alertBuilder.setMessage("Workout Name");

		  // Set an EditText view to get user input 
		  final EditText input = new EditText(this);
		  InputFilter[] filterArray = new InputFilter[1];
		  filterArray[0] = new InputFilter.LengthFilter(25);
		  input.setFilters(filterArray);
		  alertBuilder.setView(input);
          datasource = new ExcercisesDataSource(MainActivity.this);



		  alertBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
	              
	            	 //All of the fun happens inside the CustomListener now.
	                  //I had to move it to enable data validation.
			    	  
	              

		      }
		  });

		  alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int whichButton) {
		        // Canceled.
		      }
		  });
		  AlertDialog alertDialog = alertBuilder.create();
		  alertDialog.show();
		  Button newWorkoutButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		  newWorkoutButton.setOnClickListener(new WorkoutCustomListener(alertDialog, input, datasource, this));
		  
	  }
	
	
	
	 @SuppressWarnings("unchecked")
	@Override
	  protected void onResume() {

	    datasource.open();
	    super.onResume();

		if(!datasource.getAllWorkouts().isEmpty()) {
			List<Workout> allWorkouts = datasource.getAllWorkouts();
            adapter.clear();
		    addAllWorkouts(allWorkouts, adapter);
		    adapter.notifyDataSetChanged();
			savedWorkoutsHeading.setVisibility(View.VISIBLE);

		}
		else {
			adapter.clear();
		    adapter.notifyDataSetChanged();
			savedWorkoutsHeading.setVisibility(View.INVISIBLE);
			createWorkoutBtn = (Button)findViewById(R.id.createWorkoutBtn);
			createClicked(createWorkoutBtn);

		}
		datasource.close();

		
		
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
