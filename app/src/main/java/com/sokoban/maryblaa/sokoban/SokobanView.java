package com.sokoban.maryblaa.sokoban;

import android.content.Context;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanView extends android.opengl.GLSurfaceView {

    private SokobanGame game;

    public SokobanView(Context context) {
        super(context);
        game = new SokobanGame(this);
        setRenderer(game);

        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
}
