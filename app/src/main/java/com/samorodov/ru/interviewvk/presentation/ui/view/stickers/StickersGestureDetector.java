package com.samorodov.ru.interviewvk.presentation.ui.view.stickers;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;
import com.annimon.stream.function.Consumer;

import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_UP;


public class StickersGestureDetector {

    private final View view;
    private final List<StickerDrawable> stickers;

    private final ScaleGestureDetector scaleDetector;
    private final RotateGestureDetector rotateDetector;
    private final MoveGestureDetector moveGestureDetector;

    @Nullable
    private StickerDrawable capturedSticker;

    private float scale;
    private float translationX;
    private float translationY;
    private float rotation;

    @Nullable
    Consumer<StickerDrawable> onDrawableCapturedListener;

    @Nullable
    Consumer<StickerDrawable> onDrawableReleasedListener;
    private ImageView trashIcon;

    public StickersGestureDetector(View view, List<StickerDrawable> stickers) {
        this.view = view;
        this.stickers = stickers;

        Context context = view.getContext();

        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector
                .SimpleOnScaleGestureListener() {

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (capturedSticker == null) return false;
                scale *= detector.getScaleFactor();
                scale = Math.max(0.3f, Math.min(scale, 5.0f));

                capturedSticker.scale(scale);
                return true;
            }
        });

        rotateDetector = new RotateGestureDetector(context, new RotateGestureDetector
                .SimpleOnRotateGestureListener() {

            @Override
            public boolean onRotate(RotateGestureDetector detector) {
                if (capturedSticker == null) return false;
                rotation -= detector.getRotationDegreesDelta();
                capturedSticker.rotate(rotation);
                return true;
            }
        });

        moveGestureDetector = new MoveGestureDetector(context, new MoveGestureDetector
                .SimpleOnMoveGestureListener() {

            @Override
            public boolean onMove(MoveGestureDetector detector) {
                if (capturedSticker == null) return false;
                PointF d = detector.getFocusDelta();
                translationX += d.x;
                translationY += d.y;
                capturedSticker.translate(translationX, translationY);
                return true;
            }

        });
    }

    public boolean onTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getActionMasked();

        scaleDetector.onTouchEvent(event);
        rotateDetector.onTouchEvent(event);
        moveGestureDetector.onTouchEvent(event);


        switch (action) {
            case ACTION_POINTER_DOWN:
            case ACTION_DOWN:
                if (capturedSticker == null) {
                    if (event.getPointerCount() > 1) {
                        x = (event.getX(0) + event.getX(1)) / 2;
                        y = (event.getY(0) + event.getY(1)) / 2;
                    }
                    capturedSticker = findCapturedSticker(x, y);

                    if (capturedSticker != null) {
                        trashIcon.setVisibility(View.VISIBLE);
                        translationX = capturedSticker.translationX;
                        translationY = capturedSticker.translationY;
                        rotation = capturedSticker.rotate;
                        scale = capturedSticker.scale;
                        if (onDrawableCapturedListener != null) {
                            onDrawableCapturedListener.accept(capturedSticker);
                        }
                    }
                }
                return capturedSticker != null || event.getPointerCount() == 1;
            case ACTION_MOVE:
                if (capturedSticker == null) return false;
                trashIcon.setSelected(event.getPointerCount() == 1 && captureTrash(x, y));
                view.invalidate();
                return true;
            case ACTION_UP:
                if (event.getPointerCount() == 1 && capturedSticker != null) {
                    if (captureTrash(x, y)) {
                        trashIcon.setSelected(false);
                        view.invalidate();
                        capturedSticker = null;
                    }
                    if (onDrawableReleasedListener != null) {
                        onDrawableReleasedListener.accept(capturedSticker);
                    }
                    trashIcon.setVisibility(View.GONE);
                    capturedSticker = null;
                }
                return true;
            default:
                return capturedSticker != null;
        }

    }

    private boolean captureTrash(float x, float y) {
        return y > trashIcon.getTop() && y < trashIcon.getBottom() &&
                x > trashIcon.getLeft() && x < trashIcon.getRight();
    }

    @Nullable
    public StickerDrawable findCapturedSticker(float x, float y) {
        int n = stickers.size();
        for (int i = n - 1; i >= 0; i--) {
            if (stickers.get(i).capture(x, y)) {
                return stickers.get(i);
            }
        }
        return null;
    }

    public void setOnCapturedListener(@Nullable Consumer<StickerDrawable> onDrawaleCapturedListener) {
        this.onDrawableCapturedListener = onDrawaleCapturedListener;
    }

    public void setOnReleasedListener(@Nullable Consumer<StickerDrawable> onDrawaleReleasedListener) {
        this.onDrawableReleasedListener = onDrawaleReleasedListener;
    }

    public void setTrashIcon(ImageView trashIcon) {
        this.trashIcon = trashIcon;
    }
}
