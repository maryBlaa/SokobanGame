package com.sokoban.maryblaa.sokoban.objects;


import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

public class Paddle {

    protected SokobanGame game;
    String side;

    Matrix4x4 worldPaddle;

    private float paddleTranslationX = 400f;

    public static float paddleSizeDefault = 200f;
    public float paddleSize = paddleSizeDefault;
    public boolean paddleInverse = false;

    private float paddlePosition = 0;
    private float paddleFingerPosition = 0;

    public Paddle(SokobanGame game, String side) {
        this.game = game;
        this.side = side;
        init(side);
    }

    private void init(String side) {
        calculateWorldPaddles(side);
    }

    public void calculateWorldPaddles(String side) {

        if(side == "left") {
            worldPaddle = Matrix4x4.createTranslation(-paddleTranslationX, 0, 0).scale(paddleSizeDefault, paddleSize, paddleSizeDefault);
        } else {
            worldPaddle = Matrix4x4.createTranslation(paddleTranslationX, 0, 0).scale(paddleSizeDefault, paddleSize, paddleSizeDefault);
        }
    }

    public void calculateWorldPaddle() {
        worldPaddle = Matrix4x4.createTranslation(paddleTranslationX, paddlePosition, 0).scale(paddleSizeDefault, paddleSize, paddleSizeDefault);
    }

    public float getPaddleTranslationX() {
        return paddleTranslationX;
    }

    public void setPaddleTranslationX(float paddleTranslationX) {
        this.paddleTranslationX = paddleTranslationX;
    }

    public float getPaddleSize() {
        return paddleSize;
    }

    public void setPaddleSize(float paddleSize) {
        this.paddleSize = paddleSize;
    }

    public boolean isPaddleInverse() {
        return paddleInverse;
    }

    public void setPaddleInverse(boolean paddleInverse) {
        this.paddleInverse = paddleInverse;
    }

    public float getPaddlePosition() {
        return paddlePosition;
    }

    public void setPaddlePosition(float paddlePosition) {
        this.paddlePosition = paddlePosition;
    }

    public float getPaddleFingerPosition() {
        return paddleFingerPosition;
    }

    public void setPaddleFingerPosition(float paddleFingerPosition) {
        this.paddleFingerPosition = paddleFingerPosition;
    }

    public float movePaddle (float y) {
        return paddleFingerPosition = y - paddlePosition;
    }

    public float movePaddlePosition (float y) {
        return paddlePosition + y - (paddlePosition + paddleFingerPosition);
    }

    public float movePaddleInversePosition (float y) {
        return paddlePosition - y - (paddlePosition + paddleFingerPosition);
    }

    public Matrix4x4 getWorldPaddle() {
        return worldPaddle;
    }

    public void setWorldPaddle(Matrix4x4 worldPaddle) {
        this.worldPaddle = worldPaddle;
    }

    public static float getPaddleSizeDefault() {
        return paddleSizeDefault;
    }

    public static void setPaddleSizeDefault(float paddleSizeDefault) {
        Paddle.paddleSizeDefault = paddleSizeDefault;
    }
}
