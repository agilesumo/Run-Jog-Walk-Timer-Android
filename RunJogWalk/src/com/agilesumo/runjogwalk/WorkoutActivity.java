package com.agilesumo.runjogwalk;




import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class WorkoutActivity extends ListActivity {

	//=======Constants========

	public final static String EXTRA_EXCERCISE_ID = "com.agilesumo.runjogwalk.excerciseID";
	
	public final static String EXTRA_EXCERCISE_NAME = "com.agilesumo.runjogwalk.excerciseName";
	
	public final static String EXTRA_WORKOUT_ID = "com.agilesumo.runjogwalk.ID";
	
	public final static String EXTRA_EXCERCISE_MINS = "com.agilesumo.runjogwalk.excerciseMins";
	
	public final static String EXTRA_EXCERCISE_SECS = "com.agilesumo.runjogwalk.excerciseSecs";


	// =======================
	
	// =====Instance variables=====
	
	private ExcercisesDataSource datasource;

	private CustomAdapter adapter;
	
	private TextView addNewPrompt;
	
	private TextView totalDurationText;
	
	private Button startBtn;
			
	private long workoutId;

	
	// ===========================


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_workout);
		
		Intent intent = getIntent();
		
		String workoutName = intent.getStringExtra(MainActivity.EXTRA_WORKOUT_NAME);
		TextView workoutNameText = (TextView)findViewById(R.id.workout_name);		
		SpannableString spanString = new SpannableString(workoutName.toUpperCase(Locale.ENGLISH));
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		workoutNameText.setText(spanString);
		
		workoutId = intent.getLongExtra(MainActivity.EXTRA_WORKOUT_ID, 0);
				
		datasource = new ExcercisesDataSource(this);
	    datasource.open();
	    
	    ArrayList<Excercise> values = datasource.getExcercisesByWorkoutId(workoutId);
	    
	    adapter = new CustomAdapter(this, values);
		setListAdapter(adapter);
			
		addNewPrompt = (TextView)findViewById(R.id.add_new_text);
		totalDurationText = (TextView)findViewById(R.id.total_duration_text);
		startBtn = (Button)findViewById(R.id.startBtn);
		
		if(!values.isEmpty()){
			addNewPrompt.setVisibility(View.GONE);
			showViews();
	    }
		
		ListView excercisesList = getListView();
		
	    excercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	        	
        		datasource.open();
				Intent intent = null;
			    List<Excercise> values = datasource.getExcercisesByWorkoutId(workoutId);

				Excercise excercise = values.get(position);
				String excerciseName = excercise.getExcerciseName();
				long excerciseId = excercise.getId();
				
				if(excerciseName.equals("Run")){
					intent = new Intent(WorkoutActivity.this, EditRunActivity.class);
				}
				else if(excerciseName.equals("Jog")){
					intent = new Intent(WorkoutActivity.this, EditJogActivity.class);
				}
				else {
					intent = new Intent(WorkoutActivity.this, EditWalkActivity.class);
				}
								
                long totalMinutes = (excercise.getHours()*60) + excercise.getMinutes();
                long seconds = excercise.getSeconds();
                intent.putExtra(EXTRA_EXCERCISE_ID, excerciseId);
                intent.putExtra(EXTRA_EXCERCISE_NAME, excerciseName);
                intent.putExtra(EXTRA_EXCERCISE_MINS, totalMinutes);
                intent.putExtra(EXTRA_EXCERCISE_SECS, seconds);
								
	            startActivity(intent);
		            			          
	        }
	        
	      });
	    
		datasource.close();
	       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_workout, menu);
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
			        Intent intent = new Intent(this, SettingsAPI11PlusActivity.class);
			        startActivity(intent);
			        break;
	        	}
	        case R.id.action_delete:
	        	datasource.open();
	        	datasource.deleteWorkout(workoutId);
		    	finish();
		    	break;
	        case R.id.action_clear:
	        	datasource.open();
	        	datasource.deleteWorkoutExcercises(workoutId);
	        	adapter.clear();
	        	adapter.notifyDataSetChanged();
	     		addNewPrompt.setVisibility(View.VISIBLE);
	     		hideViews();
	
		    	break;  	
        	
	    }
	    
	return true;
	
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	}

	// Called when the user clicks the add run button
	public void onClick(View view) {

		Intent intent = null;
		switch (view.getId()) {
			case R.id.runBtn:
				intent = new Intent(this, AddRunActivity.class);
		        intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			    startActivity(intent);
			    break;
				
			case R.id.jogBtn:
				intent = new Intent(this, AddJogActivity.class);
				intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			    startActivity(intent);
			    break;
						    	
			case R.id.walkBtn:
				intent = new Intent(this, AddWalkActivity.class);
				intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			    startActivity(intent);
			    break;
					
			case R.id.startBtn:
			    intent = new Intent(this, WorkoutTimerActivity.class);
				intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
			    startActivity(intent);
			    break;
							    	    
		}

	}
	  
	  
	@Override
	protected void onResume() {

	    datasource.open();
	    super.onResume();

		if(!datasource.getExcercisesByWorkoutId(workoutId).isEmpty()) {

		    addNewPrompt.setVisibility(View.GONE);
		    showViews();

		    adapter.clear();
		    addAllData(datasource.getExcercisesByWorkoutId(workoutId),adapter);
		    adapter.notifyDataSetChanged();
		}
		
		else {
			addNewPrompt.setVisibility(View.VISIBLE);
			hideViews();	
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
	private void addAllData(List<Excercise> excercises, ArrayAdapter<Excercise> theAdapter) {
		
	    if (excercises != null) {
	        //If the platform supports it, use addAll, otherwise add in loop
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            theAdapter.addAll(excercises);
	        } 
	        else {
	            for(Excercise excercise: excercises) {
	                theAdapter.add(excercise);
	            }
	        }
	    }
	}
	
	// helper method to hide certain views in activity
	private void hideViews(){
		
	    startBtn.setVisibility(View.GONE);
	    totalDurationText.setVisibility(View.GONE);

	}
	// helper method to show certain views in activity
	private void showViews(){
		startBtn.setVisibility(View.VISIBLE);
	    TimeDuration totalDuration = datasource.getWorkoutDuration(workoutId);
	    totalDurationText.setText("TOTAL DURATION: " + totalDuration.toStringLong());
	    totalDurationText.setVisibility(View.VISIBLE);
		
	}
	
	// reference see: http://hmkcode.com/android-custom-listview-items-row/
	private class CustomAdapter extends ArrayAdapter<Excercise> {
		 
        private final Context context;
        private final ArrayList<Excercise> excercisesArrayList;
 
        public CustomAdapter(Context context, ArrayList<Excercise> excercisesArrayList) {
 
            super(context, R.layout.excercise_row, excercisesArrayList);
 
            this.context = context;
            this.excercisesArrayList = excercisesArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.excercise_row, parent, false);
 
            // 3. Get the two text view from the rowView
            TextView excerciseView = (TextView) rowView.findViewById(R.id.listTextViewExcercise);
 
            // 4. Set the text for textView 
            excerciseView.setText("#" + (position+1) +" " + excercisesArrayList.get(position).toString());
 
            // 5. retrn rowView
            return rowView;
        }
	}
}
