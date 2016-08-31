package com.sokoban.maryblaa.sokoban;

import android.content.Context;

public class SokobanView extends android.opengl.GLSurfaceView {

    private SokobanGame game;

    public SokobanView(Context context) {
        super(context);
        game = new SokobanGame(this);
        setRenderer(game);

        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public boolean onBackPressed() {
        return game.onBackPressed();
    }
}
