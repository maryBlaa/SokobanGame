package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class PaddleDirectionInverser extends AbstractPowerUp {

    Ball ball;

    public PaddleDirectionInverser(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    @Override
    public void performAction() {
        paddleIndex = getPaddleIndex(ball);
        game.paddleInverse[paddleIndex] = true;
    }

    @Override
    public void undoAction() {
        game.paddleInverse[paddleIndex] = false;
    }

}
