package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class PaddleShrinker extends AbstractPowerUp {

    Ball ball;
    private static final float PADDLESIZE = 25f;

    public PaddleShrinker(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    @Override
    public void performAction() {
        paddleIndex = getPaddleIndex(ball);
        game.paddleSizes[paddleIndex] -= PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }

    @Override
    public void undoAction() {
        game.paddleSizes[paddleIndex] += PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }
}
