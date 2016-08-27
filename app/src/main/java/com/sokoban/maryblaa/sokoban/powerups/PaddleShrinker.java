package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class PaddleShrinker extends AbstractPowerUp {

    private static final float PADDLESIZE = 25f;

    public PaddleShrinker(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        SokobanGame.paddleSize -= PADDLESIZE;
    }

    @Override
    public void undoAction() {
        SokobanGame.paddleSize += PADDLESIZE;
    }
}
