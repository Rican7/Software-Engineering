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
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class GameEngine extends Activity implements SensorEventListener {
	
	// Setup options
	public static boolean debug = false;
	static int gameSpeed = 20;
	private int luckyNumber = 7; // Number used to calculate lotto... arbitrary
	private int itemLottoChance = 200; // Between 0 and n
	private int itemSpawnDelay = 200; // Relative to gamespeed delay. So itemSpawnDelay * gameSpeed = time in ms
	private int timeLastItem = 0;
	private int obstructionLottoChance = 100; // Between 0 and n
	private int obstructionSpawnDelay = 50; // Relative to gamespeed delay. So obstructionSpawnDelay * gameSpeed = time in ms
	private int obstructionLastItem = 0;

	// Random number instance
	private static Random random = new Random();
	
	// Declare main panel
	Panel gamePanel;
	
	// Declare main game thread
	Thread mainGameThread;
	
	// Game running
	Boolean gameRunning = false;
	static Boolean surfaceCreated = false;
	static int gameRunningTime = 0;
    
    // Declare sensor management variables
	private SensorManager aSensorManager;
    private Sensor aAccelerometer;
    
    // Declare variables to hold values read from the accelerometer
    public static float[] gravity = new float[3];
    public static float[] linear_acceleration = new float[3];
    
    public void run_game() {
    	// Execute while the surface exists
    	if (surfaceCreated) {
    		itemController();
    		obstructionController();
    	}
    	
    	// Execute while the game is running
    	if (gameRunning) {
    		checkIfGameOver();
    		gameRunningTime();
    	}
    }
    
    /* Controls the items:
     *	- Creating
     *	- Checking boundaries
     *	- Checking collisions with player
     *	- Handles scoring based on the item
     *	- Handles health effect based on the item
     *	- Handles speed effect based on the item
     */
    private void itemController() {
    	// Generate a random number between 0 and 500
    	int itemLotto = random.nextInt(itemLottoChance);
	    
    	// Calculate the lottery, and if its correct (win)... add an item to the screen
    	if (itemLotto == (itemLottoChance / luckyNumber)) {
    		// Check to see if its been longer than n before creating another item (don't want to have too many items spawn by chance)
    		if ((gameRunningTime - timeLastItem) > itemSpawnDelay) {
		    	// Create an item element at a random location
    			synchronized (gamePanel.itemElements) {
			    	gamePanel.itemElements.add(new itemElement(getResources()));
			    	Log.i("Item Log", "Item Created at " + gameRunningTime);
			    	
			    	// Set the time that this item has spawned at
			    	timeLastItem = gameRunningTime;
    			}
    		}
    	}
    	
    	// Loop through each existing element
    	synchronized (gamePanel.itemElements) {
            for (Iterator<itemElement> it = gamePanel.itemElements.iterator(); it.hasNext();) {
            	// Get the current item in the iteration loop
            	itemElement currentItem = it.next();

            	// If any of the item elements collide with the player
            	if (currentItem.checkCollisionWithPlayer(gamePanel.player)) {
            		// Remove the item
            		it.remove();
            		
            		// Debugging
            		Log.i("Item Log", "Item Collided with player at " + gameRunningTime);
            	}
            	// If any of the item elements are out of bounds
            	else if (currentItem.checkOutOfBounds()) {
            		// Remove the item
            		it.remove();
            		
            		// Debugging
            		Log.i("Item Log", "Item Destroyed at " + gameRunningTime);
            	}
            }
        }
    }
    
    /* Controls the obstructions:
     *	- Creating
     *	- Checking boundaries
     *	- Checking collisions with player
     *	- Handles health effect based on the obstruction
     */
    private void obstructionController() {
    	// Generate a random number between 0 and the lotto
    	int obstructionLotto = random.nextInt(obstructionLottoChance);
	    
    	// Calculate the lottery, and if its correct (win)... add an obstruction to the screen
    	if (obstructionLotto == (obstructionLottoChance / luckyNumber)) {
    		// Check to see if its been longer than n before creating another obstruction (don't want to have too many obstructions spawn by chance)
    		if ((gameRunningTime - obstructionLastItem) > obstructionSpawnDelay) {
		    	// Create an obstruction element at a random location
    			synchronized (gamePanel.obstructionElements) {
			    	gamePanel.obstructionElements.add(new obstructionElement(getResources()));
			    	Log.i("Obstruction Log", "Obstruction Created at " + gameRunningTime);
			    	
			    	// Set the time that this item has spawned at
			    	obstructionLastItem = gameRunningTime;
    			}
    		}
    	}
    	
    	// Loop through each existing element
    	synchronized (gamePanel.obstructionElements) {
            for (Iterator<obstructionElement> it = gamePanel.obstructionElements.iterator(); it.hasNext();) {
            	// Get the current obstruction in the iteration loop
            	obstructionElement currentObstruction = it.next();

            	// If any of the obstruction elements collide with the player
            	if (currentObstruction.checkCollisionWithPlayer(gamePanel.player)) {
            		// Remove the item
            		it.remove();
            		
            		// Debugging
            		Log.i("Obstruction Log", "Obstruction Collided with player at " + gameRunningTime);
            	}
            	// If any of the obstruction elements are out of bounds
            	else if (currentObstruction.checkOutOfBounds()) {
            		// Remove the item
            		it.remove();
            		
            		// Debugging
            		Log.i("Obstruction Log", "Obstruction Destroyed at " + gameRunningTime);
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
					// Run the game loop
					run_game();
					
					try {
						Thread.sleep(gameSpeed);
					} catch (Throwable t) {
						// For debugging purposes
						if (debug) {
							System.err.println(t);
						}
					}
				}
			}
		});
    }

	protected void onResume() {
        super.onResume();
        aSensorManager.registerListener(this, aAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        
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
    
    private void gameRunningTime() {
    	// Incrememnt time
		gameRunningTime++;
		
		// Calculate actual game running time in seconds
		double actualRunningTime = (double)(gameRunningTime * gameSpeed) / 1000;
		
		// Log every 5 seconds (real time)
		if ((actualRunningTime % 5.0) == 0) {
			Log.i("Game Time", "Game has been running for " + (int)actualRunningTime + " seconds");
		}
    }

    // Set the surface created variable to true
	public static void surfaceCreated() {
		surfaceCreated = true;
	}
}