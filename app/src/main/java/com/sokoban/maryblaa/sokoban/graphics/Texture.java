package com.sokoban.maryblaa.sokoban.graphics;

/**
 * Created by maryBlaa on 08.02.2016.
 */
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
