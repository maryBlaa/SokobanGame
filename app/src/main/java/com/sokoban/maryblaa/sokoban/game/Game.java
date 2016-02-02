package com.sokoban.maryblaa.sokoban.game;

import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryf on 02.02.2016.
 */
public abstract class Game implements Renderer {

    private boolean initialized;
    private long lastTime;
    protected GraphicsDevice graphicsDevice;

    private boolean blink = false;

    public void onDrawFrame(GL10 gl) {
//        long currTime = System.currentTimeMillis();
//        float deltaSeconds = (currTime - lastTime) / 1000.0f;
//
//        update(deltaSeconds);
//        draw(deltaSeconds);
//
//        lastTime = currTime;


        if (blink) {
            gl.glClearColor(0.0f, 0.5f, 1.0f, 1.0f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            blink = !blink;
        } else {
            gl.glClearColor(1.0f, 0.4f, 2.0f, 0.9f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            blink = !blink;
        }
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

            initialize();
            initialized = true;
        } else {
            graphicsDevice.onSurfaceCreated(gl);
        }
    }

    public abstract void initialize();

    public abstract void update(float deltaSeconds);

    public abstract void draw(float deltaSeconds);

    public abstract void resize(int width, int height);

}