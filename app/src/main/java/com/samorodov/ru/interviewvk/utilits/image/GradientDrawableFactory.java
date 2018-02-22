package com.samorodov.ru.interviewvk.utilits.image;

import android.graphics.drawable.GradientDrawable;

public class GradientDrawableFactory {
    public static GradientDrawable createGradient(int start, int end) {
        return new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{start, end}
        );
    }
}
