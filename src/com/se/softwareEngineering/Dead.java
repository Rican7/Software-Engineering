package com.se.softwareEngineering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dead extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dead);
		
		
		//Retry button
		Button btn_Retry = (Button) findViewById (R.id.btnRetry);
		btn_Retry.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.se.softwareEngineering.gameEngine.GameEngine"));
				finish();
			}
		});
		
		
		// Level select button
		Button btn_MainMenu = (Button) findViewById (R.id.btnMM);
		btn_MainMenu.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
	}

}
