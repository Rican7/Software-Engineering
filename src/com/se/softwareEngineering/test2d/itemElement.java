package com.se.softwareEngineering.test2d;

import java.util.Random;

import com.se.softwareEngineering.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class itemElement {
    private float mX;
    private float mY;
    
    private double mSpeedX;
    private double mSpeedY;
    
    private Bitmap mBitmap;
    
    private boolean outOfBounds = false;
    
    public itemElement(Resources res, int x, int y) {
        mBitmap = BitmapFactory.decodeResource(res, R.drawable.energy);
        mX = x - mBitmap.getWidth() / 2;
        mSpeedX = Math.random() * (0.3);
        mSpeedY = 1 * (0.25);
    }
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
    	Random rand = new Random();
    	
    	// Determine if moving left or right
    	if (rand.nextInt(1) == 1) {
    		mX += mSpeedX * (elapsedTime / 5f);
    	}
    	else {
    		mX -= mSpeedX * (elapsedTime / 5f);
    	}
    	
        mY += mSpeedY * (elapsedTime / 5f);
        checkBorders();
    }

    private void checkBorders() {
        if (mY + mBitmap.getHeight() >= Panel.mHeight) {
            outOfBounds = true;
        }
    }
    
    public boolean getOutOfBounds() {
    	return outOfBounds;
    }
}
