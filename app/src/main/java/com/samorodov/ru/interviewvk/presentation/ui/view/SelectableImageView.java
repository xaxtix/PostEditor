package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.samorodov.ru.interviewvk.utilits.AndroidUtilites;

/**
 * Created by xaxtix on 13.02.2018.
 * ♪♫•*¨*•.¸¸❤¸¸.•*¨*•♫♪ﾟ+｡☆*゜+。.。:.*.ﾟ ﾟ¨ﾟﾟ･*:..｡o○☆ﾟ+｡
 */

public class SelectableImageView extends android.support.v7.widget.AppCompatImageView {

    Path path;
    RectF rect;

    float cornerRadius;

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
        cornerRadius = AndroidUtilites.dp(getContext(), 4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth() < getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();

        setMeasuredDimension(width, width);

        path.reset();
        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CCW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.restore();
    }

    public void setSelected(boolean selected){

    }
}
