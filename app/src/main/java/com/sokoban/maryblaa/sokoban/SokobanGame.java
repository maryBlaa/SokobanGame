package com.sokoban.maryblaa.sokoban;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.sokoban.maryblaa.sokoban.game.Game;
import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.graphics.CompareFunction;
import com.sokoban.maryblaa.sokoban.graphics.Material;
import com.sokoban.maryblaa.sokoban.graphics.Mesh;
import com.sokoban.maryblaa.sokoban.graphics.SpriteFont;
import com.sokoban.maryblaa.sokoban.graphics.TextBuffer;
import com.sokoban.maryblaa.sokoban.graphics.Texture;
import com.sokoban.maryblaa.sokoban.input.InputEvent;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanGame extends Game {

    public static final String TAG = SokobanGame.class.getSimpleName();

    private Camera hudCamera, sceneCamera;
    private Mesh meshTree, meshRoad;
    private Texture texTree, texRoad;
    private Material matTree, matRoad;
    private Matrix4x4 worldRoad;
    private Matrix4x4[] worldTrees;

    private SpriteFont fontTitle, fontMenu;
    private TextBuffer textTitle;
    private Matrix4x4 matTitle;
    private TextBuffer[] textMenu;
    private Matrix4x4[] matMenu;
    private boolean showMenu;

    public SokobanGame(View view) {
        super(view);
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
    }

    @Override
    public void loadContent() {
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

        fontTitle = graphicsDevice.createSpriteFont(null, 64);
        fontMenu = graphicsDevice.createSpriteFont(null, 20);

        textTitle = graphicsDevice.createTextBuffer(fontTitle, 16);
        textMenu = new TextBuffer[]{
                graphicsDevice.createTextBuffer(fontMenu, 16),
                graphicsDevice.createTextBuffer(fontMenu, 16),
                graphicsDevice.createTextBuffer(fontMenu, 16),
                graphicsDevice.createTextBuffer(fontMenu, 16)
        };

        textTitle.setText("DrivingSim");
        textMenu[0].setText("Start Game");
        textMenu[1].setText("Options");
        textMenu[2].setText("Credits");
        textMenu[3].setText("Quit");

        matTitle = Matrix4x4.createTranslation(-120, 100, 0);
        matMenu = new Matrix4x4[]{
                Matrix4x4.createTranslation(0, -50, 0),
                Matrix4x4.createTranslation(0, -80, 0),
                Matrix4x4.createTranslation(0, -110, 0),
                Matrix4x4.createTranslation(0, -140, 0)
        };
    }

    @Override
    public void update(float deltaSeconds) {
        InputEvent inputEvent = inputSystem.peekEvent();
        while (inputEvent != null) {

            switch (inputEvent.getDevice()) {
                case KEYBOARD:
                    Log.d(TAG, "KEYBOARD");
                    Log.d(TAG, "" + inputEvent.getAction().toString());
                    switch (inputEvent.getAction()) {

                        case DOWN:
                            switch (inputEvent.getKeycode()) {
                                case KeyEvent.KEYCODE_MENU:
                                    showMenu = !showMenu;
                                    break;
                            }
                            break;
                    }
                    Log.d(TAG, "" + inputEvent.getDevice());
                    break;
                case ROTATION:
//                    Log.d(TAG, "ROTATION");
                    break;
                case TOUCHSCREEN:
                    Log.d(TAG, "" + inputEvent.getAction().toString());
                    switch (inputEvent.getAction()) {
                        case DOWN:
                            showMenu = !showMenu;
                            break;
                    }
                    break;
            }

            inputSystem.popEvent();
            inputEvent = inputSystem.peekEvent();
        }
    }

    @Override
    public void draw(float deltaSeconds) {
        graphicsDevice.clear(0.0f, 0.5f, 1.0f, 1.0f, 1.0f);

        graphicsDevice.setCamera(sceneCamera);

        if (!showMenu) {
            // Text auf dem HUD zeichnen
            graphicsDevice.setCamera(hudCamera);
            renderer.drawText(textTitle, matTitle);

            for (int i = 0; i < textMenu.length; ++i)
                renderer.drawText(textMenu[i], matMenu[i]);
        } else {
            // Strasse zeichnen
            renderer.drawMesh(meshRoad, matRoad, worldRoad);

            // Bäume zeichnen
            for (Matrix4x4 worldTree : worldTrees)
                renderer.drawMesh(meshTree, matTree, worldTree);

        }
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

        matTitle.setIdentity();
        matTitle.translate(-width / 2, height / 2 - 64, 0);
    }
}
