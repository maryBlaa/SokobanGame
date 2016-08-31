package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.objects.Ball;

public class DuplicateBall extends AbstractPowerUp {

    Ball ball;

    public DuplicateBall(SokobanGame game) {
        super(game);    // Congratulations
    }

    @Override
    public void performAction() {
        ball = new Ball(game);
        ball.setSecondBall(powerUpPositionX, powerUpPositionY);
        game.balls.add(ball);
    }

    @Override
    public void undoAction() {
        game.toBeDeletedBalls.add(ball);
    }

}
