package com.samorodov.ru.interviewvk;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilites;
import com.samorodov.ru.interviewvk.view.SelectableImageView;

/**
 * Created by xaxtix on 13.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

class ImagePikcerAdapter extends RecyclerView.Adapter<ImagePikcerAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new SelectableImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SelectableImageView imageView;

        public ViewHolder(SelectableImageView itemView) {
            super(itemView);
            this.imageView = itemView;

            Glide.with(itemView)
                    .load(Uri.parse("file:///android_asset/backgrounds/bg_stars_center.png"))
                    .into(itemView);
        }
    }
}
