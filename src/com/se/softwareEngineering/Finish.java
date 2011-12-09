package com.se.softwareEngineering;

import com.se.softwareEngineering.gameEngine.GameEngine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends Activity {
	// Define variables
	boolean won;
	Integer score;
	int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Add the content from the XML file
		setContentView(R.layout.finish);
        
        // Get the data from the bundle passed in the intent
        Bundle bundle = getIntent().getExtras(); 
	    won = bundle.getBoolean("won", false);
	    score = bundle.getInt("score", 0);
	    level = bundle.getInt("level", 1);
	    
	    // Grab the views
    	View viewWon = (View) findViewById (R.id.viewWon);
    	View viewLost = (View) findViewById (R.id.viewLost);
    	TextView titleText = (TextView) findViewById (R.id.txtfinishedTitle);
    	TextView scoreText = (TextView) findViewById (R.id.txtScoreNum);
	    
	    // Depending on the status
	    if (won) {
	    	// Show the right view
	    	viewWon.setVisibility(View.VISIBLE);
	    	viewLost.setVisibility(View.GONE);
	    	
	    	// Set the title's text
	    	titleText.setText("You Won!");
	    }
	    else {
	    	// Show the right view
	    	viewWon.setVisibility(View.GONE);
	    	viewLost.setVisibility(View.VISIBLE);
	    	
	    	// Set the title's text
	    	titleText.setText("You Fail!");
	    }
	    
	    // Set the score
	    scoreText.setText(score.toString());
		
		//Retry button
		Button btn_Retry = (Button) findViewById (R.id.btnRetry);
		btn_Retry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Create new intent
	        	Intent intent = new Intent(Finish.this, GameEngine.class);
	        	
	        	// Create new bundle
	            Bundle bundle = new Bundle();
	            
	            // Add the level data to the bundle and add the bundle to the intent
	    		bundle.putInt("level", level);
	            intent.putExtras(bundle);
	            
	            // Start the actual activity
	            startActivity(intent);

				// Kill this activity
				finish();
			}
		});
		
		
		// Level select button
		Button btn_MainMenu = (Button) findViewById (R.id.btnQuit);
		btn_MainMenu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Kill this activity
				finish();
			}
		});
		
		// Level select button
		Button btn_Ok = (Button) findViewById (R.id.btnOk);
		btn_Ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Kill this activity
				finish();
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		super.finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		super.finish();
	}
}
