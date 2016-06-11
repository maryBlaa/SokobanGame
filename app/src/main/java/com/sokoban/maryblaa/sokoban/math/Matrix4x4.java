package com.sokoban.maryblaa.sokoban.math;

import android.opengl.Matrix;

/**
 * Created by maryf on 02.02.2016.
 */
public class Matrix4x4 {

    public float[] m = new float[16];

    public Matrix4x4() {
        setIdentity();
    }

    public Matrix4x4(float[] m) {
        if (m.length < 16)
            throw new ArrayIndexOutOfBoundsException();

        for (int i = 0; i < 16; ++i)
            this.m[i] = m[i];
    }

    public Matrix4x4(Matrix4x4 m) {
        for (int i = 0; i < 16; ++i)
            this.m[i] = m.m[i];
    }

    public Matrix4x4(
            float m00, float m10, float m20, float m30,
            float m01, float m11, float m21, float m31,
            float m02, float m12, float m22, float m32,
            float m03, float m13, float m23, float m33) {
        m[0] = m00;
        m[4] = m10;
        m[8] = m20;
        m[12] = m30;
        m[1] = m01;
        m[5] = m11;
        m[9] = m21;
        m[13] = m31;
        m[2] = m02;
        m[6] = m12;
        m[10] = m22;
        m[14] = m32;
        m[3] = m03;
        m[7] = m13;
        m[11] = m23;
        m[15] = m33;
    }

    public static Matrix4x4 createRotationX(float angle) {
        Matrix4x4 mat = new Matrix4x4();
        return mat.rotateX(angle);
    }

    public static Matrix4x4 createRotationY(float angle) {
        Matrix4x4 mat = new Matrix4x4();
        return mat.rotateY(angle);
    }

    public static Matrix4x4 createRotationZ(float angle) {
        Matrix4x4 mat = new Matrix4x4();
        return mat.rotateZ(angle);
    }

    public static Matrix4x4 createScale(float s) {
        Matrix4x4 mat = new Matrix4x4();
        return mat.scale(s);
    }

    public static Matrix4x4 createScale(float x, float y, float z) {
        Matrix4x4 mat = new Matrix4x4();
        return mat.scale(x, y, z);
    }

    public static Matrix4x4 createTranslation(float x, float y, float z) {
        Matrix4x4 mat = new Matrix4x4();
        return mat.translateBy(x, y, z);
    }

    public static Matrix4x4 multiply(Matrix4x4 m1, Matrix4x4 m2) {
        Matrix4x4 result = new Matrix4x4();
        Matrix.multiplyMM(result.m, 0, m1.m, 0, m2.m, 0);
        return result;
    }

    public static Vector4 multiply(Matrix4x4 m, Vector4 v) {
        Vector4 result = new Vector4();
        Matrix.multiplyMV(result.v, 0, m.m, 0, v.v, 0);
        return result;
    }

    public Matrix4x4 getInverse() {
        Matrix4x4 result = new Matrix4x4();
        Matrix.invertM(result.m, 0, m, 0);
        return result;
    }

    public Matrix4x4 getTranspose() {
        Matrix4x4 result = new Matrix4x4();
        Matrix.transposeM(result.m, 0, m, 0);
        return result;
    }

    public Matrix4x4 multiply(Matrix4x4 matrix) {
        Matrix4x4 result = new Matrix4x4();
        Matrix.multiplyMM(result.m, 0, m, 0, matrix.m, 0);
        return result;
    }

    public Vector4 multiply(Vector4 vector) {
        Vector4 result = new Vector4();
        Matrix.multiplyMV(result.v, 0, m, 0, vector.v, 0);
        return result;
    }

    public Matrix4x4 rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(m, 0, angle, x, y, z);
        return this;
    }

    public Matrix4x4 rotateX(float angle) {
        Matrix.rotateM(m, 0, angle, 1, 0, 0);
        return this;
    }

    public Matrix4x4 rotateY(float angle) {
        Matrix.rotateM(m, 0, angle, 0, 1, 0);
        return this;
    }

    public Matrix4x4 rotateZ(float angle) {
        Matrix.rotateM(m, 0, angle, 0, 0, 1);
        return this;
    }

    public Matrix4x4 scale(float s) {
        Matrix.scaleM(m, 0, s, s, s);
        return this;
    }

    public Matrix4x4 scale(float x, float y, float z) {
        Matrix.scaleM(m, 0, x, y, z);
        return this;
    }

    public Matrix4x4 translateBy(float x, float y, float z) {
        Matrix.translateM(m, 0, x, y, z);
        return this;
    }

    public Matrix4x4 setIdentity() {
        Matrix.setIdentityM(m, 0);
        return this;
    }

    public Matrix4x4 setOrthogonalProjection(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(m, 0, left, right, bottom, top, near, far);
        return this;
    }

    public Matrix4x4 setPerspectiveProjection(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(m, 0, left, right, bottom, top, near, far);
        return this;
    }
}

