package com.samorodov.ru.interviewvk.presentation.ui.view.drawables;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.samorodov.ru.interviewvk.R;

public class TextTransparentDrawable extends TextBackground {

    public TextTransparentDrawable(Context context) {
        super(context);
    }

    @Override
    public int getColor(Context context) {
        return ContextCompat.getColor(context,R.color.white_30);
    }
}
