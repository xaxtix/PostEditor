package com.samorodov.ru.interviewvk.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;

import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by xaxtix on 10.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class StickersGestureDetector {

    private final View view;
    private final List<StickerDrawable> stickers;

    private final ScaleGestureDetector scaleDetector;
    private final RotateGestureDetector rotateDetector;
    private final MoveGestureDetector moveGestureDetector;

    @Nullable
    private StickerDrawable capturedSticker;


    public StickersGestureDetector(View view, List<StickerDrawable> stickers) {
        this.view = view;
        this.stickers = stickers;

        Context context = view.getContext();

        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector
                .SimpleOnScaleGestureListener() {

            float scale;

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                if (capturedSticker == null) return false;
                scale = capturedSticker.scale;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (capturedSticker == null) return false;
                scale *= detector.getScaleFactor();
                capturedSticker.scale(scale);
                return true;
            }
        });

        rotateDetector = new RotateGestureDetector(context, new RotateGestureDetector
                .SimpleOnRotateGestureListener() {

            float rotation;

            @Override
            public boolean onRotateBegin(RotateGestureDetector detector) {
                if (capturedSticker == null) return false;
                rotation = capturedSticker.rotate;
                return true;
            }

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

            float translationX;
            float translationY;

            @Override
            public boolean onMoveBegin(MoveGestureDetector detector) {
                if (capturedSticker == null) return false;
                translationY = capturedSticker.translationY;
                translationX = capturedSticker.translationX;
                return true;
            }

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


        scaleDetector.onTouchEvent(event);
        rotateDetector.onTouchEvent(event);
        moveGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case ACTION_DOWN:
                if (capturedSticker == null) {
                    if (event.getPointerCount() == 1) {
                        capturedSticker = findCapturedSticker(x, y);
                    } else {

                    }

                }
                return capturedSticker != null;
            case ACTION_MOVE:
                if (capturedSticker == null) return false;
                view.invalidate();
                return true;
            case ACTION_UP:
                if (event.getPointerCount() == 1)
                    capturedSticker = null;
                return true;
        }

        return false;
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
}
