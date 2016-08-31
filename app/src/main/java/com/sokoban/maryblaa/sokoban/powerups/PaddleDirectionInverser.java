package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;
import com.sokoban.maryblaa.sokoban.objects.Paddle;

public class PaddleDirectionInverser extends AbstractPowerUp {

    Ball ball;

    public PaddleDirectionInverser(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    @Override
    public void performAction() {
        paddleIndex = getPaddleIndex(ball);
        game.paddles.get(paddleIndex).setPaddleInverse(true);
    }

    @Override
    public void undoAction() {
        game.paddles.get(paddleIndex).setPaddleInverse(false);
    }

}
