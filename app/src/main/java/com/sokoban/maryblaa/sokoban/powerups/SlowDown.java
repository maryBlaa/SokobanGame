package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class SlowDown extends AbstractPowerUp {

    public SlowDown(SokobanGame game) {
        super(game);    // Congratulations
    }

    private static final float SLOWDOWN = 0.75f;

    @Override
    public void performAction() {

    }

    @Override
    public void undoAction() {

    }

    @Override
    public void performAction(Ball ball) {
        ball.speedVariation *= SLOWDOWN;
    }

    @Override
    public void undoAction(Ball ball) {
        ball.speedVariation /= SLOWDOWN;
    }
}
