package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.net.Uri;
import android.view.View;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;





public abstract class ImagePickerBaseItem {

    private final Consumer<View> onClick;

    public void accept(View view){
        onClick.accept(view);
    }

    ImagePickerBaseItem(Consumer<View> onClick) {
        this.onClick = onClick;
    }

    public abstract void bindImage(SelectableImageView imageView);


}
