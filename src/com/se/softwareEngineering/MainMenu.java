package com.se.softwareEngineering;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity{

	MediaPlayer musicSong;
	
	SharedPreferences prefs;
	boolean CheckboxPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			
		//Play button
		Button btn_Play = (Button) findViewById (R.id.btnplay);
		btn_Play.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				startActivity(new Intent("com.se.softwareEngineering.LEVELSELECT"));
			}
		});
		
		//Settings button
		Button btn_Settings = (Button) findViewById (R.id.btnsettings);
		btn_Settings.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.se.softwareEngineering.PREFSACTIVITY"));
				
			}
		});
		
		Button btn_About = (Button) findViewById (R.id.btnAbout);
		btn_About.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.se.softwareEngineering.ABOUT"));
				
			}
		});
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		CheckboxPreference = prefs.getBoolean("music", true);
		
		musicSong = MediaPlayer.create(this, R.raw.rocket);
		/* Rocket 
		 * Kevin MacLeod (incompetech.com) Licensed under Creative Commons "Attribution 3.0" http://creativecommons.org/licenses/by/3.0/
		 */
		Log.i("Music Info", "Music setting: " + CheckboxPreference);
		
		if (CheckboxPreference == true)
		{
			musicSong.start();
			musicSong.setLooping(true);
		}
		else
		{
			musicSong.stop();
		}
		
		if (CheckboxPreference == false)
		{
			musicSong.stop();
		}
			
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		musicSong.stop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		musicSong.stop();
	}
	

}
