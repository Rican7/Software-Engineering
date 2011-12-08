package com.se.softwareEngineering;

import android.os.Bundle;
import android.preference.PreferenceActivity;

//import android.preference.PreferenceManager;

public class PrefsActivity extends PreferenceActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Should show at top of screen, but doesnt currently, do we want it to?
		setTitle(R.string.PrefTitle);
		
		addPreferencesFromResource(R.xml.prefs);
		
		//doesn't seem to actually work, there is a doc about this being a long know bug.
		//It should make the boxes checked by default.
		//PreferenceManager.setDefaultValues(this, R.xml.prefs, true);
		
	}
	
}
	

