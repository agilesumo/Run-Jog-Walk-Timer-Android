package com.agilesumo.runjogwalk;

import java.util.Calendar;

import com.agilesumo.runjogwalk.R;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddRunActivity extends Activity {
	
	//=======Constants========

		public final static String EXTRA_CALLER = "com.agilesumo.runjogwalk.RUNJOGWALK";
		public final static String EXTRA_HOURS = "com.agilesumo.runjogwalk.HOURS";
		public final static String EXTRA_MINUTES = "com.agilesumo.runjogwalk.MINUTES";
		public final static String EXTRA_SECONDS = "com.agilesumo.runjogwalk.SECONDS";
		

	
	// Time changed flag
	private boolean timeChanged = false;
	
	// Time scrolled flag
	private boolean timeScrolled = false;
	
	private  WheelView minutes;
	
	private WheelView seconds;
	
	private ExcercisesDataSource datasource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_run);
	
		minutes = (WheelView) findViewById(R.id.mins);
		minutes.setViewAdapter(new NumericWheelAdapter(this, 0, 90));
		minutes.setCyclic(true);

	
		seconds = (WheelView) findViewById(R.id.secs);
		seconds.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		seconds.setCyclic(true);
		
	
		// add listeners
		addChangingListener(seconds, "seconds");
		addChangingListener(minutes, "minutes");
	
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
			}
		};
		minutes.addChangingListener(wheelListener);
		seconds.addChangingListener(wheelListener);
		
		OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        minutes.addClickingListener(click);
        seconds.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				timeChanged = false;
			}
		};
		
		minutes.addScrollingListener(scrollListener);
		seconds.addScrollingListener(scrollListener);
		
		
	}

	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_run, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/** Called when the user clicks a button */
    
	public void onClick(View view) {
		switch (view.getId()) {
		    case R.id.btnAddRun:
				try {
			    	Log.d("AndyDebuggingDelete", "got here 1."); 
		
					long hours = 0;
			    	long mins = minutes.getCurrentItem();
					long secs = seconds.getCurrentItem();
					
				    if (mins == 0 && secs == 0) {
				        Toast.makeText(this, "Cannot add a run of 0 mins and 0 secs", Toast.LENGTH_SHORT).show();
				        return;
				    }
				    if(mins>59) {
				    	hours++;
				    	mins -= 60;
				    }
				    Intent intent = getIntent();
					long workoutId = intent.getLongExtra(WorkoutActivity.EXTRA_WORKOUT_ID, 0);
				    datasource = new ExcercisesDataSource(this);
				    datasource.open();
				    // save the new comment to the database
				    datasource.createExcercise("Run", hours, mins, secs, workoutId);
				    datasource.close();
					finish();
		
					
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
				
		    case R.id.btnCancel:
		    	finish();
		}
	}
	
	  @Override
	  protected void onResume() {
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	  }
	  
	  	

	
}

