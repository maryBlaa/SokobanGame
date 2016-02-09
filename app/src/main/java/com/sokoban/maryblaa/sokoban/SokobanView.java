package com.sokoban.maryblaa.sokoban;

import android.content.Context;
import android.util.Log;

/**
 * Created by maryBlaa on 02.02.2016.
 */
public class SokobanView extends android.opengl.GLSurfaceView {

    private SokobanGame game;

    public SokobanView(Context context) {
        super(context);
        game = new SokobanGame(context);
        setRenderer(game);

        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }
}
