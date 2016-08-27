package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class BallShrinker extends AbstractPowerUp {

    private static final float BALLSIZE = 25f;

    public BallShrinker(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        game.ballSize -= BALLSIZE;
    }

    @Override
    public void undoAction() {
        game.ballSize += BALLSIZE;
    }
}
