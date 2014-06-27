package com.agilesumo.runjogwalk;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends PreferenceFragment
	implements OnSharedPreferenceChangeListener {
	
	    private static int prefs = R.xml.preferences;
	    	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			Preference vibrateDurationPref;
			SharedPreferences sharedPrefs;
	
			addPreferencesFromResource(prefs);
	
			vibrateDurationPref = findPreference(SettingsKeys.KEY_PREF_VIBRATE_DURATION);
	        sharedPrefs  = PreferenceManager.getDefaultSharedPreferences(getActivity());
		        
	        boolean useVibration = sharedPrefs.getBoolean(SettingsKeys.KEY_PREF_VIBRATE, true);
	
	        vibrateDurationPref.setSummary(sharedPrefs.getString(SettingsKeys.KEY_PREF_VIBRATE_DURATION, ""));
	
	        vibrateDurationPref.setEnabled(useVibration);
	
	    }
	    
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
		public void onResume() {
	        super.onResume();
	    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	        sharedPref.registerOnSharedPreferenceChangeListener(this);
	    }
	
	    @Override
		public void onPause() {
	        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
	        super.onPause();
	
	    }
    
   
}