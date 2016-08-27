package com.sokoban.maryblaa.sokoban;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.sokoban.maryblaa.sokoban.collision.AABB;
import com.sokoban.maryblaa.sokoban.collision.MathHelper;
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
import com.sokoban.maryblaa.sokoban.powerups.AbstractPowerUp;
import com.sokoban.maryblaa.sokoban.powerups.Blink;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    public static final String TAG = SokobanGame.class.getSimpleName();

    private Camera hudCamera, sceneCamera;
    private Mesh meshBall, meshPaddle;
    private Texture texBall, texPaddle;
    private Material materialBall, materialPaddle;
    private Matrix4x4 worldBall;
    private Matrix4x4[] worldPaddles;

    private SpriteFont fontTitle, fontMenu;
    private TextBuffer textTitle;
    private Matrix4x4 posTitle;
    private TextBuffer[] textMenu;
    private Matrix4x4[] posMenu;
    private boolean showMenu = true;
    public int screenHeight;
    public int screenWidth;
    private AABB[] aabbMenu;
    private float paddleTranslationX = 400f;
    public static float paddleSizeDefault = 200f;
    public float[] paddleSizes = new float[]{paddleSizeDefault, paddleSizeDefault};
    public boolean[] paddleInverse = new boolean[]{false, false};
    public float ballSize = 70f;

    private float[] paddlePositions = new float[]{0, 0};
    private float[] paddleFingerPosition = new float[]{0, 0};
    private MediaPlayer bounce1;
    private MediaPlayer bounce2;
    private MediaPlayer bounce3;
    private MediaPlayer ooooooooh;
    private MediaPlayer miau;
    private MediaPlayer intro;

    private boolean introPlayed = false;

    private static GameState state = GameState.PRESTART;

    public List<AbstractPowerUp> powerupsActive = new ArrayList<>();

    public AbstractPowerUp visiblePowerup = null;
    private int spawnFrame = 0;

    public HashMap<AbstractPowerUp.PowerupType, Mesh> powerupMeshes;
    public HashMap<AbstractPowerUp.PowerupType, Material> powerupMaterials;


    float maxPosition = paddleTranslationX - ballSize;
    public float ballPositionX = 0;
    public float ballPositionY = 0;
    public float ballAngle = getBallStartPosition();
    boolean collisionDetectionActive = true;

    public boolean isBallBlinking = false;
    public int blinkStartFrame = 0;

    private enum GameState {
        PRESTART, PLAYING, PAUSED, GAMEOVER;

    }

    private enum Screen {
        MENU, GAME, HIGHSCORE, CREDITS

    }

    enum MenuEntry {
        RESUME("Resume"), NEWGAME("New Game"), OPTIONS("Options"), HIGHSCORE("Highscore"), CREDITS("Credits"), QUIT("Quit");

        private final String menuTitle;

        MenuEntry(String menuTitle) {
            this.menuTitle = menuTitle;
        }

        private boolean isVisible() {
            return this != RESUME || state == GameState.PAUSED;
        }
    }

    public SokobanGame(View view) {
        super(view);
    }

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

        materialBall = new Material();
        materialBall.setAlphaTestFunction(CompareFunction.GREATER);
        materialBall.setAlphaTestValue(0.9f);

        materialPaddle = new Material();

        calculateWorldPaddles();

        worldBall = new Matrix4x4();
        worldBall.scale(ballSize);
        worldBall.translateBy(0, 0, 0);

        bounce1 = MediaPlayer.create(context, R.raw.boing1);
        bounce2 = MediaPlayer.create(context, R.raw.boing2);
        bounce3 = MediaPlayer.create(context, R.raw.boing3);
        intro = MediaPlayer.create(context, R.raw.intro);
        miau = MediaPlayer.create(context, R.raw.miau);
        ooooooooh = MediaPlayer.create(context, R.raw.ohoh);

        powerupMaterials = new HashMap<>();
        powerupMeshes = new HashMap<>();

        for (AbstractPowerUp.PowerupType powerupType : AbstractPowerUp.PowerupType.values()) {

            Material materialPowerUp = new Material();
            materialPowerUp.setAlphaTestFunction(CompareFunction.GREATER);
            materialPowerUp.setAlphaTestValue(0.9f);

            InputStream stream;
            Mesh mesh = null;

            try {
                stream = context.getAssets().open(powerupType.meshSrc);
                mesh = Mesh.loadFromOBJ(stream);

                stream = context.getAssets().open(powerupType.materialSrc);
                Texture texPowerUp = graphicsDevice.createTexture(stream);
                materialPowerUp.setTexture(texPowerUp);

            } catch (IOException e) {
                e.printStackTrace();
            }

            powerupMaterials.put(powerupType, materialPowerUp);
            powerupMeshes.put(powerupType, mesh);
        }
    }

    public int frame = 0;

    @Override
    public void loadContent() {
        try {
            InputStream stream;

            stream = context.getAssets().open("ownBall.obj");
            meshBall = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("ball.jpg");
            texBall = graphicsDevice.createTexture(stream);
            materialBall.setTexture(texBall);

            stream = context.getAssets().open("ownPaddle.obj");
            meshPaddle = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("woodSmall.png");
            texPaddle = graphicsDevice.createTexture(stream);
            materialPaddle.setTexture(texPaddle);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        fontTitle = graphicsDevice.createSpriteFont(null, 150);
        fontMenu = graphicsDevice.createSpriteFont(null, 90);

        textTitle = graphicsDevice.createTextBuffer(fontTitle, 50);
        textMenu = new TextBuffer[MenuEntry.values().length];

        for (int i = 0; i < MenuEntry.values().length; i++) {
            textMenu[i] = graphicsDevice.createTextBuffer(fontMenu, 50);
        }

        textTitle.setText("Sokoban");

        for (MenuEntry entrie : MenuEntry.values()) {
            textMenu[entrie.ordinal()].setText(entrie.menuTitle);
        }

        int dist = -120;

        posTitle = Matrix4x4.createTranslation(0, 30, 0);
        posMenu = new Matrix4x4[MenuEntry.values().length];

        for (int i = 0; i < MenuEntry.values().length; i++) {
            posMenu[i] = Matrix4x4.createTranslation(0, (i + 1) * dist, 0);
        }

        aabbMenu = new AABB[MenuEntry.values().length];
        for (int i = 0; i < MenuEntry.values().length; i++) {
            aabbMenu[i] = new AABB(0, (i + 1) * dist, 1000, -dist, MenuEntry.values()[i]);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        screenWidth = width;
        screenHeight = height;
        paddleTranslationX = width / 2 - paddleSizeDefault / 5;
        resetPositions();
    }

    @Override
    public void eventUpdate(float deltaSeconds) {
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

        switch (state) {

            case PRESTART:
                state = GameState.PLAYING;
                frame = 0;
                spawnFrame = 0;
                break;
            case PLAYING:
                float x = touchPoint.getPosition().getX();
                float y = touchPoint.getPosition().getY();

                int index = x < 0 ? 0 : 1;
                paddleFingerPosition[index] = y - paddlePositions[index];
                break;
            case PAUSED:
                state = GameState.PLAYING;
                break;
            case GAMEOVER:
                state = GameState.PLAYING;
                break;
        }


    }

    private void detectPaddleTouchMove(Point touchPoint) {

        if (state != GameState.PLAYING) {
            return;
        }


        float x = touchPoint.getPosition().getX();
        float y = touchPoint.getPosition().getY();

        int index = x < 0 ? 0 : 1;

        if (Math.abs(paddleFingerPosition[index]) < paddleSizes[index]) {
            float newPaddlePosition;
            if(paddleInverse[index]) {
                newPaddlePosition = paddlePositions[index] - y - (paddlePositions[index] + paddleFingerPosition[index]);
            } else {
                newPaddlePosition = paddlePositions[index] + y - (paddlePositions[index] + paddleFingerPosition[index]);
            }
            if (Math.abs(newPaddlePosition) < screenHeight / 2 - paddleSizes[index]) {
                paddlePositions[index] = newPaddlePosition;
                calculateWorldPaddle(index);
            }
        }

    }


    private void detectMenuTouch(Point touchPoint) {
        for (AABB aabb : aabbMenu) {
            if (touchPoint.intersects(aabb)) {
                MenuEntry entry = (MenuEntry) aabb.getRelatedObj();
                if (entry.isVisible()) {
                    parseMenuEntry(entry);
                }
            }
        }
    }

    private void parseMenuEntry(MenuEntry entry) {
        if (intro.isPlaying()) {
            intro.stop();
        }
        switch (entry) {
            case RESUME:
                showMenu = !showMenu;
                state = GameState.PRESTART;
                break;
            case NEWGAME:
                resetPositions();
                showMenu = !showMenu;
                miau.start();
                break;
            case OPTIONS:
                break;
            case HIGHSCORE:
                break;
            case CREDITS:
                break;
            case QUIT:
                MediaPlayer.create(context, R.raw.exit).start();
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

    private float getBallStartPosition() {
        double random;
        do {
            random = Math.random() * 360;
        }
        while (!(random > 65 && random < 115) &&
                !(random > 245 && random < 295));

        return (float) random;
    }

    public float speed = 15f;

    private void drawGame() {
        if (state == GameState.PLAYING) {
            frame++;
        }
//        System.out.println(frame);

        float distance;

        // Collisiondetection Paddle Ball
        if (state == GameState.PLAYING) {
            ballPositionX += speed * Math.sin(Math.toRadians(ballAngle));
            ballPositionY += speed * Math.cos(Math.toRadians(ballAngle));
            if (ballPositionX > maxPosition + paddleSizes[1] * 0.2) {
                // right Paddle
                distance = ballPositionY - paddlePositions[1];
                if (Math.abs(distance) > paddleSizes[1]) {
                    setGameoverState();
                    return;
                } else {
                    turnAround(distance, false);
                }
            } else if (ballPositionX > maxPosition) {
                distance = ballPositionY - paddlePositions[1];
                if (Math.abs(distance) <= paddleSizes[1]) {
                    turnAround(distance, false);
                }
            } else if (ballPositionX < -maxPosition - paddleSizes[0] * 0.2) {
                // left Paddle
                distance = ballPositionY - paddlePositions[0];
                if (Math.abs(distance) > paddleSizes[0]) {
                    setGameoverState();
                    return;
                } else {
                    turnAround(distance, true);
                }
            } else if (ballPositionX < -maxPosition) {

                distance = ballPositionY - paddlePositions[0];
                if (Math.abs(distance) <= paddleSizes[0]) {
                    turnAround(distance, true);
                }
            } else {
                collisionDetectionActive = true;
            }

            float tmp = ballAngle;

            if (Math.abs(ballPositionY) > screenHeight / 2 - ballSize) {
                if (ballAngle > 0 * Math.PI && ballAngle < Math.PI) {
                    ballAngle = (float) ((90 + (90 - ballAngle)) % 360);
                    Log.d(TAG, "vertikale Kante: von " + tmp + " nach " + ballAngle);

                } else {
                    ballAngle = (float) ((270 + (270 - ballAngle)) % 360);
                    Log.d(TAG, "vertikale Kante: von " + tmp + " nach " + ballAngle);

                }

                if (bounce3.isPlaying()) {
                    bounce3.seekTo(0);
                } else {
                    bounce3.start();
                }
            }
        }

        // draw PowerUp
        if (shouldSpawn()) {
            visiblePowerup = AbstractPowerUp.spawn(this);
        }
        if (visiblePowerup != null) {

            if (visiblePowerup.catchPowerUp()) {
                powerupsActive.add(visiblePowerup);
                visiblePowerup.performAction();
                visiblePowerup = null;
            } else {
                if (visiblePowerup.despawnFrame >= frame) {
                    visiblePowerup.draw();
                } else {
                    visiblePowerup = null;
                }
            }

        }

        // remove expired powerups
        AbstractPowerUp toBeDeleted = null;
        for (AbstractPowerUp abstractPowerUp : powerupsActive) {
            if(abstractPowerUp.powerDownFrame <= frame) {
                abstractPowerUp.undoAction();
                toBeDeleted = abstractPowerUp;
            }
        }
        if(toBeDeleted != null) {
            powerupsActive.remove(toBeDeleted);
        }

        // draw Ball
        boolean shouldDrawBall = !isBallBlinking || ((frame - blinkStartFrame) % (2 * Blink.BLINK_FRAME_RATE)) > 14;
        if(shouldDrawBall) {
            worldBall = Matrix4x4.createTranslation(ballPositionX, ballPositionY, 0).scale(ballSize);
            renderer.drawMesh(meshBall, materialBall, worldBall);
        }


        // draw Paddles
        for (Matrix4x4 worldPaddle : worldPaddles) {
            renderer.drawMesh(meshPaddle, materialPaddle, worldPaddle);
        }
    }

    private void turnAround(float distance, boolean isLeft) {
        if (!collisionDetectionActive) {
            return;
        }
        float percentage = distance / paddleSizes[isLeft ? 0 : 1];
        float newAngle = percentage * 65;
        if (!isLeft) {
            ballAngle = 270 + newAngle;
        } else {
            ballAngle = 90 - newAngle;
        }
        collisionDetectionActive = false;

        MediaPlayer player = isLeft ? bounce1 : bounce2;
        if (player.isPlaying()) {
            player.seekTo(0);
        } else {
            player.start();
        }
    }

    private void setGameoverState() {
        if (ooooooooh.isPlaying()) {
            ooooooooh.seekTo(0);
        } else {
            ooooooooh.start();
        }
        state = GameState.GAMEOVER;
        resetPositions();
    }

    private void resetPositions() {
        paddlePositions[0] = 0;
        paddlePositions[1] = 0;
        paddleFingerPosition[0] = 0;
        paddleFingerPosition[1] = 0;
        ballPositionY = 0;
        ballPositionX = 0;
        isBallBlinking = false;
        ballAngle = getBallStartPosition();
        maxPosition = paddleTranslationX - ballSize;

        for (AbstractPowerUp abstractPowerUp : powerupsActive) {
            abstractPowerUp.undoAction();
        }
        powerupsActive.clear();
        visiblePowerup = null;
        calculateWorldPaddles();
    }

    public void calculateWorldPaddles() {
        worldPaddles = new Matrix4x4[]{
                Matrix4x4.createTranslation(-paddleTranslationX, 0, 0).scale(paddleSizeDefault, paddleSizes[0], paddleSizeDefault),
                Matrix4x4.createTranslation(paddleTranslationX, 0, 0).scale(paddleSizeDefault, paddleSizes[1], paddleSizeDefault)
        };
    }

    public void calculateWorldPaddle(int index) {
        worldPaddles[index] = Matrix4x4.createTranslation(
                (index == 0 ? -1 : 1) * paddleTranslationX,
                paddlePositions[index], 0
        ).scale(paddleSizeDefault, paddleSizes[index], paddleSizeDefault);
    }

    private void drawMenu() {
        if (!introPlayed) {
            introPlayed = true;
            intro.start();
        }
        graphicsDevice.setCamera(hudCamera);
        renderer.drawText(textTitle, posTitle);
//            renderer.drawRect(textTitle.getBounds(),textTitle.getSpriteFont().getMaterial(), posTitle);


        for (int i = 0; i < textMenu.length; ++i)
            if (MenuEntry.values()[i].isVisible()) {
                renderer.drawText(textMenu[i], posMenu[i]);
            }
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

        posTitle.setIdentity();
        posTitle.translateBy(-width / 2 + 35, height / 2 - 150, 0);
    }

    public boolean onBackPressed() {
        if (showMenu) {
            parseMenuEntry(MenuEntry.QUIT);
        } else {
            state = GameState.PAUSED;
            showMenu = true;
        }

        return true;
    }

    public boolean shouldSpawn() {
        if (frame <= 150) {
            return false;
        }
        if (visiblePowerup != null) {
            return false;
        }
        if (spawnFrame == 0) {
            spawnFrame = MathHelper.randomInt(frame + 1, frame + 150);
            return false;
        }
        if (frame == spawnFrame) {
            spawnFrame = 0;
            return true;
        }
        return false;
    }

}
