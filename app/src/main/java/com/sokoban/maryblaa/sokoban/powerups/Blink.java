package com.sokoban.maryblaa.sokoban.powerups;

import android.util.Log;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class Blink extends AbstractPowerUp {

    public static final int BLINK_FRAME_RATE = 15;

    public Blink(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        Log.d(getClass().getSimpleName(), "JEEEEEEEETZT");
        game.isBallBlinking = true;
        game.blinkStartFrame = game.frame;
    }

    @Override
    public void undoAction() {
        game.isBallBlinking = false;
    }
}
