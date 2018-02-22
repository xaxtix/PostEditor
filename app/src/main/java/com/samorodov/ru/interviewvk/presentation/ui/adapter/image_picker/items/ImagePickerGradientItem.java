package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.graphics.drawable.GradientDrawable;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;
import com.samorodov.ru.interviewvk.utilits.image.GradientDrawableFactory;

public class ImagePickerGradientItem extends ImagePickerBaseItem {

    private final GradientDrawable gradientDrawable;

    public ImagePickerGradientItem(Consumer<GradientDrawable> onClick, int starColor, int endColor) {
        super(v -> onClick.accept(GradientDrawableFactory.createGradient(starColor, endColor)));
        gradientDrawable = GradientDrawableFactory.createGradient(starColor, endColor);
    }

    @Override
    public void bindImage(SelectableImageView imageView) {
        imageView.setImageDrawable(gradientDrawable);
    }

    public GradientDrawable getDrawable() {
        return gradientDrawable;
    }
}
