package com.raimissarka.engi;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by Raimis on 12/16/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    public boolean playing;

    //TODO DEVELOPMENT FLAG
    private static boolean DEVELOPMENT = true;

    private Thread gameThread = null;

    //Creating map object
    private Map gameMap;

    private int screenWidth;
    private int screenHeight;

    //Engi character
    private EngiSprite engi;

    //Coords off visible map tile
    private int cameraX;
    private int cameraY;

    int xOnActionDown = 0;
    int yOnActionDown = 0;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    public GameView(Context context, int sWidth, int sHeight) {
        super(context);

        screenWidth = sWidth;
        screenHeight = sHeight;

        gameMap = new Map(context);

        cameraX = (gameMap.getMapSizeX() * gameMap.getTileSize() - screenWidth) / 2;
        cameraY = (gameMap.getMapSizeY()  * gameMap.getTileSize() - screenHeight) / 2;

        //initialize drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        Bitmap engiBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.engi);
        engi = new EngiSprite(context, engiBitmap, 3, 4, screenWidth / 2, screenHeight / 2);


    }

    @Override
    public void run() {
        while (playing) {

            update();

            draw();

            control();
        }
    }

    public void update() {

    }

    public void draw() {
        int drawFromI = 0;
        int drawToI = gameMap.getMapSizeX();

        int drawFromJ = 0;
        int drawToJ = gameMap.getMapSizeY();

        int mapWidthInPx;
        int mapHeightInPx;

        mapWidthInPx = gameMap.getMapSizeX() * gameMap.getTileSize();
        mapHeightInPx = gameMap.getMapSizeY() * gameMap.getTileSize();

        drawFromI = cameraX / gameMap.getTileSize();
        drawToI = drawFromI + screenWidth / gameMap.getTileSize();

        drawFromJ = cameraY / gameMap.getTileSize();
        drawToJ = drawFromJ + screenHeight / gameMap.getTileSize();

        //Checking borders
        if (drawFromI > 0) {
            drawFromI--;
        } else {
            drawFromI = 0;
        }

        if (drawToI < gameMap.getMapSizeX()) {
            drawToI++;
        } else {
            drawToI = gameMap.getMapSizeX();
        }

        if (drawFromJ > 0) {
            drawFromJ--;
        } else {
            drawFromJ = 0;
        }

        if (drawToJ < gameMap.getMapSizeY()) {
            drawToJ++;
        } else {
            drawToJ = gameMap.getMapSizeY();
        }

        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();

            //Drawing map
            for (int i = drawFromI; i < drawToI; i++) {
                for (int j = drawFromJ; j < drawToJ; j++) {
                    canvas.drawBitmap(
                            gameMap.getBitmap(i, j),
                            gameMap.GetCoordX(i) - cameraX,
                            gameMap.GetCoordY(j) - cameraY,
                            paint);
                }
            }

            engi.update();
            engi.draw(canvas);



            if (DEVELOPMENT) {

                //Drawing development-debugging data
                paint.setColor(Color.WHITE);
                canvas.drawRect(5, 320, 205, 600, paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                canvas.drawText("screenW: " + screenWidth, 10, 340, paint);
                canvas.drawText("screenH: " + screenHeight, 10, 360, paint);
                canvas.drawText("MapW: " + mapWidthInPx, 10, 380, paint);
                canvas.drawText("MapH: " + mapHeightInPx, 10, 400, paint);
                canvas.drawText("TileSize: " + gameMap.getTileSize(), 10, 420, paint);
                canvas.drawText("EngiPosX: " + engi.getX(), 10, 440, paint);
                canvas.drawText("EngiPosY: " + engi.getY(), 10, 460, paint);
                canvas.drawText("DrawFromI: " + drawFromI, 10, 480, paint);
                canvas.drawText("DrawToI: " + drawToI, 10, 500, paint);
                canvas.drawText("DrawFromJ: " + drawFromJ, 10, 520, paint);
                canvas.drawText("DrawToJ: " + drawToJ, 10, 540, paint);
                canvas.drawText("CameraX: " + cameraX, 10, 560, paint);
                canvas.drawText("CameraY: " + cameraY, 10, 580, paint);

            }
            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public boolean thereIsSelectible (int actionDownX, int actionDownY){
        boolean result = false;
        if (actionDownX > engi.getX() &&
                actionDownX < engi.getX() + engi.getEngiTileSize() &&
                actionDownY > engi.getY() &&
                actionDownY < engi.getY() + engi.getEngiTileSize()){
            result = true;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        int x = 0;
        int y = 0;

        int oldCameraX;
        int oldCameraY;

        int engiXOnMap;
        int engiYOnMap;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                //TODO check if there are somethig to select
                xOnActionDown = (int) motionEvent.getX();
                yOnActionDown = (int) motionEvent.getY();

                if (thereIsSelectible(xOnActionDown, yOnActionDown)) {
                    if (engi.ismSelected()){
                        engi.setSelected(false);
                    } else {
                        engi.setSelected(true);
                    }
                } else {
                    if (engi.ismSelected()) {
                        int movingVectorX = xOnActionDown - engi.getX() - engi.getEngiTileSize() / 2;
                        int movingVectorY = yOnActionDown - engi.getY() - engi.getEngiTileSize() / 2;

                        engi.setMovingVectorX(movingVectorX);
                        engi.setMovingVectorY(movingVectorY);
                        engi.setmDestinationX(xOnActionDown - engi.getEngiTileSize() / 2);
                        engi.setmDestinationY(yOnActionDown - engi.getEngiTileSize() / 2);
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:
                x=  (int) motionEvent.getX();
                y = (int) motionEvent.getY();

                engiXOnMap = engi.getX();
                engiYOnMap = engi.getY();

                //Save coords for calculation of difference
                oldCameraX = cameraX;
                oldCameraY = cameraY;

                //update cameras coords
                cameraX = cameraX + xOnActionDown - x;
                cameraY = cameraY + yOnActionDown - y;

                //checking boarders
                if (cameraX + screenWidth > gameMap.getMapSizeX()*gameMap.getTileSize()) {
                    cameraX = gameMap.getMapSizeX()*gameMap.getTileSize() - screenWidth;
                }

                if (cameraY + screenHeight > gameMap.getMapSizeY()*gameMap.getTileSize()) {
                    cameraY = gameMap.getMapSizeY()*gameMap.getTileSize() - screenHeight;
                }

                if (cameraX < 0) {
                    cameraX = 0;
                }

                if (cameraY < 0) {
                    cameraY = 0;
                }

                //Update Engis coords
                engiXOnMap = engiXOnMap - cameraX + oldCameraX;
                engiYOnMap = engiYOnMap - cameraY + oldCameraY;


                engi.setPos(engiXOnMap, engiYOnMap);

                engi.setMovingVectorX(engi.getMovingVectorX() - cameraX + oldCameraX);
                engi.setMovingVectorY(engi.getMovingVectorY() - cameraY + oldCameraY);
                engi.setmDestinationX(engi.getmDestinationX() - cameraX + oldCameraX);
                engi.setmDestinationY(engi.getmDestinationY() - cameraY + oldCameraY);

                xOnActionDown = x;
                yOnActionDown = y;

                break;


            case MotionEvent.ACTION_UP:

                break;
        }
        return true;

    }
}
