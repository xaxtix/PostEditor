package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerAdditionalItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerUriItem;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by xaxtix on 19.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class AdditioanalImagePickerAdapter extends RecyclerView.Adapter
        <AdditioanalImagePickerAdapter.ViewHolder>{

    @Nullable
    private Consumer<Uri> onImageSelectedListener;

    private Consumer<Boolean> onAdditionalListener;

    private final List<ImagePickerBaseItem> itemList = new ArrayList<>(3);

    private int selectedPosition = 0;

    public AdditioanalImagePickerAdapter() {
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
        SelectableImageView imageView = new SelectableImageView(parent.getContext());
        imageView.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT,MATCH_PARENT));
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImagePickerBaseItem item = itemList.get(position);
        item.bindImage(holder.imageView);
        if (selectedPosition == position) holder.imageView.setSelected(true);
        else holder.imageView.setSelected(false);

        holder.imageView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            holder.imageView.setSelected(true);
            notifyItemChanged(oldPosition);
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    public void setOnImageSelectedListener(@Nullable Consumer<Uri> onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }

    public void setOnAdditionalListener(Consumer<Boolean> onAdditionalListener) {
        this.onAdditionalListener = onAdditionalListener;
    }
}
