package com.se.softwareEngineering.test2d;

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
    private Element mElement;

    private Paint mPaint = new Paint();
    
    public Panel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
        mPaint.setColor(Color.WHITE);
    }
    
    public void doDraw(long elapsed, Canvas canvas) {
        canvas.drawColor(Color.BLACK);
		mElement.doDraw(canvas);
        canvas.drawText("FPS: " + Math.round(1000f / elapsed), 10, 10, mPaint);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mThread.isAlive()) {
            mThread = new ViewThread(this);
            mThread.setRunning(true);
            mThread.start();
        }
        
        mWidth = this.getWidth();
        mHeight = this.getHeight();
        
        mElement = new Element(getResources(), (int) mWidth/2, (int) mHeight/2);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mThread.isAlive()) {
            mThread.setRunning(false);
        }
    }
    
    public void animate(long elapsedTime) {
    	mElement.animate(elapsedTime);
    }
}
