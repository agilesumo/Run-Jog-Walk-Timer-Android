package com.agilesumo.runjogwalk;



import java.util.ArrayList;
import java.util.Calendar;
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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;




public class WorkoutTimerActivity extends Activity {
	
	
	
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

	private Vibrator v;
	
	private long millisRemaining;
	
	private NotificationManager notificaitonManager;
	NotificationCompat.Builder builder;
	private int NOTIFICATION = 10001; //Any unique number for this notification
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		setContentView(R.layout.activity_workout_timer);
		datasource = new ExcercisesDataSource(this);
	    datasource.open();

	    totalRemaining = datasource.getTotalDuration();
	    long hoursInMilli = totalRemaining.getHours()* 60 * 60 *1000;
	    long minsInMilli = totalRemaining.getMinutes() * 60 * 1000;
	    long secsInMilli = totalRemaining.getSeconds() * 1000;
	    List<Excercise> excercises = datasource.getAllExcercises();
	    
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
		finish();
	}
	
	/** Called when the user clicks pause button */
    
	public void pauseClicked(View view) {
		timer.cancel();
		Button pauseButton = (Button)findViewById(R.id.pauseBtn);
		Button quitButton =(Button)findViewById(R.id.quitBtn);
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
	  
	  
	  private void updateLayout(){
		  excerciseLabel.setText(currentName);
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
		    	
		
		    }
			
			@Override
			public void onTick(long millisUntilFinished) {
		    	
		    	totalRemaining.minusOneSecond();
		    	remaining.setText(totalRemaining.toString());
		    	durationRemaining.minusOneSecond();
		    	excerciseCountdown.setText(durationRemaining.toString());
		    	if(durationRemaining.isZero()){
		    		// Vibrate for 500 milliseconds
				    v.vibrate(1000);
		    		counter++;
		    		durationRemaining = excercisesDurations.get(counter);
		    		currentName = excerciseNames.get(counter);
		    		updateLayout();
		    	}
		    	millisRemaining = millisUntilFinished;
			    	
			}
		}
				

	
}

