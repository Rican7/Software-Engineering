package com.se.softwareEngineering.gameEngine;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

    public static float mWidth;
    public static float mHeight;
    
    private ViewThread mThread;
    private ArrayList<itemElement> itemElements = new ArrayList<itemElement>();
    private Element mElement;

    private Paint mPaint = new Paint();
    
    public Panel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(20);
    }
    
    public void doDraw(long elapsed, Canvas canvas) {
    	// Draw background
        canvas.drawColor(Color.WHITE);
        
        // Draw perspective guide lines
        canvas.drawLine(mWidth/5, 0, 0, mHeight, mPaint);
        canvas.drawLine((mWidth*4/5), 0, mWidth, mHeight, mPaint);
		
        // Draw items
		synchronized (itemElements) {
            for (itemElement itemElement : itemElements) {
            	itemElement.doDraw(canvas);
            }
        }
        
        // Draw character
		mElement.doDraw(canvas);
        
        // Draw framerate
        canvas.drawText("FPS: " + Math.round(1000f / elapsed), 10, 10, mPaint);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	// Create a timer
    	final Timer timer = new Timer();
    	
        if (!mThread.isAlive()) {
            mThread = new ViewThread(this);
            mThread.setRunning(true);
            mThread.start();
        }
        
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        
        // Create the main element
        mElement = new Element(getResources(), (int) mWidth/2, (int) mHeight/2);
        
        // Create a sub-class to run a task every n seconds
        class RemindTask extends TimerTask {
            public void run() {
            	Random rand = new Random();
            	
            	// Create an item element at a random location
            	itemElements.add(new itemElement(getResources(), (int) (rand.nextInt((int) (mWidth*3/5)) + (mWidth/5)), 0));
            	
            	// Check if any of the item elements are out of bounds, and remove them
            	synchronized (itemElements) {
                    for (itemElement itemElement : itemElements) {
                    	if (itemElement.getOutOfBounds()) {
                    		//itemElements.remove(itemElement);
                    	}
                    }
                }
            	
            	// Start this baby back up!
                timer.schedule(new RemindTask(), 4*1000);
            }
        }
        
        // Initially schedule the task
        timer.schedule(new RemindTask(), 1*1000);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mThread.isAlive()) {
            mThread.setRunning(false);
        }
    }
    
    public void animate(long elapsedTime) {
    	mElement.animate(elapsedTime);
    	
    	synchronized (itemElements) {
            for (itemElement itemElement : itemElements) {
            	itemElement.animate(elapsedTime);
            }
        }
    }
}
