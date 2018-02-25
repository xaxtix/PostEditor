package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerEmptyItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerGradientItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerUriItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerAdditionalItem;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by xaxtix on 13.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class ImagePickerAdapter extends BaseImagePickerAdapter {

    @Nullable
    private Consumer<Uri> onImageSelectedListener;

    private Consumer<Boolean> onAdditionalListener;
    private Consumer<GradientDrawable> onGradientSelectedListener;

    public ImagePickerAdapter(Context context) {
        super(context);
    }

    @Override
    protected List<ImagePickerBaseItem> createItemList() {
        List<ImagePickerBaseItem> itemList = new ArrayList<>(10);

        Consumer<Uri> uriConsumer = uri -> {
            if (onImageSelectedListener != null)
                onImageSelectedListener.accept(uri);
        };

        Consumer<GradientDrawable> gradientConsumer = gradient -> {
            if (onGradientSelectedListener != null)
                onGradientSelectedListener.accept(gradient);
        };

        itemList.add(new ImagePickerEmptyItem(v -> {
            if (onImageSelectedListener != null)
                onImageSelectedListener.accept(null);
        }));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                getColor(context, R.color.blue_start),
                getColor(context, R.color.blue_end)
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                getColor(context, R.color.green_start),
                getColor(context, R.color.green_end)
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                getColor(context, R.color.orange_start),
                getColor(context, R.color.orange_end)
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                getColor(context, R.color.red_start),
                getColor(context, R.color.red_end)
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                getColor(context, R.color.purple_start),
                getColor(context, R.color.purple_end)
        ));


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
    }

    public void setOnImageSelectedListener(@Nullable Consumer<Uri> onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }

    public void setOnAdditionalListener(Consumer<Boolean> onAdditionalListener) {
        this.onAdditionalListener = onAdditionalListener;
    }

    public void setOnGradientSelectedListener(Consumer<GradientDrawable> onGradientSelectedListener) {
        this.onGradientSelectedListener = onGradientSelectedListener;
    }
}
