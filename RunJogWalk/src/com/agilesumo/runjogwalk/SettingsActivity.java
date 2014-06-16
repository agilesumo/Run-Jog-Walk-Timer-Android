package com.agilesumo.runjogwalk;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;



public class SettingsActivity extends PreferenceActivity
implements OnSharedPreferenceChangeListener {
	
    private static int prefs = R.xml.preferences;
    
    public static final String KEY_PREF_AUDIO = "pref_audio";
    public static final String KEY_PREF_VOICE = "pref_voice_prompts";
    public static final String KEY_PREF_VIBRATE = "pref_vibrate";
    public static final String KEY_PREF_VIBRATE_DURATION = "pref_vibrate_duration";
    public static final String KEY_PREF_AWAKE = "pref_keep_screen_awake";
    
    public static boolean vibrateDurationChanged = false;
    public static boolean useVibration;
    public static boolean useAudio;
    public static boolean keepScreenAwake;



    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		Preference vibrateDurationPref;
		SharedPreferences sharedPrefs;

        try {

            getClass().getMethod("getFragmentManager");
            AddResourceApi11AndGreater();
            Log.d("andy", "settings got to 1");
            //vibrateDurationPref = getPreferenceApi11AndGreater(KEY_PREF_VIBRATE_DURATION);
            Log.d("andy", "settings got to 2");

            sharedPrefs = getSharedPreferencesApi11AndGreater();
            Log.d("andy", "settings got to 3");

            
        } catch (NoSuchMethodException e) { //Api < 11
            AddResourceApiLessThan11();
            vibrateDurationPref = getPreferenceApiLessThan11(KEY_PREF_VIBRATE_DURATION);
            sharedPrefs = getSharedPreferencesApiLessThan11();

        }
        //vibrateDurationPref.setSummary(sharedPrefs.getString(KEY_PREF_VIBRATE_DURATION, ""));
        useVibration = sharedPrefs.getBoolean(KEY_PREF_VIBRATE, true);
        //vibrateDurationPref.setEnabled(useVibration);
        useAudio = sharedPrefs.getBoolean(KEY_PREF_AUDIO, true);

        
    }

    @SuppressWarnings("deprecation")
    protected void AddResourceApiLessThan11()
    {
        addPreferencesFromResource(prefs);
    }
    
    @SuppressWarnings("deprecation")
    protected Preference getPreferenceApiLessThan11(String key)
    {
    	return findPreference(key);
    }
    
    @SuppressWarnings("deprecation")
    protected SharedPreferences getSharedPreferencesApiLessThan11()
    {
    	return PreferenceManager.getDefaultSharedPreferences(this);

    }

    @TargetApi(11)
    protected void AddResourceApi11AndGreater()
    {
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PF()).commit();
    }
    
    @TargetApi(11)
    protected Preference getPreferenceApi11AndGreater(String key)
    {
        PreferenceFragment prefFrag = (PreferenceFragment)getFragmentManager().findFragmentById(R.xml.preferences);
        Log.d("andy", "settings got to getPref ... 1");
        Preference preference = (Preference)prefFrag.getPreferenceManager().findPreference(key);
        Log.d("andy", "settings got to getPref ... 2");
    	
    	//SharedPreferences sharedPrefs = getSharedPreferencesApi11AndGreater();
    	//sharedPrefs.

        return preference;
    }
    
    @TargetApi(11)
    protected SharedPreferences getSharedPreferencesApi11AndGreater()
    {
    	return PF.getSharedPreferences();
    }
    
    @SuppressWarnings("deprecation")
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(KEY_PREF_VIBRATE_DURATION)) {
    		Preference vibrateDurationPref;

            try {


                getClass().getMethod("getFragmentManager");
                vibrateDurationPref = getPreferenceApi11AndGreater(key);
            } catch (NoSuchMethodException e) { //Api < 11
            	vibrateDurationPref = getPreferenceApiLessThan11(key);
            }
            // Set summary to be the user-description for the selected value
            vibrateDurationPref.setSummary(sharedPreferences.getString(key, ""));
            vibrateDurationChanged = true;
        }
		
		else if(key.equals(KEY_PREF_VIBRATE)) {
    		CheckBoxPreference useVibratePref;
            Preference vibrateDurationPref;
            try {

                getClass().getMethod("getFragmentManager");
                useVibratePref = useVibratePref = (CheckBoxPreference)getPreferenceApi11AndGreater(KEY_PREF_VIBRATE);
                vibrateDurationPref = getPreferenceApi11AndGreater(KEY_PREF_VIBRATE_DURATION);
            } catch (NoSuchMethodException e) { //Api < 11
                useVibratePref = useVibratePref = (CheckBoxPreference)getPreferenceApiLessThan11(KEY_PREF_VIBRATE);
            	vibrateDurationPref = getPreferenceApiLessThan11(KEY_PREF_VIBRATE_DURATION);

            }
            useVibration = useVibratePref.isChecked();
            vibrateDurationPref.setEnabled(useVibration);
            
			
		}
		
		else if(key.equals(KEY_PREF_AUDIO)) {
			CheckBoxPreference useAudioPref;
            try {

                getClass().getMethod("getFragmentManager");
                useAudioPref = (CheckBoxPreference)getPreferenceApi11AndGreater(KEY_PREF_AUDIO);

               
            } catch (NoSuchMethodException e) { //Api < 11
                useAudioPref = (CheckBoxPreference)getPreferenceApiLessThan11(KEY_PREF_AUDIO);

            }
            useAudio = useAudioPref.isChecked();		
		}
		
		else if(key.equals(KEY_PREF_AWAKE)) {
			CheckBoxPreference screenAwakePref;
            try {

                getClass().getMethod("getFragmentManager");
                screenAwakePref = (CheckBoxPreference)getPreferenceApi11AndGreater(KEY_PREF_AWAKE);

               
            } catch (NoSuchMethodException e) { //Api < 11
                screenAwakePref = (CheckBoxPreference)getPreferenceApiLessThan11(KEY_PREF_AWAKE);

            }
            keepScreenAwake = screenAwakePref.isChecked();		
		}


    }
    
    @Override
    protected void onResume() {
        super.onResume();
        try {

            getClass().getMethod("getFragmentManager");
            SharedPreferences sharedPref = getSharedPreferencesApi11AndGreater();
            sharedPref.registerOnSharedPreferenceChangeListener(this);
        } catch (NoSuchMethodException e) { //Api < 11
        	SharedPreferences sharedPref = getSharedPreferencesApiLessThan11();
            sharedPref.registerOnSharedPreferenceChangeListener(this);

        }
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {

            getClass().getMethod("getFragmentManager");
            SharedPreferences sharedPref = getSharedPreferencesApi11AndGreater();
            sharedPref.unregisterOnSharedPreferenceChangeListener(this);
        } catch (NoSuchMethodException e) { //Api < 11
        	SharedPreferences sharedPref = getSharedPreferencesApiLessThan11();
            sharedPref.unregisterOnSharedPreferenceChangeListener(this);

        }
    }

    @TargetApi(11)
    public static class PF extends PreferenceFragment
    {       
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(SettingsActivity.prefs); //outer class
            // private members seem to be visible for inner class, and
            // making it static made things so much easier
        }
        public static SharedPreferences getSharedPreferences(){
        	return getSharedPreferences();
        }
    }
}