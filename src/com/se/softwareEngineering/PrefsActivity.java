package com.se.softwareEngineering;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Should show at top of screen, but doesnt currently, do we want it to?
		setTitle(R.string.PrefTitle);
		
		addPreferencesFromResource(R.xml.prefs);
	}
	
}
