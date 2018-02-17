package com.samorodov.ru.interviewvk.presentation.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.samorodov.ru.interviewvk.R;
import com.samorodov.ru.interviewvk.di.component.DaggerPhotoEditorComponent;
import com.samorodov.ru.interviewvk.presentation.presenter.PhotoEditorPresenter;
import com.samorodov.ru.interviewvk.presentation.ui.adapter.image_picker.ImagePickerAdapter;
import com.samorodov.ru.interviewvk.presentation.ui.view.PhotoEditorView;
import com.samorodov.ru.interviewvk.presentation.ui.view.SizeNotifierLinearLayout;
import com.samorodov.ru.interviewvk.presentation.ui.view.stickers.StickersPopupView;
import com.samorodov.ru.interviewvk.utilits.AndroidUtilities;
import com.samorodov.ru.interviewvk.utilits.RecyclerViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoEditorActivity extends MvpAppCompatActivity implements
        com.samorodov.ru.interviewvk.presentation.view.PhotoEditorView {

    @BindView(R.id.font_style_button) ImageView fontStyleButton;
    @BindView(R.id.stickers_button) ImageView stickersButton;
    @BindView(R.id.toolbar) FrameLayout toolbar;
    @BindView(R.id.editor_view) PhotoEditorView editorView;
    @BindView(android.R.id.content) ViewGroup content;
    @BindView(R.id.content) SizeNotifierLinearLayout sizeNotifier;
    @BindView(R.id.image_picker) RecyclerView imagePicker;

    @Nullable
    StickersPopupView stickersPopup;

    @InjectPresenter
    PhotoEditorPresenter presenter;

    ImagePickerAdapter imagePickerAdapter;

    @ProvidePresenter
    public PhotoEditorPresenter providePresenter() {
        PhotoEditorPresenter presenter = new PhotoEditorPresenter();
        DaggerPhotoEditorComponent
                .create()
                .inject(presenter);
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        stickersButton.setOnClickListener(v -> {
            if (stickersPopup == null) {
                initStickersPopup();
            }

            stickersPopup.toggle();
        });

        imagePicker.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        ));
        imagePicker.addItemDecoration(new RecyclerViewUtils.ItemHorizontalOffsetDecoration(
                AndroidUtilities.dp(this, 4)
        ));

        imagePicker.setAdapter(imagePickerAdapter = new ImagePickerAdapter());
        imagePickerAdapter.setOnImageSelectedListener(uri -> {
            editorView.setBackgroundImage(uri);
        });

        sizeNotifier.setKeyboardSizeListenerListener(keyboardHeight -> {
            editorView.setKeyboardHeight(keyboardHeight);
        });

    }

    private void initStickersPopup() {
        stickersPopup = new StickersPopupView(PhotoEditorActivity.this);
        stickersPopup.setOnStickerSelectedListener(sticker ->
                editorView.addSticker(sticker)
        );
        content.addView(stickersPopup);
        presenter.loadStickers();
    }


    @Override
    public void setStickers(List<Uri> stickers) {
        if (stickersPopup != null)
            stickersPopup.setStickers(stickers);
    }

    @Override
    public void onBackPressed() {
        if (stickersPopup != null && stickersPopup.isExpanded()) {
            stickersPopup.toggle();
            return;
        }
        super.onBackPressed();
    }
}
