package com.se.softwareEngineering.gameEngine;

import java.util.Random;

import com.se.softwareEngineering.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class itemElement {
	// Random number instance
	private static Random random = new Random();
	
	// Its x and y positions
    private float mX;
    private float mY;
    
    // Its x and y speeds
    private double mSpeedX;
    private double mSpeedY;
    
    // Item type variables
    private final int[] itemDrawables = {
		R.drawable.energy_norm,
		R.drawable.pizza_norm
    };
    private int itemDrawable;
    private String itemType;
    private int healthEffect;
    private int timeEffect;
    private double speedEffectMultiplier;
    
    // The bitmap image
    private Bitmap mBitmap;
    
    // Constructor
    public itemElement(Resources res) {
    	// Set the item type
    	setItemType();
    	
    	// Get the bitmap from the drawable
        mBitmap = BitmapFactory.decodeResource(res, itemDrawable);
        
        // Get a random starting x position based on the elements width and the panel's bounds
        float randomStartX = random.nextInt((int) (Panel.rightBound - Panel.leftBound - mBitmap.getWidth())) + Panel.leftBound;
        
        // Set the starting position
        mX = randomStartX;
        mY = 0 - mBitmap.getHeight();
        
        // Determine the horizontal speed
        // Determine if moving left or right
        if (random.nextInt(2) == 1) {
        	mSpeedX = random.nextDouble() * (0.3);
        }
        else {
        	mSpeedX = -(random.nextDouble() * (0.3));
        }
        
        // Determine the vertical speed
        mSpeedY = 1 * (0.25);
    }
    
    // Randomly generate an item type, and determine its properties
    private void setItemType() {
    	// Get a random int to determine
    	int randomNum = random.nextInt(itemDrawables.length);
    	
    	// Set item drawable resource
    	itemDrawable = itemDrawables[randomNum];
    	
    	// Energy drink
        if (randomNum == 0) {
        	// Set properties
        	itemType = "boost";
        	healthEffect = 2;
        	speedEffectMultiplier = 1.5;
        	timeEffect = 3;
        }
        else if (randomNum == 1) {
        	// Set properties
        	itemType = "health";
        	healthEffect = 10;
        	speedEffectMultiplier = 1;
        	timeEffect = 0;
        }
    }
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
    	// Make sure it doesn't "fall off the trail"
    	checkTrailBounds();
    	
    	// Proportionately effect the y-directional speed
    	mSpeedY = mSpeedY * (double) GameEngine.scoreSpeedOrigin * (double) GameEngine.scoreSpeedMultiplier;
    	
    	// Animate the x and y cooardinates
    	mX += mSpeedX * (elapsedTime / 5f);
        mY += mSpeedY * (elapsedTime / 5f);
    }
    
    // Method to check if the item has hit the trail bounds, and then reverse the direction
    private void checkTrailBounds() {
    	if (mX <= Panel.leftBound || (mX + mBitmap.getWidth() >= Panel.rightBound)) {
            mSpeedX = -mSpeedX;
        }
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
    	// Get boundary array of this item and the player
    	int[][] itemBounds = this.getBounds();
    	int[][] playerBounds = player.getBounds();
    	
    	// Create rectangle objects for the item and player
    	Rect itemRect = new Rect(
    		itemBounds[0][0],
    		itemBounds[0][1],
    		itemBounds[2][0],
    		itemBounds[2][1]
    	);
    	Rect playerRect = new Rect(
			playerBounds[0][0],
			playerBounds[0][1],
			playerBounds[2][0],
			playerBounds[2][1]
    	);
    	
    	// Check to see if the two rectangular bounding-boxes intersect
    	if (Rect.intersects(itemRect, playerRect)) {
    		return true;
    	}
    	
    	return false;
    }
    
    // Get item type
    public String getItemType() {
    	return itemType;
    }
    
    // Get health effect
    public int getHealthEffect() {
    	return healthEffect;
    }
    
    // Get speed effect
    public double getSpeedEffectMultiplier() {
    	return speedEffectMultiplier;
    }
    
    // Get time effect
    public int getTimeEffect() {
    	return timeEffect;
    }
}
