package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;
import com.sokoban.maryblaa.sokoban.objects.Paddle;

public class PaddleEnlarger extends AbstractPowerUp {

    Ball ball;
    private static final float PADDLESIZE = 25f;

    public PaddleEnlarger(SokobanGame game, Ball ball) {
        super(game);    // Congratulations
        this.ball = ball;
    }

    @Override
    public void performAction() {
        paddleIndex = getPaddleIndex(ball);
        game.paddles.get(paddleIndex).setPaddleSize(game.paddles.get(paddleIndex).getPaddleSize() + PADDLESIZE);
        game.paddles.get(paddleIndex).calculateWorldPaddle();
    }

    @Override
    public void undoAction() {
        game.paddles.get(paddleIndex).setPaddleSize(game.paddles.get(paddleIndex).getPaddleSize() - PADDLESIZE);
        game.paddles.get(paddleIndex).calculateWorldPaddle();
    }
}
