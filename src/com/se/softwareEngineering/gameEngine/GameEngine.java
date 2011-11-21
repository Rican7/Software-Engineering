package com.se.softwareEngineering.gameEngine;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

public class GameEngine extends Activity implements SensorEventListener {
    
    // Declare sensor management variables
	private SensorManager aSensorManager;
    private Sensor aAccelerometer;
    
    // Declare variables to hold values read from the accelerometer
    public static float[] gravity = new float[3];
    public static float[] linear_acceleration = new float[3];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Panel(this));
        
        // Keep screen on
        this.getWindow().
        addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// Set sensors
		aSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    aAccelerometer = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    
	    // Set defaults
	    gravity[1] = 0;
	    linear_acceleration[1] = 0;
    }

	protected void onResume() {
        super.onResume();
        aSensorManager.registerListener(this, aAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        aSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    	// alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = (float) 0.8;

        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];

        linear_acceleration[1] = event.values[1] - gravity[1];
        
        // Create textviews
        /*
        TextView gravityText = new TextView(this);
        TextView accelText = new TextView(this);
        
        gravityText = (TextView)findViewById(R.id.gravityText);
        gravityText.setText(Float.toString(gravity[1]));
        
        accelText = (TextView)findViewById(R.id.accelText);
        accelText.setText(Float.toString(linear_acceleration[1]));
        */
    }
}