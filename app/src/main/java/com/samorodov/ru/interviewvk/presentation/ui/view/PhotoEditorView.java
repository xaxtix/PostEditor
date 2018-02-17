package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.view.drawables.TextBackground;
import com.samorodov.ru.interviewvk.presentation.ui.view.stickers.StickerDrawable;
import com.samorodov.ru.interviewvk.presentation.ui.view.stickers.StickersGestureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xaxtix on 09.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class PhotoEditorView extends FrameLayout {

    private final List<StickerDrawable> stickers = new ArrayList<>();

    StickersGestureDetector gestureDetector;

    Matrix tmpMatrix;
    Paint emptyPaint;

    Bitmap stickersLayer;

    Canvas stickersLayerCanvas;

    @Nullable
    Bitmap background;

    @Nullable
    StickerDrawable capturedSticker;

    int yOffset = 0;

    private int keyboardHeight;

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
        setWillNotDraw(false);
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

        EditText editText = new EditText(getContext());
        editText.setBackground(null);
        editText.setTextSize(24);
        editText.setHint(R.string.what_new);
        editText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        editText.setGravity(Gravity.CENTER);
        editText.setBackground(new TextBackground(getContext()));
        editText.setLayerType(LAYER_TYPE_SOFTWARE,null);



        addView(editText);
        LayoutParams lp = (LayoutParams) editText.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (stickersLayer == null || stickersLayer.getWidth() != getMeasuredWidth() ||
                stickersLayer.getHeight() != getMeasuredHeight()) {
            stickersLayer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            stickersLayerCanvas = new Canvas(stickersLayer);
            drawStickers();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (background != null)
            canvas.drawBitmap(background, 0, 0, emptyPaint);
        canvas.drawBitmap(stickersLayer, 0, 0, emptyPaint);
        if (capturedSticker != null) capturedSticker.draw(canvas, yOffset);
        super.onDraw(canvas);
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
                                stickerDrawable.getTranslationY() + (getMeasuredHeight() >> 1) - yOffset
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
            stickers.get(i).draw(stickersLayerCanvas, yOffset);
        }

    }

    public void setBackgroundImage(Uri backgroundImage) {
        background = null;

        Glide.with(this)
                .asBitmap()
                .load(backgroundImage)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        background = resource;
                        invalidate();
                    }
                });
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
        yOffset = -keyboardHeight >> 1;
        drawStickers();
        invalidate();
        Log.d("Kek", keyboardHeight + "");
    }
}
