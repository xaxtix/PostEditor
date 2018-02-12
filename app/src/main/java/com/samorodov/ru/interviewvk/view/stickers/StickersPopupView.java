package com.samorodov.ru.interviewvk.view.stickers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.adapter.StickersAdapter;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilites;
import com.samorodov.ru.interviewvk.utilits.AnimatorHelper;
import com.samorodov.ru.interviewvk.utilits.StickerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickersPopupView extends FrameLayout {

    @BindView(R.id.blackout) View blackout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.stickers_frame) View frame;
    @BindView(R.id.divider) View divider;

    boolean expand = false;

    Animator lastAnimator;

    StickersAdapter adapter;

    Consumer<Uri> stickerListener;

    public StickersPopupView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public StickersPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StickersPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        AndroidUtilites.inflateMerge(this, R.layout.view_stickers_popup);
        ButterKnife.bind(this);
        blackout.setVisibility(GONE);
        frame.setVisibility(GONE);
        blackout.setOnClickListener(v -> toggle());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        adapter = new StickersAdapter(sticker -> {
            if (stickerListener != null) {
                toggle();
                stickerListener.accept(sticker);
            }
        });

        adapter.setStickers(StickerUtils.getStickersUriList());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int y = recyclerView.computeVerticalScrollOffset();
                divider.setVisibility(y == 0 ? INVISIBLE : VISIBLE);
            }
        });
    }

    public void toggle() {
        expand = !expand;
        blackout.setClickable(expand);
        if (blackout.getVisibility() == GONE) {
            blackout.setVisibility(VISIBLE);
            frame.setVisibility(VISIBLE);
            AndroidUtilites.oneshotLayoutChangeListener(frame, view -> {
                blackout.setAlpha(0);
                frame.setTranslationY(frame.getHeight());
                toggleAnimation();
            });
        } else {
            toggleAnimation();
        }

    }

    private void toggleAnimation() {
        if (lastAnimator != null) {
            lastAnimator.removeAllListeners();
            lastAnimator.cancel();
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(blackout, View.ALPHA, blackout.getAlpha(), expand ? 1f : 0),
                ObjectAnimator.ofFloat(frame, View.TRANSLATION_Y, frame.getTranslationY(),
                        expand ? 0 : frame.getHeight())
        );

        set.setInterpolator(new FastOutSlowInInterpolator());
        set.setDuration(400);
        set.addListener(AnimatorHelper.onEnd(animator -> {
            int visibility = expand ? VISIBLE : GONE;
            blackout.setVisibility(visibility);
            frame.setVisibility(visibility);
            if (expand && recyclerView.getAdapter() == null)
                recyclerView.setAdapter(adapter);
        }));
        set.start();
        lastAnimator = set;
    }

    public void setOnStickerSelectedListener(Consumer<Uri> stickerListener) {
        this.stickerListener = stickerListener;
    }
}
