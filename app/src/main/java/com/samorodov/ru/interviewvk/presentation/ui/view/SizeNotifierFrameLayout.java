package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;


public class SizeNotifierFrameLayout extends FrameLayout {

    private final Rect rect = new Rect();

    private Consumer<Integer> keyboardSizeListener;

    int lastKeyboardSize = 0;

    public SizeNotifierFrameLayout(Context context) {
        super(context);
    }

    public SizeNotifierFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        notifyKeyboardHeightChanged();
    }

    public int getKeyboardHeight() {
        View rootView = getRootView();
        rootView.getWindowVisibleDisplayFrame(rect);
        int usableViewHeight = rootView.getHeight();
        return usableViewHeight - (rect.bottom - rect.top)
                - AndroidUtilities.getStatusBarSize(rootView, rect)
                - AndroidUtilities.getNavigationBarSize(rootView, rect);
    }

    public void notifyKeyboardHeightChanged() {
        if (keyboardSizeListener == null) return;
        int keyboardHeight = getKeyboardHeight();
        if (lastKeyboardSize == keyboardHeight) return;
        lastKeyboardSize = keyboardHeight;

        if (keyboardSizeListener != null) {
            keyboardSizeListener.accept(keyboardHeight);
        }
    }

    public void setKeyboardSizeListener(Consumer<Integer> keyboardSizeListener) {
        this.keyboardSizeListener = keyboardSizeListener;
    }
}
