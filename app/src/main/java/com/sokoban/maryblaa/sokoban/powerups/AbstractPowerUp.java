package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.collision.MathHelper;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public abstract class AbstractPowerUp {

    public enum PowerupType {

        BALLENLARGER("enlarger.png", "ownBall.obj"),
        PADDLEENLARGER("enlarger.png", "ownBall.obj"),
        BALLSHRINKER("shrinker.png", "ownBall.obj"),
        PADDLESHRINKER("shrinker.png", "ownBall.obj"),
        SPEEDUP("speedup.png", "ownBall.obj"),
        SLOWDOWN("slowdown.png", "ownBall.obj"),
        BLINK("blink.png", "ownBall.obj"),
        PADDLEDIRECTIONINVERSER("inverse.png", "ownBall.obj"),
        DUPLICATEBALL("duplicate.png", "ownBall.obj");

        public String materialSrc;
        public String meshSrc;

        PowerupType(String materialSrc, String meshSrc) {
            this.materialSrc = materialSrc;
            this.meshSrc = meshSrc;
        }
    }

    public static final int LIFETIME_MS = 15000;
    public static final int DISPLAYTIME_MIN_MS = 5000;
    public static final int DISPLAYTIME_MAX_MS = 10000;


    Texture texture;
    Matrix4x4 position;
    public float powerUpSize = 50f;
    public float powerUpPositionX;
    public float powerUpPositionY;
    public int despawnDeltaTime;
    public int powerDownDeltaTime;
    private Mesh meshPowerUp;
    private Material materialPowerUp;
    protected PowerupType type;

    public abstract void performAction();
    public abstract void undoAction();

    public static AbstractPowerUp spawn(SokobanGame game, Ball ball) {
        int powerupTypeIndex = MathHelper.randomInt(0, PowerupType.values().length - 1);
        PowerupType type = PowerupType.values()[powerupTypeIndex];

        AbstractPowerUp powerup;
        switch (type) {

            case BALLENLARGER:
                powerup = new BallEnlarger(game, ball);
                break;
            case PADDLEENLARGER:
                powerup = new PaddleEnlarger(game, ball);
                break;
            case BALLSHRINKER:
                powerup = new BallShrinker(game, ball);
                break;
            case PADDLESHRINKER:
                powerup = new PaddleShrinker(game, ball);
                break;
            case SPEEDUP:
                powerup = new SpeedUp(game, ball);
                break;
            case SLOWDOWN:
                powerup = new SlowDown(game, ball);
                break;
            case BLINK:
                powerup = new Blink(game, ball);
                break;
            case PADDLEDIRECTIONINVERSER:
                powerup = new PaddleDirectionInverser(game, ball);
                break;
            case DUPLICATEBALL:
                powerup = new DuplicateBall(game);
                break;
            default:
                return null; // this should never happen until the sun eats the earth
        }
        powerup.type = type;
        powerup.init();
        return powerup;

    }

    protected SokobanGame game;

    public AbstractPowerUp(SokobanGame game) {
        this.game = game;
    }

    private void init() {
        materialPowerUp = game.powerupMaterials.get(type);
        meshPowerUp = game.powerupMeshes.get(type);

        int displayTime = MathHelper.randomInt(DISPLAYTIME_MIN_MS, DISPLAYTIME_MAX_MS);
        despawnDeltaTime = game.currentDeltaTime + displayTime;

        powerUpPositionY = MathHelper.randomInt(game.screenHeight * -0.45, game.screenHeight * 0.45);
        powerUpPositionX = MathHelper.randomInt(game.screenWidth * -0.4, game.screenWidth * 0.4);
    }
    
    public void draw(){
        // Power Up zeichnen
        Matrix4x4 worldPowerUp = Matrix4x4.createTranslation(powerUpPositionX, powerUpPositionY, 0).scale(powerUpSize);
        game.renderer.drawMesh(meshPowerUp, materialPowerUp, worldPowerUp);
    };

    // distance between centerns (ball and power up) has to be smaller than added radii (it's real, you can google it)
    public boolean catchPowerUp(Ball ball) {
        double distanceX = ball.getBallPositionX() - powerUpPositionX;
        double distanceY = ball.getBallPositionY() - powerUpPositionY;

        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

        boolean isCaught = distance < ball.BASE_SIZE + powerUpSize;
        if(isCaught) {
            powerDownDeltaTime = game.currentDeltaTime + LIFETIME_MS;
        }
        return isCaught;
    }

    protected int paddleIndex;
    protected int getPaddleIndex(Ball ball) {
        return ball.getBallAngle() > 180 ? 1 : 0;
    }
}
