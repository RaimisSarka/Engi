package com.raimissarka.engi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Raimis on 12/16/2017.
 */

public class Player {

    private Bitmap bitmap;

    private int x;
    private int y;

    private int xOnMap;
    private int yOnMap;

    //Image drawing direction
    private int changeColCounter = 0;
    private boolean arrayReadDirectionFw = true;


    public void setMOVING_TO_THE_LEFT(boolean MOVING_TO_THE_LEFT) {
        this.MOVING_TO_THE_LEFT = MOVING_TO_THE_LEFT;
    }

    public void setMOVING_TO_THE_RIGHT(boolean MOVING_TO_THE_RIGHT) {
        this.MOVING_TO_THE_RIGHT = MOVING_TO_THE_RIGHT;
    }

    public void setMOVING_TO_THE_TOP(boolean MOVING_TO_THE_TOP) {
        this.MOVING_TO_THE_TOP = MOVING_TO_THE_TOP;
    }

    public void setMOVING_TO_THE_BOTTOM(boolean MOVING_TO_THE_BOTTOM) {
        this.MOVING_TO_THE_BOTTOM = MOVING_TO_THE_BOTTOM;
    }

    public boolean isMOVING_TO_THE_LEFT() {
        return MOVING_TO_THE_LEFT;
    }

    public boolean isMOVING_TO_THE_RIGHT() {
        return MOVING_TO_THE_RIGHT;
    }

    public boolean isMOVING_TO_THE_TOP() {
        return MOVING_TO_THE_TOP;
    }

    public boolean isMOVING_TO_THE_BOTTOM() {
        return MOVING_TO_THE_BOTTOM;
    }

    //Engi move direction
    private boolean MOVING_TO_THE_LEFT = false;
    private boolean MOVING_TO_THE_RIGHT = false;
    private boolean MOVING_TO_THE_TOP = false;
    private boolean MOVING_TO_THE_BOTTOM = false;

    private int rowToDraw;
    private int colToDraw;

    private static int imageColCount = 3;
    private static int imageRowCount = 4;

    //tile size in px
    private static int tileSize;

    private Bitmap[][] imageArray = new Bitmap[imageColCount][imageRowCount];

    private int speed;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Player(Context context, int posX, int posY, int mapX, int mapY){
        x = posX;
        y = posY;
        xOnMap = mapX;
        yOnMap = mapY;
        speed = 1;
        //array [0..imageColCount-1][0..imageRowCount-1]
        rowToDraw = 0;
        colToDraw = 0;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.engi);

        tileSize = bitmap.getWidth()/imageColCount;


        for (int i=0; i<imageColCount; i++ ){
            for (int j=0; j<imageRowCount; j++){
                imageArray[i][j] = Bitmap.createBitmap(bitmap, i*tileSize, j*tileSize, tileSize, tileSize);
            }
        }
    }

    public int getChangeColCounter() {
        return changeColCounter;
    }

    public boolean isArrayReadDirectionFw() {
        return arrayReadDirectionFw;
    }

    public int getRowToDraw() {
        return rowToDraw;
    }

    public int getColToDraw() {
        return colToDraw;
    }

    public static int getImageColCount() {
        return imageColCount;
    }

    public static int getImageRowCount() {
        return imageRowCount;
    }

    public static int getTileSize() {
        return tileSize;
    }

    public Bitmap[][] getImageArray() {
        return imageArray;
    }

    public int getxOnMap() {
        return xOnMap;
    }

    public int getyOnMap() {
        return yOnMap;
    }

    public void calcImageColumn(){
        changeColCounter++;
        if (changeColCounter >= tileSize/10) {
            changeColCounter = 0;

            if (colToDraw == imageColCount-1) {arrayReadDirectionFw = false;}
            if (colToDraw == 0) {arrayReadDirectionFw = true;}

            if (arrayReadDirectionFw){
                colToDraw++;
            } else {
                colToDraw--;
            }

        }
    }

    public void update(int maxXonMap, int maxYonMap){

        //Go to right

        if (MOVING_TO_THE_RIGHT) {
            rowToDraw = 2;
            if ((xOnMap + x) < maxXonMap) {
                xOnMap = xOnMap + speed;
            } else {
                if (xOnMap < maxXonMap - tileSize) {
                    xOnMap = xOnMap + speed;
                    x = x + speed;
                }
            }
            calcImageColumn();
        }

        //Go to left
        if (MOVING_TO_THE_LEFT) {
            rowToDraw = 3;
            if ((xOnMap - x) > 0) {
                xOnMap = xOnMap - speed;
            } else {
                if (xOnMap > 0 + tileSize) {
                    xOnMap = xOnMap - speed;
                    x = x - speed;
                }
            }
            calcImageColumn();
        }

        //Go to bottom
        if (MOVING_TO_THE_BOTTOM) {
            rowToDraw = 0;
            if ((yOnMap + y) < maxYonMap) {
                yOnMap = yOnMap + speed;
            } else {
                if (yOnMap < maxYonMap - tileSize) {
                    yOnMap = yOnMap + speed;
                    y = y + speed;
                }
            }
            calcImageColumn();
        }

        //Go to top
        if (MOVING_TO_THE_TOP) {
            rowToDraw = 1;
            if ((yOnMap - y) > 0) {
                yOnMap = yOnMap - speed;
            } else {
                if (yOnMap > 0 + tileSize) {
                    yOnMap = yOnMap - speed;
                    y = y - speed;
                }
            }
            calcImageColumn();
        }
    }

    public Bitmap getBitmap() {
        return imageArray[colToDraw][rowToDraw];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
