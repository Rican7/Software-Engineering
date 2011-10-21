package com.se.softwareEngineering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelSelect extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelselect);
		
		
		//Level 1 button
		Button btn_Level1 = (Button) findViewById (R.id.btnlevel1);
		btn_Level1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.se.softwareEngineering.LEVEL1"));
			}
		});
	}

}