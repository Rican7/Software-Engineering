package com.se.softwareEngineering;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Intstructions extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions);
	
		//Play button
		Button btn_Exit = (Button) findViewById (R.id.btnexit);
		btn_Exit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
