package com.sokoban.maryblaa.sokoban;

import android.content.Context;

import com.sokoban.maryblaa.sokoban.game.Game;
import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maryf on 02.02.2016.
 */
public class SokobanGame extends Game {

    Camera camera;
    private Mesh cube;
    private Matrix4x4 world;

    public SokobanGame(Context contex) {
        super(contex);
    }

    @Override
    public void initialize() {
        Matrix4x4 projection = new Matrix4x4();
        Matrix4x4 view = new Matrix4x4();

        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 100f);
        view.translate(0, 0, -5);

        camera = new Camera();
        camera.setProjection(projection);
        camera.setView(view);

        try {
            InputStream stream = context.getAssets().open("cube.obj");
            cube = Mesh.loadFromOBJ(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        world = new Matrix4x4();
    }

    @Override
    public void update(float deltaSeconds) {
        world.rotateY(deltaSeconds * 45);
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 1.0f, 0.0f);
        graphicsDevice.setCamera(camera);
        renderer.drawMesh(cube, world);
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / (float) height;

        Matrix4x4 projection = new Matrix4x4();
        projection.setPerspectiveProjection(-aspect * 0.1f, aspect * 0.1f, -0.1f, 0.1f, 0.1f, 100f);

        camera.setProjection(projection);
    }
}
