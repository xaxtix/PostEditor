package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;



public class ImagePickerUriItem extends ImagePickerBaseItem {

    @DrawableRes
    private final int thumbnailRes;

    private final Uri fileUri;

    public ImagePickerUriItem(int thumbnailRes, Uri fileUri, Consumer<Uri> consumer) {
        super(v -> consumer.accept(fileUri));
        this.thumbnailRes = thumbnailRes;
        this.fileUri = fileUri;
    }

    @Override
    public void bindImage(SelectableImageView imageView) {
        if (thumbnailRes > 0)
            imageView.setImageResource(thumbnailRes);
        else
            Glide.with(imageView)
                    .load(fileUri)
                    .apply(new RequestOptions().centerCrop())
                    .into(imageView);

        imageView.clearFillColor();
    }

    public boolean contains(Uri image) {
        return fileUri.equals(image);
    }
}
