package com.sokoban.maryblaa.sokoban.graphics;

import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

/**
 * Created by maryBlaa on 02.02.2016.
 */
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
}
