package com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.items.ImagePickerBaseItem;
import com.samorodov.ru.interviewvk.presentation.ui.view.SelectableImageView;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



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
        return new ViewHolder(AndroidUtilities.inflate(parent, getItemLayout()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImagePickerBaseItem item = itemList.get(position);
        item.bindImage(holder.imageView);
        if (selectedPosition == position) holder.imageView.setSelected(true);
        else holder.imageView.setSelected(false);

        holder.itemView.setOnClickListener(v -> onItemClick(item, holder,position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @LayoutRes
    public int getItemLayout() {
        return R.layout.item_image_picker;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        SelectableImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
