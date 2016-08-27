package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class SlowDown extends AbstractPowerUp {

    public SlowDown(SokobanGame game) {
        super(game);    // Congratulations
    }

    private static final float SLOWDOWN = 0.75f;

    @Override
    public void performAction() {
        game.speed *= SLOWDOWN;
    }

    @Override
    public void undoAction() {
        game.speed /= SLOWDOWN;
    }
}
