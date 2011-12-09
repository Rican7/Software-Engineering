package com.se.softwareEngineering.gameEngine;

import com.se.softwareEngineering.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class playerElement {
	// Its x and y positions
    private float mX;
    private float mY;
    
    // Its x and y speeds
    private int mSpeedX;
    private int mSpeedY;
    
    // The in-game player variables
    private int healthMax = 100;
    private int playerHealth;
    
    // The accelerometer multiplier (changes how fast he changes x direction)
    private double accelMult = 0.8;

    // The bitmap image
    private Bitmap mBitmap;
    private Bitmap bitmapLeft;
    private Bitmap bitmapStraight;
    private Bitmap bitmapRight;
    
    // Constructor
    public playerElement(Resources res, int x, int y) {
    	// Get the bitmaps from the drawables, so they're in memory
    	bitmapLeft = BitmapFactory.decodeResource(res, R.drawable.character_left_up);
    	bitmapStraight = BitmapFactory.decodeResource(res, R.drawable.character_straight_up);
    	bitmapRight = BitmapFactory.decodeResource(res, R.drawable.character_right_up);
    	
    	// Start with the main bitmap being the straight one
    	mBitmap = bitmapStraight;
        
        // Set the starting position
        mX = x - mBitmap.getWidth() / 2;
        mY = (y*2) - mBitmap.getHeight();
        
        // Set the starting speed
        mSpeedX = 0;
        
        // Set player health to max
        playerHealth = healthMax;
    }
    
    // Method to change the bitmap used by the player element
    public void changeBitmap(int direction) {
    	if (direction < 0) {
    		mBitmap = bitmapLeft;
    	}
    	else if (direction == 0) {
    		mBitmap = bitmapStraight;
    	}
    	else if (direction > 0) {
    		mBitmap = bitmapRight;
    	}
    	else {
    		mBitmap = bitmapStraight;
    	}
    }
    
    // Method to actually draw the contents to the screen
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
    	// Set the horizontal speed based on the accelerometer and the multiplier
        mSpeedX = (int) (GameEngine.gravity[1] * accelMult);
        
        // Animate in the x direction based on the speed
        mX += mSpeedX * (elapsedTime / 5f);
        
        // Make sure it doesn't leave the trail's boundaries
        checkOutOfBounds();
    }

    // Method to detect if the playerElement is outside of the visible borders
    private void checkOutOfBounds() {
        if (mX <= Panel.leftBound) {
            mSpeedX = -mSpeedX;
            mX = Panel.leftBound;
        } else if (mX + mBitmap.getWidth() >= Panel.rightBound) {
            mSpeedX = -mSpeedX;
            mX = Panel.rightBound - mBitmap.getWidth();
        }
        if (mY <= 0) {
            mY = 0;
            mSpeedY = -mSpeedY;
        }
        if (mY + mBitmap.getHeight() >= Panel.mHeight) {
            mSpeedY = -mSpeedY;
            mY = Panel.mHeight - mBitmap.getHeight();
        }
    }
    
    // Public method to get the coordinates of the playerElement (x and y)
    public int[] getPosition() {
    	// Declare array
    	int[] position = new int[2];
    	
    	// Fill array
    	position[0] = (int) mX;
    	position[1] = (int) mY;
    	
    	// Return array
    	return position;
    }
    
    // Public method to get the size of the playerElement (width and height)
    public int[] getSize() {
    	// Declare array
    	int[] size = new int[2];
    	
    	// Fill array
    	size[0] = (int) mBitmap.getWidth();
    	size[1] = (int) mBitmap.getHeight();
    	
    	// Return array
    	return size;
    }
    
    // Public method to get the bounding box of the playerElement (top left -> clockwise)
    public int[][] getBounds() {
    	// Declare multidemsional array
    	int[][] bounds = new int[4][2]; // 4 corners, 2 points per corner (x,y)
    	
    	/* Start filling array */
    	// Top-left
    	bounds[0][0] = (int) mX; // x
    	bounds[0][1] = (int) mY; // y
    	
    	// Top-right
    	bounds[1][0] = (int) ((int) mX + (int) mBitmap.getWidth());
    	bounds[1][1] = (int) mY;
    	
    	// Bottom-right
    	bounds[2][0] = (int) ((int) mX + (int) mBitmap.getWidth());
    	bounds[2][1] = (int) ((int) mY + (int) mBitmap.getHeight());
    	
    	// Bottom-left
    	bounds[3][0] = (int) mX;
    	bounds[3][1] = (int) ((int) mY + (int) mBitmap.getHeight());
    	/* End filling array */
    	
    	// Return array
    	return bounds;
    }
    
    // Public method to get the player's max health
    public int getHealthMax() {
    	return healthMax;
    }
    
    // Public method to get the player's current health
    public int getPlayerHealth() {
    	return playerHealth;
    }
    
    // Public method to get the player's health percentage
    public double getPlayerPercent() {
    	return (double) ((double) playerHealth / (double) healthMax);
    }
    
    // Public method to hurt the player
    public void hurtPlayer(int damagePoints) {
    	playerHealth = playerHealth - damagePoints;
    }
    
    // Public method to hurt the player
    public void healPlayer(int healPoints) {
    	if ((playerHealth + healPoints) > 100) {
    		playerHealth = 100;
    	}
    	else {
    		playerHealth = playerHealth + healPoints;
    	}
    }
}
