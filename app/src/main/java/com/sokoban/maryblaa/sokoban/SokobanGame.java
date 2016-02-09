package com.sokoban.maryblaa.sokoban;

import android.content.Context;
import android.util.Log;

import com.sokoban.maryblaa.sokoban.game.Game;
import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.graphics.CompareFunction;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    private Camera camera;
    private Mesh meshTree, meshRoad;
    private Mesh cubeMesh;
    private Texture texTree, texRoad;
    private Texture cubeTex;
    private Material matTree, matRoad;
    private Material matCube;
    private Matrix4x4 worldRoad;
    private Matrix4x4 worldCube;
    private Matrix4x4[] worldTrees;

    public SokobanGame(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        Log.i("", "initialize() SokobanGame");
        Matrix4x4 projection = new Matrix4x4();
        Matrix4x4 view = new Matrix4x4();

        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 100f);
        view.translate(0, 0, -5);

        camera = new Camera();
        camera.setProjection(projection);
        camera.setView(view);

        matTree = new Material();
        matTree.setAlphaTestFunction(CompareFunction.GREATER);
        matTree.setAlphaTestValue(0.9f);

        matRoad = new Material();
        matCube = new Material();

        worldTrees = new Matrix4x4[] {
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

        worldCube = new Matrix4x4();
    }

    @Override
    public void loadContent() {
        Log.i("", "loadContent() SokobanGame");
        try {
            InputStream stream;

            stream = context.getAssets().open("box.obj");
            cubeMesh = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("wood.png");
            cubeTex = graphicsDevice.createTexture(stream);
            matCube.setTexture(cubeTex);

            stream = context.getAssets().open("tree.obj");
            meshTree = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("tree.png");
            texTree = graphicsDevice.createTexture(stream);
            matTree.setTexture(texTree);

            stream = context.getAssets().open("road.obj");
            meshRoad = Mesh.loadFromOBJ(stream);

            stream = context.getAssets().open("road.png");
            texRoad = graphicsDevice.createTexture(stream);
            matRoad.setTexture(texRoad);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void update(float deltaSeconds) {
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        graphicsDevice.setCamera(camera);

        // Strasse zeichnen
        renderer.drawMesh(meshRoad, matRoad, worldRoad);

        renderer.drawMesh(cubeMesh, matCube, worldCube);

        // Bauume zeichnen
        for (Matrix4x4 worldTree : worldTrees)
            renderer.drawMesh(meshTree, matTree, worldTree);
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / (float) height;

        Matrix4x4 projection = new Matrix4x4();
        projection.setPerspectiveProjection(-aspect * 0.1f, aspect * 0.1f, -0.1f, 0.1f, 0.1f, 100f);

        camera.setProjection(projection);
    }

}
