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

    public static float mWidth;
    public static float mHeight;
    
    private ViewThread mThread;
    public ArrayList<itemElement> itemElements = new ArrayList<itemElement>();
    private Element mElement;

    private Paint mPaint = new Paint();
    
    public Panel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
    }
    
    public void doDraw(long elapsed, Canvas canvas) {
    	// Draw background
        canvas.drawColor(Color.WHITE);
        
        // Draw perspective guide lines
        canvas.drawLine(mWidth/5, 0, 0, mHeight, mPaint);
        canvas.drawLine((mWidth*4/5), 0, mWidth, mHeight, mPaint);
		
        // Draw items
		synchronized (itemElements) {
			if (itemElements.size() > 0) {
	            for (Iterator<itemElement> it = itemElements.iterator(); it.hasNext();) {
	            	it.next().doDraw(canvas);
	            }
			}
        }
        
        // Draw character
		mElement.doDraw(canvas);
        
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
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        
        // Create the main element
        mElement = new Element(getResources(), (int) mWidth/2, (int) mHeight/2);
    	
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
    
    public void animate(long elapsedTime) {
    	mElement.animate(elapsedTime);
    	
    	synchronized (itemElements) {
    		if (itemElements.size() > 0) {
	            for (Iterator<itemElement> it = itemElements.iterator(); it.hasNext();) {
	            	it.next().animate(elapsedTime);
	            }
    		}
        }
    }
}
