package com.se.softwareEngineering;

import com.se.softwareEngineering.gameEngine.GameEngine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LevelSelect extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelselect);
		
		Button btn_exit = (Button) findViewById (R.id.btnBack);
		btn_exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		//Grid layout
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	// Create new intent
            	Intent intent = new Intent(LevelSelect.this, GameEngine.class);
            	
            	// Create new bundle
                Bundle bundle = new Bundle();
            	
            	//Goes to LevelStats need to look up statistics later dependent on level that is picked (position)
            	if (position == 0 || position == 1 || position == 2) {
                   	// Add the level data to the bundle and add the bundle to the intent
            		bundle.putInt("level", (position+1));
                    intent.putExtras(bundle);
                    
                    // Start the actual activity (the game engine)
                    startActivity(intent);
            	}
            	else {
            		Toast.makeText(LevelSelect.this, "Coming Soon...",Toast.LENGTH_SHORT).show();
            	}
            }
        });

		
	}

}