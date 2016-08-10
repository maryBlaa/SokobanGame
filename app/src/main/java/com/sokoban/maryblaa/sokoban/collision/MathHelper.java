package com.sokoban.maryblaa.sokoban.collision;

/**
 * Created by maryf on 16.05.2016.
 */
public class MathHelper {

    public static float clamp(float value, float min, float max) {
        value = Math.min(value, max);
        value = Math.max(value, min);
        return value;
    }

    public static int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max-min));
    }
}
