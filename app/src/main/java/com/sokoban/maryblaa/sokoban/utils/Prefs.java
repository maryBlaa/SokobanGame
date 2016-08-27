package com.sokoban.maryblaa.sokoban.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sokoban.maryblaa.sokoban.App;


/**
 * Helper class to provide simple access to Shared Preferences
 */
public class Prefs {
    private static final String TAG = Prefs.class.getSimpleName();
    private static final String SHARED_PREF_FILE = "sokoban.pref";

    public static final String PLAYER1 = "player_1";
    public static final String PLAYER2 = "player_2";


    public static SharedPreferences getSharedPreferences() {
        return getSharedPreferences(SHARED_PREF_FILE);
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