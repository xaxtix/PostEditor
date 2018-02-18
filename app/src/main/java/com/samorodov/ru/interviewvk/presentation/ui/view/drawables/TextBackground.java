package com.samorodov.ru.interviewvk.presentation.ui.view.drawables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;

public abstract class TextBackground extends Drawable {

    private final Paint paint;
    private final Paint shadowPaint;

    private final RectF rectF;

    private final float offsetY;
    private final int horizontalPadding;
    private final int verticalPadding;
    private final PorterDuffXfermode porterDuffXfermode;

    private final float round;

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        padding.set(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        return true;
    }

    public abstract int getColor(Context context);


    public TextBackground(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getColor(context));

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(ContextCompat.getColor(context, R.color.shadow));
        rectF = new RectF();
        offsetY = AndroidUtilities.dp(context, 1);

        float dp_8 = AndroidUtilities.dp(context, 8);

        round = dp_8;

        horizontalPadding = (int) dp_8;
        verticalPadding = horizontalPadding >> 1;

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        rectF.set(getBounds());
        canvas.drawRoundRect(rectF, round, round, shadowPaint);
        rectF.set(rectF.left, rectF.top, rectF.right, rectF.bottom - offsetY);
        if (paint.getAlpha() < 255) {
            paint.setXfermode(porterDuffXfermode);
            canvas.drawRoundRect(rectF, round, round, paint);
            paint.setXfermode(null);
        }
        canvas.drawRoundRect(rectF, round, round, paint);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }


}
