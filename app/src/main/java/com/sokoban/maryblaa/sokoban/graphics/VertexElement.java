package com.sokoban.maryblaa.sokoban.graphics;

/**
 * Created by maryf on 02.02.2016.
 */
public class VertexElement {

    private int offset;
    private int stride;
    private int type;
    private int count;
    private VertexSemantic semantic;

    public VertexElement(int offset, int stride, int type, int count, VertexSemantic semantic) {
        this.offset = offset;
        this.stride = stride;
        this.type = type;
        this.count = count;
        this.semantic = semantic;
    }

    public enum VertexSemantic {
        VERTEX_ELEMENT_NONE,
        VERTEX_ELEMENT_POSITION,
        VERTEX_ELEMENT_COLOR,
        VERTEX_ELEMENT_TEXCOORD
    }

    ;

    public int getOffset() {
        return offset;
    }

    public int getStride() {
        return stride;
    }

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public VertexSemantic getSemantic() {
        return semantic;
    }
}
