package com.se.softwareEngineering.gameEngine;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {
	// Set public static properties of the Panel
    public static float mWidth;
    public static float mHeight;
    public static float leftBound;
    public static float rightBound;
    
    // Declare the view thread
    private ViewThread mThread;
    
    // Declare the publicly accessible elements (items, obstructions, and the player)
    public ArrayList<itemElement> itemElements = new ArrayList<itemElement>();
    public ArrayList<obstructionElement> obstructionElements = new ArrayList<obstructionElement>();
    public playerElement player;

    // Create a painting object to paint lines to the screen
    private Paint mPaint = new Paint();
    
    // Panel constructor
    public Panel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
    }
    
    // Method to draw the contents of the screen to the surface
    public void doDraw(long elapsed, Canvas canvas) {
    	// Draw background
        canvas.drawColor(Color.WHITE);
        
        // Draw perspective guide lines
        mPaint.setColor(Color.rgb(180, 180, 180));
        canvas.drawLine(leftBound, 0, leftBound, mHeight, mPaint);
        canvas.drawLine(rightBound, 0, rightBound, mHeight, mPaint);
		
        // Draw items
		synchronized (itemElements) {
			if (itemElements.size() > 0) {
	            for (Iterator<itemElement> it = itemElements.iterator(); it.hasNext();) {
	            	it.next().doDraw(canvas);
	            }
			}
        }
		
		// Draw obstructions
		synchronized (obstructionElements) {
			if (obstructionElements.size() > 0) {
	            for (Iterator<obstructionElement> it = obstructionElements.iterator(); it.hasNext();) {
	            	it.next().doDraw(canvas);
	            }
			}
        }
        
        // Draw character
		player.doDraw(canvas);
        
        // Draw framerate
        //canvas.drawText("FPS: " + Math.round(1000f / elapsed), 10, 10, mPaint);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	// Set the panel's width and height
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        
        // Set the left and right boundaries (where the trail sides end and the trees reside)
        leftBound = mWidth/8;
        rightBound = mWidth*7/8;
        
        // Create the main player
        player = new playerElement(getResources(), (int) mWidth/2, (int) mHeight/2);
    	
        if (!mThread.isAlive()) {
            mThread = new ViewThread(this);
            mThread.setRunning(true);
            mThread.start();
        }
        
        // Tell the engine that the surface has been created
        GameEngine.surfaceCreated();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mThread.isAlive()) {
            mThread.setRunning(false);
        }
    }
    
    // Animation method... here, all of the elements must animate
    public void animate(long elapsedTime) {
    	// Animate the player
    	player.animate(elapsedTime);
    	
    	// Animate each item element
    	synchronized (itemElements) {
    		if (itemElements.size() > 0) {
	            for (Iterator<itemElement> it = itemElements.iterator(); it.hasNext();) {
	            	it.next().animate(elapsedTime);
	            }
    		}
        }
    	
    	// Animate each obstruction element
    	synchronized (obstructionElements) {
    		if (obstructionElements.size() > 0) {
	            for (Iterator<obstructionElement> it = obstructionElements.iterator(); it.hasNext();) {
	            	it.next().animate(elapsedTime);
	            }
    		}
        }
    }
}
