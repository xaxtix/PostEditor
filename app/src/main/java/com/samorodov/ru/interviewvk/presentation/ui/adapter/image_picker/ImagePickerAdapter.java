package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerUriItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerAdditionalItem;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xaxtix on 13.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {


    @Nullable
    private Consumer<Uri> onImageSelectedListener;

    private Consumer<Boolean> onAdditionalListener;

    private final List<ImagePickerBaseItem> itemList = new ArrayList<>(3);

    public ImagePickerAdapter() {
        Consumer<Uri> uriConsumer = uri -> {
            if (onImageSelectedListener != null)
                onImageSelectedListener.accept(uri);
        };

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

        itemList.add(new ImagePickerAdditionalItem(v -> {
            if (onAdditionalListener != null)
                onAdditionalListener.accept(true);
        }));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new SelectableImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImagePickerBaseItem item = itemList.get(position);
        item.bindImage(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            item.accept(v);
            if (position != itemList.size() - 1 && onAdditionalListener != null)
                onAdditionalListener.accept(false);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SelectableImageView imageView;

        public ViewHolder(SelectableImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }

    public void setOnImageSelectedListener(@Nullable Consumer<Uri> onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }

    public void setOnAdditionalListener(Consumer<Boolean> onAdditionalListener) {
        this.onAdditionalListener = onAdditionalListener;
    }
}
