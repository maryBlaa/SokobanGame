package com.sokoban.maryblaa.sokoban;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    SokobanView sokobanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
