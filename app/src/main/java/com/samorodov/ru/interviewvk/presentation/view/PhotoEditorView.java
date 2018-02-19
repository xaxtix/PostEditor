package com.samorodov.ru.interviewvk.presentation.view;

import android.net.Uri;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface PhotoEditorView extends MvpView {

    void setStickers(List<Uri> stickers);

    void setBackgroundImagesToPicker(List<Uri> images);

    void addBackgroundImageToPicker(Uri image);
}
