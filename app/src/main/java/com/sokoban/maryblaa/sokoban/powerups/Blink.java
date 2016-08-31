package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class Blink extends AbstractPowerUp {

    // Blinks per second
    public static final int BLINK_DURATION_MS = 589;

    public Blink(SokobanGame game) {
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
        ball.isBallBlinking = true;
        ball.blinkStartDeltaTime = game.currentDeltaTime;
    }

    @Override
    public void undoAction(Ball ball) {
        ball.isBallBlinking = false;
    }
}
