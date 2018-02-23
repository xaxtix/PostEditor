package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.support.annotation.DrawableRes;
import android.view.View;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;




public class ImagePickerAdditionalItem extends ImagePickerBaseItem {

    @DrawableRes
    private final int resId;

    public ImagePickerAdditionalItem(int iconRes, Consumer<View> onClick) {
        super(onClick);
        this.resId = iconRes;
    }

    @Override
    public void bindImage(SelectableImageView imageView) {
        imageView.setImageResource(resId);
        imageView.setFillColor(R.color.blue_20);
    }
}
