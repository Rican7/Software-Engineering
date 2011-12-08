package com.se.softwareEngineering.gameEngine;

import java.util.Random;

import com.se.softwareEngineering.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class obstructionElement {
	// Random number instance
	private static Random random = new Random();
	
	// Its x and y positions
    private float mX;
    private float mY;
    
    // Its speed
    private double mSpeedY;
    
    // Item type variables
    private final int[] itemDrawables = {
		R.drawable.log1_norm,
		R.drawable.log2_norm,
		R.drawable.rock1_norm,
		R.drawable.rock2_norm
    };
    private int itemDrawable;
    private int healthEffect;
    
    // The bitmap image
    private Bitmap mBitmap;
    
    // Constructor
    public obstructionElement(Resources res) {
    	// Set the item type
    	setItemType();
    	
    	// Get the bitmap from the drawable
        mBitmap = BitmapFactory.decodeResource(res, itemDrawable);
        
        // Get a random starting x position based on the elements width and the panel's bounds
        float randomStartX = random.nextInt((int) (Panel.rightBound - Panel.leftBound - mBitmap.getWidth())) + Panel.leftBound;
        
        // Set the starting position
        mX = randomStartX;
        mY = 0 - mBitmap.getHeight();
        
        // Determine the vertical speed
        mSpeedY = 1 * (0.25);
    }
    
    // Randomly generate an item type, and determine its properties
    private void setItemType() {
    	// Get a random int to determine
    	int randomNum = random.nextInt(itemDrawables.length);
    	
    	// Set item drawable resource
    	itemDrawable = itemDrawables[randomNum];
    	
    	// Log
        if (randomNum == 0 || randomNum == 1) {
        	// Set properties
        	healthEffect = -2;
        }
        // Rock
        else if (randomNum == 2) {
        	// Set properties
        	healthEffect = -10;
        }
        // BIG rock
        else if (randomNum == 3) {
        	// Set properties
        	healthEffect = -20;
        }
    }
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
    	// Animate the y cooardinate
        mY += mSpeedY * (elapsedTime / 5f);
    }

    // Method to check the borders of the item to see if its out of bounds (no longer visible)
    public boolean checkOutOfBounds() {
        if (mY - mBitmap.getHeight() >= Panel.mHeight) {
            return true;
        }
        
        return false;
    }
    
    // Public method to get the coordinates of the element (x and y)
    public int[] getPosition() {
    	// Declare array
    	int[] position = new int[2];
    	
    	// Fill array
    	position[0] = (int) mX;
    	position[1] = (int) mY;
    	
    	// Return array
    	return position;
    }
    
    // Public method to get the size of the element (width and height)
    public int[] getSize() {
    	// Declare array
    	int[] size = new int[2];
    	
    	// Fill array
    	size[0] = (int) mBitmap.getWidth();
    	size[1] = (int) mBitmap.getHeight();
    	
    	// Return array
    	return size;
    }
    
    // Public method to get the bounding box of the element (top left -> clockwise)
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
    
    // Check collision with player
    public boolean checkCollisionWithPlayer(playerElement player) {
    	// Get boundary array of this obstruction and the player
    	int[][] obstructionBounds = this.getBounds();
    	int[][] playerBounds = player.getBounds();
    	
    	// Create rectangle objects for the obstruction and player
    	Rect obstructionRect = new Rect(
    		obstructionBounds[0][0],
    		obstructionBounds[0][1],
    		obstructionBounds[2][0],
    		obstructionBounds[2][1]
    	);
    	Rect playerRect = new Rect(
			playerBounds[0][0],
			playerBounds[0][1],
			playerBounds[2][0],
			playerBounds[2][1]
    	);
    	
    	// Check to see if the two rectangular bounding-boxes intersect
    	if (Rect.intersects(obstructionRect, playerRect)) {
    		return true;
    	}
    	
    	return false;
    }
    
    // Get health effect
    public int getHealthEffect() {
    	return healthEffect;
    }
}
