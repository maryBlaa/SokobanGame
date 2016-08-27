package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.collision.MathHelper;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

/**
 * Created by DragooNick on 05.07.2016.
 */
public abstract class AbstractPowerUp {

    public enum PowerupType {

        BALLENLARGER ("ball.jpg", "ownBall.obj"),
        PADDLEENLARGER ("ball.jpg", "ownBall.obj"),
        BALLSHRINKER ("ball.jpg", "ownBall.obj"),
        PADDLESHRINKER ("ball.jpg", "ownBall.obj"),
        SPEEDUP ("ball.jpg", "ownBall.obj"),
        SLOWDOWN ("ball.jpg", "ownBall.obj"),
        BLINK ("ball.jpg", "ownBall.obj"),
        PADDLEDIRECTIONINVERSER ("ball.jpg", "ownBall.obj");

        public String materialSrc;
        public String meshSrc;

        PowerupType(String materialSrc, String meshSrc) {
            this.materialSrc = materialSrc;
            this.meshSrc = meshSrc;
        }
    }

    Texture texture;
    Matrix4x4 position;
    public float powerUpSize = 50f;
    public float powerUpPositionX;
    public float powerUpPositionY;
    public float speed;
    public float powerUpAngle;
    public int despawnFrame;
    public int powerUpTime;
    private Mesh meshPowerUp;
    private Material materialPowerUp;
    protected PowerupType type;

    public abstract void performAction();
    public abstract void undoAction();

    public static AbstractPowerUp spawn(SokobanGame game) {
        int powerupTypeIndex = MathHelper.randomInt(0, PowerupType.values().length - 1);
        PowerupType type = PowerupType.values()[powerupTypeIndex];


        AbstractPowerUp powerup;
        switch(type) {

            case BALLENLARGER:
                powerup = new BallEnlarger(game);
                break;
            case PADDLEENLARGER:
                powerup = new PaddleEnlarger(game);
                break;
            case BALLSHRINKER:
                powerup = new BallShrinker(game);
                break;
            case PADDLESHRINKER:
                powerup = new PaddleShrinker(game);
                break;
            case SPEEDUP:
                powerup = new SpeedUp(game);
                break;
            case SLOWDOWN:
                powerup = new SlowDown(game);
                break;
            case BLINK:
                powerup = new Blink(game);
                break;
            case PADDLEDIRECTIONINVERSER:
                powerup = new PaddleDirectionInverser(game);
                break;
            default:
                return null; // this should never happen until the sun eats the earth
        }
        powerup.type = type;
        powerup.init();
        return powerup;

    }

    private SokobanGame game;

    public AbstractPowerUp(SokobanGame game) {
        this.game = game;
    }

    private void init() {
        materialPowerUp = game.powerupMaterials.get(type);
        meshPowerUp = game.powerupMeshes.get(type);

        int displayTime = MathHelper.randomInt(150, 350);
        despawnFrame = displayTime + game.frame;

        powerUpPositionY = MathHelper.randomInt(game.screenHeight * -0.45, game.screenHeight * 0.45);
        powerUpPositionX = MathHelper.randomInt(game.screenWidth * -0.4, game.screenWidth * 0.4);
    }

    public void draw(){
        // Power Up zeichnen
        Matrix4x4 worldPowerUp = Matrix4x4.createTranslation(powerUpPositionX, powerUpPositionY, 0).scale(powerUpSize);
        game.renderer.drawMesh(meshPowerUp, materialPowerUp, worldPowerUp);
    };

    // distance between centerns (ball and power up) has to be smaller than added radii (it's real, you can google it)
    public boolean catchPowerUp() {
        double distanceX = game.ballPositionX - powerUpPositionX;
        double distanceY = game.ballPositionY - powerUpPositionY;

        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

        return distance < SokobanGame.ballSize + powerUpSize;
    }
}
