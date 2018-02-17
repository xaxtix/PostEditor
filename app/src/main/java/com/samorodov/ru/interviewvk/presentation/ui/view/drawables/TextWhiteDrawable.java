package com.samorodov.ru.interviewvk.presentation.ui.view.drawables;

import android.content.Context;
import android.graphics.Color;

public class TextWhiteDrawable extends TextBackground {

    public TextWhiteDrawable(Context context) {
        super(context);
    }

    @Override
    public int getColor(Context context) {
        return Color.WHITE;
    }
}
