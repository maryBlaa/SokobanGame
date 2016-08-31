package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class PaddleEnlarger extends AbstractPowerUp {

    private static final float PADDLESIZE = 25f;

    public PaddleEnlarger(SokobanGame game) {
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
        game.paddleSizes[paddleIndex] += PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }

    @Override
    public void undoAction(Ball ball) {
        game.paddleSizes[paddleIndex] -= PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }
}
