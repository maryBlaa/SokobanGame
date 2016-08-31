package com.sokoban.maryblaa.sokoban.graphics;

import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

public class SokobanRenderer {

    private GraphicsDevice graphicsDevice;

    public SokobanRenderer(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
    }

    public void drawMesh(Mesh mesh, Material material, Matrix4x4 world) {
        graphicsDevice.setWorldMatrix(world);

        setupMaterial(material);

        VertexBuffer vertexBuffer = mesh.getVertexBuffer();
        graphicsDevice.bindVertexBuffer(vertexBuffer);
        graphicsDevice.draw(mesh.getMode(), 0, vertexBuffer.getNumVertices());
        graphicsDevice.unbindVertexBuffer(vertexBuffer);
    }

    public void drawText(TextBuffer textBuffer, Matrix4x4 position) {
        Mesh mesh = textBuffer.getMesh();
        drawMesh(mesh, textBuffer.getSpriteFont().getMaterial(), position);
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }

    private void setupMaterial(Material material) {
        graphicsDevice.bindTexture(material.getTexture());
        graphicsDevice.setTextureFilters(material.getTextureFilterMin(), material.getTextureFilterMag());
        graphicsDevice.setTextureWrapMode(material.getTextureWrapModeU(), material.getTextureWrapModeV());
        graphicsDevice.setTextureBlendMode(material.getTextureBlendMode());
        graphicsDevice.setTextureBlendColor(material.getTextureBlendColor());

        graphicsDevice.setMaterialColor(material.getMaterialColor());
        graphicsDevice.setBlendFactors(material.getBlendSourceFactor(), material.getBlendDestFactor());

        graphicsDevice.setCullSide(material.getCullSide());
        graphicsDevice.setDepthTest(material.getDepthTestFunction());
        graphicsDevice.setDepthWrite(material.getDepthWrite());
        graphicsDevice.setAlphaTest(material.getAlphaTestFunction(), material.getAlphaTestValue());
    }

    public void drawRect(float[] bounds, Material material, Matrix4x4 world) {
        VertexElement[] elements = new VertexElement[]{
                new VertexElement(0, 16, GL10.GL_FLOAT, 2, VertexElement.VertexSemantic.VERTEX_ELEMENT_POSITION),
                new VertexElement(8, 16, GL10.GL_FLOAT, 2, VertexElement.VertexSemantic.VERTEX_ELEMENT_TEXCOORD)
        };

        ByteBuffer data = ByteBuffer.allocateDirect(6 * 16 * 1);

        VertexBuffer vertexBuffer = new VertexBuffer();
        vertexBuffer.setElements(elements);
        vertexBuffer.setBuffer(data);
        vertexBuffer.setNumVertices(0);

        Mesh mesh = new Mesh(vertexBuffer, GL10.GL_TRIANGLES);

        data.position(0);

        // Dreieck 1
        float posLeft = 0;
        float posRight = bounds[0];
        float posTop =0;
        float posBottom = (bounds[1]);
        float texLeft = 0;
        float texRight = (float) bounds[0] / (float) material.getTexture().getWidth();
        float texTop = 1.0f;
        float texBottom = 1.0f - (float)bounds[1] / (float) material.getTexture().getHeight();

        posLeft = 5.0f;
        texBottom = 0.8598633f;
        posBottom = -2.0f;
        texLeft = 0.60253906f;
        posRight = 84.0f;
        texTop = 0.9140625f;
        texRight = 0.6411133f;
        posTop = 109.0f;

        // Dreieck 1
        data.putFloat(posLeft);
        data.putFloat(posTop);
        data.putFloat(texLeft);
        data.putFloat(texTop);
        data.putFloat(posLeft);
        data.putFloat(posBottom);
        data.putFloat(texLeft);
        data.putFloat(texBottom);
        data.putFloat(posRight);
        data.putFloat(posTop);
        data.putFloat(texRight);
        data.putFloat(texTop);

        // Dreieck 2
        data.putFloat(posRight);
        data.putFloat(posTop);
        data.putFloat(texRight);
        data.putFloat(texTop);
        data.putFloat(posLeft);
        data.putFloat(posBottom);
        data.putFloat(texLeft);
        data.putFloat(texBottom);
        data.putFloat(posRight);
        data.putFloat(posBottom);
        data.putFloat(texRight);
        data.putFloat(texBottom);

        data.position(0);
        mesh.getVertexBuffer().setNumVertices(6);

        drawMesh(new Mesh(vertexBuffer, GL10.GL_TRIANGLES), material, world);
    }
}
