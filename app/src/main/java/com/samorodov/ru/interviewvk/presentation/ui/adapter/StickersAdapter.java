package com.samorodov.ru.interviewvk.presentation.ui.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilites;
import com.samorodov.ru.interviewvk.presentation.ui.view.SquareImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.ViewHolder> {

    private List<Uri> stickers = new ArrayList<>();

    @Nullable
    Consumer<Uri> listener;

    public StickersAdapter(@Nullable Consumer<Uri> listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                AndroidUtilites.inflate(parent, R.layout.item_view_sticker)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri sticker = stickers.get(position);
        holder.setSticker(sticker);

        Glide.with(holder.itemView)
                .load(sticker)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public void setStickers(Collection<Uri> stickers) {
        this.stickers.clear();
        this.stickers.addAll(stickers);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        public SquareImageView imageView;
        private Uri sticker;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.accept(sticker);
            });
        }

        public void setSticker(Uri sticker) {
            this.sticker = sticker;
        }
    }

}
