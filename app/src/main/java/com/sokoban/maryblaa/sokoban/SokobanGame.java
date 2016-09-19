package com.sokoban.maryblaa.sokoban;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
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
import com.sokoban.maryblaa.sokoban.objects.Ball;
import com.sokoban.maryblaa.sokoban.powerups.AbstractPowerUp;
import com.sokoban.maryblaa.sokoban.utils.JSONSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class SokobanGame extends Game {

    public static final String TAG = SokobanGame.class.getSimpleName();

    private static final int MIN_DELAY_UNTIL_POWERUP = 3000;
    private static final int MIN_DELAY_BETWEEN_POWERUPS = 1500;
    private static final int MAX_DELAY_BETWEEN_POWERUPS = 3000;
    private static final int AI_REACTION_TIME = 300;
    private static final double AI_MAX_MOVEMENT = 1;
    public int MAX_MATCHPOINTS = 2;

    private Camera hudCamera, sceneCamera;

    //Balls
    public Mesh meshBall;
    public Texture texBall;
    public Material materialBall;
    public ArrayList<Ball> balls = new ArrayList<>();
    public ArrayList<Ball> toBeDeletedBalls = new ArrayList<>();
    public ArrayList<Ball> toBeAddedBalls = new ArrayList<>();

    //Paddle
    private Mesh meshPaddle;
    private Texture texPaddle;
    private Material materialPaddle;
    private Matrix4x4[] worldPaddles;

    private SpriteFont fontTitle, fontMenu;
    private TextBuffer textTitle;
    private Matrix4x4 posTitle;
    private TextBuffer[] textMenu;
    private TextBuffer[] textHighscore;
    private Matrix4x4[] posMenu;
    private Matrix4x4[] posHighscore;
    public int screenHeight;
    public int screenWidth;
    public int largestWidth;
    private AABB[] aabbMenu;
    private float paddleTranslationX = 400f;
    public static float paddleSizeDefault = 200f;
    public float[] paddleSizes = new float[]{paddleSizeDefault, paddleSizeDefault};
    public boolean[] paddleInverse = new boolean[]{false, false};

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
    public static GameState gameState = GameState.PRESTART;

    public List<AbstractPowerUp> powerupsActive = new ArrayList<>();

    public AbstractPowerUp visiblePowerup = null;
    private int spawnDeltaTime = 0;

    public HashMap<AbstractPowerUp.PowerupType, Mesh> powerupMeshes;
    public HashMap<AbstractPowerUp.PowerupType, Material> powerupMaterials;


    float maxPosition;
    boolean collisionDetectionActive = true;

    private SpriteFont fontTimeCounter;
    private TextBuffer textTimeCounter;

    public boolean isAIGame = false;

    //Score
    private SpriteFont fontScore;
    private TextBuffer textScore;

    //Gameover
    private SpriteFont fontGameover;
    private TextBuffer textGameover;
    private Matrix4x4 posGameover;
    private SpriteFont fontP;
    private TextBuffer textP1;
    private Matrix4x4 posP1;
    private TextBuffer textP2;
    private Matrix4x4 posP2;

    //Options
    private SpriteFont fontOptions;
    private TextBuffer textOptions;
    private Matrix4x4 posOptions;
    private TextBuffer textPlus;
    private Matrix4x4 posPlus;
    private TextBuffer textMinus;
    private Matrix4x4 posMinus;

    // Credits
    private SpriteFont fontCredits;
    private TextBuffer textCredits1;
    private Matrix4x4 posCredits1;
    private TextBuffer textCredits2;
    private Matrix4x4 posCredits2;
    private TextBuffer textCredits3;
    private Matrix4x4 posCredits3;
    private TextBuffer textCredits4;
    private Matrix4x4 posCredits4;

    public int frame = 0;

    public long startTime;
    public int currentDeltaTime;
    private int pauseDeltaTime;
    public double fpms = 0.03;

    public String nameP1 = "Mary";
    public String nameP2 = "Nick";
    public int scoreP1 = 0;
    public int scoreP2 = 0;
    private int scoreTime = 0;
    private AABB aabbplus;
    private AABB aabbminus;
//    private long reactionTime;

    public enum GameState {
        PRESTART, PLAYING, PAUSED, MATCHPOINT, GAMEOVER

    }

    private enum Screen {
        MENU, GAME, OPTIONS, HIGHSCORE, CREDITS

    }

    enum MenuEntry {
        RESUME("Resume"), AIGAME("AI Game"), VERSUS("Versus"), OPTIONS("Options"), HIGHSCORE("Highscore"), CREDITS("Credits"), QUIT("Quit");

        private final String menuTitle;

        MenuEntry(String menuTitle) {
            this.menuTitle = menuTitle;
        }

        private boolean isVisible() {
            return this != RESUME || gameState == GameState.PAUSED || gameState == GameState.MATCHPOINT;
        }
    }

    public SokobanGame(View view) {
        super(view);
    }

    @Override
    public void initialize() {
        Matrix4x4 projection;
        Matrix4x4 view;

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

        balls.add(new Ball(this));
        materialBall = new Material();
        materialBall.setAlphaTestFunction(CompareFunction.GREATER);
        materialBall.setAlphaTestValue(0.9f);

        materialPaddle = new Material();
        calculateWorldPaddles();

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

            stream = context.getAssets().open("football.jpg");
            texBall = graphicsDevice.createTexture(stream);

            materialBall.setTexture(texBall);

            stream = context.getAssets().open("ownPaddle.obj");
            meshPaddle = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("wood1.jpg");
            texPaddle = graphicsDevice.createTexture(stream);
            materialPaddle.setTexture(texPaddle);
        } catch (IOException e) {
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

        //Options
        fontOptions = graphicsDevice.createSpriteFont(null, 90);
        textOptions = graphicsDevice.createTextBuffer(fontOptions, 90);
        posOptions = Matrix4x4.createTranslation(-530, 0, 0);
        textPlus = graphicsDevice.createTextBuffer(fontOptions, 90);
        posPlus = Matrix4x4.createTranslation(0, 150, 0);
        textMinus = graphicsDevice.createTextBuffer(fontOptions, 90);
        posMinus = Matrix4x4.createTranslation(0, -150, 0);

        // Highscore
        fontCredits = graphicsDevice.createSpriteFont(null, 60);
        textHighscore = new TextBuffer[10];
        posHighscore = new Matrix4x4[10];

        int pos = +600;

        for(int i = 0; i < 10; i++) { // maximum 10 scores
            textHighscore[i] = graphicsDevice.createTextBuffer(fontCredits, 100);
            posHighscore[i] = Matrix4x4.createTranslation(-400, pos - 120, 0);
            pos -= 100;
        }

        //Credits
        fontCredits = graphicsDevice.createSpriteFont(null, 90);
        textCredits1 = graphicsDevice.createTextBuffer(fontCredits, 90);
        textCredits2 = graphicsDevice.createTextBuffer(fontCredits, 90);
        textCredits3 = graphicsDevice.createTextBuffer(fontCredits, 90);
        textCredits4 = graphicsDevice.createTextBuffer(fontCredits, 90);

        posCredits1 = Matrix4x4.createTranslation(-271, 400, 0);
        posCredits2 = Matrix4x4.createTranslation(-297, 200, 0);
        posCredits3 = Matrix4x4.createTranslation(-531, -400, 0);
        posCredits4 = Matrix4x4.createTranslation(-297, -600, 0);

        for (int i = 0; i < MenuEntry.values().length; i++) {
            textMenu[i] = graphicsDevice.createTextBuffer(fontMenu, 50);
        }

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
                    break;
                case ROTATION:
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
                            } else if (screen == Screen.GAME){
                                detectPaddleTouchDown(touchPoint);
                            } else if (screen == Screen.OPTIONS) {
                                detectOptionsTouch(touchPoint);
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

                String winner = scoreP1 > scoreP2 ? nameP1 : nameP2;
                try {
                    // load last winners
                    JSONObject oldHighscore;

                    oldHighscore = JSONSharedPreferences.loadJSONObject(context, "sokoban", "highscore");
                    JSONArray scoresArray = oldHighscore.getJSONArray("highscore");

                    // add first ten highscores
                    if(scoresArray.length() < 10) {

                        createStats(winner, scoresArray);

                        JSONObject tmpHighscore = new JSONObject();
                        tmpHighscore.put("highscore", scoresArray);

                        JSONSharedPreferences.saveJSONObject(context, "sokoban", "highscore", tmpHighscore);
                    } else {
                        // add highscore on correct position with more than 10 highscores
                        JSONArray sortedJsonArray = getSortedJsonArray(scoresArray);

                        for (int i = 0; i < sortedJsonArray.length(); i++) {
                            JSONObject tmp = sortedJsonArray.getJSONObject(i);

                            if(scoreTime > tmp.getInt("time")) {
                                JSONObject tmpStats = new JSONObject();
                                tmpStats.put("player", winner);
                                tmpStats.put("time", scoreTime);
                                sortedJsonArray.put(9, tmpStats); // change the last one with this one

                                JSONObject newHighscore = new JSONObject();
                                newHighscore.put("highscore", sortedJsonArray);

                                JSONSharedPreferences.saveJSONObject(context, "sokoban", "highscore", newHighscore);
                                break;
                            }
                        }
                    }

                } catch (JSONException jsonError) {
                    Log.d(TAG, "there is no JSONSharedPreferences, add new one" + jsonError);
                    try {
                        JSONArray tmpArray = new JSONArray();

                        createStats(winner, tmpArray);

                        JSONObject tmpHighscore = new JSONObject();
                        tmpHighscore.put("highscore", tmpArray);

                        JSONSharedPreferences.saveJSONObject(context, "sokoban", "highscore", tmpHighscore);
                    } catch (JSONException jsonErrorTwo) {
                        Log.d(TAG, "" + jsonErrorTwo);
                    }
                }
                resetScore();
                screen = Screen.MENU;
                break;
        }
    }

    @NonNull
    private JSONArray getSortedJsonArray(JSONArray scoresArray) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < scoresArray.length(); i++) {
            jsonValues.add(scoresArray.getJSONObject(i));
        }

        // see @ http://stackoverflow.com/questions/19543862/how-can-i-sort-a-jsonarray-in-java
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "time";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                Integer valA = 0;
                Integer valB = 0;

                try {
                    valA = (Integer) a.get(KEY_NAME);
                    valB = (Integer) b.get(KEY_NAME);
                }
                catch (JSONException e) {
                    //do something
                }
                return valB.compareTo(valA);
            }
        });

        for (int i = 0; i < scoresArray.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    private void createStats(String winner, JSONArray scoresArray) throws JSONException {
        JSONObject tmpStats = new JSONObject();
        tmpStats.put("player", winner);
        tmpStats.put("time", scoreTime);
        scoresArray.put(tmpStats);
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

        if (isAIGame && index == 1) {
            return;
        }

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

    private int aiReactionStartDeltaTime = 0;

    private void aiPaddleMove() {

        // perfect AI
        if (gameState != GameState.PLAYING) {
            return;
        }

        Ball closestBall = null;
        float largestPositionX = -screenWidth;
        for (Ball ball : balls) {
            if(ball.getBallAngle() > 180) {
                continue;
            } else if(largestPositionX == -screenWidth || ball.getBallPositionX() > largestPositionX) {
                largestPositionX = ball.getBallPositionX();
                closestBall = ball;
            }
        }

        if(closestBall != null) {
            if(aiReactionStartDeltaTime == 0) {
                aiReactionStartDeltaTime = currentDeltaTime;
            } else if(currentDeltaTime - aiReactionStartDeltaTime >= AI_REACTION_TIME) {
                float newPaddlePosition = closestBall.getBallPositionY();

                if (Math.abs(newPaddlePosition) < screenHeight / 2 - paddleSizes[1]) {
                    calculateWorldAiPaddle(newPaddlePosition);
                }
            }
        } else {
            aiReactionStartDeltaTime = 0;
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

    private void detectOptionsTouch(Point touchPoint) {
       if (touchPoint.intersects(aabbplus)) {
           MAX_MATCHPOINTS += 1;
       } else if (touchPoint.intersects(aabbminus)) {
           if(MAX_MATCHPOINTS > 1) {
               MAX_MATCHPOINTS -= 1;
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
            case AIGAME:
                isAIGame = true;
                resetPositions();
                resetScore();
                nameP2 = "AI";
                screen = Screen.GAME;
                gameState = GameState.PRESTART;
                miau.start();
                break;
            case VERSUS:
                isAIGame = false;
                resetPositions();
                resetScore();
                screen = Screen.GAME;
                gameState = GameState.PRESTART;
                miau.start();
                break;
            case OPTIONS:
                screen = Screen.OPTIONS;
                break;
            case HIGHSCORE:
                screen = Screen.HIGHSCORE;
                break;
            case CREDITS:
                screen = Screen.CREDITS;
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
        scoreTime = 0;
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
            case OPTIONS:
                drawOptions();
                break;
            case HIGHSCORE:
                drawHighscore();
                break;
            case CREDITS:
                drawCredits();
                break;
        }
    }

    private int getDeltaTime() {
        return (int) (Calendar.getInstance().getTimeInMillis() - startTime);
    }


    private void drawGame() {
        if (gameState == GameState.PLAYING) {
            currentDeltaTime = getDeltaTime();

            //AI Movement only in AIGame
            if (isAIGame) {
                aiPaddleMove();
            }

            frame++;

            if (currentDeltaTime > 0) {
                fpms = frame / ((double) currentDeltaTime);
            }

            textTimeCounter.setText((currentDeltaTime / 1000) + "s, " + (int) (fpms * 1000) + " fps");
            renderer.drawText(textTimeCounter, posTitle);

            for(Ball ball : toBeDeletedBalls) {
                balls.remove(ball);
            }
            toBeDeletedBalls.clear();

            for(Ball ball : toBeAddedBalls) {
                balls.add(ball);
            }
            toBeAddedBalls.clear();


            for (Ball ball : balls){
                // Collisiondetection Paddle Ball
                maxPosition = paddleTranslationX - ball.BASE_SIZE;
                float distance;
                if (ball.getBallPositionX() > maxPosition + paddleSizes[1] * 0.2) {
                    // right Paddle
                    distance = ball.getBallPositionY() - paddlePositions[1];
                    if (Math.abs(distance) > paddleSizes[1]) {
                        scoreP1 += 1;
                        scoreTime += (currentDeltaTime / 1000);
                        setMatchpointState();
                        return;
                    } else {
                        turnAround(distance, false, ball);
                    }
                } else if (ball.getBallPositionX() > maxPosition) {
                    distance = ball.getBallPositionY() - paddlePositions[1];
                    if (Math.abs(distance) <= paddleSizes[1]) {
                        turnAround(distance, false, ball);
                    }
                } else if (ball.getBallPositionX() < -maxPosition - paddleSizes[0] * 0.2) {
                    // left Paddle
                    distance = ball.getBallPositionY() - paddlePositions[0];
                    if (Math.abs(distance) > paddleSizes[0]) {
                        scoreTime += (currentDeltaTime / 1000);
                        scoreP2 += 1;
                        setMatchpointState();
                        return;
                    } else {
                        turnAround(distance, true, ball);
                    }
                } else if (ball.getBallPositionX() < -maxPosition) {

                    distance = ball.getBallPositionY() - paddlePositions[0];
                    if (Math.abs(distance) <= paddleSizes[0]) {
                        turnAround(distance, true, ball);
                    }
                } else {
                    collisionDetectionActive = true;
                }

                if (Math.abs(ball.getBallPositionY()) > screenHeight / 2 - ball.BASE_SIZE) {
                    if (ball.getBallAngle() > 0 * Math.PI && ball.getBallAngle() < Math.PI) {
                        ball.setBallAngle(((90 + (90 - ball.getBallAngle())) % 360));

                    } else {
                        ball.setBallAngle(((270 + (270 - ball.getBallAngle())) % 360));
                    }

                    if (bounce3.isPlaying()) {
                        bounce3.seekTo(0);
                    } else {
                        bounce3.start();
                    }
                }
                drawPowerUp(ball);
                removeExpiredPowerUp();
            }
        } else if (gameState == GameState.GAMEOVER) {
            drawGameover();
        } else {
            resetDeltaTimeCounter();
            textScore.setText(scoreP1 + " : " + scoreP2);
            renderer.drawText(textScore, posTitle);
        }

        // draw Ball
        for (Ball ball : balls) {
            ball.drawBall();
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

    private void drawPowerUp(Ball ball) {
        if (shouldSpawn()) {
            visiblePowerup = AbstractPowerUp.spawn(this, ball);
        }
        if (visiblePowerup != null) {

            if (visiblePowerup.catchPowerUp(ball)) {
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
        renderer.drawText(textGameover, posGameover);
        renderer.drawText(textP1, posP1);
        renderer.drawText(textP2, posP2);
    }

    private void turnAround(float distance, boolean isLeft, Ball ball) {
        if (!collisionDetectionActive) {
            return;
        }
        float percentage = distance / paddleSizes[isLeft ? 0 : 1];
        float newAngle = percentage * 65;
        if (!isLeft) {
            ball.setBallAngle(270 + newAngle);
        } else {
            ball.setBallAngle(90 - newAngle);
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
        balls.clear();
        balls.add(new Ball(this));
        maxPosition = paddleTranslationX - balls.get(0).BASE_SIZE;

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

    public void calculateWorldAiPaddle(float destinationY) {
        float maxPixelsPerFrame = (float) (largestWidth / (fpms * Ball.BASE_SPEED)
                * (((double)screenHeight) / largestWidth));
        maxPixelsPerFrame *= AI_MAX_MOVEMENT;

        float gap = paddlePositions[1] - destinationY;
        if(gap > maxPixelsPerFrame) {
            paddlePositions[1] -= maxPixelsPerFrame;
        } else if(gap < -maxPixelsPerFrame) {
            paddlePositions[1] += maxPixelsPerFrame;
        } else {
            paddlePositions[1] = destinationY;
        }

        calculateWorldPaddle(1);
    }

    public void calculateWorldPaddle(int index) {
        worldPaddles[index] = Matrix4x4.createTranslation(
                (index == 0 ? -1 : 1) * paddleTranslationX,
                paddlePositions[index], 0
        ).scale(paddleSizeDefault, paddleSizes[index], paddleSizeDefault);
    }

    private void drawMenu() {
        textTitle.setText("Sokoban");
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

    private void drawOptions() {
        textTitle.setText("Options");
        renderer.drawText(textTitle, posTitle);

        textOptions.setText("Maximale Matchpoints: " + MAX_MATCHPOINTS);
        renderer.drawText(textOptions, posOptions);

        textPlus.setText("+");
        renderer.drawText(textPlus, posPlus);
        textMinus.setText("-");
        renderer.drawText(textMinus, posMinus);

        float[] boundsPlus = textPlus.getBounds();
        aabbplus = new AABB(0, 150, boundsPlus[0], boundsPlus[1]);

        float[] boundsMinus = textPlus.getBounds();
        aabbminus = new AABB(0, -150, boundsMinus[0], boundsMinus[1]);
    }

    private void drawHighscore() {
        textTitle.setText("Highscore");
        renderer.drawText(textTitle, posTitle);

        try {
            JSONObject highscore = JSONSharedPreferences.loadJSONObject(context, "sokoban", "highscore");

            JSONArray scoresArray = highscore.getJSONArray("highscore");
            scoresArray = getSortedJsonArray(scoresArray);

            for(int i = 0; i < scoresArray.length(); i++) {
                JSONObject score = scoresArray.getJSONObject(i);
                textHighscore[i].setText("player: " + score.get("player") + " time: " + score.get("time") + " Sekunden");
            }

            for (int i = 0; i < textHighscore.length; i++) {
                renderer.drawText(textHighscore[i], posHighscore[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void drawCredits() {
        textTitle.setText("Credits");
        renderer.drawText(textTitle, posTitle);

        textCredits1.setText("Marissa Füeß"); //breite: 541.0    höhe: 69.0
        renderer.drawText(textCredits1, posCredits1);
        textCredits2.setText("Nicolai Schenk"); //breite: 593.0    höhe: 68.0
        renderer.drawText(textCredits2, posCredits2);
        textCredits3.setText("Special Thanks for Sounds"); //breite: 1062.0    höhe: 69.0
        renderer.drawText(textCredits3, posCredits3);
        textCredits4.setText("Thorsten Hack"); //593.0    höhe: 68.0
        renderer.drawText(textCredits4, posCredits4);

    }

    @Override
    public void resize(int width, int height) {
//        float aspect = (float) width / (float) height;
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
        } else if (gameState == GameState.PLAYING) {
            pauseDeltaTime = getDeltaTime();
            gameState = GameState.PAUSED;
            screen = Screen.MENU;
        } else {
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
