package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;

/**
 * Created by xaxtix on 13.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class SelectableImageView extends android.support.v7.widget.AppCompatImageView {

    Path path;
    RectF rect;
    Rect insetRect;

    Paint strokePaint;
    Paint backgroundPaint;

    float cornerRadius;

    boolean selected = false;

    int padding;

    boolean showFillColor;

    public SelectableImageView(Context context) {
        super(context);
        init();
    }

    public SelectableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        rect = new RectF();
        insetRect = new Rect();
        float dp_4 = AndroidUtilities.dp(getContext(), 4);
        cornerRadius = dp_4;
        padding = (int) dp_4;

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        strokePaint.setStrokeWidth(dp_4);
        strokePaint.setStyle(Paint.Style.STROKE);

        backgroundPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int size = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(size, size);

        int dp_4 = AndroidUtilities.dp(getContext(), 4);

        path.reset();
        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CCW);
        insetRect.set(dp_4, dp_4, getMeasuredWidth() - dp_4, getMeasuredHeight() - dp_4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(path);
        if (selected) {
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, strokePaint);
            canvas.clipRect(insetRect);
            if (showFillColor) canvas.drawRect(insetRect, backgroundPaint);
            super.onDraw(canvas);
        } else {
            if (showFillColor) canvas.drawPaint(backgroundPaint);
            super.onDraw(canvas);
        }
        canvas.restore();

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        invalidate();
    }

    public void setFillColor(@ColorRes int fillColor) {
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), fillColor));
        showFillColor = true;
    }

    public void clearFillColor() {
        showFillColor = false;
    }
}
