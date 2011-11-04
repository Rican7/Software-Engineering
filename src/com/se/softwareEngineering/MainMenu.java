package com.se.softwareEngineering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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

}
