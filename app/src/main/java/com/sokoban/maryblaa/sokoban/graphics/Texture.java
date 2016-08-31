package com.sokoban.maryblaa.sokoban.graphics;

public class Texture {

    private int handle;
    private int width, height;

    Texture(int handle, int width, int height) {
        this.handle = handle;
        this.width = width;
        this.height = height;
    }

    int getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
