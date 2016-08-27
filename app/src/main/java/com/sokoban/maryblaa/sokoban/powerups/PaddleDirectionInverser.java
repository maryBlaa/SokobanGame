package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class PaddleDirectionInverser extends AbstractPowerUp {

    public PaddleDirectionInverser(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        paddleIndex = getPaddleIndex();
        game.paddleInverse[paddleIndex] = true;
    }

    @Override
    public void undoAction() {
        game.paddleInverse[paddleIndex] = false;
    }
}
