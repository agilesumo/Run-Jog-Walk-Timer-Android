package com.agilesumo.runjogwalk;




import java.util.List;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class MainActivity extends ListActivity {

	//=======Constants========

	
	// =======================
	
	// =====Instance variables=====
	private ExcercisesDataSource datasource;

		
	private SharedPreferences settings;
	
	ArrayAdapter<Excercise> adapter;
	
	private TextView addNewPrompt;
	
	private TextView totalDurationText;
	
	private Button startBtn;
	
	private Button clearBtn;

	
	// ===========================



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		datasource = new ExcercisesDataSource(this);
	    datasource.open();

	    List<Excercise> values = datasource.getAllExcercises();
	    
	    adapter = new ArrayAdapter<Excercise>(this, android.R.layout.simple_list_item_1, values);
		    setListAdapter(adapter);
		    
		
		values = datasource.getAllExcercises();
		addNewPrompt = (TextView)findViewById(R.id.add_new_text);
		totalDurationText = (TextView)findViewById(R.id.total_duration_text);
		startBtn = (Button)findViewById(R.id.startBtn);
	    clearBtn = (Button)findViewById(R.id.clearBtn);
		if(!values.isEmpty()){
			addNewPrompt.setVisibility(View.GONE);
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
	    /* Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_close:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }*/
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
			    	
			    
			      
			    /*	delete the first item in the list and the database	
			    case R.id.delete:
				    if (getListAdapter().getCount() > 0) {
					    excercise = (Excercise) getListAdapter().getItem(0);
					    datasource.deleteExcercise(excercise);;
					    adapter.remove(excercise);
						adapter.notifyDataSetChanged();		
				    }
			        break;*/
			    case R.id.clearBtn:
			    	datasource.deleteAllExcercises();
			    	adapter.clear();
			    	adapter.notifyDataSetChanged();
					addNewPrompt.setVisibility(View.VISIBLE);
					hideViews();

			        break;  
			      
		    }

	  }  
	  
	  
	 @SuppressWarnings("unchecked")
	@Override
	  protected void onResume() {
	   Log.d("AndyDebuggingDelete", "got to on resume."); 

	    datasource.open();
	    super.onResume();

		if(!datasource.getAllExcercises().isEmpty()) {
			
		    addNewPrompt.setVisibility(View.GONE);
		    showViews();
 
		    adapter.clear();
		    addAllData(datasource.getAllExcercises(),adapter);
		    adapter.notifyDataSetChanged();
		}
		
		else {
			addNewPrompt.setVisibility(View.VISIBLE);
			hideViews();
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
	    TimeDuration totalDuration = datasource.getTotalDuration();
	    totalDurationText.setText("Total Duration: " + totalDuration.toStringLong());
	    totalDurationText.setVisibility(View.VISIBLE);
		
	}
}
