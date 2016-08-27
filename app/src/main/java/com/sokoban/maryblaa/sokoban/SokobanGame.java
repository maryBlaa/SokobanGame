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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    public static final String TAG = SokobanGame.class.getSimpleName();

    private static final int MIN_DELAY_UNTIL_POWERUP = 3000;
    private static final int MIN_DELAY_BETWEEN_POWERUPS = 1500;
    private static final int MAX_DELAY_BETWEEN_POWERUPS = 3000;
    // base speed: 2000ms for largest width
    public static int BASE_SPEED = 2000;
    public int MAX_MATCHPOINTS = 5;

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
    public int screenHeight;
    public int screenWidth;
    public int largestWidth;
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

    private static Screen screen = Screen.MENU;
    private static GameState gameState = GameState.PRESTART;

    public List<AbstractPowerUp> powerupsActive = new ArrayList<>();

    public AbstractPowerUp visiblePowerup = null;
    private int spawnDeltaTime = 0;

    public HashMap<AbstractPowerUp.PowerupType, Mesh> powerupMeshes;
    public HashMap<AbstractPowerUp.PowerupType, Material> powerupMaterials;


    float maxPosition = paddleTranslationX - ballSize;
    public float ballPositionX = 0;
    public float ballPositionY = 0;
    public float ballAngle = getBallStartPosition();
    boolean collisionDetectionActive = true;

    public boolean isBallBlinking = false;
    public int blinkStartDeltaTime = 0;
    private SpriteFont fontTimeCounter;
    private TextBuffer textTimeCounter;

    private SpriteFont fontScore;
    private TextBuffer textScore;

    private SpriteFont fontGameover;
    private TextBuffer textGameover;
    private Matrix4x4 posGameover;
    private SpriteFont fontP;
    private TextBuffer textP1;
    private Matrix4x4 posP1;
    private TextBuffer textP2;
    private Matrix4x4 posP2;


    public int frame = 0;
    public double speedVariation = 1;

    public long startTime;
    public int currentDeltaTime;
    private int pauseDeltaTime;
    private double fpms = 0.03;

    public int scoreP1 = 0;
    public int scoreP2 = 0;

    private enum GameState {
        PRESTART, PLAYING, PAUSED, MATCHPOINT, GAMEOVER;

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
            return this != RESUME || gameState == GameState.PAUSED;
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

        fontTimeCounter = graphicsDevice.createSpriteFont(null, 120);
        textTimeCounter = graphicsDevice.createTextBuffer(fontTimeCounter, 16);

        fontScore = graphicsDevice.createSpriteFont(null, 120);
        textScore = graphicsDevice.createTextBuffer(fontScore, 16);

        fontGameover = graphicsDevice.createSpriteFont(null, 150);
        textGameover = graphicsDevice.createTextBuffer(fontGameover, 90);
        fontP = graphicsDevice.createSpriteFont(null, 200);
        textP1 = graphicsDevice.createTextBuffer(fontP, 90);
        textP2 = graphicsDevice.createTextBuffer(fontP, 90);

        posGameover = Matrix4x4.createTranslation(-367, 425, 0);
        posP1 = Matrix4x4.createTranslation(-375, 0, 0);
        posP2 = Matrix4x4.createTranslation(270, 0, 0);

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
        largestWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
        paddleTranslationX = width / 2 - paddleSizeDefault / 5;
        resetPositions();
    }

    @Override
    public void eventUpdate(float deltaSeconds) {
        InputEvent inputEvent = inputSystem.peekEvent();

        while (inputEvent != null) {

            switch (inputEvent.getDevice()) {
                case KEYBOARD:
                    switch (inputEvent.getAction()) {

                        case DOWN:
                            switch (inputEvent.getKeycode()) {
                                case KeyEvent.KEYCODE_MENU:
                                    screen = Screen.MENU;
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

                            if (screen == Screen.MENU) {
                                detectMenuTouch(touchPoint);
                            } else {
                                detectPaddleTouchDown(touchPoint);
                            }


                            break;
                        case MOVE:
                            if (screen == Screen.GAME) {
                                detectPaddleTouchMove(touchPoint);
                            }

                    }


            }
            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();

        }
    }

    private void detectPaddleTouchDown(Point touchPoint) {

        switch (gameState) {

            case PRESTART:
                gameState = GameState.PLAYING;
                resetDeltaTimeCounter();
                spawnDeltaTime = 0;
                break;
            case PLAYING:
                float x = touchPoint.getPosition().getX();
                float y = touchPoint.getPosition().getY();

                int index = x < 0 ? 0 : 1;
                paddleFingerPosition[index] = y - paddlePositions[index];
                break;
            case PAUSED:
                gameState = GameState.PLAYING;
                pauseDeltaTimeCounter();
                break;
            case MATCHPOINT:
                if (scoreP1 < MAX_MATCHPOINTS && scoreP2 < MAX_MATCHPOINTS) {
                    gameState = GameState.PLAYING;
                    resetDeltaTimeCounter();
                    spawnDeltaTime = 0;
                    break;
                } else {
                    gameState = GameState.GAMEOVER;
                    break;
                }
            case GAMEOVER:
                int winner;
                if (scoreP1 > scoreP2) {
                    winner = 1;
                } else {
                    winner = 2;
                }
                resetScore();
                screen = Screen.MENU;
                break;
        }


    }

    private void resetDeltaTimeCounter() {
        startTime = Calendar.getInstance().getTimeInMillis();
        frame = 0;
    }

    private void pauseDeltaTimeCounter() {
        startTime = Calendar.getInstance().getTimeInMillis() - pauseDeltaTime;
        pauseDeltaTime = 0;
    }

    private void detectPaddleTouchMove(Point touchPoint) {

        if (gameState != GameState.PLAYING) {
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
                screen = Screen.GAME;
                gameState = GameState.PAUSED;
                break;
            case NEWGAME:
                resetPositions();
                resetScore();
                screen = Screen.GAME;
                gameState = GameState.PRESTART;
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

    private void resetScore() {
        scoreP1 = 0;
        scoreP2 = 0;
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        graphicsDevice.setCamera(sceneCamera);

        switch (screen) {
            case MENU:
                drawMenu();
                break;
            case GAME:
                drawGame();
                break;
            case HIGHSCORE:
                break;
            case CREDITS:
                break;
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

    private int getDeltaTime() {
        return (int) (Calendar.getInstance().getTimeInMillis() - startTime);
    }


    private void drawGame() {
        currentDeltaTime = getDeltaTime();

        if (gameState == GameState.PLAYING) {
            frame++;

            if (currentDeltaTime > 0) {
                fpms = frame / ((double) currentDeltaTime);
            }

            textTimeCounter.setText((currentDeltaTime / 1000) + "s, " + (int) (fpms * 1000) + " fps");
            renderer.drawText(textTimeCounter, posTitle);
        }

        float distance;

        // Collisiondetection Paddle Ball
        if (gameState == GameState.PLAYING) {
            double speed = speedVariation * (largestWidth / (fpms * BASE_SPEED));
            ballPositionX += speed * Math.sin(Math.toRadians(ballAngle));
            ballPositionY += speed * Math.cos(Math.toRadians(ballAngle));
            if (ballPositionX > maxPosition + paddleSizes[1] * 0.2) {
                // right Paddle
                distance = ballPositionY - paddlePositions[1];
                if (Math.abs(distance) > paddleSizes[1]) {
                    scoreP1 += 1;
                    setMatchpointState();
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
                    scoreP2 += 1;
                    setMatchpointState();
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
            drawPowerUp();
            removeExpiredPowerUp();
        } else if (gameState == GameState.GAMEOVER) {
            drawGameover();
        } else {
            textScore.setText(scoreP1 + " : " + scoreP2);
            renderer.drawText(textScore, posTitle);
        }

        // draw Ball
        boolean shouldDrawBall = !isBallBlinking || ((currentDeltaTime - blinkStartDeltaTime) % Blink.BLINK_DURATION_MS) > Blink.BLINK_DURATION_MS/2;
        if(shouldDrawBall) {
            worldBall = Matrix4x4.createTranslation(ballPositionX, ballPositionY, 0).scale(ballSize);
            renderer.drawMesh(meshBall, materialBall, worldBall);
        }

        // draw Paddles
        for (Matrix4x4 worldPaddle : worldPaddles) {
            renderer.drawMesh(meshPaddle, materialPaddle, worldPaddle);
        }
    }

    private void removeExpiredPowerUp() {
        AbstractPowerUp toBeDeleted = null;
        for (AbstractPowerUp abstractPowerUp : powerupsActive) {
            if(abstractPowerUp.powerDownDeltaTime <= currentDeltaTime) {
                abstractPowerUp.undoAction();
                toBeDeleted = abstractPowerUp;
            }
        }
        if(toBeDeleted != null) {
            powerupsActive.remove(toBeDeleted);
        }
    }

    private void drawPowerUp() {
        if (shouldSpawn()) {
            visiblePowerup = AbstractPowerUp.spawn(this);
        }
        if (visiblePowerup != null) {

            if (visiblePowerup.catchPowerUp()) {
                powerupsActive.add(visiblePowerup);
                visiblePowerup.performAction();
                visiblePowerup = null;
            } else {
                if (visiblePowerup.despawnDeltaTime >= currentDeltaTime) {
                    visiblePowerup.draw();
                } else {
                    visiblePowerup = null;
                }
            }

        }
    }

    private void drawGameover() {
        textGameover.setText("Game Over");
        textP1.setText("" + scoreP1);
        textP2.setText("" + scoreP2);
        float[] bounds = textP1.getBounds();
        Log.d(TAG, "breite: " + bounds[0] + "    höhe: " + bounds[1]);
        renderer.drawText(textGameover, posGameover);
        renderer.drawText(textP1, posP1);
        renderer.drawText(textP2, posP2);
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

    private void setMatchpointState() {
        if (ooooooooh.isPlaying()) {
            ooooooooh.seekTo(0);
        } else {
            ooooooooh.start();
        }
        gameState = GameState.MATCHPOINT;
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
        if (screen == Screen.MENU) {
            parseMenuEntry(MenuEntry.QUIT);
        } else {
            pauseDeltaTime = getDeltaTime();
            gameState = GameState.PAUSED;
            screen = Screen.MENU;
        }

        return true;
    }

    public boolean shouldSpawn() {
        if (currentDeltaTime <= MIN_DELAY_UNTIL_POWERUP) {
            return false;
        }
        if (visiblePowerup != null) {
            return false;
        }
        if (spawnDeltaTime == 0) {
            spawnDeltaTime = MathHelper.randomInt(currentDeltaTime + MIN_DELAY_BETWEEN_POWERUPS, currentDeltaTime + MAX_DELAY_BETWEEN_POWERUPS);
            return false;
        }
        if (currentDeltaTime >= spawnDeltaTime) {
            spawnDeltaTime = 0;
            return true;
        }
        return false;
    }

}
