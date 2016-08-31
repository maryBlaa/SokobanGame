package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class SlowDown extends AbstractPowerUp {

    Ball ball;

    public SlowDown(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    private static final float SLOWDOWN = 0.75f;

    @Override
    public void performAction() {
        ball.setSpeedVariation(ball.getSpeedVariation() * SLOWDOWN);
    }

    @Override
    public void undoAction() {
        ball.setSpeedVariation(ball.getSpeedVariation() / SLOWDOWN);
    }
}
