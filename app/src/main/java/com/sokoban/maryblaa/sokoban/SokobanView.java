package com.sokoban.maryblaa.sokoban;

import android.content.Context;

/**
 * Created by maryf on 02.02.2016.
 */
public class SokobanView extends android.opengl.GLSurfaceView {


    public SokobanView(Context context) {
        super(context);
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
