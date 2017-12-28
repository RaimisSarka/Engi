package com.raimissarka.engi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.provider.Settings;

/**
 * Created by Raimis on 12/23/2017.
 */

public class EngiSprite extends Sprite {

    //Coordinates on screen
    private int x;
    private int y;

    private int movingVectorX = 0;
    private int movingVectorY = 0;
    private int mDestinationX;
    private int mDestinationY;

    //Is Engi selected
    private boolean mSelected = false;

    private long lastDrawNanoTime =-1;

    private Bitmap pictureToDraw;
    private Bitmap selectionPicture;

    // Velocity of game character (pixel/millisecond)
    public static final float VELOCITY = 0.1f;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public EngiSprite(Context context, Bitmap image, int iColCount, int iRowCount, int posX, int posY) {
        super(context, image, iColCount, iRowCount, posX, posY);

        x = posX;
        y = posY;
        mDestinationX = x;
        mDestinationY = y;

        pictureToDraw = getImageArray()[0][0];
        selectionPicture = BitmapFactory.decodeResource(context.getResources(),R.drawable.selection);

    }

    public void setPos(int xx, int yy){
        x = xx;
        y = yy;
    }

    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }

    public void setMovingVectorX(int movingVectorX) {
        this.movingVectorX = movingVectorX;
    }

    public void setMovingVectorY(int movingVectorY) {
        this.movingVectorY = movingVectorY;
    }

    public void setmDestinationX(int mDestinationX) {
        this.mDestinationX = mDestinationX;
    }

    public int getMovingVectorX() {
        return movingVectorX;
    }

    public int getMovingVectorY() {
        return movingVectorY;
    }

    public int getmDestinationX() {
        return mDestinationX;
    }

    public int getmDestinationY() {
        return mDestinationY;
    }

    public void setmDestinationY(int mDestinationY) {
        this.mDestinationY = mDestinationY;
    }

    public void update() {
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        // Distance moves
        float distance = VELOCITY * deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        if ((float) (distance * movingVectorX / movingVectorLength) > 0){
            if (x >= mDestinationX) {
                x = mDestinationX;
            }
        } else if ((float) (distance * movingVectorX / movingVectorLength) < 0) {
            if (x <= mDestinationX) {
                x = mDestinationX;
            }
        }

        if ((float) (distance * movingVectorY / movingVectorLength) > 0){
            if (y >= mDestinationY) {
                y = mDestinationY;
            }
        } else if ((float) (distance * movingVectorY / movingVectorLength) < 0){
            if (y <= mDestinationY){
                y = mDestinationY;
            }
        }

        //If destination not reached, check direction and set image to draw
        if (mDestinationX != x || mDestinationY != y) {

            rowToDraw = checkDirection(x, y, mDestinationX, mDestinationY);
            calcImageColumn();
        }

        if (mDestinationX != x) {
            if ((int) (distance * movingVectorX / movingVectorLength) != 0) {
                x = x + (int) (distance * movingVectorX / movingVectorLength);
            } else if ((float) (distance * movingVectorX / movingVectorLength) > 0 ) {
                x = x + 1;
            } else if ((float) (distance * movingVectorX / movingVectorLength) < 0 ) {
                x = x - 1;
            }
        }
        if (mDestinationY != y) {
            if ((int) (distance * movingVectorY / movingVectorLength) != 0) {
                y = y + (int) (distance * movingVectorY / movingVectorLength);
            } else if ((float) (distance * movingVectorY / movingVectorLength) > 0) {
                y = y + 1;
            } else if ((float) (distance * movingVectorY / movingVectorLength) < 0) {
                y = y - 1;
            }
        }

    }

    private int checkDirection(int x, int y, int mDestinationX, int mDestinationY) {
        int mDiffX;
        int mDiffY;
        int mRowToDraw = 0;

        mDiffX = mDestinationX - x;
        mDiffY = mDestinationY - y;

        if (mDiffY > 0) {
            if (mDiffY >= mDiffX) {
                mRowToDraw = 0;
            } else {
                mRowToDraw = 2;
            }
        } else {
            if (mDiffY <= mDiffX) {
                mRowToDraw = 1;
            } else {
                mRowToDraw = 3;
            }
        }

        return mRowToDraw;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    @Override
    public void calcImageColumn() {
        super.calcImageColumn();

        pictureToDraw = getImageArray()[colToDraw] [rowToDraw];
    }

    public void draw(Canvas canvas){
        Bitmap bitmap = pictureToDraw;
        if (mSelected) {
            canvas.drawBitmap(selectionPicture, x - getEngiTileSize(), y - getEngiTileSize(), null);
        }
        canvas.drawBitmap(bitmap, x, y, null);
        lastDrawNanoTime = System.nanoTime();
    }

    public int getEngiTileSize(){
        return Sprite.getTileSize();
    }
}
