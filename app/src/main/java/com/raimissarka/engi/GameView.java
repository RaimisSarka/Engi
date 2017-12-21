package com.raimissarka.engi;

import android.content.Context;
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
        // Current time in nanoseconds
        long now = System.nanoTime();
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

        /*drawFromI = gameMap.getMapSizeX() / 2 / gameMap.getTileSize() - screenWidth / 2 / gameMap.getTileSize();
        drawToI = drawFromI + screenWidth / gameMap.getTileSize();

        drawFromJ = gameMap.getMapSizeY()/ 2 / gameMap.getTileSize() - screenHeight/ 2 / gameMap.getTileSize();
        drawToJ = drawFromJ + screenHeight / gameMap.getTileSize();*/

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


            if (DEVELOPMENT) {

                //Drawing development-debugging data
                paint.setColor(Color.WHITE);
                paint.setTextSize(20);
                canvas.drawText("screenW: " + screenWidth, 10, 340, paint);
                canvas.drawText("screenH: " + screenHeight, 10, 360, paint);
                canvas.drawText("MapW: " + mapWidthInPx, 10, 380, paint);
                canvas.drawText("MapH: " + mapHeightInPx, 10, 400, paint);
                canvas.drawText("TileSize: " + gameMap.getTileSize(), 10, 420, paint);
                canvas.drawText("XposOnMap: " + mapWidthInPx / 2, 10, 440, paint);
                canvas.drawText("YposOnMap: " + mapHeightInPx / 2, 10, 460, paint);
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

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        int x = 0;
        int y = 0;

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                //TODO check if there are somethig to select
                xOnActionDown = (int) motionEvent.getX();
                yOnActionDown = (int) motionEvent.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                x=  (int) motionEvent.getX();
                y = (int) motionEvent.getY();


                //update camaras coords
                cameraX = cameraX + xOnActionDown - x;
                cameraY = cameraY + yOnActionDown - y;
                xOnActionDown = x;
                yOnActionDown = y;
                break;


            case MotionEvent.ACTION_UP:

                break;
        }
        return true;

    }
}
