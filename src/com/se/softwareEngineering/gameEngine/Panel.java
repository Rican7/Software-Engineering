package com.se.softwareEngineering.gameEngine;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.util.TypedValue;
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
    
    // Method to draw the heads up display
    private void drawHUD(Canvas canvas) {
    	// Set variables
    	int barMargin = (int) (leftBound * 0.1); // 10% of the sides
    	int outlineWidth = (int) Math.ceil(barMargin * 0.2); // 20% of the barMargin
    	int leftCenterPoint = (int) (leftBound / 2);
    	int rightCenterPoint = (int) (rightBound + leftCenterPoint);
    	int titleFontSize = 20;
    	
    	// Create paints
    	Paint titlePaint = new Paint();
    	Paint outlinePaint = new Paint();
    	Paint emptyInnerPaint = new Paint();
    	Paint filledInnerHealthPaint = new Paint();
    	Paint filledInnerScorePaint = new Paint();
    	
    	// Customize paints
    	titlePaint.setColor(Color.BLACK);
    	titlePaint.setTextSize((float) titleFontSize * getContext().getResources().getDisplayMetrics().density);
    	titlePaint.setTextAlign(Align.CENTER);
    	outlinePaint.setColor(Color.BLACK);
    	outlinePaint.setStrokeWidth(outlineWidth);
    	emptyInnerPaint.setColor(Color.WHITE);
    	emptyInnerPaint.setStrokeWidth(0);
    	filledInnerHealthPaint.setColor(Color.RED);
    	filledInnerHealthPaint.setStrokeWidth(0);
    	filledInnerScorePaint.setColor(Color.GREEN);
    	filledInnerScorePaint.setStrokeWidth(0);
    	
    	/* Begin draw the health bar */
    	// Calculate the health bar size
    	int healthBarMaxLeftPoint = barMargin;
    	int healthBarMaxRightPoint = (int) (leftBound - barMargin);
    	int healthBarMaxLength = healthBarMaxRightPoint - healthBarMaxLeftPoint;
    	int healthBarCurrLength = (int) (healthBarMaxLength * (double) player.getPlayerPercent());
    	int healthBarCurrRightPoint = healthBarMaxLeftPoint + healthBarCurrLength - outlineWidth;
    	int healthBarMaxHeight = (int) (healthBarMaxLength * 0.4); // 40% of the length
    	int healthBarMaxTopPoint = barMargin + titleFontSize + barMargin;
    	int healthBarMaxBottomPoint = healthBarMaxTopPoint + healthBarMaxHeight;
    	
    	// Fix length so its not negative
    	if (healthBarCurrLength - outlineWidth < 0) {
    		healthBarCurrRightPoint = healthBarMaxLeftPoint + outlineWidth;
    	}
    	
    	// Draw the health bar title
    	canvas.drawText("Health", leftCenterPoint, (barMargin + titleFontSize), titlePaint);
    	
    	// Draw the actual health bar
    	// Draw the outline rectangle
        canvas.drawRect(healthBarMaxLeftPoint, healthBarMaxTopPoint, healthBarMaxRightPoint, healthBarMaxBottomPoint, outlinePaint);
        // Draw the "blank"/"empty" inside rectangle
        canvas.drawRect((healthBarMaxLeftPoint + outlineWidth), (healthBarMaxTopPoint + outlineWidth), (healthBarMaxRightPoint - outlineWidth), (healthBarMaxBottomPoint - outlineWidth), emptyInnerPaint);
        // Draw the filled inside (the actual current value) rectangle
        canvas.drawRect((healthBarMaxLeftPoint + outlineWidth),(healthBarMaxTopPoint + outlineWidth), healthBarCurrRightPoint, (healthBarMaxBottomPoint - outlineWidth), filledInnerHealthPaint);
        
    	/* End the health bar */
        
        /* Begin draw the score bar */
    	// Calculate the score bar size
    	int scoreBarMaxLeftPoint = (int) (rightBound + barMargin);
    	int scoreBarMaxRightPoint = (int) (mWidth - barMargin);
    	int scoreBarMaxLength = scoreBarMaxRightPoint - scoreBarMaxLeftPoint;
    	int scoreBarCurrLength = (int) (scoreBarMaxLength * (double) GameEngine.getScorePercent());
    	int scoreBarCurrRightPoint = scoreBarMaxLeftPoint + scoreBarCurrLength - outlineWidth;
    	int scoreBarMaxHeight = (int) (scoreBarMaxLength * 0.4); // 40% of the length
    	int scoreBarMaxTopPoint = barMargin + titleFontSize + barMargin;
    	int scoreBarMaxBottomPoint = scoreBarMaxTopPoint + scoreBarMaxHeight;
    	
    	// Fix length so its not negative
    	if (scoreBarCurrLength - outlineWidth < 0) {
    		scoreBarCurrRightPoint = scoreBarMaxLeftPoint + outlineWidth;
    	}
    	
    	// Draw the score bar title
    	canvas.drawText("Score", rightCenterPoint, (barMargin + titleFontSize), titlePaint);
    	
    	// Draw the actual score bar
    	// Draw the outline rectangle
        canvas.drawRect(scoreBarMaxLeftPoint, scoreBarMaxTopPoint, scoreBarMaxRightPoint, scoreBarMaxBottomPoint, outlinePaint);
        // Draw the "blank"/"empty" inside rectangle
        canvas.drawRect((scoreBarMaxLeftPoint + outlineWidth), (scoreBarMaxTopPoint + outlineWidth), (scoreBarMaxRightPoint - outlineWidth), (scoreBarMaxBottomPoint - outlineWidth), emptyInnerPaint);
        // Draw the filled inside (the actual current value) rectangle
        canvas.drawRect((scoreBarMaxLeftPoint + outlineWidth),(scoreBarMaxTopPoint + outlineWidth), scoreBarCurrRightPoint, (scoreBarMaxBottomPoint - outlineWidth), filledInnerScorePaint);
        
    	/* End the score bar */
    }
    
    // Method to draw the contents of the screen to the surface
    public void doDraw(long elapsed, Canvas canvas) {
    	// Draw background
        canvas.drawColor(Color.WHITE);
        
        // Draw perspective guide lines
        mPaint.setColor(Color.rgb(180, 180, 180));
        canvas.drawLine(leftBound, 0, leftBound, mHeight, mPaint);
        canvas.drawLine(rightBound, 0, rightBound, mHeight, mPaint);
        
        // Draw the heads up display
        drawHUD(canvas);
		
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
        leftBound = mWidth/6;
        rightBound = mWidth*5/6;
        
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
