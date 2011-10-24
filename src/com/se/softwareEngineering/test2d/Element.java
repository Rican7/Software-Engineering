package com.se.softwareEngineering.test2d;

import com.se.softwareEngineering.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Element {
    private float mX;
    private float mY;
    
    private int mSpeedX;
    private int mSpeedY;
    
    private Bitmap mBitmap;
    
    public Element(Resources res, int x, int y) {
        //Random rand = new Random();
        mBitmap = BitmapFactory.decodeResource(res, R.drawable.icon);
        mX = x - mBitmap.getWidth() / 2;
        mY = y - mBitmap.getHeight() / 2;
        mSpeedX = 0;
        //mSpeedY = rand.nextInt(7) - 3;
    }
    
    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
    
    /**
     * @param elapsedTime in ms.
     */
    public void animate(long elapsedTime) {
        mSpeedX = (int) Test2D.gravity[1];
        mX += mSpeedX * (elapsedTime / 5f);
        //mY += mSpeedY * (elapsedTime / 20f);
        checkBorders();
    }

    private void checkBorders() {
        if (mX <= 0) {
            mSpeedX = -mSpeedX;
            mX = 0;
        } else if (mX + mBitmap.getWidth() >= Panel.mWidth) {
            mSpeedX = -mSpeedX;
            mX = Panel.mWidth - mBitmap.getWidth();
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
}
