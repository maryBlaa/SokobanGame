package com.sokoban.maryblaa.sokoban.graphics;

import java.nio.ByteBuffer;

/**
 * Created by maryf on 02.02.2016.
 */
public class VertexBuffer {

    ByteBuffer buffer;
    private VertexElement[] elements;
    int countVertices;

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public VertexElement[] getElements() {
        return elements;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public int getCountVertices() {
        return countVertices;
    }

    public void setElements(VertexElement[] elements) {
        this.elements = elements;
    }

    public void setNumVertices(int countVertices) {
        this.countVertices = countVertices;
    }
}
