package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerEmptyItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerUriItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerAdditionalItem;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by xaxtix on 13.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class ImagePickerAdapter extends BaseImagePickerAdapter {

    @Nullable
    private Consumer<Uri> onImageSelectedListener;

    private Consumer<Boolean> onAdditionalListener;


    @Override
    protected List<ImagePickerBaseItem> createItemList() {
        List<ImagePickerBaseItem> itemList = new ArrayList<>(4);

        Consumer<Uri> uriConsumer = uri -> {
            if (onImageSelectedListener != null)
                onImageSelectedListener.accept(uri);
        };

        itemList.add(new ImagePickerEmptyItem(v -> {
            if (onImageSelectedListener != null)
                onImageSelectedListener.accept(null);
        }));

        itemList.add(new ImagePickerUriItem(
                R.drawable.thumb_beach,
                Uri.parse("file:///android_asset/backgrounds/background2.png"),
                uriConsumer
        ));

        itemList.add(new ImagePickerUriItem(
                R.drawable.thumb_stars,
                Uri.parse("file:///android_asset/backgrounds/bg_stars_center.png"),
                uriConsumer
        ));

        itemList.add(new ImagePickerAdditionalItem(R.drawable.ic_toolbar_new, v -> {
            if (onAdditionalListener != null)
                onAdditionalListener.accept(true);
        }));

        return itemList;
    }

    protected void onItemClick(ImagePickerBaseItem item, ViewHolder holder, int position) {
        super.onItemClick(item, holder, position);
        int lastPosition = getItemCount() - 1;
        if (position != lastPosition)
            setSelectedPosition(position);

        if (position != getItemCount() - 1 && onAdditionalListener != null)
            onAdditionalListener.accept(false);
    }

    public void setOnImageSelectedListener(@Nullable Consumer<Uri> onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }

    public void setOnAdditionalListener(Consumer<Boolean> onAdditionalListener) {
        this.onAdditionalListener = onAdditionalListener;
    }

}
