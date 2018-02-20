package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.net.Uri;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerAdditionalItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerUriItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xaxtix on 19.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class AdditionalImagePickerAdapter extends BaseImagePickerAdapter {

    private Consumer onPickImageListener;
    private Consumer onTakePhotoListener;
    private Consumer<Uri> onImageSelectedListener;
    private Consumer<Uri> uriConsumer = uri -> onImageSelectedListener.accept(uri);

    @Override
    protected void onItemClick(ImagePickerBaseItem item, ViewHolder holder, int position) {
        super.onItemClick(item, holder, position);
        if (position > 1)
            setSelectedPosition(position);
    }

    @Override
    protected List<ImagePickerBaseItem> createItemList() {
        List<ImagePickerBaseItem> itemList = new ArrayList<>();

        itemList.add(new ImagePickerAdditionalItem(R.drawable.ic_photopicker_camera, v -> {
            if (onTakePhotoListener != null) onTakePhotoListener.accept(null);
        }));

        itemList.add(new ImagePickerAdditionalItem(R.drawable.ic_photopicker_albums, v -> {
            if (onPickImageListener != null) onPickImageListener.accept(null);
        }));


        setSelectedPosition(-1);
        return itemList;
    }

    public void setOnImageSelectedListener(Consumer<Uri> listener){
        onImageSelectedListener = listener;
    }
    public void setOnPickImageListener(Consumer listener) {
        this.onPickImageListener = listener;
    }

    public void setTakeFotoListener(Consumer<Uri> listener) {
        this.onTakePhotoListener = listener;
    }

    public void addImage(Uri image) {
        getItemList().add(2, new ImagePickerUriItem(0, image, uriConsumer));
        setSelectedPosition(2);
        notifyItemInserted(2);
    }
}
