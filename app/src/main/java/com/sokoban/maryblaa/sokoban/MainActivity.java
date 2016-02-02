package com.sokoban.maryblaa.sokoban;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    SokobanView sokobanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sokobanView = new SokobanView(this);
        setContentView(sokobanView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sokobanView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sokobanView.onResume();
    }
}
