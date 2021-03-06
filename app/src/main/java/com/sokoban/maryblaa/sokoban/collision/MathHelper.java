package com.sokoban.maryblaa.sokoban.collision;

public class MathHelper {

    public static float clamp(float value, float min, float max) {
        value = Math.min(value, max);
        value = Math.max(value, min);
        return value;
    }

    public static int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min));
    }

    public static int randomInt(double min, double max) {
        return randomInt((int) min, (int) max);
    }
}