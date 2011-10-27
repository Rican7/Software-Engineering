package com.se.softwareEngineering;

import android.app.Activity;
import android.os.Bundle;

public class About extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		
        Thread splashTimer = new Thread (){
			
			public void run (){
				try{
					int splashTimer = 0;
					while (splashTimer <5000){
						sleep(100);
						splashTimer = splashTimer + 100;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				finally{
					finish();
				}
			}
		};
		splashTimer.start();
	}
}
