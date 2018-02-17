package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.samorodov.ru.interviewvk.presentation.ui.view.drawables.TextTransparentDrawable;
import com.samorodov.ru.interviewvk.presentation.ui.view.drawables.TextWhiteDrawable;

/**
 * Created by xaxtix on 18.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

class EditTextStyleDelegate {

    private final EditText editText;

    private int styleType = 0;

    EditTextStyleDelegate(EditText editText) {
        this.editText = editText;
        editText.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
    }

    public void toggleStyle(){
        styleType = (styleType + 1) % 3;
        updateStyle();
    }

    private void updateStyle() {
        switch (styleType){
            case 0:
                editText.setTextColor(Color.BLACK);
                editText.setBackground(null);
                break;
            case 1:
                editText.setBackground(new TextWhiteDrawable(editText.getContext()));
                editText.setTextColor(Color.BLACK);
                break;
            case 2:
                editText.setBackground(new TextTransparentDrawable(editText.getContext()));
                editText.setTextColor(Color.WHITE);
                break;
        }
    }
}
