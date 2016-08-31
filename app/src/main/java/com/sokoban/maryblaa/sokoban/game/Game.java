package com.sokoban.maryblaa.sokoban.game;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.view.View;

import com.sokoban.maryblaa.sokoban.graphics.GraphicsDevice;
import com.sokoban.maryblaa.sokoban.graphics.SokobanRenderer;
import com.sokoban.maryblaa.sokoban.input.InputSystem;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class Game implements Renderer {

    private boolean initialized;
    private long lastTime;

    protected View view;
    public Context context;
    public GraphicsDevice graphicsDevice;
    public SokobanRenderer renderer;
    protected InputSystem inputSystem;

    public Game(View view) {
        this.view = view;
        this.context = view.getContext();

        inputSystem = new InputSystem(view);

    }

    public void onDrawFrame(GL10 gl) {
        long currTime = System.currentTimeMillis();
        float deltaSeconds = (currTime - lastTime) / 1000.0f;

        eventUpdate(deltaSeconds);
        draw(deltaSeconds);

        lastTime = currTime;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        graphicsDevice.resize(width, height);
        resize(width, height);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        lastTime = System.currentTimeMillis();

        if (!initialized) {
            graphicsDevice = new GraphicsDevice();
            graphicsDevice.onSurfaceCreated(gl);

            renderer = new SokobanRenderer(graphicsDevice);

            initialize();
            initialized = true;

            loadContent();
        } else {
            graphicsDevice.onSurfaceCreated(gl);
            loadContent();
        }
    }

    public abstract void initialize();

    public abstract void loadContent();

    public abstract void eventUpdate(float deltaSeconds);

    public abstract void draw(float deltaSeconds);

    public abstract void resize(int width, int height);

    public boolean onBackPressed() {
        return false;
    }
}
