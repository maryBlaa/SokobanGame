package com.sokoban.maryblaa.sokoban.game;

import com.sokoban.maryblaa.sokoban.graphics.Camera;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryf on 02.02.2016.
 */
public class GraphicsDevice {

    GL10 gl;

    public void onSurfaceCreated(GL10 gl) {
        this.gl = gl;
    }

    public void clear(float red, float green, float blue, float alpha) {
        gl.glClearColor(red, green, blue, alpha);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void clear(float red, float green, float blue) {
        gl.glClearColor(red, green, blue, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void resize(int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    public void setCamera(Camera camera) {

        Matrix4x4 projection = camera.getProjection();
        Matrix4x4 view = camera.getView();

        Matrix4x4 viewProjection = Matrix4x4.multiply(projection, view);


        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(viewProjection.m, 0);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

}
