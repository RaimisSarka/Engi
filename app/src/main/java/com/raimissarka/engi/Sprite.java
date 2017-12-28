package com.raimissarka.engi;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Raimis on 12/16/2017.
 */

public class Sprite {

    private Bitmap bitmap;


    private int xOnScreen;
    private int yOnScreen;

    //Image drawing direction
    private int changeColCounter = 0;
    private boolean arrayReadDirectionFw = true;

    public int rowToDraw;
    public int colToDraw;

    private static int imageColCount = 1;
    private static int imageRowCount = 1;

    //tile size in px
    private static int tileSize;

    private Bitmap[][] imageArray;

    private int speed;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Bitmap[][] getImageArray() {
        return imageArray;
    }

    public static int getTileSize() {
        return tileSize;
    }

    public Sprite(Context context, Bitmap image, int iColCount, int iRowCount, int posX, int posY){

        xOnScreen = posX;
        yOnScreen = posY;

        imageColCount = iColCount;
        imageRowCount = iRowCount;

        imageArray = new Bitmap[imageColCount][imageRowCount];

        speed = 1;
        //array [0..imageColCount-1][0..imageRowCount-1]
        rowToDraw = 0;
        colToDraw = 0;

        bitmap = image;

        tileSize = bitmap.getWidth()/imageColCount;


        for (int i=0; i<imageColCount; i++ ){
            for (int j=0; j<imageRowCount; j++){
                imageArray[i][j] = Bitmap.createBitmap(bitmap, i*tileSize, j*tileSize, tileSize, tileSize);
            }
        }
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

    public void update(int addX, int addY){
        xOnScreen = xOnScreen + addX + speed;
        yOnScreen = yOnScreen + addY + speed;

    }

}
