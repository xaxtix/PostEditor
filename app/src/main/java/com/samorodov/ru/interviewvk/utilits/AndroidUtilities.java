package com.samorodov.ru.interviewvk.utilits;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.annimon.stream.function.Consumer;

public class AndroidUtilities {

    public static View inflate(@NonNull ViewGroup parent, @LayoutRes int resId) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    public static View inflateMerge(@NonNull ViewGroup parent, @LayoutRes int resId) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, true);
    }

    public static void oneshotLayoutChangeListener(View view, Consumer<View> consumer) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                view.removeOnLayoutChangeListener(this);
                consumer.accept(view);
            }
        });
    }

    public static int dp(Context context, float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    private static int bottomBarSize = -1;
    private static int statusBarSize = -1;
    private static int screenHeight = -1;
    private static int screenWeigth = -1;

    public static int getNavigationBarSize(View rootView, Rect rect) {
        if (bottomBarSize == -1)
            measureStatusAndNavigationBarSize(rootView, rect);
        return bottomBarSize;
    }

    public static int getStatusBarSize(View rootView, Rect rect) {
        if (statusBarSize == -1)
            measureStatusAndNavigationBarSize(rootView, rect);
        return statusBarSize;
    }


    public static int measureStatusAndNavigationBarSize(View rootView, Rect rectTmp) {
        if (bottomBarSize == -1) {
            if (rectTmp == null) rectTmp = new Rect();
            rootView.getWindowVisibleDisplayFrame(rectTmp);
            Context context = rootView.getContext();

            bottomBarSize = getScreenHeight(context) - rectTmp.bottom;
            statusBarSize = rectTmp.top;
        }

        return bottomBarSize;
    }

    private static int getScreenHeight(Context context) {
        if (screenHeight == -1) {
            initScreenSizeDimension(context);
        }
        return screenHeight;
    }

    private static void initScreenSizeDimension(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }

        screenWeigth = size.x;
        screenHeight = size.y;
    }

    public static void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.isActive(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
