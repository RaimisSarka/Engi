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
    private static boolean DEVELOPMENT = true;

    private Thread gameThread = null;

    //Creating player object
    private Player engiPlayer;

    //Creating map object
    private Map gameMap;

    //Creating JoyStick
    private Control joyStick;

    private int screenWidth;
    private int screenHeight;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    public GameView(Context context, int sWidth, int sHeight) {
        super(context);

        screenWidth = sWidth;
        screenHeight = sHeight;

        gameMap = new Map(context);

        //Creating player
        engiPlayer = new Player(context,
                (int) screenWidth / 2, //Position on screen X
                (int) screenHeight / 2, //Position on screen Y
                (int) gameMap.getMapSizeX() * gameMap.getTileSize() / 2, //Position on map X
                (int) gameMap.getMapSizeY() * gameMap.getTileSize() / 2); //Position on map X

        joyStick = new Control(context, screenWidth, screenHeight);

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
        engiPlayer.update(
                gameMap.getMapSizeX() * gameMap.getTileSize(),
                gameMap.getMapSizeY() * gameMap.getTileSize()
        );
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

        drawFromI = engiPlayer.getxOnMap() / gameMap.getTileSize() - engiPlayer.getX() / gameMap.getTileSize();
        drawToI = drawFromI + screenWidth / gameMap.getTileSize();

        drawFromJ = engiPlayer.getyOnMap() / gameMap.getTileSize() - engiPlayer.getY() / gameMap.getTileSize();
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
                            gameMap.GetCoordX(i) - engiPlayer.getxOnMap() + engiPlayer.getX(),
                            gameMap.GetCoordY(j) - engiPlayer.getyOnMap() + engiPlayer.getY(),
                            paint);
                }
            }

            //Drawing the player
            canvas.drawBitmap(
                    engiPlayer.getBitmap(),
                    engiPlayer.getX(),
                    engiPlayer.getY(),
                    paint);

            //Drawing joyStick
            for (int i = 0; i < joyStick.getImageColCount(); i++) {
                for (int j = 0; j < joyStick.getImageRowCount(); j++) {
                    canvas.drawBitmap(
                            joyStick.getBitmap(i, j),
                            joyStick.getX() + i * joyStick.getTileSize(),
                            joyStick.getY() + j * joyStick.getTileSize(),
                            paint);
                }
            }


            if (DEVELOPMENT) {
                //Changing speed
                engiPlayer.setSpeed(5);

                //Changing direction
               /* engiPlayer.setMOVING_TO_THE_BOTTOM(false);
                engiPlayer.setMOVING_TO_THE_TOP(false);
                engiPlayer.setMOVING_TO_THE_LEFT(false);
                engiPlayer.setMOVING_TO_THE_RIGHT(false);*/

                //Drawing development-debugging data
                paint.setColor(Color.WHITE);
                paint.setTextSize(20);
                canvas.drawText("posX: " + engiPlayer.getX(), 10, 300, paint);
                canvas.drawText("posY: " + engiPlayer.getY(), 10, 320, paint);
                canvas.drawText("screenW: " + screenWidth, 10, 340, paint);
                canvas.drawText("screenH: " + screenHeight, 10, 360, paint);
                canvas.drawText("MapW: " + mapWidthInPx, 10, 380, paint);
                canvas.drawText("MapH: " + mapHeightInPx, 10, 400, paint);
                canvas.drawText("TileSize: " + gameMap.getTileSize(), 10, 420, paint);
                canvas.drawText("XposOnMap: " + engiPlayer.getxOnMap(), 10, 440, paint);
                canvas.drawText("YposOnMap: " + engiPlayer.getyOnMap(), 10, 460, paint);
                canvas.drawText("DrawFromI: " + drawFromI, 10, 480, paint);
                canvas.drawText("DrawToI: " + drawToI, 10, 500, paint);
                canvas.drawText("DrawFromJ: " + drawFromJ, 10, 520, paint);
                canvas.drawText("DrawToJ: " + drawToJ, 10, 540, paint);
                canvas.drawText("ButtonUpX1: " + joyStick.getButtoUpArea().getX1(), 10, 560, paint);
                canvas.drawText("ButtonUpY1: " + joyStick.getButtoUpArea().getY1(), 10, 580, paint);
                canvas.drawText("ButtonUpX2: " + joyStick.getButtoUpArea().getX2(), 10, 600, paint);
                canvas.drawText("ButtonUpY2: " + joyStick.getButtoUpArea().getY2(), 10, 620, paint);
                canvas.drawText("Movement: " + engiPlayer.isMOVING_TO_THE_TOP(), 10, 640, paint);

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

    public String buttonClicked(int x, int y) {
        String UP = "up";
        String DOWN = "down";
        String LEFT = "left";
        String RIGHT = "right";
        String ELSEWHERE = "elsewhere";

        String result = ELSEWHERE;

        Control.Coords buttonUpCoords = joyStick.getButtoUpArea();
        Control.Coords buttonDownCoords = joyStick.getButtoDownArea();
        Control.Coords buttonLeftCoords = joyStick.getButtoLeftArea();
        Control.Coords buttonRightCoords = joyStick.getButtoRightArea();

        if (x >= buttonUpCoords.getX1() && x <= buttonUpCoords.getX2() &&
                y >= buttonUpCoords.getY1() && y <= buttonUpCoords.getY2()) {
            result = UP;
        }

        if (x >= buttonDownCoords.getX1() && x <= buttonDownCoords.getX2() &&
                y >= buttonDownCoords.getY1() && y <= buttonDownCoords.getY2()) {
            result = DOWN;
        }

        if (x >= buttonLeftCoords.getX1() && x <= buttonLeftCoords.getX2() &&
                y >= buttonLeftCoords.getY1() && y <= buttonLeftCoords.getY2()) {
            result = LEFT;
        }

        if (x >= buttonRightCoords.getX1() && x <= buttonRightCoords.getX2() &&
                y >= buttonRightCoords.getY1() && y <= buttonRightCoords.getY2()) {
            result = RIGHT;
        }
        return result;
    }

    public void chooseDirection(int xx, int yy){

        switch (buttonClicked(xx, yy)) {

            case "up":
                engiPlayer.setMOVING_TO_THE_TOP(true);
                engiPlayer.setMOVING_TO_THE_RIGHT(false);
                engiPlayer.setMOVING_TO_THE_LEFT(false);
                engiPlayer.setMOVING_TO_THE_BOTTOM(false);
                break;

            case "down":
                engiPlayer.setMOVING_TO_THE_TOP(false);
                engiPlayer.setMOVING_TO_THE_RIGHT(false);
                engiPlayer.setMOVING_TO_THE_LEFT(false);
                engiPlayer.setMOVING_TO_THE_BOTTOM(true);
                break;

            case "right":
                engiPlayer.setMOVING_TO_THE_TOP(false);
                engiPlayer.setMOVING_TO_THE_RIGHT(true);
                engiPlayer.setMOVING_TO_THE_LEFT(false);
                engiPlayer.setMOVING_TO_THE_BOTTOM(false);
                break;

            case "left":
                engiPlayer.setMOVING_TO_THE_TOP(false);
                engiPlayer.setMOVING_TO_THE_RIGHT(false);
                engiPlayer.setMOVING_TO_THE_LEFT(true);
                engiPlayer.setMOVING_TO_THE_BOTTOM(false);
                break;

            default:
                engiPlayer.setMOVING_TO_THE_TOP(false);
                engiPlayer.setMOVING_TO_THE_RIGHT(false);
                engiPlayer.setMOVING_TO_THE_LEFT(false);
                engiPlayer.setMOVING_TO_THE_BOTTOM(false);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                chooseDirection(x, y);
                break;

            case MotionEvent.ACTION_MOVE:

                x = (int) motionEvent.getX();
                y = (int) motionEvent.getY();
                chooseDirection(x, y);
                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                
                engiPlayer.setMOVING_TO_THE_TOP(false);
                engiPlayer.setMOVING_TO_THE_RIGHT(false);
                engiPlayer.setMOVING_TO_THE_LEFT(false);
                engiPlayer.setMOVING_TO_THE_BOTTOM(false);

                break;
        }
        return true;

    }
}
