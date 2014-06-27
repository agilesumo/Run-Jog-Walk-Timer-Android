package com.agilesumo.runjogwalk;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/*
 * Settings Activity for Older android devices ( API < 11 )
 */


public class SettingsActivity extends PreferenceActivity
	implements OnSharedPreferenceChangeListener {
	
		private static int prefs = R.xml.preferences;
		 
		    @SuppressWarnings("deprecation")
			@Override
		    protected void onCreate(final Bundle savedInstanceState)
		    {
		        super.onCreate(savedInstanceState);
				Preference vibrateDurationPref;
				SharedPreferences sharedPrefs;
		
				addPreferencesFromResource(prefs);
				vibrateDurationPref = findPreference(SettingsKeys.KEY_PREF_VIBRATE_DURATION);
		        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		        
		        boolean useVibration = sharedPrefs.getBoolean(SettingsKeys.KEY_PREF_VIBRATE, true);
		
		        vibrateDurationPref.setSummary(sharedPrefs.getString(SettingsKeys.KEY_PREF_VIBRATE_DURATION, ""));
		
		        vibrateDurationPref.setEnabled(useVibration);
			        
		    }
	    
	    @SuppressWarnings("deprecation")
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(SettingsKeys.KEY_PREF_VIBRATE_DURATION)) {
	    		Preference vibrateDurationPref;
	    		vibrateDurationPref = findPreference(key);
	            
	            // Set summary to be the user-description for the selected value
	            vibrateDurationPref.setSummary(sharedPreferences.getString(key, ""));
	            
	        }
			
			else if(key.equals(SettingsKeys.KEY_PREF_VIBRATE)) {
	    		CheckBoxPreference useVibratePref;
	            Preference vibrateDurationPref;
	            useVibratePref = (CheckBoxPreference)findPreference(SettingsKeys.KEY_PREF_VIBRATE);
	            vibrateDurationPref = findPreference(SettingsKeys.KEY_PREF_VIBRATE_DURATION);
	            vibrateDurationPref.setEnabled(useVibratePref.isChecked());
	            
			}
			
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        sharedPref.registerOnSharedPreferenceChangeListener(this);
	        
	    }
	
	    @Override
	    protected void onPause() {
	        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
	        super.onPause();
	
	    }

}