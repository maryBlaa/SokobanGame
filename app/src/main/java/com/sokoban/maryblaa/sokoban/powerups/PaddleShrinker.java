package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class PaddleShrinker extends AbstractPowerUp {

    private static final float PADDLESIZE = 25f;

    public PaddleShrinker(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {

    }

    @Override
    public void undoAction() {

    }

    @Override
    public void performAction(Ball ball) {
        paddleIndex = getPaddleIndex(ball);
        game.paddleSizes[paddleIndex] -= PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }

    @Override
    public void undoAction(Ball ball) {
        game.paddleSizes[paddleIndex] += PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }
}
