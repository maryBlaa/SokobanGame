package com.sokoban.maryblaa.sokoban.graphics;

import com.sokoban.maryblaa.sokoban.math.Matrix4x4;

/**
 * Created by maryf on 02.02.2016.
 */
public class Camera {

    private Matrix4x4 projection;
    private Matrix4x4 view;

    public Matrix4x4 getProjection() {
        return projection;
    }

    public void setProjection(Matrix4x4 projection) {
        this.projection = projection;
    }

    public Matrix4x4 getView() {
        return view;
    }

    public void setView(Matrix4x4 view) {
        this.view = view;
    }
}
