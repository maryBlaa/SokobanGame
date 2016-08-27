package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;

/**
 * Created by DragooNick on 05.07.2016.
 */
public class Blink extends AbstractPowerUp {

    // Blinks per second
    public static final int BLINK_DURATION_MS = 589;

    public Blink(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        game.isBallBlinking = true;
        game.blinkStartDeltaTime = game.currentDeltaTime;
    }

    @Override
    public void undoAction() {
        game.isBallBlinking = false;
    }
}
