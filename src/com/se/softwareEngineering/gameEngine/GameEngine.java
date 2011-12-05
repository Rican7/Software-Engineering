package com.se.softwareEngineering.gameEngine;

import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class GameEngine extends Activity implements SensorEventListener {
	
	// Setup options
	public static boolean debug = false;
	private int luckyNumber = 7; // Number used to calculate lotto... arbitrary
	private int itemLottoChance = 50000; // Between 0 and n
	
	// Declare main panel
	Panel gamePanel;
	
	// Declare main game thread
	Thread mainGameThread;
	
	// Game running
	Boolean gameRunning = false;
	static Boolean surfaceCreated = false;
    
    // Declare sensor management variables
	private SensorManager aSensorManager;
    private Sensor aAccelerometer;
    
    // Declare variables to hold values read from the accelerometer
    public static float[] gravity = new float[3];
    public static float[] linear_acceleration = new float[3];
    
    Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			run_game();
		}
	};
    
    public void run_game() {
    	// Execute while the game is running
    	while(gameRunning) {
    		// Run methods
    		/*
    		 * moveControls();
    		fireControls();
    		EnemyController();
    		shipfireCollisionDetector();
    		playerCollisionDetector();
    		*/
    		
    		checkIfGameOver();
    	}
    	
    	// Execute while the surface exists
    	while(surfaceCreated) {
    		moderateItems();
    	}
    }
    
    private void moderateItems() {
    	Random rand = new Random();
    	
    	// Generate a random number between 0 and 500
    	int itemLotto = rand.nextInt(itemLottoChance);
	    
    	// Calculate the lottery, and if its correct (win)... add an item to the screen
    	if (itemLotto == (itemLottoChance / luckyNumber)) {
	    	// Create an item element at a random location
	    	gamePanel.itemElements.add(new itemElement(getResources(), (int) (rand.nextInt((int) (Panel.mWidth*3/5)) + (Panel.mWidth/5)), 0));
	    	Log.i("Item Log", "Item Created!");
    	}
    	
    	// Check if any of the item elements are out of bounds, and remove them
    	synchronized (gamePanel.itemElements) {
            for (Iterator<itemElement> it = gamePanel.itemElements.iterator(); it.hasNext();) {
            	if (it.next().getOutOfBounds()) {
            		it.remove();
            		Log.i("Item Log", "Item Destroyed!");
            	}
            }
        }
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create the Panel
        gamePanel = new Panel(this);
        setContentView(gamePanel);
        
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
    
    /** Called when the activity is first started. */
    @Override
    public void onStart() {
    	super.onStart();
    	
    	mainGameThread = new Thread(new Runnable() {
			public void run() {
				while (gameRunning) {
					try {
						run_game();
						handler.sendMessage(handler.obtainMessage());
						Thread.sleep(1000);
					} catch (Throwable t) {
						// For debugging purposes
						if (debug) {
							System.err.println(t);
						}
					}
				}
			}
		});

		mainGameThread.start();
    }

	protected void onResume() {
        super.onResume();
        aSensorManager.registerListener(this, aAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
        // Resume handler
        gameRunning = true;
		mainGameThread.start();
    }

    protected void onPause() {
        super.onPause();
        aSensorManager.unregisterListener(this);
        
        // Pause handler
        gameRunning = false;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    	// alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = (float) 0.8;
        
        // Get display rotation
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        // Account for devices different default orientations
        if (rotation != 0) {
	        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
	        linear_acceleration[1] = event.values[1] - gravity[1];
        }
        else {
        	gravity[1] = alpha * gravity[1] + (1 - alpha) * (event.values[0] * -1);
	        linear_acceleration[1] = event.values[0] - gravity[1];
        }
    }
    
    private void checkIfGameOver() {
    	// If player has lost all of their health
    	/*
    	if (player2Lives.size() < 1 && player1Lives.size() < 1) {
    		gameOver();
    	}
    	*/
    }

    // Set the surface created variable to true
	public static void surfaceCreated() {
		surfaceCreated = true;
	}
}