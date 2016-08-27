package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class SpeedUp extends AbstractPowerUp {

    public SpeedUp(SokobanGame game) {
        super(game);    // Congratulations
    }

    private static final float SPEEDUP = 1.5f;

    @Override
    public void performAction() {
        game.speed *= SPEEDUP;
    }

    @Override
    public void undoAction() {
        game.speed /= SPEEDUP;
    }
}
