package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class BallShrinker extends AbstractPowerUp {

    private static final float BALLSIZE = 25f;

    public BallShrinker(SokobanGame game) {
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
        ball.BASE_SIZE -= BALLSIZE;
    }

    @Override
    public void undoAction(Ball ball) {
        ball.BASE_SIZE += BALLSIZE;
    }
}
