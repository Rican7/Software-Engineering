package com.se.softwareEngineering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelStatistics extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelstats);
		
		Button btn_PlayFinal = (Button) findViewById (R.id.btnPlayFinal);
		btn_PlayFinal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.se.softwareEngineering.test2d.Test2D"));
				
			}
		});
		Button btn_exit = (Button) findViewById (R.id.btnBack);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
}
