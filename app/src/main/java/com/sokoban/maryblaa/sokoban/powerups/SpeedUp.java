package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class SpeedUp extends AbstractPowerUp {

    Ball ball;

    public SpeedUp(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    private static final float SPEEDUP = 1.5f;

    @Override
    public void performAction() {
        ball.setSpeedVariation(ball.getSpeedVariation() * SPEEDUP);
    }

    @Override
    public void undoAction() {
        ball.setSpeedVariation(ball.getSpeedVariation() / SPEEDUP);
    }
}
