package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;

/**
 * Created by xaxtix on 19.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class ImagePickerUriItem extends ImagePickerBaseItem {

    @DrawableRes
    private final int thumbnailRes;

    private final Uri fileUri;

    public ImagePickerUriItem(int thumbnailRes, Uri fileUri,Consumer<Uri> consumer) {
        super(v -> consumer.accept(fileUri));
        this.thumbnailRes = thumbnailRes;
        this.fileUri = fileUri;
    }

    @Override
    public void bindImage(SelectableImageView imageView) {
        imageView.setImageResource(thumbnailRes);
    }
}
