package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class BallEnlarger extends AbstractPowerUp {

    private static final float BALLSIZE = 25f;

    public BallEnlarger(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        SokobanGame.ballSize += BALLSIZE;
    }

    @Override
    public void undoAction() {
        SokobanGame.ballSize -= BALLSIZE;
    }
}
