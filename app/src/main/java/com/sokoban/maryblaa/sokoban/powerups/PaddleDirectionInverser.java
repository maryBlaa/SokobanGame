package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class PaddleDirectionInverser extends AbstractPowerUp {

    public PaddleDirectionInverser(SokobanGame game) {
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
        paddleIndex = getPaddleIndex(ball);
        game.paddleInverse[paddleIndex] = true;
    }

    @Override
    public void undoAction(Ball ball) {
        game.paddleInverse[paddleIndex] = false;
    }

}
