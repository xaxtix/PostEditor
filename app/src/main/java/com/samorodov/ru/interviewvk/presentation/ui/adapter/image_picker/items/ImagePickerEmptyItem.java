package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.view.View;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;



public class ImagePickerEmptyItem extends ImagePickerBaseItem {

    public ImagePickerEmptyItem(Consumer<View> onClick) {
        super(onClick);
    }

    @Override
    public void bindImage(SelectableImageView imageView) {
        imageView.setImageDrawable(null);
        imageView.setFillColor(R.color.ligth_grey);
    }
}
