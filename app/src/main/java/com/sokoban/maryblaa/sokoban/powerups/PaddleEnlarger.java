package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class PaddleEnlarger extends AbstractPowerUp {

    private static final float PADDLESIZE = 25f;

    public PaddleEnlarger(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        paddleIndex = getPaddleIndex();
        game.paddleSizes[paddleIndex] += PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }

    @Override
    public void undoAction() {
        game.paddleSizes[paddleIndex] -= PADDLESIZE;
        game.calculateWorldPaddle(paddleIndex);
    }
}
