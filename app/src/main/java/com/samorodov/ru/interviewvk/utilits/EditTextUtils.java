package com.samorodov.ru.interviewvk.utilits;

import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by xaxtix on 23.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class EditTextUtils {

    public static void addRemoveHintTextWatcher(EditText editText, @StringRes int hintRes) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    editText.setHint(editText.getContext().getString(hintRes));
                } else {
                    editText.setHint("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
