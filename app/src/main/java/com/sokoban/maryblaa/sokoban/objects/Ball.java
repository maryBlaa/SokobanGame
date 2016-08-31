package com.sokoban.maryblaa.sokoban.objects;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;
import com.sokoban.maryblaa.sokoban.powerups.Blink;

public class Ball {

    protected SokobanGame game;

    public Matrix4x4 worldBall;

    public float BASE_SIZE = 70f;

    // base speed: 2000ms for largest width
    public static int BASE_SPEED = 2000;
    public double speedVariation = 1;

    public boolean isBallBlinking = false;
    public int blinkStartDeltaTime = 0;

    public float ballPositionX = 0;
    public float ballPositionY = 0;
    public float ballAngle;

    public Ball (SokobanGame game) {
        this.game = game;
        init();
    }

    private void init() {
        ballAngle = getBallStartPosition();

        worldBall = new Matrix4x4();
        worldBall.scale(BASE_SIZE);
        worldBall.translateBy(0, 0, 0);
    }

    public void drawBall() {
        boolean shouldDrawBall = !isBallBlinking || ((game.currentDeltaTime - blinkStartDeltaTime) % Blink.BLINK_DURATION_MS) > Blink.BLINK_DURATION_MS/2;
        if(shouldDrawBall) {
            worldBall = Matrix4x4.createTranslation(ballPositionX, ballPositionY, 0).scale(BASE_SIZE);
            game.renderer.drawMesh(game.meshBall, game.materialBall, worldBall);
        }
        if (game.gameState == SokobanGame.GameState.PLAYING) {
            double speed = speedVariation * (game.largestWidth / (game.fpms * BASE_SPEED));
            ballPositionX += speed * Math.sin(Math.toRadians(ballAngle));
            ballPositionY += speed * Math.cos(Math.toRadians(ballAngle));
        }
    }

    public float getBallStartPosition() {
        double random;
        do {
            random = Math.random() * 360;
        }
        while (!(random > 65 && random < 115) &&
                !(random > 245 && random < 295));

        return (float) random;
    }

    public void resetBall() {
        ballPositionX = 0;
        ballPositionY = 0;
        isBallBlinking = false;
        ballAngle = getBallStartPosition();
    }

    public void setSecondBall(float x, float y) {
        ballPositionX = x;
        ballPositionY = y;
        speedVariation = 0.5;
    }
}