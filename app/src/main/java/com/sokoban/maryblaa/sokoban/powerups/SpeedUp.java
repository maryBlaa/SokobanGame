package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class SpeedUp extends AbstractPowerUp {

    public SpeedUp(SokobanGame game) {
        super(game);    // Congratulations
    }

    private static final float SPEEDUP = 1.5f;

    @Override
    public void performAction() {

    }

    @Override
    public void undoAction() {

    }

    @Override
    public void performAction(Ball ball) {
        ball.speedVariation *= SPEEDUP;
    }

    @Override
    public void undoAction(Ball ball) {
        ball.speedVariation /= SPEEDUP;
    }
}
