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

    Consumer onPickImageListener;

    Consumer<Uri> uriConsumer = uri -> {

    };

    @Override
    protected List<ImagePickerBaseItem> createItemList() {
        List<ImagePickerBaseItem> itemList = new ArrayList<>();

        itemList.add(new ImagePickerAdditionalItem(R.drawable.ic_photopicker_camera, v -> {

        }));

        itemList.add(new ImagePickerAdditionalItem(R.drawable.ic_photopicker_albums, v -> {
            if (onPickImageListener != null) onPickImageListener.accept(null);
        }));


        return itemList;
    }

    public void setOnPickImageListener(Consumer onPickImageListener) {
        this.onPickImageListener = onPickImageListener;
    }

    public void addImage(Uri image) {
        getItemList().add(new ImagePickerUriItem(0, image, uriConsumer));
        notifyItemInserted(getItemCount() - 1);
    }
}
