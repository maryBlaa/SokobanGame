package com.sokoban.maryblaa.sokoban;

import android.content.Context;
import android.util.Log;

import com.sokoban.maryblaa.sokoban.game.Game;
import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.graphics.CompareFunction;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.MeshFile;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    private static final String TAG = SokobanGame.class.getSimpleName();

    private Camera camera;
    private MeshFile meshTree, meshRoad;
    private Texture texTree, texRoad;
    private Material matTree, matRoad;
    private Matrix4x4 worldRoad;
    private Matrix4x4[] worldTrees;

    public SokobanGame(Context contex) {
        super(contex);
    }

    @Override
    public void initialize() {
        Matrix4x4 projection = new Matrix4x4();
        Matrix4x4 view = new Matrix4x4();

        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 100f);
        view.translate(0, -1, 0);

        camera = new Camera();
        camera.setProjection(projection);
        camera.setView(view);

        matTree = new Material();
        matTree.setAlphaTestFunction(CompareFunction.GREATER);
        matTree.setAlphaTestValue(0.9f);

        matRoad = new Material();

        worldTrees = new Matrix4x4[]{
                Matrix4x4.multiply(Matrix4x4.createTranslation(-1.2f, 0, -1), Matrix4x4.createRotationY(-20)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(-1.2f, 0, -3), Matrix4x4.createRotationY(45)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(-1.2f, 0, -5), Matrix4x4.createRotationY(-30)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(-1.2f, 0, -7), Matrix4x4.createRotationY(160)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(+1.2f, 0, -1), Matrix4x4.createRotationY(35)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(+1.2f, 0, -3), Matrix4x4.createRotationY(-50)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(+1.2f, 0, -5), Matrix4x4.createRotationY(90)),
                Matrix4x4.multiply(Matrix4x4.createTranslation(+1.2f, 0, -7), Matrix4x4.createRotationY(15)),
        };

        worldRoad = new Matrix4x4();
        worldRoad.translate(0, 0, 1);
    }

    @Override
    public void loadContent() {

        try {
            InputStream stream;

            stream = context.getAssets().open("tree.obj");
            meshTree = MeshFile.loadFromOBJ(stream);

            stream = context.getAssets().open("tree.png");
            texTree = graphicsDevice.createTexture(stream);
            matTree.setTexture(texTree);

            stream = context.getAssets().open("road.obj");
            meshRoad = MeshFile.loadFromOBJ(stream);

            stream = context.getAssets().open("road.png");
            texRoad = graphicsDevice.createTexture(stream);
            matRoad.setTexture(texRoad);

        } catch (IOException e) {
            Log.e(TAG, "" + e);
        }
    }

    @Override
    public void update(float deltaSeconds) {
//        world.rotateY(deltaSeconds * 45);
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        graphicsDevice.setCamera(camera);

        // Strasse zeichnen
        renderer.drawMesh(meshRoad, matRoad, worldRoad);

        // Bäume zeichnen
        for (Matrix4x4 worldTree : worldTrees)
            renderer.drawMesh(meshTree, matTree, worldTree);
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / (float) height;

        Matrix4x4 projection = new Matrix4x4();
        projection.setPerspectiveProjection(-aspect * 0.1f, aspect * 0.1f, -0.1f, 0.1f, 0.1f, 100f);

        camera.setProjection(projection);
        ;
    }


}
