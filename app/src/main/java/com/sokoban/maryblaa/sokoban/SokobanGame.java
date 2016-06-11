package com.sokoban.maryblaa.sokoban;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.sokoban.maryblaa.sokoban.collision.AABB;
import com.sokoban.maryblaa.sokoban.collision.Point;
import com.sokoban.maryblaa.sokoban.game.Game;
import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.graphics.CompareFunction;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.SpriteFont;
import com.sokoban.maryblaa.sokoban.graphics.TextBuffer;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.input.InputEvent;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;
import com.sokoban.maryblaa.sokoban.math.Vector3;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    public static final String TAG = SokobanGame.class.getSimpleName();

    private Camera hudCamera, sceneCamera;
    private Mesh meshBall, meshPaddle;
    private Texture texBall, texPaddle;
    private Material matBall, matPaddle;
    private Matrix4x4 worldBall;
    private Matrix4x4[] worldPaddles;

    private SpriteFont fontTitle, fontMenu;
    private TextBuffer textTitle;
    private Matrix4x4 matTitle;
    private TextBuffer[] textMenu;
    private Matrix4x4[] matMenu;
    private boolean showMenu = false;
    private int screenHeight;
    private int screenWidth;
    private AABB[] aabbMenu;
    private float paddleTranslationX = 400f;
    private static final float paddleSize = 200f;
    private static final float ballSize = 70f;

    private float[] paddlePositions = new float[]{0, 0};
    private float[] paddleFingerPosition = new float[]{0, 0};
    private MediaPlayer bounce1;
    private MediaPlayer bounce2;
    private MediaPlayer ooooooooh;

    public SokobanGame(View view) {
        super(view);
    }

    enum MenuEntry {
        STARTGAME("Start Game"), OPTIONS("Options"), CREDITS("Credits"), QUIT("Quit");

        private final String menuTitle;

        MenuEntry(String menuTitle) {
            this.menuTitle = menuTitle;
        }
    }

    ;

    @Override
    public void initialize() {
        Matrix4x4 projection = new Matrix4x4();
        Matrix4x4 view = new Matrix4x4();

        projection = new Matrix4x4();
        projection.setOrthogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        hudCamera = new Camera();
        hudCamera.setProjection(projection);
        hudCamera.setView(view);

        projection = new Matrix4x4();
        projection.setOrthogonalProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 8f);
        view = new Matrix4x4();
        view.translateBy(0, -1, 0);
        sceneCamera = new Camera();
        sceneCamera.setProjection(projection);
        sceneCamera.setView(view);

        matBall = new Material();
        matBall.setAlphaTestFunction(CompareFunction.GREATER);
        matBall.setAlphaTestValue(0.9f);

        matPaddle = new Material();

        worldPaddles = new Matrix4x4[]{
                Matrix4x4.createTranslation(-paddleTranslationX, 0, 0).scale(paddleSize),
                Matrix4x4.createTranslation(paddleTranslationX, 0, 0).scale(paddleSize)
        };

        worldBall = new Matrix4x4();
        worldBall.scale(ballSize);
        worldBall.translateBy(0, 0, 0);

        bounce1 = MediaPlayer.create(context, R.raw.boing1);
        bounce2 = MediaPlayer.create(context, R.raw.boing2);
        ooooooooh = MediaPlayer.create(context, R.raw.ohoh);
    }

    private int frame = 0;

    @Override
    public void loadContent() {
        try {
            InputStream stream;

            stream = context.getAssets().open("ownBall.obj");
            meshBall = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("ball.jpg");
            texBall = graphicsDevice.createTexture(stream);
            matBall.setTexture(texBall);

            stream = context.getAssets().open("ownPaddle.obj");
            meshPaddle = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("woodSmall.png");
            texPaddle = graphicsDevice.createTexture(stream);
            matPaddle.setTexture(texPaddle);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        fontTitle = graphicsDevice.createSpriteFont(null, 150);
        fontMenu = graphicsDevice.createSpriteFont(null, 90);

        textTitle = graphicsDevice.createTextBuffer(fontTitle, 50);
        textMenu = new TextBuffer[]{
                graphicsDevice.createTextBuffer(fontMenu, 50),
                graphicsDevice.createTextBuffer(fontMenu, 50),
                graphicsDevice.createTextBuffer(fontMenu, 50),
                graphicsDevice.createTextBuffer(fontMenu, 50)
        };

        textTitle.setText("Sokoban");

        for (MenuEntry entrie : MenuEntry.values()) {
            textMenu[entrie.ordinal()].setText(entrie.menuTitle);
        }

        int dist = -120;

        matTitle = Matrix4x4.createTranslation(0, 30, 0);
        matMenu = new Matrix4x4[]{
                Matrix4x4.createTranslation(0, dist, 0),
                Matrix4x4.createTranslation(0, 2 * dist, 0),
                Matrix4x4.createTranslation(0, 3 * dist, 0),
                Matrix4x4.createTranslation(0, 4 * dist, 0)
        };

        aabbMenu = new AABB[]{
                new AABB(0, dist, 1000, -dist, MenuEntry.STARTGAME),
                new AABB(0, dist * 2, 1000, -dist, MenuEntry.OPTIONS),
                new AABB(0, dist * 3, 1000, -dist, MenuEntry.CREDITS),
                new AABB(0, dist * 4, 1000, -dist, MenuEntry.QUIT)
        };
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public void update(float deltaSeconds) {
        InputEvent inputEvent = inputSystem.peekEvent();

        while (inputEvent != null) {

            switch (inputEvent.getDevice()) {
                case KEYBOARD:
                    Log.d(TAG, "KEYBOARD");
                    Log.d(TAG, "" + inputEvent.getAction().toString());
                    switch (inputEvent.getAction()) {

                        case DOWN:
                            switch (inputEvent.getKeycode()) {
                                case KeyEvent.KEYCODE_MENU:
                                    showMenu = !showMenu;
                                    break;
                                case KeyEvent.KEYCODE_BACK:
                                    onBackPressed();
                                    break;
                            }
                            break;
                    }
//                    Log.d(TAG, "" + inputEvent.getDevice());
                    break;
                case ROTATION:
//                    Log.d(TAG, "ROTATION");
                    break;
                case TOUCHSCREEN:

                    Vector3 screenTouchPosition = new Vector3(
                            (inputEvent.getValues()[0] / (screenWidth / 2) - 1),
                            -(inputEvent.getValues()[1] / (screenHeight / 2) - 1),
                            0);

                    Vector3 worldTouchPosition = hudCamera.unproject(screenTouchPosition, 1);


                    Point touchPoint = new Point(worldTouchPosition.getX(), worldTouchPosition.getY());

                    switch (inputEvent.getAction()) {
                        case DOWN:

                            if (showMenu) {
                                detectMenuTouch(touchPoint);
                            } else {
                                detectPaddleTouchDown(touchPoint);
                            }


                            break;
                        case MOVE:
                            if (!showMenu) {
                                detectPaddleTouchMove(touchPoint);
                            }

                    }


            }
            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();

        }
    }

    private void detectPaddleTouchDown(Point touchPoint) {


        float x = touchPoint.getPosition().getX();
        float y = touchPoint.getPosition().getY();

        int index = x < 0 ? 0 : 1;
        paddleFingerPosition[index] = y - paddlePositions[index];

    }

    private void detectPaddleTouchMove(Point touchPoint) {


        float x = touchPoint.getPosition().getX();
        float y = touchPoint.getPosition().getY();

        int index = x < 0 ? 0 : 1;
        Log.d(TAG, "X is " + x + ", y is " + y + ", Index is " + index);

        if(Math.abs(paddleFingerPosition[index]) < paddleSize) {
            float newPosition = paddlePositions[index] + y - (paddlePositions[index] + paddleFingerPosition[index]);
            if(Math.abs(newPosition) < screenHeight/2 - paddleSize) {
                paddlePositions[index] = newPosition;
                worldPaddles[index] = Matrix4x4.createTranslation(
                        (index == 0 ? -1 : 1) * paddleTranslationX,
                        paddlePositions[index], 0
                ).scale(paddleSize);
            }
        }

    }


    private void detectMenuTouch(Point touchPoint) {
        for (AABB aabb : aabbMenu) {
            if (touchPoint.intersects(aabb)) {
                MenuEntry entry = (MenuEntry) aabb.getRelatedObj();
                parseMenuEntry(entry);
            }

        }
    }

    private void parseMenuEntry(MenuEntry entry) {
        switch (entry) {
            case STARTGAME:
                showMenu = !showMenu;
                break;
            case OPTIONS:
                break;
            case CREDITS:
                break;
            case QUIT:
                ((Activity) view.getContext()).finish();
                break;
        }

    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        graphicsDevice.setCamera(sceneCamera);

        if (showMenu) {
            // Text auf dem HUD zeichnen
            drawMenu();
        } else {
            drawGame();


        }
    }

    float maxPosition = paddleTranslationX - ballSize;
    float ballPositionX = 0;
    float ballPositionY = 0;
    float ballAngle = getBallStartPosition();

    private float getBallStartPosition() {
        double random;
        do {
            random = Math.random() * 360;
        }
        while(!(random > 85 * Math.PI && random > 95) &&
              !(random > 265 && random > 275));

        return (float) random;
    }

    float speed = 10f;

    private void drawGame() {
        frame++;

        ballPositionX += speed * Math.sin(Math.toRadians(ballAngle));
        ballPositionY += speed * Math.cos(Math.toRadians(ballAngle));
        if (ballPositionX > maxPosition) {
            // right Paddle


            if(bounce1.isPlaying()) {
                bounce1.seekTo(1);
            }
            bounce1.start();
        } else if(ballPositionX < -maxPosition) {
            // left Paddle
            if(bounce2.isPlaying()) {
                bounce2.seekTo(1);
            }
            bounce2.start();
        }

        float tmp = ballAngle;

        if(Math.abs(ballPositionY) > screenHeight / 2 - ballSize) {
            if(ballAngle > 0 * Math.PI && ballAngle < Math.PI) {
                ballAngle = (float) ((90 + (90 - ballAngle)) % 360);
                Log.d(TAG, "vertikale Kante: von " + tmp + " nach " + ballAngle);

            } else {
                ballAngle = (float) ((270 + (270 - ballAngle)) % 360);
                Log.d(TAG, "vertikale Kante: von " + tmp + " nach " + ballAngle);

            }
        } else {
            if(Math.abs(ballPositionX) > maxPosition) {
                if(ballAngle > 90 && ballAngle < 270) {
                    ballAngle = (float) ((180 + (180 - ballAngle)) % 360);
                    Log.d(TAG, "horizontale Kante: von " + tmp + " nach " + ballAngle);

                } else {
                    ballAngle = (float) ((360 - ballAngle) % 360);
                    Log.d(TAG, "horizontale Kante: von " + tmp + " nach " + ballAngle);
                }
            }
        }


        // Ball zeichnen
        worldBall = Matrix4x4.createTranslation(ballPositionX, ballPositionY, 0).scale(ballSize);
        renderer.drawMesh(meshBall, matBall, worldBall);

        // Paddles zeichnen
        for (Matrix4x4 worldPaddle : worldPaddles)
            renderer.drawMesh(meshPaddle, matPaddle, worldPaddle);
    }

    private void drawMenu() {
        graphicsDevice.setCamera(hudCamera);
        renderer.drawText(textTitle, matTitle);
//            renderer.drawRect(textTitle.getBounds(),textTitle.getSpriteFont().getMaterial(), matTitle);


        for (int i = 0; i < textMenu.length; ++i)
            renderer.drawText(textMenu[i], matMenu[i]);
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / (float) height;
        Matrix4x4 projection;

        projection = new Matrix4x4();
        projection.setOrthogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        hudCamera.setProjection(projection);

        projection = new Matrix4x4();
        projection.setOrthogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        sceneCamera.setProjection(projection);

        matTitle.setIdentity();
        matTitle.translateBy(-width / 2 + 35, height / 2 - 150, 0);
    }

    public boolean onBackPressed() {
        if (showMenu) {
            parseMenuEntry(MenuEntry.QUIT);
        } else {
            showMenu = !showMenu;
        }

        return true;
    }


}
