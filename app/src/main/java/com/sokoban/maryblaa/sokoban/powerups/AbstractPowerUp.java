package com.sokoban.maryblaa.sokoban.powerups;

import com.sokoban.maryblaa.sokoban.SokobanGame;
import com.sokoban.maryblaa.sokoban.graphics.CompareFunction;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by DragooNick on 05.07.2016.
 */
public abstract class AbstractPowerUp {
    Texture texture;
    Matrix4x4 position;
    float powerUpSize = 50f;
    float powerUpPositionX;
    float powerUpPositionY;
    float speed;
    float powerUpAngle;
    int displayTime;
    int powerUpTime;
    private Mesh meshPowerUp;
    private Material materialPowerUp;

    abstract void performAction();
    abstract void undoAction();

    private SokobanGame game;

    public AbstractPowerUp(SokobanGame game) {
        this.game = game;

        materialPowerUp = new Material();
        materialPowerUp.setAlphaTestFunction(CompareFunction.GREATER);
        materialPowerUp.setAlphaTestValue(0.9f);

        InputStream stream;

        try {
            stream = game.context.getAssets().open("ownBall.obj");
            meshPowerUp = Mesh.loadFromOBJ(stream);

            stream = game.context.getAssets().open("ball.jpg");
            Texture texPowerUp = game.graphicsDevice.createTexture(stream);
            materialPowerUp.setTexture(texPowerUp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(){
        powerUpPositionX += speed * Math.sin(Math.toRadians(powerUpAngle));
        powerUpPositionY += speed * Math.cos(Math.toRadians(powerUpAngle));

        //reflection top/bottom
        if (Math.abs(powerUpPositionY) > game.screenHeight / 2 - powerUpSize) {
            if (powerUpAngle > 0 * Math.PI && powerUpAngle < Math.PI) {
                powerUpAngle = (float) ((90 + (90 - powerUpAngle)) % 360);
            } else {
                powerUpAngle = (float) ((270 + (270 - powerUpAngle)) % 360);
            }
        }
        //reflection left/right
        if (Math.abs(powerUpPositionX) > game.screenWidth / 2 - powerUpSize) {
            if (powerUpAngle > 0 * Math.PI && powerUpAngle < Math.PI) {
                powerUpAngle = (float) ((90 + (90 - powerUpAngle)) % 360);
            } else {
                powerUpAngle = (float) ((270 + (270 - powerUpAngle)) % 360);
            }
        }
        // Ball zeichnen
        Matrix4x4 worldPowerUp = Matrix4x4.createTranslation(powerUpPositionX, powerUpPositionY, 0).scale(powerUpSize);

        game.renderer.drawMesh(meshPowerUp, materialPowerUp, worldPowerUp);
    };
}
