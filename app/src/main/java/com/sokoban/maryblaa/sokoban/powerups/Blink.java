package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class Blink extends AbstractPowerUp {

    // Blinks per second
    public static final int BLINK_DURATION_MS = 589;
    Ball ball;

    public Blink(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    @Override
    public void performAction() {
        ball.setBallBlinking(true);
        ball.setBlinkStartDeltaTime(game.currentDeltaTime);
    }

    @Override
    public void undoAction() {
        ball.setBallBlinking(false);
    }
}
