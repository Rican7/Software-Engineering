package com.se.softwareEngineering;

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
            	//Goes to LevelStats need to look up statistics later dependent on level that is picked (position)
            	if (position == 0){
            		startActivity(new Intent("com.se.softwareEngineering.gameEngine.GameEngine"));
            		//set  variables here also for difficulty
            		//speed - luck 
            		//max score so you know when the level ends
            		
            	}
            	else if (position ==1){
            		startActivity(new Intent("com.se.softwareEngineering.gameEngine.GameEngine"));
            		//set  variables here also for difficulty
            		//max score so you know when the level ends
            		
            	}
            	else if (position ==2){
            		startActivity(new Intent("com.se.softwareEngineering.gameEngine.GameEngine"));
            		//set  variables here also for difficulty
            		//max score = 2345635634634645633654654634
            	}
            	else {
            		Toast.makeText(LevelSelect.this, "Comming Soon",Toast.LENGTH_SHORT).show();
            	}
            }
        });

		
	}

}