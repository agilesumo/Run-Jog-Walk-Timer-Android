package com.agilesumo.runjogwalk;



import java.util.ArrayList;
import java.util.List;

import com.agilesumo.runjogwalk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;




public class WorkoutTimerActivity extends Activity {
	
	
	private static final String DEFAULT_VIRATE_DURAION ="4 secs";
	
    private MoreAccurateTimer timer;
    private TimeDuration totalRemaining;

	private ExcercisesDataSource datasource;
	private TextView excerciseLabel;
	private RelativeLayout workoutMainLayout;
	private TextView excerciseCountdown;
	private TextView remaining;
	private TextView totalText;
	
	private final int INTERVAL = 1000;
	
	private List<TimeDuration> excercisesDurations;
	private List<String> excerciseNames;
	
	private TimeDuration durationRemaining;
	private String currentName;
	
	private int counter = 0;
	
	private long vibrateDuration;

	private Vibrator v;
	
    private boolean workoutFinished = false;
	
	private long millisRemaining;
	
	private NotificationManager notificaitonManager;
	NotificationCompat.Builder builder;
	private int NOTIFICATION = 10001; //Any unique number for this notification
	
	private boolean useVibration;
	
	private boolean useAudio;
	
	private boolean keepScreenAwake;
	
	private long workoutId;
	
	private String vibrateDurationPref;
	
	private SharedPreferences sharedPref;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		// Test if its the first time the app has been used. If so load default preferences.
		if(!sharedPref.contains(SettingsKeys.KEY_PREF_VIBRATE)){ 
			SharedPreferences.Editor prefEditor = sharedPref.edit();
			prefEditor.putBoolean(SettingsKeys.KEY_PREF_VIBRATE, true);
			prefEditor.putBoolean(SettingsKeys.KEY_PREF_AUDIO, true);
			prefEditor.putBoolean(SettingsKeys.KEY_PREF_AWAKE, true);
			prefEditor.putString(SettingsKeys.KEY_PREF_VIBRATE_DURATION, DEFAULT_VIRATE_DURAION);
			prefEditor.commit();
			useVibration = true;
			useAudio = true;
			keepScreenAwake = true;
			vibrateDurationPref = DEFAULT_VIRATE_DURAION;
		}
		/*
		Log.d("andy", "shared pref conains useVibration "+sharedPref.contains(SettingsKeys.KEY_PREF_VIBRATE));
		Log.d("andy", "shared pref conains vinration duration "+sharedPref.contains(SettingsKeys.KEY_PREF_VIBRATE_DURATION));
		Log.d("andy", "use Vibration Value is = " + sharedPref.getBoolean(SettingsKeys.KEY_PREF_VIBRATE, false));
		Log.d("andy", "use Vibration duration Value is = " + sharedPref.getString(SettingsKeys.KEY_PREF_VIBRATE_DURATION, "default"));
		*/

		else {   	
			useVibration = sharedPref.getBoolean(SettingsKeys.KEY_PREF_VIBRATE, false);
			useAudio = sharedPref.getBoolean(SettingsKeys.KEY_PREF_AUDIO, false);
			keepScreenAwake = sharedPref.getBoolean(SettingsKeys.KEY_PREF_AWAKE, false);
			vibrateDurationPref = sharedPref.getString(SettingsKeys.KEY_PREF_VIBRATE_DURATION, "");
		}	

		if(useVibration){
	        setupVibrate();
		}
		
		

