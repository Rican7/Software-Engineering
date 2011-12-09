package com.se.softwareEngineering.gameEngine;

import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	public final static boolean debug = false;
	final static int gameSpeed = 20; // Don't CHANGE. Meant purely for the game looping speed
	private int luckyNumber = 7; // Number used to calculate lotto... arbitrary
	private int itemLottoChance = 200; // Between 0 and n
	private int itemSpawnDelay = 200; // Relative to gamespeed delay. So itemSpawnDelay * gameSpeed = time in ms
	private int timeLastItem = 0;
	private int timeLastBoost = 0;
	private int itemTimeEffect = 0; // in seconds
	private int obstructionLottoChance = 60; // Between 0 and n
	private int obstructionSpawnDelay = 50; // Relative to gamespeed delay. So obstructionSpawnDelay * gameSpeed = time in ms
	private int obstructionLastItem = 0;
	private final int scoreIncrement = 1;
	
	// In game variables
	static double scoreSpeedOrigin;
	static double scoreSpeedMultiplier;
	private int levelFinishScore;
	private int gameScore;
	private boolean boostOn;

	// Random number instance
	private static Random random = new Random();
	
	// Declare main panel
	Panel gamePanel;
	
	// Declare main game thread
	Thread mainGameThread;
	
	// Game running
	Boolean gameRunning = false;
	Boolean gameOver = false;
	static Boolean surfaceCreated = false;
	static int gameRunningTime = 0;
    
    // Declare sensor management variables
	private SensorManager aSensorManager;
    private Sensor aAccelerometer;
    
    // Declare variables to hold values read from the accelerometer
    public static float[] gravity = new float[3];
    public static float[] linear_acceleration = new float[3];
    
    // Constructor
    public GameEngine(/*int level*/) {
		super();
    	
    	// Set the initial values of the game variables
		scoreSpeedOrigin = 1;
		scoreSpeedMultiplier = 1;
    	gameScore = 0;
    	
    	// Set the difficulty of the game
    	//setDifficulty(level);
	}
    
    // Create main game loop logic
    public void run_game() {
    	// Execute while the game is running
    	if (gameRunning) {
	    	// Execute while the surface exists
	    	if (surfaceCreated) {
	    		itemController();
	    		obstructionController();
	    		checkPlayerHealth();
	    	}
	    	
	    	// Execute regardless of whether the canvas surface has been created
    		gameRunningTime();
    		incrementScore();
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
    	
    	// Make sure that the speed of the game goes back to normal after the boost is done
    	if (boostOn) {
    		// Calculate the time since the boost
    		int timeSinceBoostStarted = gameRunningTime - timeLastBoost;
    		
    		// Calculate the duration of a proper boost
    		int properBoostTime = (int) (itemTimeEffect * gameSpeed * 2.46);
    		
        	if (timeSinceBoostStarted > properBoostTime) {
        		// Turn off boost
        		boostOn = false;
        		
        		// Set speed back to normal
        		scoreSpeedMultiplier = 1;
    			
        		// Debugging
    			Log.i("Item Log", "Boost ended at " + gameRunningTime);
        	}
        }
    	
    	// Loop through each existing element
    	synchronized (gamePanel.itemElements) {
            for (Iterator<itemElement> it = gamePanel.itemElements.iterator(); it.hasNext();) {
            	// Get the current item in the iteration loop
            	itemElement currentItem = it.next();

            	// If any of the item elements collide with the player
            	if (currentItem.checkCollisionWithPlayer(gamePanel.player)) {
            		// Heal the player
            		gamePanel.player.healPlayer(currentItem.getHealthEffect());
            		
            		// Grab the time of the effect, in seconds
            		itemTimeEffect = currentItem.getTimeEffect();
            		
            		// If the item is a boost
            		if (currentItem.getItemType() == "boost") {
	            		// Turn on boost
	            		boostOn = true;
	            		
                		// Affect the player's speed (really just the speed of everything else)
                		scoreSpeedMultiplier = currentItem.getSpeedEffectMultiplier();
                		
            			// Make the boost only last a certain time
            			timeLastBoost = gameRunningTime;

                		// Debugging
            			Log.i("Item Log", "Boost started at " + timeLastBoost);
            		}
            		
            		// Remove the item
            		it.remove();
            		
            		// Debugging
            		Log.i("Item Log", "Item Collided with player at " + gameRunningTime);
            		Log.i("Player Log", "Player healed for " + currentItem.getHealthEffect() + " points.");
            		Log.i("Player Log", "Player health now at " + gamePanel.player.getPlayerHealth());
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
            		// Hurt the player
            		gamePanel.player.hurtPlayer(currentObstruction.getHealthEffect());
            		
            		// Check to see if the player is dead or not
    	    		checkPlayerHealth();
            		
            		// Remove the item
            		it.remove();
            		
            		// Debugging
            		Log.i("Obstruction Log", "Obstruction Collided with player at " + gameRunningTime);
            		Log.i("Player Log", "Player hurt for " + currentObstruction.getHealthEffect() + " points.");
            		Log.i("Player Log", "Player health now at " + gamePanel.player.getPlayerHealth());
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
    
    // Public method to check the player's health
    private void checkPlayerHealth() {
    	// Get the player's health
    	int playerHealth = gamePanel.player.getPlayerHealth();
    	
    	// If the player's health reaches zero (or below), end the game
    	if (playerHealth <= 0) {
    		// Bring up the end game (failed) screen
    		startActivity(new Intent("com.se.softwareEngineering.DEAD"));
    		
    		// Debugging
    		Log.i("Game Log", "The player has died. Game over.");
    		
    		// Set the game to no longer run
    		setGameOver();
    	}
    }
    
    // Called when the activity gets killed
    @Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Let's do some cleanup
		surfaceCreated = false;
		gameRunningTime = 0;
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
    	
    	// Create the main game thread
    	mainGameThread = new Thread(new Runnable() {
			public void run() {
				// While the game is still running and the game ISN'T over
				while (gameRunning && !gameOver) {
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
				
				finish();
			}
		});
    }

	protected void onResume() {
        super.onResume();
        aSensorManager.registerListener(this, aAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        
        // If the game ISN'T over
        if (gameOver != true) {
        	// Resume the game/thread
        	gameRunning = true;
			mainGameThread.start();
        }
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
    
    // Method to set variables that flag the end of the game
    private void setGameOver() {
    	gameRunning = false;
    	gameOver = true;
    }
    
    // Method to increment the game running time and calculate the actual seconds of time for the log
    private void gameRunningTime() {
    	// Increment time
		gameRunningTime++;
		
		// Calculate actual game running time in seconds
		double actualRunningTime = (double)(gameRunningTime * gameSpeed) / 1000;
		
		// Log every 5 seconds (real time)
		if ((actualRunningTime % 5.0) == 0) {
			Log.i("Game Time", "Game has been running for " + (int)actualRunningTime + " seconds");
			Log.i("Game Time", "Game running time: " + gameRunningTime);
		}
    }

    // Set the surface created variable to true
	public static void surfaceCreated() {
		surfaceCreated = true;
	}
	
	// Method to set the difficulty (score to beat and the speed), based on the level given
	public void setDifficulty(int level) {
		if (level == 1) {
			levelFinishScore = 500;
			scoreSpeedOrigin = 1;
		}
		else if (level == 2) {
			levelFinishScore = 1000;
			scoreSpeedOrigin = 1.2;
		}
		else if (level == 3) {
			levelFinishScore = 2000;
			scoreSpeedOrigin = 1.5;
		}
	}
	
	// Method to calculate and increment the score
	public void incrementScore() {
		// Make the score increment based on the speed multiplier, proportionately
		double timeCalculator = gameRunningTime * (double) scoreSpeedOrigin * (double) scoreSpeedMultiplier;
		
		// Increment the score every quarter of a second (sort of... Java is bad at using modulus with doubles)
		if ((timeCalculator % 12) == 0) {
			gameScore += scoreIncrement;
			
			// Debugging
			Log.i("Score Log", "Game score is " + gameScore);
		}
	}
}