package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerGradientItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerUriItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerAdditionalItem;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;
import com.samorodov.ru.interviewvk.utilits.image.GradientDrawableFactory;

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
    private Consumer<GradientDrawable> onGradientSelectedListener;


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
                Color.parseColor("#30F2D2"),
                Color.parseColor("#2E7AE6")
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                Color.parseColor("#CBE645"),
                Color.parseColor("#47B347")
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                Color.parseColor("#FFCC33"),
                Color.parseColor("#FF7733")
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                Color.parseColor("#FF3355"),
                Color.parseColor("#990F6B")
        ));

        itemList.add(new ImagePickerGradientItem(gradientConsumer,
                Color.parseColor("#F8A6FF"),
                Color.parseColor("#6C6CD9")
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

        if (position != getItemCount() - 1 && onAdditionalListener != null)
            onAdditionalListener.accept(false);
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