		setContentView(R.layout.activity_workout_timer);
		
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
		   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		else {
		   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
		
		
		Intent intent = getIntent();
		workoutId = intent.getLongExtra(WorkoutActivity.EXTRA_WORKOUT_ID, 0);

		datasource = new ExcercisesDataSource(this);
	    datasource.open();

	    totalRemaining = datasource.getWorkoutDuration(workoutId);
	    long hoursInMilli = totalRemaining.getHours()* 60 * 60 *1000;
	    long minsInMilli = totalRemaining.getMinutes() * 60 * 1000;
	    long secsInMilli = totalRemaining.getSeconds() * 1000;
	    List<Excercise> excercises = datasource.getExcercisesByWorkoutId(workoutId);
	    
	    remaining = (TextView)findViewById(R.id.overall_remaining);
		remaining.setText(totalRemaining.toString());
		
		totalText = (TextView)findViewById(R.id.overall_duration);
		totalText.setText(totalRemaining.toString());
		
		
		excercisesDurations = new ArrayList<TimeDuration>();
		excerciseNames = new ArrayList<String>();
		
		for (Excercise excercise : excercises){
			excercisesDurations.add(excercise.getTimeDuration());
			excerciseNames.add(excercise.getExcercise());
			
		}

		
		currentName = excerciseNames.get(counter);		
		excerciseLabel = (TextView)findViewById(R.id.excercise_name);		
		workoutMainLayout = (RelativeLayout)findViewById(R.id.workout_main_layout);
		
		durationRemaining = excercisesDurations.get(counter);
		excerciseCountdown = (TextView)findViewById(R.id.countdown_excercise);
		
		showNotification(currentName);
		updateLayout();

	    long totalWorkoutInMilli = hoursInMilli + minsInMilli + secsInMilli;
	    
        timer = new Timer (totalWorkoutInMilli, INTERVAL); // timer ticks 1 time per second
        timer.start();

	 
	 
	    
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
	
	
	/** Called when the user clicks quit button */
    
	public void quitClicked(View view) {
		timer.cancel();
		datasource.close();
		finish();
	}
	
	/** Called when the user clicks close button after timer has finished */
    
	public void closeClicked(View view) {
		datasource.close();
		finish();
	}
	
	/** Called when the user clicks pause button */
    
	public void pauseClicked(View view) {
		timer.cancel();
		Button pauseButton = (Button)findViewById(R.id.pauseBtn);
		Button resumeButton = (Button)findViewById(R.id.resumeBtn);
		
		pauseButton.setVisibility(View.INVISIBLE);
		//quitButton.setVisibility(View.INVISIBLE);
		resumeButton.setVisibility(View.VISIBLE);
		updateNotification("Paused");
		
	}
	

	/** Called when the user clicks pause button */
    
	public void resumeClicked(View view) {
		Button pauseButton = (Button)findViewById(R.id.pauseBtn);
		Button quitButton =(Button)findViewById(R.id.quitBtn);
		Button resumeButton = (Button)findViewById(R.id.resumeBtn);
		
		pauseButton.setVisibility(View.VISIBLE);
		quitButton.setVisibility(View.VISIBLE);
		resumeButton.setVisibility(View.INVISIBLE);

		timer = new Timer (millisRemaining, INTERVAL); // timer ticks 1 time per second
        timer.start();
        updateNotification(currentName);
		
	}
	
	  @Override
	  public void onBackPressed() {
		  new AlertDialog.Builder(this)
          .setMessage("End current Session?").setCancelable(true)
          .setPositiveButton("Yes",
	          new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int id) {
	            	  timer.cancel();
	        		  WorkoutTimerActivity.super.onBackPressed();                      }
	          }).setNegativeButton("No", null).show();
		  
