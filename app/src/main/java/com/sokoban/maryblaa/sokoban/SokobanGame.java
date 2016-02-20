package com.sokoban.maryblaa.sokoban;

import android.content.Context;
import android.graphics.Typeface;

import com.sokoban.maryblaa.sokoban.game.Game;
import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.graphics.CompareFunction;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.SpriteFont;
import com.sokoban.maryblaa.sokoban.graphics.TextBuffer;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    private Camera hudCamera, sceneCamera;
    private Mesh meshTree, meshRoad;
    private Texture texTree, texRoad;
    private Material matTree, matRoad;
    private Matrix4x4 worldRoad;
    private Matrix4x4[] worldTrees;

    private SpriteFont font;
    private TextBuffer text;
    private TextBuffer hudText;
    private Matrix4x4 hudMatr, worldText;

    public SokobanGame(Context context) {
        super(context);
    }

    @Override
    public void initialize() {
        Matrix4x4 projection = new Matrix4x4();
        Matrix4x4 view = new Matrix4x4();

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-100f, 100f, -100f, 100f, 0.0f, 100.0f);
        view = new Matrix4x4();
        hudCamera = new Camera();
        hudCamera.setProjection(projection);
        hudCamera.setView(view);

        projection = new Matrix4x4();
        projection.setPerspectiveProjection(-0.1f, 0.1f, -0.1f, 0.1f, 0.1f, 16.0f);
        view = new Matrix4x4();
        view.translate(0, -1, 0);
        sceneCamera = new Camera();
        sceneCamera.setProjection(projection);
        sceneCamera.setView(view);

        matTree = new Material();
        matTree.setAlphaTestFunction(CompareFunction.GREATER);
        matTree.setAlphaTestValue(0.9f);

        matRoad = new Material();

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

        hudMatr = new Matrix4x4();

        worldText = new Matrix4x4();
        worldText.translate(-0.75f, 0, -1);
        worldText.scale(0.05f); // textgröße
        worldText.rotateY(90);
    }

    @Override
    public void loadContent() {
        font = graphicsDevice.createSpriteFont(Typeface.DEFAULT, 16);
        text = graphicsDevice.createTextBuffer(font, 128);
        text.setText("Hallo, Welt!");

        hudText = graphicsDevice.createTextBuffer(font, 128);
        hudText.setText("Testspieler");

        try {
            InputStream stream;

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

        graphicsDevice.setCamera(sceneCamera);

        // Strasse zeichnen
        renderer.drawMesh(meshRoad, matRoad, worldRoad);

        // Bäume zeichnen
        for (Matrix4x4 worldTree : worldTrees)
            renderer.drawMesh(meshTree, matTree, worldTree);

        // Text in 3D zeichnen
        renderer.drawText(text, worldText);

        // Text auf dem HUD zeichnen
        graphicsDevice.setCamera(hudCamera);
        renderer.drawText(hudText, hudMatr);
    }

    @Override
    public void resize(int width, int height) {
        float aspect = (float) width / (float) height;
        Matrix4x4 projection;

        projection = new Matrix4x4();
        projection.setOrhtogonalProjection(-width / 2, width / 2, -height / 2, height / 2, 0.0f, 100.0f);
        hudCamera.setProjection(projection);

        projection = new Matrix4x4();
        projection.setPerspectiveProjection(-0.1f * aspect, 0.1f * aspect, -0.1f, 0.1f, 0.1f, 100.0f);
        sceneCamera.setProjection(projection);

        hudMatr.translate(-width / 50, height / 2 - 80, 0); // wie weit nach rechts, wie hoch von oben weg, ...
    }


}
