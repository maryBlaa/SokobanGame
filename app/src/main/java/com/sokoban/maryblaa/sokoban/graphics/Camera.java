package com.sokoban.maryblaa.sokoban.graphics;

import com.sokoban.maryblaa.sokoban.math.Matrix4x4;
import com.sokoban.maryblaa.sokoban.math.Vector3;
import com.sokoban.maryblaa.sokoban.math.Vector4;

public class Camera {

    private Matrix4x4 projection;
    private Matrix4x4 view;

    public Camera() {
        projection = new Matrix4x4();
        view = new Matrix4x4();
    }

    public Matrix4x4 getProjection() {
        return projection;
    }

    public Matrix4x4 getView() {
        return view;
    }

    public void setProjection(Matrix4x4 projection) {
        this.projection = projection;
    }

    public void setView(Matrix4x4 view) {
        this.view = view;
    }

    public Vector3 unproject(Vector3 v, float w) {
        Matrix4x4 viewProjection = projection.multiply(view);
        Matrix4x4 inverse = viewProjection.getInverse();
        Vector4 result = inverse.multiply(new Vector4(v, w));
        return new Vector3(
                result.getX() / result.getW(),
                result.getY() / result.getW(),
                result.getZ() / result.getW());
    }


    public Vector3 project(Vector3 v, float w) {
        Matrix4x4 viewProjection = projection.multiply(view);
        Vector4 result = viewProjection.multiply(new Vector4(v, w));
        return new Vector3(
                result.getX() / result.getW(),
                result.getY() / result.getW(),
                result.getZ() / result.getW());
    }
}
