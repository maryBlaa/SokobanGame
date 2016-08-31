package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class BallEnlarger extends AbstractPowerUp {

    Ball ball;
    private static final float BALLSIZE = 25f;

    public BallEnlarger(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    @Override
    public void performAction() {
        ball.BASE_SIZE += BALLSIZE;
    }

    @Override
    public void undoAction() {
        this.ball.BASE_SIZE -= BALLSIZE;
    }

}
