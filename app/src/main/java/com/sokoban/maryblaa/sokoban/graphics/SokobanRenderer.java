package com.sokoban.maryblaa.sokoban.graphics;

import com.sokoban.maryblaa.sokoban.game.GraphicsDevice;
import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryf on 02.02.2016.
 */
public class SokobanRenderer implements android.opengl.GLSurfaceView.Renderer {

    private boolean blink = false;

    private GraphicsDevice graphicsDevice;

    public SokobanRenderer(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) { // wird zu Beginn aufgerufen und dient somit als initalize-Methode in unserer Spielschleife
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) { // wird aufgerufen wenn sich die Größe bzw. Auflösung der Surface ändert. Dies passiert z.B. wenn das Gerät gedreht wird
    }

    @Override
    public void onDrawFrame(GL10 gl) { // wird immer dann aufgerufen wenn das Bild neu gezeichnet werden soll
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

    public void drawMesh(Mesh mesh, Matrix4x4 world) {
        graphicsDevice.setWorldMatrix(world);

        VertexBuffer vertexBuffer = mesh.getVertexBuffer();
        graphicsDevice.bindVertexBuffer(vertexBuffer);
        graphicsDevice.draw(mesh.getMode(), 0, vertexBuffer.getNumVertices());
        graphicsDevice.unbindVertexBuffer(vertexBuffer);
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }
}