		  return;
      }
	
	  @Override
	  protected void onResume() {
		  
	    super.onResume();

	    if(keepScreenAwake == true){
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    }
	    else {
	    	getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    }
	  }
	
	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	  
	  @Override
	  protected void onDestroy() {
		  notificaitonManager.cancel(NOTIFICATION);
		  super.onDestroy();
	  }
	  
	  private void setupVibrate(){
	      v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		  long durationinMilliSecs = Long.valueOf(vibrateDurationPref.substring(0,1)).longValue() * 1000;
		  vibrateDuration = durationinMilliSecs;

	  }
	  
	  
	  private void updateLayout(){
		  excerciseCountdown.setText(durationRemaining.toString());


			
			if(currentName.equals("Run")){
				workoutMainLayout.setBackgroundColor(getResources().getColor(R.color.green));
				excerciseLabel.setText("RUN");
				updateNotification("Run");

			}
			else if(currentName.equals("Jog")){
				workoutMainLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
				excerciseLabel.setText("JOG");
				updateNotification("Jog");

			}
			else {
				workoutMainLayout.setBackgroundColor(getResources().getColor(R.color.red));
				excerciseLabel.setText("Walk");
				updateNotification("Walk");

			}
			
	  }
	  
	  private void playAudio(){
		 
	      if(useVibration == true){
			  v.vibrate(vibrateDuration);	  
	      }
	      if(useAudio == true){
	    	  
	      
		      MediaPlayer mPlayer;
	   		  if(workoutFinished == true){
				  mPlayer = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.finished_tone);
			  }
			  else if(currentName.equals("Run")){
				  mPlayer = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.run_tone);
			  }
			  else if(currentName.equals("Jog")){
				  mPlayer = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.jog_tone);
			  }
			  else {
				  mPlayer = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.walk_tone);
			  }
	          mPlayer.setOnCompletionListener(new OnCompletionListener() {
	              public void onCompletion(MediaPlayer mp) {
	                  mp.stop();
                      mp.reset();
	                  mp.release();
	                  mp = null;
	        		  if(workoutFinished == true){
	        			  mp = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.finished);
	        		  }
	        		  else if(currentName.equals("Run")){
	        			  mp = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.run);
	        		  }
	        		  else if(currentName.equals("Jog")){
	        			  mp = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.jog);
	        		  }
	        		  else {
	        			  mp = MediaPlayer.create(WorkoutTimerActivity.this, R.raw.walk);
	        		  }
	                  mp.setOnCompletionListener(new OnCompletionListener() {
	                      public void onCompletion(MediaPlayer theMp) {
	                          theMp.stop();
	                          theMp.reset();
	                          theMp.release();
	                          theMp = null;
	                      }
	                  });
	                  mp.start();
	              }
	          });
			  mPlayer.start();
	     
	      }
	  
		  


		  
	  }
	  
	  private void showNotification(String excerciseStr) {
		    // In this sample, we'll use the same text for the ticker and the expanded notification
		    CharSequence notificationTitle = getText(R.string.notification_title);
		    CharSequence notificationText;
		    
		    
		    notificationText = excerciseStr;
		    

		    
		    builder = new NotificationCompat.Builder(this).setContentTitle(notificationTitle).setContentText(notificationText).
		    		setSmallIcon(R.drawable.ic_launcher);

		    // The PendingIntent to launch our activity if the user selects this notification
		    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, WorkoutTimerActivity.class), 0);
            builder.setContentIntent(contentIntent).setOngoing(true);
            notificaitonManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 		    Notification notification = builder.build();

            
     	    // Send the notification.
		    notificaitonManager.notify(NOTIFICATION, notification);
		}
	  
	  	private void updateNotification(String updateStr){
	  		builder.setContentText(updateStr);
	  		Notification notification = builder.build();
	  		
	  		notificaitonManager.notify(NOTIFICATION, notification);
	  	}
	  
	  
	  
	  
		public class Timer extends MoreAccurateTimer {
		
		    public void startCountdownTimer() {  
		    }
		    
		   public Timer(long startTime, long interval) {
		        super(startTime, interval);
		    }
		
		    @Override
		    public void onFinish() {
		    	setContentView(R.layout.activity_workout_timer_finished);
		    	updateNotification("Workout Finished");
		    	workoutFinished = true;
		    	playAudio();
               
		    }
			
			@Override
			public void onTick(long millisUntilFinished) {
		    	
		    	totalRemaining.minusOneSecond();
		    	remaining.setText(totalRemaining.toString());
		    	durationRemaining.minusOneSecond();
		    	excerciseCountdown.setText(durationRemaining.toString());
		    	if(durationRemaining.isZero()){
				    counter++;
		    		durationRemaining = excercisesDurations.get(counter);
		    		currentName = excerciseNames.get(counter);
		    		updateLayout();
		    		playAudio();

		    	}
		    	millisRemaining = millisUntilFinished;
			
		    }
		 
		}
				

	
}

