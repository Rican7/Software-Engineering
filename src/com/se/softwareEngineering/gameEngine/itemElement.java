package com.se.softwareEngineering.gameEngine;

import java.util.Random;

import com.se.softwareEngineering.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class itemElement {
	// Random number instance
	private static Random random = new Random();
	
	// Its x and y positions
    private float mX;
    private float mY;
    
    // Its x and y speeds
    private double mSpeedX;
    private double mSpeedY;
    
    // The bitmap image
    private Bitmap mBitmap;
    
    private boolean outOfBounds = false;
    
    // Constructor
    public itemElement(Resources res, int x, int y) {
    	// Set the starting position
        mBitmap = BitmapFactory.decodeResource(res, R.drawable.energy);
        mX = x - mBitmap.getWidth() / 2;
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
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
    	mX += mSpeedX * (elapsedTime / 5f);
        mY += mSpeedY * (elapsedTime / 5f);
        checkBorders();
    }

    private void checkBorders() {
        if (mY - mBitmap.getHeight() >= Panel.mHeight) {
            outOfBounds = true;
        }
    }
    
    public boolean getOutOfBounds() {
    	return outOfBounds;
    }
}
