package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.view.View;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;



/**
 * Created by xaxtix on 19.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public abstract class ImagePickerBaseItem {

    private final Consumer<View> onClick;

    public void accept(View view){
        onClick.accept(view);
    }

    protected ImagePickerBaseItem(Consumer<View> onClick) {
        this.onClick = onClick;
    }

    public abstract void bindImage(SelectableImageView imageView);

}
