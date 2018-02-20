package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;

import java.util.List;

/**
 * Created by xaxtix on 19.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public abstract class BaseImagePickerAdapter extends RecyclerView.Adapter<BaseImagePickerAdapter.ViewHolder> {


    private final List<ImagePickerBaseItem> itemList;

    public BaseImagePickerAdapter() {
        this.itemList = createItemList();
    }

    protected abstract List<ImagePickerBaseItem> createItemList();

    private int selectedPosition = 0;

    protected void onItemClick(ImagePickerBaseItem item, ViewHolder holder, int position) {
        item.accept(holder.imageView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectableImageView imageView = new SelectableImageView(parent.getContext());
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImagePickerBaseItem item = itemList.get(position);
        item.bindImage(holder.imageView);
        if (selectedPosition == position) holder.imageView.setSelected(true);
        else holder.imageView.setSelected(false);

        holder.imageView.setOnClickListener(v -> onItemClick(item, holder, position));
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

    public void setSelectedPosition(int selectedPosition) {
        int oldPosition = getSelectedPosition();
        this.selectedPosition = selectedPosition;

        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public List<ImagePickerBaseItem> getItemList() {
        return itemList;
    }
}
