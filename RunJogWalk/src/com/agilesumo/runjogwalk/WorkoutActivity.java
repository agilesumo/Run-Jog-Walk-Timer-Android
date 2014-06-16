package com.agilesumo.runjogwalk;




import java.util.List;








import android.graphics.Typeface;
import android.inputmethodservice.ExtractEditText;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class WorkoutActivity extends ListActivity {

	//=======Constants========

	public final static String EXTRA_WORKOUT_ID = "com.agilesumo.runjogwalk.ID";

	// =======================
	
	// =====Instance variables=====
	private ExcercisesDataSource datasource;

		
	
	ArrayAdapter<Excercise> adapter;
	
	private TextView addNewPrompt;
	
	private TextView totalDurationText;
	
	private Button startBtn;
	
	private Button clearBtn;
	
	private Button deleteBtn;
	
	private long workoutId;

	
	// ===========================



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout);
		
		Intent intent = getIntent();
		String workoutName = intent.getStringExtra(MainActivity.EXTRA_WORKOUT_NAME);
		
		TextView workoutNameText = (TextView)findViewById(R.id.workout_name);
		SpannableString spanString = new SpannableString(workoutName);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		workoutNameText.setText(spanString);
		
		workoutId = intent.getLongExtra(MainActivity.EXTRA_WORKOUT_ID, 0);
		
		
		
		datasource = new ExcercisesDataSource(this);
	    datasource.open();
	    
	    

	    List<Excercise> values = datasource.getExcercisesByWorkoutId(workoutId);
	    
	    adapter = new ArrayAdapter<Excercise>(this, android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		    
		
		addNewPrompt = (TextView)findViewById(R.id.add_new_text);
		totalDurationText = (TextView)findViewById(R.id.total_duration_text);
		startBtn = (Button)findViewById(R.id.startBtn);
	    clearBtn = (Button)findViewById(R.id.clearBtn);
	    deleteBtn = (Button)findViewById(R.id.deleteBtn);
		if(!values.isEmpty()){
			addNewPrompt.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.GONE);
			showViews();
	    }


	    // use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    
	      
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
		/*
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("musicOn", musicBtn.isChecked());
	    // Commit the edits!
        editor.commit();*/
	}

	
	
	
	/** Called when the user clicks the add run button */

	  public void onClick(View view) {
		    @SuppressWarnings("unchecked")
		    ArrayAdapter<Excercise> adapter = (ArrayAdapter<Excercise>) getListAdapter();


		    switch (view.getId()) {
			    case R.id.runBtn:
			    	try {
						Intent intent = new Intent(this, AddRunActivity.class);
		                intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			            startActivity(intent);
			            break;
						
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
			    case R.id.jogBtn:
			    	try {
						Intent intent = new Intent(this, AddJogActivity.class);
						intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			            startActivity(intent);
			            break;
						
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
			    	
			    case R.id.walkBtn:
			    	try {
						Intent intent = new Intent(this, AddWalkActivity.class);
						intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			            startActivity(intent);
			            break;
						
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
			    	
			    case R.id.startBtn:
			    	try {
						Intent intent = new Intent(this, WorkoutTimerActivity.class);
						intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			            startActivity(intent);
			            break;
						
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
			    	
			    case R.id.clearBtn:
			    	datasource.deleteWorkoutExcercises(workoutId);
			    	adapter.clear();
			    	adapter.notifyDataSetChanged();
					addNewPrompt.setVisibility(View.VISIBLE);
					deleteBtn.setVisibility(View.VISIBLE);
					hideViews();

			        break;
			        
			    case R.id.deleteBtn:
			    	datasource.deleteWorkout(workoutId);
			    	finish();

			        break;  
			      
		    }

	  }  
	  
	  
	 @SuppressWarnings("unchecked")
	@Override
	  protected void onResume() {

	    datasource.open();
	    super.onResume();

		if(!datasource.getExcercisesByWorkoutId(workoutId).isEmpty()) {

		    addNewPrompt.setVisibility(View.GONE);
		    deleteBtn.setVisibility(View.GONE);
		    showViews();

		    adapter.clear();
		    addAllData(datasource.getExcercisesByWorkoutId(workoutId),adapter);
		    adapter.notifyDataSetChanged();
		}
		
		else {
			try {
				
			
				addNewPrompt.setVisibility(View.VISIBLE);
				deleteBtn.setVisibility(View.VISIBLE);
				hideViews();
			}
			catch (Exception e) {
			    // handle any errors
			    Log.e("ErrorWrokoutActivity", "Error in resume", e);  // log the error
			    // Also let the user know something went wrong
			    Toast.makeText(
			        getApplicationContext(),
			        e.getClass().getName() + " " + e.getMessage(),
			        Toast.LENGTH_LONG).show();
			}
		}
      }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	  
	 // method added because ArrayAdapter.addAll() method is not supported in older versions of android api
	@SuppressLint("NewApi")
	private void addAllData(List<Excercise> excercises, ArrayAdapter<Excercise> theAdapter) {
		    if (excercises != null) {
		        //If the platform supports it, use addAll, otherwise add in loop
		        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		            theAdapter.addAll(excercises);
		        } else {
		            for(Excercise excercise: excercises) {
		                theAdapter.add(excercise);
		            }
		        }
		    }
	}
	
	// helper method to hide certain views in activity
	private void hideViews(){
	    startBtn.setVisibility(View.GONE);
	    clearBtn.setVisibility(View.GONE);
	    totalDurationText.setVisibility(View.GONE);


	}
	// helper method to show certain views in activity
	private void showViews(){
		startBtn.setVisibility(View.VISIBLE);
	    clearBtn.setVisibility(View.VISIBLE);
	    TimeDuration totalDuration = datasource.getWorkoutDuration(workoutId);
	    totalDurationText.setText("TOTAL DURATION: " + totalDuration.toStringLong());
	    totalDurationText.setVisibility(View.VISIBLE);
		
	}
}
