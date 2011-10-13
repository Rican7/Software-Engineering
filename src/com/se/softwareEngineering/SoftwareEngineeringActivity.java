package com.se.softwareEngineering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SoftwareEngineeringActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread splashTimer = new Thread (){
			
			public void run (){
				try{
					int splashTimer = 0;
					
					while (splashTimer <2000){
						sleep(100);
						splashTimer = splashTimer + 100;
						
					}
					startActivity(new Intent("com.se.softwareEngineering.CLEARSCREEN"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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