package com.samorodov.ru.interviewvk.presentation.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.presentation.ui.view.stickers.StickerDrawable;
import com.samorodov.ru.interviewvk.presentation.ui.view.stickers.StickersGestureDetector;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

import static android.animation.AnimatorInflater.loadStateListAnimator;
import static android.text.TextUtils.isEmpty;
import static com.samorodov.ru.interviewvk.utilits.EditTextUtils.addRemoveHintTextWatcher;
import static com.samorodov.ru.interviewvk.utilits.image.ImageUtils.createBackgroundDrawable;

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
    Drawable background;

    @Nullable
    StickerDrawable capturedSticker;

    EditTextStyleDelegate editTextStyleDelegate;

    EditText editText;

    ImageView trashIcon;

    public PhotoEditorView(Context context) {
        super(context);
        init();
    }

    public PhotoEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        gestureDetector = new StickersGestureDetector(this, stickers);
        gestureDetector.setOnCapturedListener(drawable -> {
            stickers.remove(drawable);
            capturedSticker = drawable;
            drawStickers();
            invalidate();
        });
        gestureDetector.setOnReleasedListener(drawable -> {
            if (drawable != null)
                stickers.add(drawable);
            capturedSticker = null;
            drawStickers();
            invalidate();
        });

        tmpMatrix = new Matrix();
        emptyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        emptyPaint.setFilterBitmap(true);

        editText = new EditText(getContext());
        editText.setBackground(null);
        editText.setTextSize(24);
        editText.setHint(R.string.what_new);
        addRemoveHintTextWatcher(editText, R.string.what_new);
        editText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        editText.setGravity(Gravity.CENTER);

        editTextStyleDelegate = new EditTextStyleDelegate(editText);


        addView(editText);
        LayoutParams lp = (LayoutParams) editText.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.WRAP_CONTENT;

        trashIcon = new android.support.v7.widget.AppCompatImageView(getContext()) {
            @Override
            public void setSelected(boolean selected) {
                super.setSelected(selected);
                trashIcon.setImageResource(selected ?
                        R.drawable.ic_fab_trash_released : R.drawable.ic_fab_trash
                );
            }
        };
        trashIcon.setImageResource(R.drawable.ic_fab_trash);
        addView(trashIcon);
        lp = (LayoutParams) trashIcon.getLayoutParams();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        int dp_56 = AndroidUtilities.dp(getContext(), 56);
        lp.height = dp_56;
        lp.width = dp_56;
        lp.bottomMargin = dp_56 + (dp_56 >> 2);

        trashIcon.setBackgroundResource(R.drawable.trash_background);
        trashIcon.setVisibility(GONE);

        gestureDetector.setTrashIcon(trashIcon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            trashIcon.setElevation(AndroidUtilities.dp(getContext(), 2));

            trashIcon.setStateListAnimator(
                    loadStateListAnimator(getContext(), R.animator.lift_on_touch_scale)
            );
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (stickersLayer == null || stickersLayer.getWidth() != getMeasuredWidth() ||
                stickersLayer.getHeight() != getMeasuredHeight()) {
            stickersLayer = null;
            if (getMeasuredWidth() <= 0 || getMeasuredHeight() <= 0) return;
            stickersLayer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            stickersLayerCanvas = new Canvas(stickersLayer);
            drawStickers();
        }
        updateBackgroundSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (background != null) {
            canvas.clipRect(background.getBounds());
            background.draw(canvas);
        }
        if (stickersLayer != null && stickers.size() > 0)
            canvas.drawBitmap(stickersLayer, 0, 0, emptyPaint);
        if (capturedSticker != null) capturedSticker.draw(canvas);
        canvas.restore();
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
                                stickerDrawable.getTranslationY() + (getMeasuredHeight() >> 1)
                        );


                        stickers.add(stickerDrawable);
                        drawStickers();
                        invalidate();
                    }
                });
    }

    private void drawStickers() {
        if (stickersLayer == null) return;
        stickersLayerCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        int n = stickers.size();
        for (int i = 0; i < n; i++) {
            stickers.get(i).draw(stickersLayerCanvas);
        }

    }


    public void setBackgroundImage(@Nullable Uri backgroundImage) {
        background = null;

        if (backgroundImage == null) {
            invalidate();
            return;
        }

        Glide.with(this)
                .asBitmap()
                .load(backgroundImage)
                .into(new ViewTarget<PhotoEditorView, Bitmap>(this) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        createBackgroundDrawable(PhotoEditorView.this, getMeasuredWidth(), resource);
                    }
                });
    }


    private void updateBackgroundSize() {
        if (background == null) return;
        int height = (int) (((float) getMeasuredWidth()) / background.getIntrinsicWidth()
                * background.getIntrinsicHeight());
        int cY = getMeasuredHeight() >> 1;
        int heightHalf = height >> 1;
        background.setBounds(
                0,
                cY - heightHalf,
                getMeasuredWidth(),
                cY + heightHalf
        );
    }

    public void toggleEditTextStyle() {
        editTextStyleDelegate.toggleStyle();
    }


    public void setBack(Drawable back) {
        this.background = back;
        updateBackgroundSize();
        invalidate();
    }

    public Bitmap prepareBitmap(DrawingState state) {

        editText.clearFocus();
        editText.setCursorVisible(false);
        if (isEmpty(editText.getText()))
            editText.setVisibility(GONE);
        int textType = editText.getInputType();

        editText.setInputType(textType | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        draw(state.canvas);

        editText.setCursorVisible(true);
        editText.setVisibility(VISIBLE);

        editText.setInputType(textType);

        return state.bitmap;
    }


    public DrawingState createDrawingState() {
        Bitmap bitmap;
        Canvas canvas;

        if (background == null) {
            bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
        } else {
            Rect bounds = background.getBounds();
            bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.translate(0, -(getMeasuredHeight() - bounds.height() >> 1));
        }

        return new DrawingState(bitmap, canvas, this);
    }

    public static class DrawingState {
        final Bitmap bitmap;
        final Canvas canvas;
        final PhotoEditorView editorView;

        public DrawingState(Bitmap bitmap, Canvas canvas, PhotoEditorView editorView) {

            this.bitmap = bitmap;
            this.canvas = canvas;
            this.editorView = editorView;
        }

        public Bitmap prepareBitmap() {
            return editorView.prepareBitmap(this);
        }
    }
}
