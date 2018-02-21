package com.samorodov.ru.interviewvk.utilits.image;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.samorodov.ru.interviewvk.presentation.ui.view.PhotoEditorView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class ImageUtils {

    public static void createBackgroundDrawable(PhotoEditorView target, int width,
                                                Bitmap bitmap) {
        Resources res = target.getContext().getResources();
        if (bitmap.getWidth() != width) {
            WeakReference<PhotoEditorView> targetReference = new WeakReference<>(target);
            new Thread(() -> {
                int height = (int) (((float) width) / bitmap.getWidth()
                        * bitmap.getHeight());
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                BitmapDrawable drawable = new BitmapDrawable(res, newBitmap);
                PhotoEditorView view = targetReference.get();
                if (view != null)
                    view.post(() -> view.setBack(drawable));


            }).start();
        } else {
            BitmapDrawable drawable = new BitmapDrawable(res, bitmap);
            target.setBack(drawable);
        }
    }
}
