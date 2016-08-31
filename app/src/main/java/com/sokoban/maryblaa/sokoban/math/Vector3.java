package com.sokoban.maryblaa.sokoban.math;

public class Vector3 {

    public float[] v = new float[3];

    public Vector3() {
        v[0] = 0.0f;
        v[1] = 0.0f;
        v[2] = 0.0f;
    }

    public Vector3(Vector2 v2, float z) {
        v[0] = v2.v[0];
        v[1] = v2.v[1];
        v[2] = z;
    }

    public Vector3(float x, float y, float z) {
        v[0] = x;
        v[1] = y;
        v[2] = z;
    }

    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.v[0] + v2.v[0],
                v1.v[1] + v2.v[1],
                v1.v[2] + v2.v[2]);
    }

    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        Vector3 result = new Vector3();
        result.v[0] = v1.v[1] * v2.v[2] - v1.v[2] * v2.v[1];
        result.v[1] = v1.v[2] * v2.v[0] - v1.v[0] * v2.v[2];
        result.v[2] = v1.v[0] * v2.v[1] - v1.v[1] * v2.v[0];
        return result;
    }

    public static Vector3 divide(Vector3 v, float s) {
        return new Vector3(
                v.v[0] / s,
                v.v[1] / s,
                v.v[2] / s);
    }

    public static float dot(Vector3 v1, Vector3 v2) {
        return v1.v[0] * v2.v[0] + v1.v[1] * v2.v[1] + v1.v[2] * v2.v[2];
    }

    public static Vector3 multiply(float s, Vector3 v) {
        return new Vector3(
                s * v.v[0],
                s * v.v[1],
                s * v.v[2]);
    }

    public static Vector3 subtract(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.v[0] - v2.v[0],
                v1.v[1] - v2.v[1],
                v1.v[2] - v2.v[2]);
    }

    public float get(int index) {
        return v[index];
    }

    public float getX() {
        return v[0];
    }

    public float getY() {
        return v[1];
    }

    public float getZ() {
        return v[2];
    }

    public float getLength() {
        return (float) Math.sqrt(
                v[0] * v[0] +
                        v[1] * v[1] +
                        v[2] * v[2]);
    }

    public float getLengthSqr() {
        return (v[0] * v[0] +
                v[1] * v[1] +
                v[2] * v[2]);
    }

    public Vector3 normalize() {
        float l = getLength();
        for (int i = 0; i < 3; ++i)
            v[i] /= l;
        return this;
    }

    public static Vector3 normalize(Vector3 v) {
        Vector3 result = new Vector3();
        float l = v.getLength();
        for (int i = 0; i < 3; ++i)
            result.v[i] /= l;
        return result;
    }

    public void set(int index, float value) {
        v[index] = value;
    }

    public void setX(float x) {
        v[0] = x;
    }

    public void setY(float y) {
        v[1] = y;
    }

    public void setZ(float z) {
        v[2] = z;
    }

}
