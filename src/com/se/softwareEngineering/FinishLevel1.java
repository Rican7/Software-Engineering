package com.se.softwareEngineering;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class FinishLevel1 extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Retry button
		Button btn_Continue = (Button) findViewById (R.id.btnContinue);
		btn_Continue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Goes to level 2
				startActivity(new Intent("com.se.softwareEngineering.gameEngine.GameEngine"));
				
				//set values
				//gamespeed = 40;
				//luck = higher;
				
				finish();
			}
		});
		
		
		//Main Menu button
		Button btn_MainMenu = (Button) findViewById (R.id.btnMM);
		btn_MainMenu.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setContentView(R.layout.main);
				finish();
				
			}
		});
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		super.finish();
	}

}
