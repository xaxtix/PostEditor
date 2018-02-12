package com.samorodov.ru.interviewvk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.samorodov.ru.interviewvk.view.stickers.StickerDrawable;
import com.samorodov.ru.interviewvk.view.stickers.StickersGestureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xaxtix on 09.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class PhotoEditorView extends View {

    private final List<StickerDrawable> stickers = new ArrayList<>();

    StickersGestureDetector gestureDetector;

    Matrix tmpMatrix;
    Paint emptyPaint;

    Bitmap stickersLayer;

    Canvas stickersLayerCanvas;

    @Nullable
    StickerDrawable capturedSticker;

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
        gestureDetector = new StickersGestureDetector(this, stickers);
        gestureDetector.setOnDrawaleCapturedListener(drawable -> {
            stickers.remove(drawable);
            capturedSticker = drawable;
            drawStickers();
            invalidate();
        });
        gestureDetector.setOnDrawaleReleasedListener(drawable -> {
            stickers.add(drawable);
            capturedSticker = null;
            drawStickers();
            invalidate();
        });

        tmpMatrix = new Matrix();
        emptyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        emptyPaint.setFilterBitmap(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        stickersLayer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        stickersLayerCanvas = new Canvas(stickersLayer);
        drawStickers();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(stickersLayer, 0, 0, emptyPaint);
        if (capturedSticker != null) capturedSticker.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouch(event);
    }


    public void addSticker(Uri sticker) {
        Glide.with(this)
                .asBitmap()
                .load(sticker)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        StickerDrawable stickerDrawable = new StickerDrawable(resource, emptyPaint, tmpMatrix);

                        stickerDrawable.translate(
                                stickerDrawable.getTranslationX() + (getMeasuredWidth() >> 1),
                                stickerDrawable.getTranslationY() + (getMeasuredHeight() >> 1)
                        );

                        stickers.add(stickerDrawable);
                        drawStickers();
                        invalidate();
                    }
                });
    }

    private void drawStickers() {
        stickersLayerCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        int n = stickers.size();
        for (int i = 0; i < n; i++) {
            stickers.get(i).draw(stickersLayerCanvas);
        }

    }
}
