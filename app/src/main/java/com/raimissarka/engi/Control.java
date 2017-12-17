package com.raimissarka.engi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.reflect.Array;

/**
 * Created by Raimis on 12/17/2017.
 */

public class Control {

    private Bitmap bitmap;

    private int imageColCount = 6;
    private int imageRowCount = 6;

    private int tileSize;

    private Bitmap[][] imageArray = new Bitmap[imageColCount][imageRowCount];

    //Where to draw
    //Left upper corner of all joystic
    private int x;
    private int y;

    //Area of touchables buttons
    public class Coords {

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        public void setX1(int x1) {
            this.x1 = x1;
        }

        public void setY1(int y1) {
            this.y1 = y1;
        }

        public void setX2(int x2) {
            this.x2 = x2;
        }

        public void setY2(int y2) {
            this.y2 = y2;
        }

        public int getX1() {
            return x1;
        }

        public int getY1() {
            return y1;
        }

        public int getX2() {
            return x2;
        }

        public int getY2() {
            return y2;
        }
    }

    private Coords buttoUpArea; //Touchable area
    private Coords buttoDownArea; //Touchable area
    private Coords buttoLeftArea; //Touchable area
    private Coords buttoRightArea; //Touchable area


    //Constructor
    public Control (Context context, int screenWidth, int screenHeight){

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.joystick);

        tileSize = bitmap.getWidth()/imageColCount;

        buttoUpArea = new Coords();
        buttoDownArea = new Coords();
        buttoLeftArea = new Coords();
        buttoRightArea = new Coords();


        x = 10;
        y = screenHeight - bitmap.getHeight() - 10;


        for (int i=0; i<imageColCount; i++ ){
            for (int j=0; j<imageRowCount; j++){
                imageArray[i][j] = Bitmap.createBitmap(bitmap, i*tileSize, j*tileSize, tileSize, tileSize);


                //setting of buttons toucheable areas
                if (i == 2 && j == 0) { //Button up left top corner
                    buttoUpArea.x1 = i * tileSize + x;
                    buttoUpArea.y1 = j * tileSize + y;
                    buttoUpArea.x2 = buttoUpArea.getX1() + 2 * tileSize;
                    buttoUpArea.y2 = buttoUpArea.getY1() + 2 * tileSize;
                }

                if (i == 0 && j == 2) { //Button left left top corner
                    buttoLeftArea.x1 = i * tileSize + x;
                    buttoLeftArea.y1 = j * tileSize + y;
                    buttoLeftArea.x2 = buttoLeftArea.getX1() + 2 * tileSize;
                    buttoLeftArea.y2 = buttoLeftArea.getY1() + 2 * tileSize;
                }

                if (i == 2 && j == 4) { //Button down left top corner
                    buttoDownArea.x1 = i * tileSize + x;
                    buttoDownArea.y1 = j * tileSize + y;
                    buttoDownArea.x2 = buttoDownArea.getX1() + 2 * tileSize;
                    buttoDownArea.y2 = buttoDownArea.getY1() + 2 * tileSize;
                }

                if (i == 4 && j == 2) { //Button right left top corner
                    buttoRightArea.x1 = i * tileSize + x;
                    buttoRightArea.y1 = j * tileSize + y;
                    buttoRightArea.x2 = buttoRightArea.getX1() + 2 * tileSize;
                    buttoRightArea.y2 = buttoRightArea.getY1() + 2 * tileSize;
                }
            }
        }
    }

    public Bitmap getBitmap(int i, int j) {
        return imageArray[i][j];
    }

    public int getImageColCount() {
        return imageColCount;
    }

    public int getImageRowCount() {
        return imageRowCount;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Bitmap[][] getImageArray() {
        return imageArray;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coords getButtoUpArea() {
        return buttoUpArea;
    }

    public Coords getButtoDownArea() {
        return buttoDownArea;
    }

    public Coords getButtoLeftArea() {
        return buttoLeftArea;
    }

    public Coords getButtoRightArea() {
        return buttoRightArea;
    }
}
