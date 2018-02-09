package com.samorodov.ru.interviewvk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.samorodov.ru.interviewvk.utilits.MultitouchGestureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xaxtix on 09.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class PhotoEditorView extends View {

    private List<StickerDrawable> stickers = new ArrayList<>();

    MultitouchGestureDetector gestureDetector;

    @Nullable
    private StickerDrawable captureSticker;

    float firstX;
    float firstY;

    public PhotoEditorView(Context context) {
        super(context);
        init(null);
    }

    public PhotoEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PhotoEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        gestureDetector = new MultitouchGestureDetector();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int n = stickers.size();
        for (int i = 0; i < n; i++) {
            stickers.get(i).draw(canvas);
        }
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                captureSticker = findCapturedSticker(x, y);
                if (captureSticker != null) {
                    firstX = captureSticker.translationX - x;
                    firstY = captureSticker.translationY - y;
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                if (captureSticker != null) {
                    captureSticker.translate(firstX + x, firstY + y);
                    invalidate(); 
                }

                return true;
            default:
                captureSticker = null;
                return false;

        }
    }

    @Nullable
    public StickerDrawable findCapturedSticker(float x, float y) {
        int n = stickers.size();
        for (int i = 0; i < n; i++) {
            if (stickers.get(i).capture(x, y)) {
                return stickers.get(i);
            }
        }
        return null;
    }


    public void addSticker(Uri sticker) {
        Glide.with(this)
                .asBitmap()
                .load(sticker)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        StickerDrawable stickerDrawable = new StickerDrawable(resource);
                        stickerDrawable.translate(
                                stickerDrawable.translationX + (getMeasuredWidth() >> 1),
                                stickerDrawable.translationY + (getMeasuredHeight() >> 1)
                        );
                        stickers.add(stickerDrawable);
                        invalidate();
                    }
                });
    }
}
