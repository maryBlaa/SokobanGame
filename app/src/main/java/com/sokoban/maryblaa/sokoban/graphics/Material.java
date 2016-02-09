package com.sokoban.maryblaa.sokoban.graphics;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by maryBlaa on 08.02.2016.
 */
public class Material {

    private Texture texture;

    private TextureFilter texFilterMin, texFilterMag;
    private TextureWrapMode texWrapU, texWrapV;
    private TextureBlendMode texBlendMode;
    private float[] texBlendColor;

    private float[] colorMaterial;
    private BlendFactor blendSrcFactor, blendDstFactor;

    private Side cullSide;
    private CompareFunction depthTestFunction;
    private boolean depthWrite;
    private CompareFunction alphaTestFunction;
    private float alphaTestValue;

    public Material() {
        Log.i("", "Meterial() Material");
        texture = null;

        texFilterMin = TextureFilter.LINEAR;
        texFilterMag = TextureFilter.LINEAR;
        texWrapU = TextureWrapMode.REPEAT;
        texWrapV = TextureWrapMode.REPEAT;
        texBlendMode = TextureBlendMode.MODULATE;
        texBlendColor = new float[] {0.0f, 0.0f, 0.0f, 0.0f};

        colorMaterial = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
        blendSrcFactor = BlendFactor.ONE;
        blendDstFactor = BlendFactor.ZERO;

        cullSide = Side.NONE;
        depthTestFunction = CompareFunction.LESS;
        depthWrite = true;
        alphaTestFunction = CompareFunction.ALWAYS;
        alphaTestValue = 0.0f;
    }

    public Texture getTexture() {
        return texture;
    }

    public TextureFilter getTextureFilterMin() {
        return texFilterMin;
    }

    public TextureFilter getTextureFilterMag() {
        return texFilterMag;
    }

    public TextureWrapMode getTextureWrapModeU() {
        return texWrapU;
    }

    public TextureWrapMode getTextureWrapModeV() {
        return texWrapV;
    }

    public TextureBlendMode getTextureBlendMode() {
        return texBlendMode;
    }

    public float[] getTextureBlendColor() {
        return texBlendColor;
    }

    public float[] getMaterialColor() {
        return colorMaterial;
    }

    public BlendFactor getBlendSourceFactor() {
        return blendSrcFactor;
    }

    public BlendFactor getBlendDestFactor() {
        return blendDstFactor;
    }

    public Side getCullSide() {
        return cullSide;
    }

    public CompareFunction getDepthTestFunction() {
        return depthTestFunction;
    }

    public boolean getDepthWrite() {
        return depthWrite;
    }

    public CompareFunction getAlphaTestFunction() {
        return alphaTestFunction;
    }

    public float getAlphaTestValue() {
        return alphaTestValue;
    }

    public void setTexture(Texture texture) {
        Log.i("", "setTexture() Material");
        this.texture = texture;
    }

    public void setTextureFilter(TextureFilter filterMin, TextureFilter filterMag) {
        if (filterMag != TextureFilter.NEAREST && filterMag != TextureFilter.LINEAR)
            throw new InvalidParameterException("Magnification filter must be either NEAREST or LINEAR");

        this.texFilterMin = filterMin;
        this.texFilterMag = filterMag;
    }

    public void setTextureWrap(TextureWrapMode texWrapU, TextureWrapMode texWrapV) {
        this.texWrapU = texWrapU;
        this.texWrapV = texWrapV;
    }

    public void setTextureBlendMode(TextureBlendMode texBlendMode) {
        this.texBlendMode = texBlendMode;
    }

    public void setTextureBlendColor(float[] color) {
        if (colorMaterial.length != 4)
            throw new InvalidParameterException("Color must contain 4 elements (RGBA).");

        this.texBlendColor = color.clone();
    }

    public void setColorMaterial(float[] colorMaterial) {
        if (colorMaterial.length != 4)
            throw new InvalidParameterException("Color must contain 4 elements (RGBA).");

        this.colorMaterial = colorMaterial;
    }

    public void setBlendFactors(BlendFactor srcFactor, BlendFactor dstFactor) {
        if (srcFactor == BlendFactor.SRC_COLOR || srcFactor == BlendFactor.ONE_MINUS_SRC_COLOR)
            throw new InvalidParameterException("Invalid source factor.");
        if (dstFactor == BlendFactor.DST_COLOR || dstFactor == BlendFactor.ONE_MINUS_DST_COLOR)
            throw new InvalidParameterException("Invalid destination factor.");

        this.blendSrcFactor = srcFactor;
        this.blendDstFactor = dstFactor;
    }

    public void setCullSide(Side cullSide) {
        this.cullSide = cullSide;
    }

    public void setDepthTestFunction(CompareFunction depthTestFunction) {
        this.depthTestFunction = depthTestFunction;
    }

    public void setDepthWrite(boolean depthWrite) {
        this.depthWrite = depthWrite;
    }

    public void setAlphaTestFunction(CompareFunction alphaTestFunction) {
        this.alphaTestFunction = alphaTestFunction;
    }

    public void setAlphaTestValue(float ref) {
        if (ref < 0.0f || ref > 1.0f)
            throw new InvalidParameterException("Alpha test reference value must lie in the range between 0 and 1");

        this.alphaTestValue = ref;
    }
}
