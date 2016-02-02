package com.sokoban.maryblaa.sokoban;

import android.content.Context;

import com.sokoban.maryblaa.sokoban.graphics.SokobanRenderer;

/**
 * Created by maryf on 02.02.2016.
 */
public class SokobanView extends android.opengl.GLSurfaceView {

    private SokobanRenderer renderer;

    public SokobanView(Context context) {
        super(context);

        renderer = new SokobanRenderer();
        setRenderer(renderer);

        setRenderMode(RENDERMODE_CONTINUOUSLY);

    }


    @Override
    public void onPause() {
        super.onPause();
        //game.pause();
    }

    @Override
    public void onResume() {
        //game.resume();
        super.onResume();
    }
}
