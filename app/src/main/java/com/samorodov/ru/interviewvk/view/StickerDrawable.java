package com.samorodov.ru.interviewvk.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;

public class StickerDrawable {

    private final Bitmap bitmap;

    private final Matrix transformMatrix;
    private final Matrix invertMatrix;
    private final Paint paint;
    private final float[] point = new float[2];

    private final int height;
    private final int width;

    float translationX;
    float translationY;

    public StickerDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        transformMatrix = new Matrix();
        invertMatrix = new Matrix();

        translationX = -width >> 1;
        translationY = -height >> 1;
        transformMatrix.setTranslate(translationX, translationY);

        paint = new Paint();
        paint.setFilterBitmap(true);
    }

    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.setMatrix(transformMatrix);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.restore();
    }

    public void translate(float x, float y) {
        transformMatrix.setTranslate(translationX = x, translationY = y);
    }

    public void rotate(float a) {
        transformMatrix.preRotate(a, width >> 1, height >> 1);
    }

    public void scale(int s) {
        transformMatrix.preScale(s, s, width >> 1, height >> 1);
    }

    public boolean capture(float x, float y) {
        transformMatrix.invert(invertMatrix);
        point[0] = x;
        point[1] = y;
        invertMatrix.mapPoints(point);
        if (point[0] > 0 && point[0] < width &&
                point[1] > 0 && point[1] < height) return true;
        return false;
    }

    public float getTranslationX() {
        return translationX;
    }

    public float getTranslationY() {
        return translationY;
    }
}
