package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.annimon.stream.function.Consumer;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;


public class SizeNotifierLinearLayout extends LinearLayout {

    private Rect rect = new Rect();

    Consumer<Integer> keyboardSizeListener;

    public SizeNotifierLinearLayout(Context context) {
        super(context);
    }

    public SizeNotifierLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        notifyKeyboardHeightChanged();
    }

    public int getKeyboardHeight() {
        View rootView = getRootView();
        getWindowVisibleDisplayFrame(rect);
        int usableViewHeight = rootView.getHeight();
        return usableViewHeight - (rect.bottom - rect.top);
    }

    public void notifyKeyboardHeightChanged() {
        if (keyboardSizeListener == null) return;
        int keyboardHeight = getKeyboardHeight();
        post(() -> {
            if (keyboardSizeListener != null) {
                keyboardSizeListener.accept(keyboardHeight);
            }
        });

    }

    public void setKeyboardSizeListenerListener(Consumer<Integer> keyboardSizeListener) {
        this.keyboardSizeListener = keyboardSizeListener;
    }
}
