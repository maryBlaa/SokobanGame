package com.sokoban.maryblaa.sokoban.graphics;

/**
 * Created by maryBlaa on 08.02.2016.
 */
public class Texture {

    private int height;
    private int width;
    private int handle;

    Texture(int height, int handle, int width) {
        this.height = height;
        this.handle = handle;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    int getHandle() {
        return handle;
    }
}
