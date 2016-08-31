package com.sokoban.maryblaa.sokoban.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sokoban.maryblaa.sokoban.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Prefs {
    private static final String TAG = Prefs.class.getSimpleName();
    private static final String SHARED_PREF_FILE = "sokoban.pref";

    public static final String PLAYER1 = "player_1";
    public static final String PLAYER2 = "player_2";

    public static SharedPreferences getSharedPreferences() {
        return getSharedPreferences(SHARED_PREF_FILE);
    }

    public static void setHighscore(String player, int score) {

        Set<String> q = new HashSet<>();
        q.add(player);
        q.add(score+"");

        getSharedPreferences().edit().putStringSet("1", q).apply();
    }

    public static Set<String> getHighscore(String key) {
        Set<String> q = new HashSet<>();
        q.add("");
        return getSharedPreferences().getStringSet(key, q);
    }

    public static SharedPreferences getSharedPreferences(String fileName) {
        return App.context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void setString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }


    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }

    public static void setInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }


    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static void setBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }
}